/**
 * 24/01/2011 11:25:59 Darío L. García
 */
package ar.com.iron.helpers;

/**
 * Esta excepción se produce cuando el helper no puede copiar un archivo
 * 
 * @author D. García
 */
public class CantCopyException extends RuntimeException {
	private static final long serialVersionUID = 3566022579154402627L;

	public CantCopyException(String message, Exception e) {
		super(message, e);
	}

	public CantCopyException(String message) {
		super(message);
	}

}
