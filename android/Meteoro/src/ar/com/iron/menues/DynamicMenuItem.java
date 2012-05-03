/**
 * 20/04/2011 03:48:17 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.menues;

/**
 * Esta interfaz es aplicable a opciones de menú que pueden cambiar su estado durante la ejecución o
 * incluso desaparecer de las opciones disponibles
 * 
 * @author D. García
 */
public interface DynamicMenuItem {

	/**
	 * @return si este item debería encontrarse habilitado o no (si está deshabilitado se ve pero no
	 *         es seleccionable)
	 */
	public abstract boolean isEnabled();

	/**
	 * @return si este item debería verse entre las opciones mostradas (si no es visible tampoco es
	 *         seleccionable)
	 */
	public abstract boolean isVisible();

}