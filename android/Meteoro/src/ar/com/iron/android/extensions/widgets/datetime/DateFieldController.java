/**
 * 28/03/2011 23:01:18 Copyright (C) 2011 Darío L. García
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

import java.util.Calendar;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import ar.com.iron.helpers.ViewHelper;

/**
 * Esta clase representa un control de UI para editar un campo de la fecha
 * 
 * @author D. García
 */
public class DateFieldController {

	private int calendarField;
	private ImageButton plusButton;
	private ImageButton minusButton;
	private TextView fieldValueShow;

	private OnDateFieldChangeListener listener;

	public OnDateFieldChangeListener getListener() {
		return listener;
	}

	public void setListener(OnDateFieldChangeListener listener) {
		this.listener = listener;
	}

	public static DateFieldController create(Calendar modifiedCalendar, int calendarField, int plusButtonId,
			int minusButtonId, int fieldValueShowId, View parentView) {
		ImageButton plusButton = ViewHelper.findImageButton(plusButtonId, parentView);
		ImageButton minusButton = ViewHelper.findImageButton(minusButtonId, parentView);
		TextView fieldValueText = ViewHelper.findTextView(fieldValueShowId, parentView);
		return create(modifiedCalendar, calendarField, plusButton, minusButton, fieldValueText);
	}

	public static DateFieldController create(Calendar modifiedCalendar, int calendarField, ImageButton plusButton,
			ImageButton minusButton, TextView fieldValueShow) {
		DateFieldController control = new DateFieldController();
		control.calendarField = calendarField;
		control.fieldValueShow = fieldValueShow;
		control.minusButton = minusButton;
		control.plusButton = plusButton;
		control.configurarHandlers();
		return control;
	}

	/**
	 * Configura los listeners y handlers de eventos de cada control para la edición
	 */
	private void configurarHandlers() {
		this.minusButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onRestarApretado();
			}
		});
		this.plusButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onSumarApretado();
			}
		});
		this.fieldValueShow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onValorApretado();
			}
		});
	}

	/**
	 * Invocado al hacer click el usuario sobre el valor del campo
	 */
	protected void onValorApretado() {
		if (this.listener != null) {
			this.listener.onDateFieldSelected(this);
		}
	}

	/**
	 * Invocado al ser apretado el botón para sumar
	 */
	protected void onSumarApretado() {
		notificarCambioDatos(1);
	}

	/**
	 * Notifica al listener del cambio de estado para que actualice la vista general
	 * 
	 * @param delta
	 *            Variación producida
	 */
	private void notificarCambioDatos(int delta) {
		if (listener != null) {
			listener.onDateFieldChanged(calendarField, delta);
		}
	}

	/**
	 * Invocado al ser apretado el botón para restar
	 */
	protected void onRestarApretado() {
		notificarCambioDatos(-1);
	}

	/**
	 * Cambia el estado visible de este control a sólo texto no editable
	 */
	public void mostrarNoEditable() {
		setEditable(false);
	}

	/**
	 * Cambia el estado de este control para que sea editable o no de acuerdo al flag pasado
	 * 
	 * @param editable
	 *            true si este control debe ser editable
	 */
	private void setEditable(boolean editable) {
		int editableVisibility = editable ? View.VISIBLE : View.GONE;
		this.plusButton.setVisibility(editableVisibility);
		this.minusButton.setVisibility(editableVisibility);
	}

	/**
	 * Cambia el estado de este control para que sea editable por el usuario
	 */
	public void mostrarEditable() {
		setEditable(true);
	}

	public int getCalendarField() {
		return calendarField;
	}

	/**
	 * Establece el valor mostrado para este campo de fecha
	 * 
	 * @param fieldValue
	 */
	public void setValue(String fieldValue) {
		this.fieldValueShow.setText(fieldValue);
	}

}
