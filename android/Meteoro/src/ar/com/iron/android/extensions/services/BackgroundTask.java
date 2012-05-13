/**
 * 02/03/2011 01:02:40 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package ar.com.iron.android.extensions.services;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * Esta interfaz representa una tarea ejecutada en background por un proceso secundario
 * 
 * @author D. García
 * @param <R>
 *            Tipo del resultado
 */
public abstract class BackgroundTask<R> implements Runnable {

	private Context backgroundContext;

	/**
	 * Handler que permite derivar tareas al thread principal
	 */
	private Handler mainHandler;

	public Handler getMainHandler() {
		return mainHandler;
	}

	public void setMainHandler(Handler mainHandler) {
		this.mainHandler = mainHandler;
	}

	public Context getBackgroundContext() {
		return backgroundContext;
	}

	public void setBackgroundContext(Context backgroundContext) {
		this.backgroundContext = backgroundContext;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		procesarTareaEnBg();
	}

	/**
	 * Ejecuta la tarea en el hilo del servicio en background con respecto al hilo principal, y
	 * deriva el resultado al hilo principal. Si se produce un error se dirige la excepción
	 */
	private void procesarTareaEnBg() {
		try {
			// Ejecutamos la tarea en este thread
			final R result = execute(backgroundContext);
			// Devolvemos el resultado al thread principal
			forwardResultToMainHandler(result);
		} catch (Exception e) {
			forwardErrorToMainHandler(e);
		}
	}

	/**
	 * Envía el error producido durante la ejecución para ser tratado en el thread principal
	 * 
	 * @param e
	 *            La excepción producida al ejecutar el código en background
	 */
	private void forwardErrorToMainHandler(final Exception e) {
		if (mainHandler == null) {
			Log.e("BackgroundTask", "No existe handler al thread principal para devolver un error", e);
			return;
		}
		Runnable procesarError = new Runnable() {
			public void run() {
				onFailure(e);
			}
		};
		mainHandler.post(procesarError);
	}

	/**
	 * Envía el resultado pasado al thread principal, invocando en ese thread el método
	 * {@link #onSuccess(Object)} definido en esta clase
	 * 
	 * @param result
	 *            El resultado del código ejecutado en bacground recibido por el thread principal
	 */
	private void forwardResultToMainHandler(final R result) {
		if (mainHandler == null) {
			// No hay a quién devolverle el resultado
			return;
		}
		// Le derivamos el resultado al thread invocante mediante su handler
		Runnable procesarOnSuccess = new Runnable() {
			public void run() {
				onSuccess(result);
			}
		};
		mainHandler.post(procesarOnSuccess);
	}

	/**
	 * Invocado por el thread en background que permite la ejecución en paralelo.<br>
	 * El contexto normalmente representa una servicio en background
	 * 
	 * @param backgroundContext
	 *            El contexto desde el cual interactuar con el sistema android
	 * @return El objeto considerado como resultado de la tarea que será devuelvo al thread
	 *         principal
	 */
	public abstract R execute(Context backgroundContext);

	/**
	 * Este método es invocado al producirse una excepción en el código de la tarea para tratar el
	 * error.<br>
	 * Este código es ejecutado en el thread principal.
	 * 
	 * @param exceptionThrown
	 *            Excepción lanzada durante la ejecución del código {@link #execute(Context)}
	 */
	public void onFailure(Exception exceptionThrown) {
		throw new RuntimeException(exceptionThrown);
	}

	/**
	 * Invocado en el thread principal cuando se obtiene el resultado de la tarea ejecutada en
	 * background
	 * 
	 * @param result
	 *            El resultado devuelto por el código al ser ejecutado en background
	 */
	public void onSuccess(R result) {
		// Por defecto no hacemos nada para que no sea necesario implementarlo
	}
}
