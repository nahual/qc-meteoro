/**
 * 18/01/2011 16:08:12 Copyright (C) 2006 Darío L. García
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package ar.com.iron.helpers;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.android.helpers.ContextHelper;

/**
 * Esta clase permite la generación de mensajes fácilmente a través de un singleton
 * 
 * @author D. García
 */
public class ToastHelper {

	private Context currentContext;

	private LayoutInflater inflater;

	public LayoutInflater getInflater() {
		if (inflater == null) {
			inflater = ContextHelper.getLayoutInflater(currentContext);
		}
		return inflater;
	}

	public static ToastHelper create(Context contexto) {
		if (contexto == null) {
			throw new IllegalArgumentException("El contexto no puede ser null para el ToastHelper");
		}
		ToastHelper helper = new ToastHelper();
		helper.currentContext = contexto;
		return helper;
	}

	/**
	 * Muestra un mensaje con duración corta
	 * 
	 * @param mensaje
	 *            El mensaje a mostrar
	 */
	public void showShort(Object mensaje) {
		showMsg(mensaje, Toast.LENGTH_SHORT);
	}

	/**
	 * Muestra un mensaje con duración larga
	 * 
	 * @param mensaje
	 *            El mensaje a mostrar
	 */
	public void showLong(Object mensaje) {
		showMsg(mensaje, Toast.LENGTH_LONG);
	}

	/**
	 * Muestra el mensaje indicado con la duración establecida
	 * 
	 * @param mensaje
	 *            El mensaje a mostrar
	 * @param duracion
	 *            La duración del mensaje
	 */
	private void showMsg(Object mensaje, int duracion) {
		Toast makeText;
		if (mensaje instanceof CharSequence) {
			CharSequence text = (CharSequence) mensaje;
			makeText = Toast.makeText(currentContext, text, duracion);
		} else if (mensaje instanceof Number) {
			Number stringResourceId = (Number) mensaje;
			makeText = Toast.makeText(currentContext, stringResourceId.intValue(), duracion);
		} else {
			throw new IllegalArgumentException("Se esperaba un String o resource ID, se recibió: " + mensaje);
		}
		makeText.show();
	}

	/**
	 * Muestra un toast custom en el que se utiliza un layout especial y un bloque para popularlo.<br>
	 * El toast estará centrado y será de duración larga
	 * 
	 * @param <T>
	 *            El tipo de objeto usado como modelo desde el cual popular el toast
	 * @param toastLayoutId
	 *            El id del layout para el toast
	 * @param model
	 *            El objeto usado como fuente de los datos
	 * @param toastRenderblock
	 *            El bloque de código para popular el toast que debe tener relación con el layout
	 * @param toastDuration
	 *            Duración del toast
	 */
	public <T> void showCustom(int toastLayoutId, T model, RenderBlock<T> toastRenderblock, int toastDuration) {
		LayoutInflater inflater = getInflater();
		View layout = inflater.inflate(toastLayoutId, null);

		// Popula los datos del toast
		toastRenderblock.render(layout, model, inflater);

		Toast toast = new Toast(currentContext);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(toastDuration);
		toast.setView(layout);
		toast.show();
	}
}
