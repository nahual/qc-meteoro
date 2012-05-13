/**
 * 08/03/2011 03:15:43 Copyright (C) 2011 Darío L. García
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

/**
 * Esta interfaz representa un bloque de código que puede utilizarse para popular una vista que
 * representa un grupo de elementos
 * 
 * @author D. García
 * @param <T>
 *            Tipo del objeto del que se toman los datos
 */
public interface GroupRenderBlock<T> {

	/**
	 * Popula la vista y sus controles a partir de los datos tomados del item pasado
	 * 
	 * @param itemView
	 *            Vista a popular con datos
	 * @param item
	 *            Entidad portadora de los datos
	 * @param inflater
	 *            LayoutInflater para poder inflar layouts
	 */
	void render(View itemView, T item, boolean isExpanded, LayoutInflater inflater);

}