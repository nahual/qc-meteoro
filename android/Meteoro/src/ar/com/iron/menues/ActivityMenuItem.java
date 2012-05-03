/**
 * 19/04/2011 23:52:28 Copyright (C) 2011 Darío L. García
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Esta interfaz representa una opción de menú de un activity
 * 
 * @author D. García
 */
public interface ActivityMenuItem<A extends Activity> extends CustomMenuItem {

	/**
	 * Indica el intent disparado al seleccionar esta opción.<br>
	 * Si se define el intent, el método {@link #onSelection(Activity)} no será invocado.<br>
	 * El intent devuelto será invocado mediante {@link Activity#startActivity(Intent)}
	 * 
	 * @return el Intent que se enviará luego de la selección de este item, o null si no se usa uno
	 */
	Intent getFiredActivityIntent(Context contexto);

	/**
	 * Determina la lógica a ejecutar cuando se selecciona un item del menu contextual.<br>
	 * 
	 * @return false si el evento de selección no es tratado
	 */
	boolean onSelection(A activity);

	/**
	 * Identificador opcional del ícono a utilizar en esta opción de menú
	 * 
	 * @return Identificador del drawable a utilizar como ícono
	 */
	Integer getIconId();

}
