package ar.com.iron.android.extensions.activities;

import android.view.View;

/**
 * Esta interfaz define el metodo que debe implementar un bloque de codigo que permite a partir de
 * una vista modificar los atributos de un objeto
 * 
 * @author D.Garcia
 */
public interface ViewToObjectBlock<T> {

	/**
	 * Copia los datos de la vista pasada al objeto
	 * 
	 * @param model
	 *            Objeto al que se copiaran los datos de la vista
	 * @param vista
	 *            Vista de la que se tomaran los datos
	 */
	public void copyDataToObject(T model, View vista);
}
