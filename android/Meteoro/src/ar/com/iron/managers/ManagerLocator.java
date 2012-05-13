package ar.com.iron.managers;

import ar.com.iron.caching.DefaultCachingInstantiator;
import ar.com.iron.caching.SimpleCachingConcurrentMap;

/**
 * Esta clase modela la forma de acceder a las instancias de los objetos managers.<br>
 * Existe una unica instancia del ManagerLocator y esta modelado como un singleton. Se registran
 * todas las instancias de los managers vinculados a su clase. <br>
 * Existira solo una instancia de manager por cada tipo.<br>
 * Cada uno de los managers se referencian desde un mapa donde la key es la clase del mismo.
 * 
 * @author Maximiliano Vazquez
 * 
 */
public class ManagerLocator {

	/**
	 * Constantes con los nombres de las variables para aceeder por reflection desde tests
	 */
	public static final String registeredManagers_FIELD = "registeredManagers";

	/**
	 * Este mapa sirve de indice para acceder a las instancias de los managers a partir de su clase
	 * usada como key.<br>
	 * Este mapa cuenta con la ventaja de soportar multithreading para accesos concurrentes.
	 * Segmenta el mapa de manera que no se pisen threads accediendo a distintos values, y
	 * sincroniza accesos al mismo value.<br>
	 * Además sabe como crear instancias cuando no existen previamente
	 */
	private static final SimpleCachingConcurrentMap<Class<? extends Manager>, Manager> registeredManagers = new SimpleCachingConcurrentMap<Class<? extends Manager>, Manager>(
			DefaultCachingInstantiator.<Manager> getInstance());

	/**
	 * Obtiene la única instancia del manager pedido a través de su clase.<br>
	 * Este método crea la instancia del manager si no existe y se asegura que la creación sea unica
	 * y thread-safe.
	 * 
	 * @param <T>
	 *            Tipo del manager
	 * @param managerClass
	 *            Clase que permite identificar o instanciar el manager pedido
	 * @return La instancia singleton del manager
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Manager> T getManager(Class<T> managerClass) {
		// Es seguro castear por que la instacia se crea a partir de la clase
		return (T) registeredManagers.getOrCreateIfNull(managerClass);
	}

}
