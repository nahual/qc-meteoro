package ar.com.iron.caching;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Esta clase permite implementar un esquema de cacheo muy simple utilizando un mapa concurrente
 * como base para instancias de objetos que si no existen se pueden crear on-demand.<br>
 * 
 * @author D.L. García
 */
public class SimpleCachingConcurrentMap<K, V> extends ConcurrentHashMap<K, V> {
	private static final long serialVersionUID = 9192792199658175644L;

	/**
	 * Generador de los values faltantes cuando no existe la key
	 */
	private CachingInstantiator<K, V> instantiator;

	/**
	 * Inicializador opcional de las instancias creadas
	 */
	private CachingInitializer<V> initializer;

	/**
	 * Constructor que toma el instanciador utilizado para generar las instancias faltantes. Se
	 * asume que no es necesaria inicializacion
	 */
	public SimpleCachingConcurrentMap(CachingInstantiator<K, V> instantiator) {
		this.instantiator = instantiator;
	}

	/**
	 * Constructor que define el instanciador y el inicializador de las instancias creadas
	 */
	public SimpleCachingConcurrentMap(CachingInstantiator<K, V> instantiator, CachingInitializer<V> initializer) {
		this(instantiator);
		this.initializer = initializer;
	}

	/**
	 * Este método devuelve el objeto guardado en el mapa bajo la key, o crea una instancia nueva si
	 * no existe, utilizando el instanciador.<br>
	 * La creación se realiza sólo una vez sincronizando los hilos a través del objeto pasado. Es
	 * decir, sólo un hilo va a crear la instancia que no exista, y los demás quedaran bloqueados
	 * hasta que se termine la operación
	 * 
	 * @param key
	 *            Objeto qeu sirve de key y sincronización para la creación. No puede ser null
	 * @return La instancia guardada en este mapa
	 */
	public V getOrCreateIfNull(K key) {
		V valorAlmacenado = get(key);
		if (valorAlmacenado != null) {
			// Para la mayoría de las veces esta va a ser la rama ejecutada y no es
			// necesario usar sincronización
			return valorAlmacenado;
		}
		// Si llegamos a este punto en un entorno multithreading, podemos estar compitiendo con
		// otros threads.
		// Al sincronizar con la key:
		// - Sólo bloqueo los threads intentando crear el mismo value. Si hay otro thread
		// registrando el value va a tener que esperar a que termine ese.
		// - Si hay otro thread sincronizando contra esa key también que no tiene nada que ver con
		// este mapa (efecto no deseado) se podría bloquear también. Si ese thread tiene algo que la
		// instanciacion necesita se puede producir un deadlock
		synchronized (key) {
			// Volvemos a chequear dentro del sincronized para asegurarnos que somos los unicos que
			// estamos creando la instancia ya que el mapa esta vacio y nadie más se apropio de la
			// clase
			if (!containsKey(key)) {
				V valorCreado = instantiator.instanciar(key);

				// Sólo lo agrego si no está. Si ya hay una (cosa que no debería pasar en esta rama
				// del if), cree una instancia de más que se va a perder ya que usaré la instancia
				// creada previamente
				V valorPrevio = putIfAbsent(key, valorCreado);
				if (valorPrevio == null && initializer != null) {
					// Significa que no había uno, y por lo tanto debemos inicializar el que creamos
					initializer.initialize(valorCreado);
				}
			}
		}
		// Seamos o no los que instanciamos el value, accedemos a la única instancia válida
		valorAlmacenado = get(key);
		return valorAlmacenado;
	}

}
