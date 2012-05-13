package ar.com.iron.android.extensions.services;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import ar.com.iron.android.extensions.messages.IntentReceptor;

/**
 * Esta clase representa un proceso que se ejecuta en background. Permite la ejecución en un thread
 * separado a la vez que mantiene un contexto y la recepción de mensajes en forma de intents dentro
 * del sistema android. Para lo cual se crea su propio {@link Looper} y {@link Handler}.<br>
 * Esta clase puede pensarse en sí misma como un componente más de Android
 * 
 * @author D. García
 */
public class BackgroundProcess extends Thread {

	/**
	 * Cantidad de segundos que puede esperar un thread a que este consiga su handler
	 */
	public static final long HANDLER_OPENING_TIMEOUT = 60;

	/**
	 * Unidad de la espera
	 */
	public static final TimeUnit HANDLER_OPENING_TIMEOUT_UNIT = TimeUnit.SECONDS;

	/**
	 * Receptores que permiten la comunicación de este servicio en background
	 */
	private IntentReceptor intentReceptor;

	/**
	 * Handler de la cola de mensajes para poder enviarse mensajes a sí mismo
	 */
	private Handler propioHandler;

	/**
	 * Contexto al que se derivan las cosas específicas de android
	 */
	private final Context contexto;

	/**
	 * Barrera para impedir que otros threas accedan al handler hasta que esté creado (unos
	 * milisegundos)
	 */
	private final CountDownLatch barreraInicializacion;

	/**
	 * Looper creado para este hilo
	 */
	private Looper propioLooper;

	/**
	 * Flag para saber si alguna vez fue ejecutado
	 */
	private final AtomicBoolean started = new AtomicBoolean(false);

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// Crea el looper propio
		Looper.prepare();
		propioLooper = Looper.myLooper();

		// Crea el handler para poder mandarse mensajes a si mismo a partir de la llamada a métodos,
		// de otros
		propioHandler = new Handler();
		intentReceptor = new IntentReceptor(getContexto());
		intentReceptor.setCustomHandler(propioHandler);
		initDependencies();
		initMessageReceivers();

		// En caso de que exista código de inicialización lo ponemos como primero a ejecutar
		Runnable codigoInicializacion = getExecuteOnStartCode();
		if (codigoInicializacion != null) {
			propioHandler.post(codigoInicializacion);
		}

		// Habilita a que otros usen el handler
		barreraInicializacion.countDown();

		// Inicia el ciclo de procesamiento
		Looper.loop();

