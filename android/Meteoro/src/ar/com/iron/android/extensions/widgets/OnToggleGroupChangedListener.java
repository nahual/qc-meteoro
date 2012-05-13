/**
 * 24/01/2011 15:48:13 Darío L. García
 */
package ar.com.iron.android.extensions.widgets;

import android.widget.ToggleButton;

/**
 * Esta interfaz define el comportamiento esperado cuando cambia el botón elegido de un conjunto
 * {@link HorizontalToggleGroup}
 * 
 * @author D. García
 */
public interface OnToggleGroupChangedListener {

	/**
	 * Invocado al cambiar el botón elegido por el usuario del grupo
	 * 
	 * @param indiceBotonElegido
	 *            Indice empezando en 0 del boton elegido
	 * @param botonElegido
	 *            El botón activo de todo el grupo
	 */
	void onToggleChange(int indiceBotonElegido, ToggleButton botonElegido);

}
