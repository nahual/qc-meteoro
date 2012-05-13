/**
 * 26/01/2011 14:45:43 Darío L. García
 */
package ar.com.iron.android.extensions.dialogs;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import ar.com.iron.android.extensions.adapters.CustomArrayAdapter;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.annotations.CantBeNull;
import ar.com.iron.annotations.MayBeNull;
import ar.com.iron.helpers.ViewHelper;

/**
 * Esta clase permite implementar un diálogo propio en el cual se elige un elemento de un conjunto
 * 
 * @author D. García
 * @param <T>
 *            Tipo de los elementos mostrados y elegidos
 */
public abstract class SelectOneCustomDialog<T> extends CustomDialog {

	private final List<T> availableItems;
	private T selectedItem;
	private CustomArrayAdapter<T> listAdapter;

	/**
	 * Constructor que toma los elementos entre los cuales el usuario puede elegir
	 * 
	 * @param context
	 *            El contexto para interactuar con la plataforma
	 * @param elements
	 *            La lista de elementos elegibles por el usuario en el orden que serán mostrados
	 */
	public SelectOneCustomDialog(Context context, List<T> elements) {
		super(context);
		availableItems = elements;
	}

	/**
	 * Indica el elemento que el usuario eligió de los disponibles.<br>
	 * Utilice el método
	 * {@link #setOnDismissListener(android.content.DialogInterface.OnDismissListener)} para ser
	 * notificado cuando el usuario realizó la acción
	 * 
	 * @return El elemento elegido por el usuario al cerrar el diálogo
	 */
	@MayBeNull
	public T getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(T elegido) {
		this.selectedItem = elegido;
	}

	public CustomArrayAdapter<T> getListAdapter() {
		return listAdapter;
	}

	/**
	 * Devuelve el ID del layout para cada elemento mostrado en el listado del dialogo
	 */
	protected abstract int getLayoutIdForElements();

	/**
	 * Define el código utilizado para mapear los datos del objeto sobre la vista.<br>
	 * Este código se encarga de popular cada vista del listado a partir del elemento a mostrar
	 * 
	 * @return El bloque ejecutado por cada item
	 */
	@CantBeNull
	protected abstract RenderBlock<T> getRenderBlock();

	/**
	 * Define comportamiento adicional para configurar otros componentes que pueda tener el diálogo
	 */
	@Override
	protected void setUpComponents() {
		setupListView();
	}

	/**
	 * Configura la vista de listado de los elementos
	 */
	private void setupListView() {
		ListView listView = ViewHelper.findInnerViewAs(ListView.class, android.R.id.list, getContentView());

		int itemLayoutId = getLayoutIdForElements();
		RenderBlock<T> itemRenderBlock = getRenderBlock();
		listAdapter = new CustomArrayAdapter<T>(getContext(), itemLayoutId, availableItems, itemRenderBlock);
		listView.setAdapter(listAdapter);

		TextView emptyView = ViewHelper.findTextView(android.R.id.empty, getContentView());
		listView.setEmptyView(emptyView);

		OnItemClickListener clickListener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				T selectedItem = listAdapter.getItem(position);
				setSelectedItem(selectedItem);
				// Cuando se elige un elemento cerramos el diálogo
				dismiss();
			}

		};
		listView.setOnItemClickListener(clickListener);
	}

}