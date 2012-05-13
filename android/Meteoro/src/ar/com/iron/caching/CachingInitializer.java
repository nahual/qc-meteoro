package ar.com.iron.caching;

/**
 * Esta interfaz representa el codigo inicializador de la instancia creada en cache que permite
 * hacer algo más sobre la instancia una vez creada
 * 
 * @author D.L. García
 */
public interface CachingInitializer<V> {

	/**
	 * Realiza la inicialización de una instancia recien creada
	 * 
	 * @param valor
	 *            Objeto a inicializar para la caché
	 */
	public void initialize(V valor);

}
