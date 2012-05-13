package ar.com.iron.android.helpers;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * Esta clase es un listener de click, que oculta la vista indicada en base al estado de una
 * determinado tipo de view, para ello se debe implementar el método checkStatusNow() para cada tipo
 * de vista
 * 
 * @author sfernandez
 */
public abstract class OcultadorDeVistaAbstractListener implements OnClickListener {

	protected View vistaDependiente;

	public OcultadorDeVistaAbstractListener(View vista) {
		vistaDependiente = vista;
	}

	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View disparador) {
		checkStatusNow(disparador);
	}

	/**
	 * Verifica el estado del check y refleja el estado en la visibilidad de la vista
	 * 
	 * @param check
	 *            Check a controlar
	 */
	public abstract void checkStatusNow(View disparador);
}