		// Si hay código para la detención
		Runnable codigoDetencion = getExecuteOnStopCode();
		if (codigoDetencion != null) {
			codigoDetencion.run();
		}
	}

	/**
	 * Inicializa las dependencias necesarias para este thread
	 */
	protected void initDependencies() {
	}

	/**
	 * Comienza la ejecución de este proceso disparando un thread a parte. Este método esta pensado
	 * para ser llamado una sola vez desde el componente que es owner de este proceso (el que va a
	 * controlar el ciclo de vida)
	 */
	public void comenzar() {
		// Sólo iniciamos el thread la primera vez
		if (started.compareAndSet(false, true)) {
			// Disparamos el thread que creará el handler si todo sale bien
			this.start();
			// Esperamos la inicialización que ocurrira en paralelo
			waitProcessInitialization();
		}
	}

	/**
	 * Indica si este proceso ya fue inicializado y sus variables son consistente
	 * 
	 * @return true si acceder a sus variables desde otros threads no provoca espera
	 */
	public boolean isInitialized() {
		return barreraInicializacion.getCount() == 0;
	}

	/**
	 * Devuelve el handler que el proceso ofrece para agregar tareas a la cola de este proceso.<br>
	 * Las tareas agregadas serán procesadas una por una, a menos que se detenga la ejecución, en
	 * cuyo caso pueden quedar tareas pendientes
	 * 
	 * @return El handler para el encolado de tareas
	 */
	public Handler getProcessHandler() {
		if (!started.get()) {
			throw new RuntimeException("No se puede obtener el handler si nunca se inicio el thread");
		}
		if (!isInitialized()) {
			// Tenemos que esperar que se cree el handler
			waitProcessInitialization();
		}
		return propioHandler;
	}

	/**
	 * Este método permite a threads externos esperar hasta que este proceso se inicialice y tenga
	 * un estado consistente.<br>
	 * Este método falla si en el tiempo de espera no se logró inicializar.<br>
	 * Si termina normalmente es seguro acceder a las variables de instancia de este proceso
	 */
	private void waitProcessInitialization() {
		try {
			boolean consiguioHandler = barreraInicializacion.await(HANDLER_OPENING_TIMEOUT,
					HANDLER_OPENING_TIMEOUT_UNIT);
			if (!consiguioHandler) {
				throw new RuntimeException("No se pudo crear un handler para el hilo " + this.getClass());
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("Se produjo un error esperando el handler del hilo " + this.getClass(), e);
		}
	}

	/**
	 * Devuelve el código que se debe ejecutar cuando este thread es iniciado
	 * 
	 * @return Un runnable ejecutado despues de inicializar este proceso, cuando se ejecuta el
	 *         método {@link #comenzar()} de este thread. O null si no se necesita ejecutar nada
	 */
	protected Runnable getExecuteOnStartCode() {
		return null;
	}

	/**
	 * Constructor que necesita el contexto
	 */
	public BackgroundProcess(Context contexto, String threadName) {
		if (threadName != null) {
			this.setName(threadName);
		}
		this.contexto = contexto;
		this.barreraInicializacion = new CountDownLatch(1);
	}

	/**
	 * Detiene la ejecucion de este thread inmediatamente agregando un mensaje a la cola que será
	 * ejecutada apenas termine lo que esté procesando en el momento.<br>
	 * Las tareas sin procesar quedarán en la cola.<br>
	 * Una vez detenido este proceso no puede ser reutilizado
	 */
	public void detener() {
		if (propioLooper != null) {
			propioLooper.quit();
		}
	}

	/**
	 * Codigo a ejecutar cuando se detiene este hilo forzadamente
	 * 
	 * @return null si no se debe ejecutar nada, o el runnable con el código a ejecutar
	 */
	protected Runnable getExecuteOnStopCode() {
		return null;
	}

	protected Handler getPropioHandler() {
		return propioHandler;
	}

	public Context getContexto() {
		return contexto;
	}

	protected Looper getPropioLooper() {
		return propioLooper;
	}

	/**
	 * Metodo para la subclase que indica el momento adecuado para registrar los receivers de este
	 * activity que sirven para la comunicación de fondo.<br>
	 * Al registrar los receivers en este metodo se asegura que se recibirán los mensajes aún cuando
	 * la pantalla no es visible
	 */
	protected void initMessageReceivers() {
	}

	/**
	 * Registra en este activity un receiver que será disparado al recibir un intent del action
	 * declarado.<br>
	 * Al recibir el intent con el action declarado, se ejecuta el listener pasado
	 * 
	 * @param expectedAction
	 *            Action que declara el mensaje esperado
	 * @param executedWhenReceived
	 *            Listener a ejecutar cuando se recibe el mensaje
	 */
	protected void registerMessageReceiver(String expectedAction, BroadcastReceiver executedWhenReceived) {
		getIntentReceptor().registerMessageReceiver(expectedAction, executedWhenReceived);
	}

	public IntentReceptor getIntentReceptor() {
		return intentReceptor;
	}

	/**
	 * Agrega la tarea indicada para ser ejecutada en la cola de procesamiento de este proceso en
	 * background. El resultado o error serán devueltos al thread invocante (debe tener un looper)
	 * 
	 * @param backgroundTask
	 *            La tarea a ejecutar
	 */
	public void addTask(BackgroundTask<?> backgroundTask) {
		// Definimos el handler que nos invoca, al que le devolemos los resultados
		Handler mainThreadhandler = new Handler();
		backgroundTask.setMainHandler(mainThreadhandler);

		// Mandamos la tarea a la cola
		Handler processHandler = getProcessHandler();
		backgroundTask.setBackgroundContext(contexto);
		processHandler.post(backgroundTask);
	}

}
