package ar.com.iron.android.extensions.adapters;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Esta interfaz representa un bloque de código que puede utilizarse para popular una vista a partir
 * de un objeto con los datos
 * 
 * @author D. García
 * @param <T>
 *            Tipo del objeto del que se toman los datos
 */
public interface RenderBlock<T> {

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
	void render(View itemView, T item, LayoutInflater inflater);

}
