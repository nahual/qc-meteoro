/**
 * 28/03/2011 23:33:36 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.widgets.datetime;

/**
 * Esta interfaz permite recibir notificaciones al modificarse el valor de un campo de la fecha
 * 
 * @author D. García
 */
public interface OnDateFieldChangeListener {
	/**
	 * Invocado cuando se produce un cambio en un campo del calendario.<br>
	 * Al ser invocado este listener es posible que otros campos sean modificados para mantener la
	 * validez de la fecha
	 * 
	 * @param calendarField
	 *            Campo modificado del calendar
	 * @param newDelta
	 *            Variación producida en el campo. Puede ser positiva o negativa
	 */
	public void onDateFieldChanged(int calendarField, int newDelta);

	/**
	 * Invocado cuando se selecciona un campo para la edición
	 * 
	 * @param dateFieldController
	 *            El campo seleccionado
	 */
	public void onDateFieldSelected(DateFieldController dateFieldController);
}
