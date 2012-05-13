/**
 * 28/01/2011 14:14:06 Darío L. García
 */
package ar.com.iron.android.helpers;

/**
 * Esta excepción es lanzada cuando se produce un error de comunicación en la conexión con un
 * servidor
 * 
 * @author D. García
 */
public class FailedConnectionException extends RuntimeException {
	private static final long serialVersionUID = 4592136650388211490L;

	public FailedConnectionException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public FailedConnectionException(String detailMessage) {
		super(detailMessage);
	}

	public FailedConnectionException(Throwable throwable) {
		super(throwable);
	}

}
