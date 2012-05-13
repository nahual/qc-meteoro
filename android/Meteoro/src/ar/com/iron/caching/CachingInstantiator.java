package ar.com.iron.caching;

/**
 * Esta interfaz representa un instanciador de elementos cacheables.<br>
 * Este instanciador sabe como crear una nueva instancia de V, a partir de la instancia de K pasada.
 * 
 * @author D.L. García
 * @param <K>
 *            Tipo de objeto usado como base para la instanciacion
 * @param <V>
 *            Tipo del objeto instanciado
 */
public interface CachingInstantiator<K, V> {

	/**
	 * Crea una nueva instancia para almacenar en la caché
	 * 
	 * @param base
	 *            Objeto a partir del cual se creará la instancia
	 * @return La instancia creada
	 */
	public V instanciar(K base);
}
