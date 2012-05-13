package ar.com.iron.persistence.transactions;

/**
 * Esta clase modela una unidad de ejecucion transaccional.<br>
 * Funciona en conjunto con {@link TransactionManager}.
 * 
 * @author Maximiliano Vazquez
 * 
 */
public interface TransactionBlock {

	/**
	 * Este método es ejecutado dentro del contexto de una transaccion activa
	 */
	Object execute();

}
