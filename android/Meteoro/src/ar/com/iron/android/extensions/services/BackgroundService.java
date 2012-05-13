package ar.com.iron.android.extensions.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * Esta clase representa un servicio que ejecuta su lógica en un thread aparte. Cuando el servicio
 * es creado se dispara un thread autónomo que es terminado al eliminarse el servicio.<br>
 * Esta clase sirve de pasarela entre el servicio como componente y el thread como procesador del
 * código
 * 
 * @author D.Garcia
 * @since 23/09/2009
 */
public abstract class BackgroundService extends Service {

	/**
	 * Thread que se encarga de la ejecucion del servicio
	 */
	private BackgroundProcess backgroundProcess;

	/**
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		backgroundProcess = createBackgroundThread();
		if (backgroundProcess == null) {
			throw new RuntimeException("Este servicio requiere un thread para ejecutarse");
		}
		beforeProcessStart();
		// Inicio el hilo Java y le indico que empiece a ejecutar logica propia del thread
		backgroundProcess.comenzar();
		afterProcessStart();
	}

	/**
	 * Invocado después de que el proceso inició y tiene un estado consistente (puede interactuarse
	 * con él)
	 */
	protected void afterProcessStart() {
		// No es necesario implementarlo
	}

	/**
	 * Invocado antes de iniciar la ejecución del proceso pero después de crearlo.<br>
	 * Permite la inicialización de componentes adicionales antes de ejecutar todo
	 */
	protected void beforeProcessStart() {
		// No hacemos nada así no es necesario implementarlo
	}

	/**
	 * Crea y devuelve el proceso que será utilizado para ejecutar este servicio.<br>
	 * Normalmente el proceso es una subclase que agrega comportamiento especial
	 * 
	 * @return El proceso que se ejecutará en hilo a parte
	 */
	protected abstract BackgroundProcess createBackgroundThread();

	/**
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		if (backgroundProcess != null) {
			backgroundProcess.detener();
		}

		super.onDestroy();
	}

	/**
	 * Devuelve el proceso en background que realiza la ejecución de las tareas
	 * 
	 * @return El proceso en ejecución
	 */
	public BackgroundProcess getBackgroundProcess() {
		return backgroundProcess;
	}

	/**
	 * Se devuelve a si mismo como contexto
	 * 
	 * @return Este servicio como contexto
	 */
	public Context getContext() {
		return this;
	}
}
