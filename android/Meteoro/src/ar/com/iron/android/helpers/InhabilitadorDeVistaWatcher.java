package ar.com.iron.android.helpers;

import java.util.ArrayList;
import java.util.List;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Esta clase controla el texto de uno o varios EditText para deshabilitar un botón si están
 * vacíos., Para que el botón esté activo todos los EditText tienen que tener contenido
 * 
 * @author D. García
 */
final class InhabilitadorDeVistaWatcher implements TextWatcher {
	private View affectedView;

	private List<EditText> editTexts;

	public void onTextChanged(CharSequence texto, int start, int before, int count) {
		checkButtonNow(texto);
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	public void afterTextChanged(Editable editText) {
	}

	public List<EditText> getEditTexts() {
		if (editTexts == null) {
			editTexts = new ArrayList<EditText>();
		}
		return editTexts;
	}

	/**
	 * Verifica que el botón tenga un estado coherente al elemento Editable pasado
	 * 
	 * @param texto2
	 *            Editable de referencia
	 */
	private void checkButtonNow(CharSequence texto2) {
		// Antes que el resto chequeo el texto editado para optimizar
		String texto = texto2.toString().trim();
		boolean hayAlgoEscrito = texto.length() > 0;
		if (hayAlgoEscrito) {
			hayAlgoEscrito = restoDeEditTextsConTexto();
		}
		affectedView.setEnabled(hayAlgoEscrito);
	}

	/**
	 * Verifica que el botón este activado o no de acuerdo a los editTexts
	 */
	public void checkButtonNow() {
		boolean hayAlgoEscrito = restoDeEditTextsConTexto();
		affectedView.setEnabled(hayAlgoEscrito);
	}

	/**
	 * Revisa si los edit texts pasados, todos tienen algún texto
	 */
	private boolean restoDeEditTextsConTexto() {
		List<EditText> controlados = getEditTexts();
		for (EditText controlado : controlados) {
			Editable editable = controlado.getText();
			// Chequeo sin trimmear para optimizar
			if (editable.length() == 0) {
				return false;
			}
			// Trimeo por las dudas que hayan espacios
			if (editable.toString().trim().length() == 0) {
				return false;
			}
		}
		return true;
	}

	public static InhabilitadorDeVistaWatcher create(View view, EditText... editTexts) {
		InhabilitadorDeVistaWatcher inhabilitador = new InhabilitadorDeVistaWatcher();
		inhabilitador.affectedView = view;
		for (EditText editText : editTexts) {
			inhabilitador.getEditTexts().add(editText);
		}
		return inhabilitador;
	}

}