package ar.com.iron.android.helpers;

import android.view.View;
import android.widget.CheckBox;

/**
 * Esta clase es un listener de click, que oculta la vista indicada en base al estado del check
 * asociado
 * 
 * @author D.L. García
 */
public class OcultadorDeVistaConCheckBoxListener extends OcultadorDeVistaAbstractListener {

	public OcultadorDeVistaConCheckBoxListener(View vista) {
		super(vista);
	}

	/**
	 * Verifica el estado del check y refleja el estado en la visibilidad de la vista
	 * 
	 * @param check
	 *            Check a controlar
	 */
	public void checkStatusNow(View disparador) {
		CheckBox check = (CheckBox) disparador;
		int visibilidad = View.VISIBLE;
		if (!check.isChecked()) {
			visibilidad = View.GONE;
		}
		vistaDependiente.setVisibility(visibilidad);
	}
}
