package ar.com.iron.caching;

import ar.com.iron.reflection.ReflectionUtils;

/**
 * Instanciador por defecto que crea la instancia a partir de la clase pasada usando reflection con
 * el constructor por defecto
 * 
 * @author D.L. García
 */
public class DefaultCachingInstantiator implements CachingInstantiator<Class<?>, Object> {

	/**
	 * Instancia singleton creada con la clase
	 */
	private static final DefaultCachingInstantiator instance = new DefaultCachingInstantiator();

	/**
	 * @see com.yah.commons.caching.CachingInstantiator#instanciar(java.lang.Object)
	 */
	public Object instanciar(Class<?> clase) {
		return ReflectionUtils.instanciar(clase);
	}

	/**
	 * Devuelve la instancia de este instantiator que sirve para cualquier clase con constructor
	 * niládico
	 * 
	 * @param <T>
	 *            Tipo a instanciar
	 * @return La instancia
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> CachingInstantiator<Class<? extends T>, T> getInstance() {
		return (CachingInstantiator) instance;
	}

}
