package ar.com.iron.android.extensions.activities;

import android.view.View;

/**
 * Esta interfaz define el método que debe implementar un bloque de código para copiar datos desde
 * un objeto a una vista
 * 
 * @author D. Garcia
 */
public interface ObjectToViewBlock<T> {

	/**
	 * Copia los datos desde el objeto hacia la vista
	 * 
	 * @param model
	 *            Objeto usado como modelo de los datos
	 * @param vista
	 *            Vista a modificar con los datos del objeto
	 */
	public void copyFromObject(T model, View vista);
}
