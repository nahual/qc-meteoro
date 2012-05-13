/**
 * 07/03/2011 21:44:26 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;

/**
 * Esta clase permite implementar un listener de diálogos que pueden ser cancelados o aceptados.<br>
 * Esta clase ofrece una implementación de los listeners default para ser utilizado como cualquier
 * otro listener pero que se encarga de derivar en un ok o cancel, que será tratado por la subclase.<br>
 * <br>
 * Para usar este listener es necesario declararlo en ambos eventos:<br>
 * <br>
 * edicionDialog.setOnCancelListener(dialogListener);
 * edicionDialog.setOnDismissListener(dialogListener);
 * 
 * 
 * @author D. García
 * @param <D>
 *            Tipo de diálogo para el que este listener es aplicado
 */
public abstract class OnOkCancelDialogListener<D extends Dialog> implements OnCancelListener, OnDismissListener {

	private boolean wasCancelled = false;

	/**
	 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
	 */
	public void onCancel(DialogInterface dialog) {
		wasCancelled = true;
	}

	/**
	 * @see android.content.DialogInterface.OnDismissListener#onDismiss(android.content.DialogInterface)
	 */
	@SuppressWarnings("unchecked")
	public void onDismiss(DialogInterface dialog) {
		if (wasCancelled) {
			onDialogCancelled((D) dialog);
		} else {
			onDialogAcepted((D) dialog);
		}
	}

	/**
	 * Invocado cuando el diálogo es cerrado y aceptado
	 * 
	 * @param dialog
	 *            El diálogo aceptado
	 */
	public abstract void onDialogAcepted(D dialog);

	/**
	 * Invocado cuando el diálogo se cierra y fue cancelado
	 * 
	 * @param dialog
	 *            Diálogo cancelado
	 */
	public void onDialogCancelled(D dialog) {
		// Vacío para no obligar implementación
	};

}
