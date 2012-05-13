/**
 * 03/07/2011 23:21:12 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.helpers;

import android.widget.ArrayAdapter;
import ar.com.iron.annotations.CantBeNull;
import ar.com.iron.annotations.MayBeNull;

/**
 * Esta clase ofrece algunos métodos para trabajar con adapters
 * 
 * @author D. García
 */
public class AdapterHelper {

	/**
	 * Este método permite reemplazar todos los elementos de un adapter por los pasados, incluyendo
	 * una cabecera opcional sin notificar de los cambios hasta que se agrega el último. Después de
	 * agregar todos se invoca el {@link ArrayAdapter#notifyDataSetChanged()} para que este
	 * actualice sus vistas
	 * 
	 * @param <T>
	 *            Tipo de los elementos agregados
	 * @param adapter
	 *            El adapter a modificar
	 * @param firstElement
	 *            El primer elemento opcional (si es null se omite)
	 * @param newElements
	 *            Los elementos a agregar en el adapter para reemplazar a los existentes
	 */
	public static <T> void replaceAll(@CantBeNull ArrayAdapter<? super T> adapter, @MayBeNull T firstElement,
			@CantBeNull Iterable<? extends T> newElements) {
		adapter.setNotifyOnChange(false);
		adapter.clear();
		if (firstElement != null) {
			adapter.add(firstElement);
		}
		for (T newElement : newElements) {
			adapter.add(newElement);
		}
		adapter.setNotifyOnChange(true);
		adapter.notifyDataSetChanged();
	}

}
