/**
 * 08/03/2011 15:23:00 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.helpers;

import android.app.Dialog;
import android.view.View;

/**
 * Esta clase ofrece varios métodos utilitarios para tratar con diálogos
 * 
 * @author D. García
 */
public class DialogHelper {

	/**
	 * Configura los botones indicados por id para que acepten o cancelen el diálogo actual
	 * 
	 * @param dialog
	 *            Diálogo que se cancelará o aceptará
	 * @param aceptarButtonId
	 *            Id de la vista que corresponde al botón de aceptar
	 * @param cancelarButtonId
	 *            Id de la vista que corresponde a la botón de cancelar
	 * @param contentView
	 *            Vista contenedora de los botones
	 */
	public static void setupOkCancelButtonsFor(final Dialog dialog, int aceptarButtonId, int cancelarButtonId,
			View contentView) {
		View aceptarButton = contentView.findViewById(aceptarButtonId);
		aceptarButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		View cancelarButton = contentView.findViewById(cancelarButtonId);
		cancelarButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
	}

}
