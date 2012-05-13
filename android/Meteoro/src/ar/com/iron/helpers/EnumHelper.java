/**
 * 07/03/2011 18:03:58 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package ar.com.iron.helpers;

/**
 * Esta clase ofrece alguno métodos comunes a la hora de trabajar con enums
 * 
 * @author D. García
 */
public class EnumHelper {

	/**
	 * Busca el enum con el nombre indicado en el array pasado, devolviendolo si lo encuentra
	 * 
	 * @param values
	 *            El conjunto de todos los enums
	 * @param name
	 *            El nombre del enum buscado
	 * @return El enum encontrado o null si no existe ninguno
	 */
	public static <E extends Enum<E>> E findByNameIn(E[] values, String name) {
		for (E enumerated : values) {
			if (enumerated.name().equals(name)) {
				return enumerated;
			}
		}
		return null;
	}

}
