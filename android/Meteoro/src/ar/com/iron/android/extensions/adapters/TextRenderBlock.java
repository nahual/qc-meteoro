/**
 * 10/03/2011 19:22:16 Copyright (C) 2011 Darío L. García
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

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Esta clase es una implementación de render block que asume la existencia de un TextView como
 * vista a popular
 * 
 * @author D. García
 * @param <T>
 */
public abstract class TextRenderBlock<T> implements RenderBlock<T> {

	/**
	 * @see ar.com.iron.android.extensions.adapters.RenderBlock#render(android.view.View,
	 *      java.lang.Object, android.view.LayoutInflater)
	 */
	public void render(View itemView, T item, LayoutInflater inflater) {
		if (!(itemView instanceof TextView)) {
			throw new IllegalArgumentException("Este render asume que se usa un textview como vista del grupo: "
					+ itemView);
		}
		TextView textView = (TextView) itemView;
		CharSequence groupDescription = getDescriptionFor(item);
		textView.setText(groupDescription);
	};

	/**
	 * Devuelve el texto que se debe usar como descripción del grupo
	 * 
	 * @param group
	 *            El item correspondiente al grupo
	 * @return El texto que será mostrado como cabecera del grupo
	 */
	protected abstract CharSequence getDescriptionFor(T group);

}