/**
 * 22/01/2011 16:49:27 Darío L. García
 */
package ar.com.iron.concurrency;

/**
 * Esta excepción es lanzada cuando esperando un recurso el thread es interrumpido y se altera su
 * flujo normal de ejecución
 * 
 * @author D. García
 */
public class InterruptedWaitException extends RuntimeException {
	private static final long serialVersionUID = 711010854762527722L;

	public InterruptedWaitException(String mensaje, Exception e) {
		super(mensaje, e);
	}

}
