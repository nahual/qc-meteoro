package ar.com.iron.android.helpers;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Esta clase posee algunos métodos útiles para tratar los widgets de pantalla
 * 
 * @author D. García
 */
public class WidgetHelper {

	/**
	 * Vincula los EditText con el Botón pasados de manera que el botón se habilite sólo si se
	 * ingresó algún texto en todos los EditTexts.<br>
	 * El texto evaluado es trimeado primero.
	 * 
	 * @param boton
	 *            Vista que se inhabilitará si no hay texto
	 * @param editTexts
	 *            Cuadros de texto que serán tomados en cuenta para habilitar o deshabilitar el
	 *            Botón
	 */
	public static void habilitarBotonEnPresenciaDeTexto(View boton, EditText... editTexts) {
		InhabilitadorDeVistaWatcher inhabilitadorCuandoVacio = InhabilitadorDeVistaWatcher.create(boton, editTexts);
		for (EditText editText : editTexts) {
			editText.addTextChangedListener(inhabilitadorCuandoVacio);
		}
		inhabilitadorCuandoVacio.checkButtonNow();
	}

	/**
	 * Este método vincula el check pasado agregando un listener que oculta la vista indicada al
	 * estar el check desactivado
	 * 
	 * @param checkBox
	 *            Checkbox que en estado activado muestra la vista
	 * @param vistaDependiente
	 *            Vista a ocultar si el check está desactivado
	 */
	public static void ocultarVistaDesdeCheck(CheckBox checkBox, View vistaDependiente) {
		OcultadorDeVistaConCheckBoxListener ocultadorCuandoDesactivado = new OcultadorDeVistaConCheckBoxListener(
				vistaDependiente);
		checkBox.setOnClickListener(ocultadorCuandoDesactivado);
		ocultadorCuandoDesactivado.checkStatusNow(checkBox);
	}

	/**
	 * Este método es una facilidad para los renderes de items en una grilla que sólo tienen un
	 * textView al cuál le configuran un ícono arriba y un texto debajo
	 * 
	 * @param gridItemView
	 *            Vista obtenida de la grilla que es en realidad un TextView
	 * @param itemDescription
	 *            Descripción del item debajo del ícono
	 * @param itemIconIdOrDrawable
	 *            Id o drawable para usar en el ícono
	 */
	public static void renderGridItem(View gridItemView, String itemDescription, Object itemIconIdOrDrawable) {
		if (!(gridItemView instanceof TextView)) {
			throw new IllegalArgumentException("La vista pasada de la grilla debe ser un textview. Es: " + gridItemView);
		}
		TextView gridItem = (TextView) gridItemView;
		gridItem.setText(itemDescription);
		if (itemIconIdOrDrawable instanceof Number) {
			Number itemIconId = (Number) itemIconIdOrDrawable;
			gridItem.setCompoundDrawablesWithIntrinsicBounds(0, itemIconId.intValue(), 0, 0);
		} else if (itemIconIdOrDrawable instanceof Drawable) {
			Drawable itemDrawable = (Drawable) itemIconIdOrDrawable;
			gridItem.setCompoundDrawablesWithIntrinsicBounds(null, itemDrawable, null, null);
		} else {
			throw new IllegalArgumentException("El icono debe ser especificado por ID o como drawable: "
					+ itemIconIdOrDrawable);
		}
	}

}
