/**
 * 05/03/2011 17:59:02 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.persistence;

/**
 * Esta excepción es lanzada cuando se produce un error con la operación realizada para persistencia
 * 
 * @author D. García
 */
public class PersistenceException extends RuntimeException {
	private static final long serialVersionUID = 4895328673037779910L;

	public PersistenceException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public PersistenceException(String detailMessage) {
		super(detailMessage);
	}

	public PersistenceException(Throwable throwable) {
		super(throwable);
	}

}
