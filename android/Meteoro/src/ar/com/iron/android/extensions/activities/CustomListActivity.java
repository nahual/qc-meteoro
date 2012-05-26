package ar.com.iron.android.extensions.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import ar.com.iron.android.extensions.activities.model.CustomableListActivity;
import ar.com.iron.android.extensions.adapters.CustomArrayAdapter;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.android.extensions.messages.IntentReceptor;
import ar.com.iron.helpers.ActivityHelper;
import ar.com.iron.menues.ActivityMenuItem;
import ar.com.iron.menues.ContextMenuItem;
import ar.com.iron.menues.ScreenMenuHelper;

/**
 * Esta clase reune comportamiento común a todos las actividades que muestran un listado de
 * resultados
 * 
 * @author D.García, Maximiliano Vázquez
 */
public abstract class CustomListActivity<D> extends ListActivity implements CustomableListActivity<D> {

	/**
	 * Receptor de intents que permite la comunicación en background
	 */
	private IntentReceptor intentReceptor;

	public IntentReceptor getIntentReceptor() {
		if (intentReceptor == null) {
			intentReceptor = new IntentReceptor(this);
		}
		return intentReceptor;
	}

	/**
	 * Ejecuta código específico de la subclase para configurar cosas adicionales (es opcional)
	 */
	@Override
	public void afterOnCreate() {
	}

	/**
	 * @return Devuelve la vista raíz de toda la pantalla
	 */
	@Override
	public View getContentView() {
		return ActivityHelper.getContentViewFrom(this);
	}

	/**
	 * @return Devuelve el adapter real utilizado por este activity
	 */
	@Override
	@SuppressWarnings("unchecked")
	public CustomArrayAdapter<D> getCustomAdapter() {
		return (CustomArrayAdapter<D>) getListAdapter();
	}

	/**
	 * @return Devuelve el array de items para crear en el menu de opcioens accesible desde el botón
	 *         "menu"
	 */
	@Override
	public ActivityMenuItem<? extends CustomListActivity<D>>[] getMenuItems() {
		return null;
	}

	/**
	 * Al llamarlo si el teclado esta activo lo esconde
	 */
	protected void hideSoftInput() {
		ActivityHelper.hideSoftKeyboardFor(this);
	}

	/**
	 * Obtiene los managers o servicios que sean necesarios para el funcionamiento de esta pantalla
	 */
	@Override
	public void initDependencies() {
	}

	/**
	 * Metodo para la subclase que indica el momento adecuado para registrar los receivers de este
	 * activity que sirven para la comunicación de fondo.<br>
	 * Al registrar los receivers en este metodo se asegura que se recibirán los mensajes aún cuando
	 * la pantalla no es visible
	 */
	@Override
	public void initMessageReceivers() {
	}

	/**
	 * Notifica al adapter que se modificaron los datos y se debe actualizar la vista de los items
	 */
	@Override
	public void notificarCambioEnLosDatos() {
		final List<D> newList = safeGetInitialElementList();
		updateElementList(newList);
	}

	/**
	 * Actualiza la lista de resultados mostrados en este activity, sin disparar los eventos por
	 * cada cambio, si no al final de todo el cambio.<br>
	 * Este método permite cambiar todos los elementos mostrados como una sola unidad.<br>
	 * La lista pasada no será modificada, sólo se actualiza la lista original del adapter
	 * 
	 * @param newList
	 *            La lista con los nuevos resultados
	 */
	protected void updateElementList(final List<D> newList) {
		final CustomArrayAdapter<D> currentAdapter = getCustomAdapter();
		// Evitamos que notifique los cambios
		currentAdapter.setNotifyOnChange(false);

		// Actualizamos los nuevos elementos
		currentAdapter.clear();
		for (final D element : newList) {
			currentAdapter.add(element);
		}
		// Habilitamos para que notifique nuevamente
		currentAdapter.setNotifyOnChange(true);
		currentAdapter.notifyDataSetChanged();
	}

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(this.getLayoutIdForActivity());
		if (getContextMenuItems() != null && getContextMenuItems().length > 0) {
			registerForContextMenu(getListView());
		}

		this.initDependencies();
		this.initMessageReceivers();
		this.setUpListAdapter(new ArrayList<D>());
		this.setUpComponents();

