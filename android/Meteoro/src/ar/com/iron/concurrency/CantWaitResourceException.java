/**
 * 22/01/2011 16:42:03 Darío L. García
 */
package ar.com.iron.concurrency;

/**
 * Esta excepción es lanzada cuando se considera que el recurso está colgado, y por lo tanto no
 * puede accederse a él
 * 
 * @author D. García
 */
public class CantWaitResourceException extends RuntimeException {
	private static final long serialVersionUID = -418681998385251313L;

}
