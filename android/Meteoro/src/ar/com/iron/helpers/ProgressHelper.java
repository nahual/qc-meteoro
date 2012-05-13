/**
 * 08/07/2011 22:47:49 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.com.iron.helpers;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 
 * @author D. García
 */
public class ProgressHelper {

	/**
	 * Genera y muestra un dialogo de progreso bloqueante que no es cancelable. Se debe llamar
	 * explicitamente a cancel para ocultarlo
	 * 
	 * @param contexto
	 *            El contexto del cual obtener los strings
	 * @param dialogTitleId
	 *            Id del recurso para el titulo
	 * @param dialogtextId
	 *            Id del recurso para el texto
	 * @return
	 */
	public static ProgressDialog showBlocking(final Context contexto, final int dialogTitleId, final int dialogtextId) {
		final CharSequence dialogTitle = contexto.getText(dialogTitleId);
		final CharSequence dialogText = contexto.getText(dialogtextId);
		return ProgressDialog.show(contexto, dialogTitle, dialogText, true, false);
	}

}
