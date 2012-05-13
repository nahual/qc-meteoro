/**
 * 22/01/2011 16:24:35 Darío L. García
 */
package ar.com.iron.concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Esta clase representa un mutex (Mutual-exclusion) para sincronizar el acceso de código a algún
 * recurso. Utilizando esta clase se evita que dos threads tengan acceso a un tercer recurso al
 * mismo tiempo.<br>
 * Esta clase simplemente abstrae el uso de {@link Lock} para simplificar su uso en condiciones de
 * sincronizar el acceso a recursos
 * 
 * @author D. García
 */
public class CodeMutex {

	/**
	 * Timeout por defecto. 1h
	 */
	public static final long DEFAULT_TIMEOUT_LIMIT = 1000 * 60 * 60;

	/**
	 * Lock para excluir los accesos
	 */
	private Lock resourceLock;

	private String resourceName;

	/**
	 * Tiempo límite que se espera el lock después del cuál se considera que el recurso esta
	 * "colgado"
	 */
	private long timeoutLimit;

	public String getResourceName() {
		return resourceName;
	}

	/**
	 * Nombre opcional del recurso.<br>
	 * Este nombre sirve sólo para fines identificatorios
	 * 
	 * @param resourceName
	 *            Nombre del recurso sincronizado con este mutex
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * @return the timeoutLimit
	 */
	public long getTimeoutLimit() {
		return timeoutLimit;
	}

	/**
	 * Establece el tiempo de espera en milisegundos por un recurso. Superado este tiempo se
	 * considerará que el recurso está bloqueado.<br>
	 * El tiempo fijado debe ser exageradamente grande respecto a cuánto puede tardar otro hilo en
	 * devolver el recurso para que no se den falsos negativos. El valor establecido debe ser
	 * considerado el punto límite de tiempo que es inaceptable para ese recurso
	 * 
	 * @param timeoutLimit
	 *            El tiempo límite en milisegundos. El default es 1h
	 */
	public void setTimeoutLimit(long timeoutLimit) {
		this.timeoutLimit = timeoutLimit;
	}

	/**
	 * @return the resourceLock
	 */
	public Lock getResourceLock() {
		return resourceLock;
	}

	public static CodeMutex create() {
		CodeMutex mutex = new CodeMutex();
		mutex.resourceLock = new ReentrantLock();
		mutex.timeoutLimit = DEFAULT_TIMEOUT_LIMIT;
		return mutex;
	}

	/**
	 * Ejecuta el código pasado en forma exclusiva asegurando que ningún otro thread utilizando este
	 * mismo mutex comparta el recurso.<br>
	 * Si el recurso está siendo usado por otro thread, este espera el tiempo indicado por
	 * {@link #timeoutLimit}, si se supera ese tiempo (debe ser suficientemente grande) se considera
	 * que el recurso está colgado y no es posible accederlo.
	 * 
	 * @param code
	 *            Código a ejecutar exclusivamente
	 * @throws CantWaitResourceException
	 *             Si se supera el tiempo límite de espera y se debe considerar el recurso como
	 *             inaccesible
	 * @throws InterruptedWaitException
	 *             Si el thread actual es interrumpido mientras esperaba por obtener acceso al
	 *             recurso
	 */
	public void runExclusively(Runnable code) throws CantWaitResourceException, InterruptedWaitException {
		try {
			resourceLock.tryLock(timeoutLimit, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new InterruptedWaitException("El thread actual fue interrumpido mientras esperaba al recurso", e);
		}
		try {
			code.run();
		} finally {
			resourceLock.unlock();
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName());
		if (this.resourceName != null) {
			builder.append("[\"");
			builder.append(resourceName);
			builder.append("\"]");
		}
		builder.append("{ timeout: ");
		builder.append(this.timeoutLimit);
		builder.append(" ms.}");
		return builder.toString();
	}
}