		final List<D> initalElements = safeGetInitialElementList();
		getCustomAdapter().addAllFrom(initalElements);
		this.afterOnCreate();
	}

	/**
	 * Crea el menu contextual usando la definicion de opciones del array pasado
	 * 
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View,
	 *      android.view.ContextMenu.ContextMenuInfo)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
		@SuppressWarnings("rawtypes")
		final ContextMenuItem[] contextMenuItems = getContextMenuItems();
		ScreenMenuHelper.createContextOptionsFor(menu, contextMenuItems, getContextMenuHeaderTitleOrId(), this,
				getCustomAdapter());
	}

	/**
	 * Crea el menu de opciones basándose en el array definido por la subclase
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		if (ActivityHelper.usesDynamicMenuOptions(this)) {
			// No creamos el menú ahora, ya que en el prepare se hará de nuevo por ser dinámico
			return true;
		}
		return ActivityHelper.createActivityOptionsFor(this, menu);
	}

	/**
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		if (ActivityHelper.usesDynamicMenuOptions(this)) {
			return ActivityHelper.prepareDynamicOptionsMenuFor(this, menu);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Crea las opciones de menú de esta actividad
	 * 
	 * @param menu
	 *            El menú en el que se crearán
	 * @return true si se crearon opciones para el menú
	 */
	@SuppressWarnings("unchecked")
	protected boolean createActivityOptions(final Menu menu) {
		@SuppressWarnings("rawtypes")
		final ActivityMenuItem[] menuItems = getMenuItems();
		return ScreenMenuHelper.createActivityOptionsFor(menu, this, menuItems);
	}

	/**
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if (intentReceptor != null) {
			intentReceptor.unregisterMessageReceivers();
		}
		super.onDestroy();
	}

	/**
	 * Registra en este activity un receiver que será disparado al recibir un intent del action
	 * declarado.<br>
	 * Al recibir el intent con el action declarado, se ejecuta el listener pasado
	 * 
	 * @param expectedAction
	 *            Action que declara el mensaje esperado
	 * @param executedWhenReceived
	 *            Listener a ejecutar cuando se recibe el mensaje
	 */
	@Override
	public void registerMessageReceiver(final String expectedAction, final BroadcastReceiver executedWhenReceived) {
		getIntentReceptor().registerMessageReceiver(expectedAction, executedWhenReceived);
	}

	/**
	 * @return Devuelve la lista chequeando que exista primero
	 */
	private List<D> safeGetInitialElementList() {
		final List<D> elements = getElementList();
		if (elements == null) {
			return Collections.emptyList();
		}
		return elements;
	}

	/**
	 * @return Devuelve el bloque de código chequeando que exista primero
	 */
	private RenderBlock<D> safeGetRenderBlock() {
		final RenderBlock<D> renderBlock = getElementRenderBlock();
		if (renderBlock == null) {
			throw new RuntimeException("Se esperaba un bloque de codigo para popular la vista de cada elemento");
		}
		return renderBlock;
	}

	/**
	 * Crea y configura los controles que se usaran en la pantalla. Opcional para aquellas pantallas
	 * que no tengan controles
	 */
	@Override
	public void setUpComponents() {
	}

	/**
	 * Configura la vista de la lista
	 */
	protected void setUpListAdapter(final List<D> elements) {
		final RenderBlock<D> renderBlock = safeGetRenderBlock();
		final int itemLayoutId = getLayoutIdForElements();
		final ListAdapter listAdapter = new CustomArrayAdapter<D>(this, itemLayoutId, elements, renderBlock);
		setListAdapter(listAdapter);
	}

	/**
	 * Por defecto no se usa menú contextual
	 * 
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getContextMenuHeaderTitleOrId()
	 */
	@Override
	public abstract Object getContextMenuHeaderTitleOrId();

	/**
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View,
	 *      int, long)
	 */
	@Override
	protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
		final D selectedItem = getCustomAdapter().getItem(position);
		onItemSelected(selectedItem);
	}

	/**
	 * Invocado al hacer click el usuario sobre un elemento de la lista
	 * 
	 * @param selectedItem
	 *            El elemento elegido
	 */
	protected void onItemSelected(final D selectedItem) {
		// Por defecto no hacemos nada
	}

	/**
	 * Devuelve esta actividad como contexto para los lugares donde se necesite
	 * 
	 * @return La actividad como contexto
	 */
	public Context getContext() {
		return this;
	}
}
