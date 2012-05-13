/**
 * 20/06/2011 02:09:35 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.connections;

/**
 * Esta excepción es lanzada cuando un request pedido no puede completarse por un error
 * 
 * @author D. García
 */
public class FailedRequestException extends RuntimeException {
	private static final long serialVersionUID = 7600773013995182266L;

	/**
	 * Indica si esta excepción es producida por una desconexión del S.O. a internet (no es posible
	 * satisfacer ningún tipo de request)
	 */
	public final boolean disconnected;

	public FailedRequestException(String detailMessage, boolean disconnected, Throwable throwable) {
		super(detailMessage, throwable);
		this.disconnected = disconnected;
	}

	public FailedRequestException(String detailMessage, boolean disconnected) {
		super(detailMessage);
		this.disconnected = disconnected;
	}

	public FailedRequestException(boolean disconnected, Throwable throwable) {
		super(throwable);
		this.disconnected = disconnected;
	}

}
