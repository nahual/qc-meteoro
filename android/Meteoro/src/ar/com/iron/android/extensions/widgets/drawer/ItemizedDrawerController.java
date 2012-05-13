/**
 * 27/02/2011 20:05:39 Copyright (C) 2011 Darío L. García
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

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SlidingDrawer;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.menues.ContextMenuItem;
import ar.com.iron.menues.ScreenMenuHelper;

/**
 * Esta clase representa un widget utilizado como drawer de templates que el usuario puede abrir
 * para elegir un template. Este controller utilizará un drawer existente que controlará para
 * agregar el comportamiento esperado.<br>
 * El {@link SlidingDrawer} debe tener como contenido un {@link GridView} que servirá de contenedor
 * para las imágenes que harán de botón para cada template
 * 
 * @author D. García
 * @param <T>
 *            Tipo de los elementos utilizados como template
 */
public class ItemizedDrawerController<T> {

	/**
	 * Drawer con los elementos
	 */
	private SlidingDrawer drawer;

	/**
	 * Grilla en la que mostrar los items
	 */
	private GridView gridView;

	/**
	 * Listener de las interacciones del usuario
	 */
	private DrawerItemListener<T> listener;

	/**
	 * Adapter para modificar los elementos de la grilla
	 */
	private ItemizedGridAdapter<T> adapter;

	/**
	 * Crea un nuevo controller del drawer con items que se encargará de articular el comportamiento
	 * de sus componentes para que el drawer sirva de contenedor de items con imágenes
	 * 
	 * @param <T>
	 *            Tipo de los items controlados
	 * @param contexto
	 *            El contexto del cual obtener recursos
	 * @param containerView
	 *            la vista contenedora del drawer y su sub grilla
	 * @param drawerId
	 *            El id del drawer para obtenerlo de la vista contenedora
	 * @param gridId
	 *            el id de la grilla para obtenerlo del drawer
	 * @return El controlador con el cuál interactuar con el drawer de templates
	 */
	public static <T> ItemizedDrawerController<T> create(final Context contexto, View containerView, int drawerId,
			int gridId, RenderBlock<T> elementRenderBlock) {
		SlidingDrawer drawerView = ViewHelper.findSlidingDrawer(drawerId, containerView);
		GridView gridView = ViewHelper.findGridView(gridId, drawerView);
		return create(contexto, drawerView, gridView, elementRenderBlock);
	}

	/**
	 * Crea un nuevo controller del drawer con items que se encargará de articular el comportamiento
	 * de sus componentes para que el drawer sirva de contenedor de items con imágenes
	 * 
	 * @param <T>
	 *            Tipo de los items controlados
	 * @param contexto
	 *            El contexto del cual obtener recursos
	 * @param drawerView
	 *            la vista que sirve de drawer
	 * @param gridView
	 *            La grilla que contiene los elementos
	 * @return El nuevo controlador de todos los componentes
	 */
	private static <T> ItemizedDrawerController<T> create(final Context contexto, SlidingDrawer drawerView,
			GridView gridView, RenderBlock<T> elementRenderBlock) {
		final ItemizedDrawerController<T> controller = new ItemizedDrawerController<T>();
		controller.drawer = drawerView;
		controller.gridView = gridView;
		controller.adapter = ItemizedGridAdapter.create(contexto, elementRenderBlock);
		controller.gridView.setAdapter(controller.adapter);
		controller.gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				controller.drawer.animateClose();
				controller.onTemplateSelected(position);
			}
		});
		return controller;
	}

	/**
	 * Invocado cuando un elemento del drawer es seleccionado
	 * 
	 * @param position
	 *            Índice del elemento seleccionado
	 */
	protected void onTemplateSelected(int position) {
		if (listener == null) {
			return;
		}
		T selectedItem = getItemAt(position);
		listener.onItemSelection(selectedItem);
	}

	/**
	 * Devuelve el item del usuario en la posición indicada
	 * 
	 * @param position
	 *            La posición del item a devolver
	 * @return null si no existe un item en esa posición
	 */
	public T getItemAt(int position) {
		T selectedItem = adapter.getItem(position);
		return selectedItem;
	}

	/**
	 * Define el listener para utilizar con este controller en las interacciones del usuario con el
	 * drawer
	 * 
	 * @param drawerItemListener
	 *            El listener para este controller
	 */
	public void setInteractionListener(DrawerItemListener<T> drawerItemListener) {
		this.listener = drawerItemListener;
	}

	public DrawerItemListener<T> getListener() {
		return listener;
	}

	public void setListener(DrawerItemListener<T> listener) {
		this.listener = listener;
	}

	public SlidingDrawer getDrawer() {
		return drawer;
	}

	public GridView getGridView() {
		return gridView;
	}

	public ItemizedGridAdapter<T> getAdapter() {
		return adapter;
	}

	/**
	 * Define un menú contextual para utilizar en este controller, con los elementos de la grilla
	 * 
	 * @param values
	 *            los valores para el menu
	 * @param menuTitleOrId
	 *            Texto que indica el titulo del menú o un id del recurso string
	 */
	public <D, A extends Activity> void setContextMenuItems(final ContextMenuItem<A, D>[] values,
			final Object menuTitleOrId, final A activity) {
		final ListAdapter listAdapter = getGridView().getAdapter();
		getGridView().setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				ScreenMenuHelper.createContextOptionsFor(menu, values, menuTitleOrId, activity, listAdapter);
			}
		});
	}
}
