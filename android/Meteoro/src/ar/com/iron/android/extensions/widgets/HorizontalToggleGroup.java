/**
 * 24/01/2011 15:35:03 Darío L. García
 */
package ar.com.iron.android.extensions.widgets;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

/**
 * Esta clase emula el comportamiento de un radio group de botones pero utilizando botones de
 * estado. Sólo se puede activar uno de los botones de todo el grupo.<br>
 * Permite que los botones se organicen horizontalmente con un texto encima de cada botón.<br>
 * 
 * @author D. García
 */
public class HorizontalToggleGroup {

	private ToggleButton[] botones;

	private ToggleButton selectedButton;

	private OnToggleGroupChangedListener listener;

	public OnToggleGroupChangedListener getListener() {
		return listener;
	}

	public void setListener(final OnToggleGroupChangedListener listener) {
		this.listener = listener;
	}

	/**
	 * Crea el grupo de botones como un radio group de HTML, de manera que sólo uno puede estar
	 * activo a la vez. Se modifica el onclicklistener de cada botón para alterar el comportamiento
	 * 
	 * @param botones
	 *            Los botones que forman parte del grupo
	 * @return El grupo creado
	 */
	public static HorizontalToggleGroup create(final ToggleButton... botones) {
		final HorizontalToggleGroup group = new HorizontalToggleGroup();
		group.botones = botones;

		for (final ToggleButton boton : botones) {
			boton.setOnClickListener(new OnClickListener() {
				public void onClick(final View v) {
					final ToggleButton boton = (ToggleButton) v;
					group.onBotonClickeado(boton);
				}
			});
		}
		return group;
	}

	/**
	 * Ejecutado al clickear el usuario sobre un botón del conjunto
	 * 
	 * @param boton
	 *            El botón clickeado
	 */
	protected void onBotonClickeado(final ToggleButton boton) {
		if (boton == selectedButton) {
			// No cambiamos nada
			return;
		}
		changeActiveButton(boton);
	}

	/**
	 * Ejecutado cuando el usuario elige el botón indicado
	 * 
	 * @param botonElegido
	 *            El botón elegido por el usuario
	 */
	private void changeActiveButton(final ToggleButton botonElegido) {
		selectedButton = botonElegido;
		int indiceBotonElegido = 0;
		for (int i = 0; i < botones.length; i++) {
			final ToggleButton boton = botones[i];
			boolean nuevoEstado = false;
			if (boton == botonElegido) {
				nuevoEstado = true;
				indiceBotonElegido = i;
			}
			boton.setChecked(nuevoEstado);
		}
		if (listener != null) {
			listener.onToggleChange(indiceBotonElegido, botonElegido);
		}
	}

}
