package ar.com.iron.reflection;

/**
 * Esta clase simplifica alguna de las operacion de relfection
 * 
 * @author D.L. García
 */
public class ReflectionUtils {
	/**
	 * Crea una nueva instancia de la clase pasada usando el constructor niladico. Tira excepciones
	 * {@link UnExpectedException} si se produce un error al instanciar
	 * 
	 * @param clase
	 *            Clase a instanciar
	 * @return La instancia creada
	 */
	public static <T> T instanciar(Class<T> clase) {
		try {
			return clase.newInstance();
		} catch (IllegalAccessException e) {
			throw new RuntimeException("La clase pasada [" + clase
					+ "]no es instanciable con el constructor por defecto", e);
		} catch (InstantiationException e) {
			throw new RuntimeException("Se produjo un error en el constructor por defecto[" + clase + "]", e);
		}
	}

}
