package ar.com.iron.daos;

import ar.com.iron.caching.DefaultCachingInstantiator;
import ar.com.iron.caching.SimpleCachingConcurrentMap;
import ar.com.iron.managers.ManagerLocator;

/**
 * Esta clase sigue exactamente la misma logica que {@link ManagerLocator}.<br>
 * Pero para localizar los daos de la aplicación.
 * 
 * @author Maximiliano Vazquez
 */
public class DaoLocator {

	private static final SimpleCachingConcurrentMap<Class<? extends Dao<?>>, Dao<?>> registeredDaos = new SimpleCachingConcurrentMap<Class<? extends Dao<?>>, Dao<?>>(
			DefaultCachingInstantiator.<Dao<?>> getInstance());

	/**
	 * Devuelve la instancia de dao existente correspondiente a la clase pasada, creando una nueva
	 * instancia si no existe.<br>
	 * Se asegura que la creación sea única para un entorno multi-threading
	 * 
	 * @param <D>
	 *            Tipo del dao esperado
	 * @param daoClass
	 *            Clase concreta del dao esperado que permite identificar o instanciarlo
	 * @return El dao existente previamente o creado a partir de esta llamada
	 */
	@SuppressWarnings("unchecked")
	public static <D extends Dao<?>> D getDao(Class<D> daoClass) {
		return (D) registeredDaos.getOrCreateIfNull(daoClass);
	}

}
