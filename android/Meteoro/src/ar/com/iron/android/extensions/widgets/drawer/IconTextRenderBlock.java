/**
 * 10/03/2011 18:50:01 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.widgets.drawer;

import android.view.LayoutInflater;
import android.view.View;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.android.helpers.WidgetHelper;

/**
 * Esta clase permite implementar un bloque de código para mostrar en pantalla un ícono con una
 * descripción debajo.<br>
 * Heredando de esta clase sólo se indica cómo obtener el drawable para el ícono, y el texto para la
 * descripción
 * 
 * @author D. García
 */
public abstract class IconTextRenderBlock<T> implements RenderBlock<T> {

	/**
	 * @see ar.com.iron.android.extensions.adapters.RenderBlock#render(android.view.View,
	 *      java.lang.Object, android.view.LayoutInflater)
	 */
	public void render(View itemView, T item, LayoutInflater inflater) {
		Object itemIconId = getItemIconIdOrDrawableFor(item);
		String itemName = getItemDescriptionFor(item);
		WidgetHelper.renderGridItem(itemView, itemName, itemIconId);
	}

	/**
	 * Devuelve el texto para utilizar como descripción debajo del item en la grilla
	 * 
	 * @param item
	 *            El item a evaluar
	 * @return La descripción para mostrar del ítem
	 */
	protected abstract String getItemDescriptionFor(T item);

	/**
	 * Devuelve el id del drawable a utilizar como ícono del item o la instancia de drawable
	 * directamente
	 * 
	 * @param item
	 *            El item evaluado
	 * @return La instancia de drawable o su id de recurso para mostrar como ícono del ítem
	 */
	protected abstract Object getItemIconIdOrDrawableFor(T item);

}
