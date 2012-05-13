/**
 * 10/03/2011 19:06:58 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.adapters;

import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import ar.com.iron.android.helpers.ContextHelper;

/**
 * Esta clase implementa el render para el texto grupo en una lista expandible.<br>
 * La implementación actual asume que se usa el layout de android
 * {@link R.layout#simple_list_item_1} y que sólo se debe popuar un textview
 * 
 * @author D. García
 */
public abstract class TextGroupRenderBlock<T> extends TextRenderBlock<T> implements GroupRenderBlock<T> {

	final Context contexto;

	/**
	 * Constructor que usa el contexto para calcular dimensiones de píxeles
	 */
	public TextGroupRenderBlock(Context contexto) {
		this.contexto = contexto;
	}

	/**
	 * @see ar.com.iron.android.extensions.adapters.GroupRenderBlock#render(android.view.View,
	 *      java.lang.Object, boolean, android.view.LayoutInflater)
	 */
	public void render(View itemView, T item, boolean isExpanded, LayoutInflater inflater) {
		// Primero populamos el textview con el texto
		super.render(itemView, item, inflater);
		// Y después ajustamos por que el layout default de android no tiene margen, lo hackeamos
		// para que deje espacio al costado y no pise el botón de expandir/colapsar items
		int leftPadding = itemView.getPaddingLeft();
		if (leftPadding < 36) {
			leftPadding = ContextHelper.convertDpToPixels(contexto, 36);
			itemView.setPadding(leftPadding, 0, 0, 0);
		}
	}
}
