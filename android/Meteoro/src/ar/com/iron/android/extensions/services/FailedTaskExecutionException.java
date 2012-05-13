/**
 * 28/01/2011 12:43:45 Darío L. García
 */
package ar.com.iron.android.extensions.services;

/**
 * Esta excepción es lanzada por una tarea en servicio cronado cuando no pudo terminar exitósamente
 * (sea por la razón que sea), y debe reintentar su ejecución después del intervalo de espera
 * 
 * @author D. García
 */
public class FailedTaskExecutionException extends RuntimeException {
	private static final long serialVersionUID = -7438461628544924463L;

	public FailedTaskExecutionException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public FailedTaskExecutionException(String detailMessage) {
		super(detailMessage);
	}

	public FailedTaskExecutionException(Throwable throwable) {
		super(throwable);
	}

}
