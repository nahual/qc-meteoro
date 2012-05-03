/**
 * 09/12/2009 10:32:53<br>
 * Copyright (C) 2009 YAH Group<br>
 * <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by-nc-sa/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/" property="dc:title">YAH Tasks</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">YAH Group</span> is
 * licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by-nc-sa/2.5/ar/">Creative Commons
 * Attribution-Noncommercial-Share Alike 2.5 Argentina License</a>.
 */
package ar.com.iron.android.extensions.activities.model;

import java.util.List;

import android.app.ListActivity;
import ar.com.iron.android.extensions.adapters.CustomArrayAdapter;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.menues.ContextMenuItem;

/**
 * Esta clase reune los métodos custom para una {@link ListActivity}
 * 
 * @author D.L. García
 */
public interface CustomableListActivity<D> extends CustomableActivity {

	/***************************************
	 * Menu contextual
	 ***************************************/
	/**
	 * Retorna el texto o id del recurso que será utilizado como titulo del menu contextual.
	 * 
	 * @return null indica que no se utiliza menu contextual
	 */
	Object getContextMenuHeaderTitleOrId();

	/**
	 * Devuelve el array de items que definen los menues para crear en el menu contextual
	 * 
	 * @return null indica que no se utiliza menu contextual
	 */
	ContextMenuItem<? extends CustomableListActivity<D>, D>[] getContextMenuItems();

	/***************************************
	 * Lista
	 ***************************************/
	/**
	 * @return Devuelve el adapter real utilizado por este activity, permitiendo comportamiento más
	 *         avanzado que el normal
	 */
	CustomArrayAdapter<D> getCustomAdapter();

	/**
	 * @return Devuelve la lista de elementos que se mostraran en este activity
	 */
	List<D> getElementList();

	/**
	 * @return Devuelve el render block utilizado para popular la vista de cada elemento de la lista
	 */
	RenderBlock<D> getElementRenderBlock();

	/**
	 * @return Devuelve el id de la vista a usar para los items
	 */
	int getLayoutIdForElements();

	/**
	 * Notifica al adapter que se modificaron los datos y se debe actualizar la vista de los items
	 */
	void notificarCambioEnLosDatos();

}
