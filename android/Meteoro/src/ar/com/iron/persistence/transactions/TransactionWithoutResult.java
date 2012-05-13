package ar.com.iron.persistence.transactions;

/**
 * Esta clase es una facilida para implementar transacciones que no generar un resultado.<br>
 * Copiado de como lo usa spring
 * 
 * @author D. Garcia
 */
public abstract class TransactionWithoutResult implements TransactionBlock {

	/**
	 * @see com.yah.commons.android.persistence.db4o.mobile.persistence.db4o.TransactionBlock#executeInBg()
	 */
	public Object execute() {
		doExecute();
		return null;
	}

	/**
	 * Metodo a implementar por subclases que es ejecutado en una transaccion sin devolver un valor
	 */
	protected abstract void doExecute();

}
