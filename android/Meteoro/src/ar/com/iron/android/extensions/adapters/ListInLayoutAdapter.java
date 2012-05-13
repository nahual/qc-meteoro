package ar.com.iron.android.extensions.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import ar.com.iron.android.helpers.ContextHelper;

/**
 * Esta clase permite implementar una lista de elementos dentro de un {@link LinearLayout} como
 * contenedor de las vistas para cada item.<br>
 * Mediante esta clase se puede interactuar con el conjunto de elementos representados como lista
 * dentro del layout.<br>
 * Normalmente esta clase debe usarse para listas de pocos elementos
 * 
 * @author D. García
 */
public class ListInLayoutAdapter<T> {

	private List<T> items;
	private List<View> itemViews;
	private LinearLayout linearLayout;
	private LayoutInflater layoutInflater;
	private int childLayoutId;
	private RenderBlock<T> itemRenderer;

	public static <T> ListInLayoutAdapter<T> create(Context contexto, LinearLayout containerLayout, int childLayoutId) {
		ListInLayoutAdapter<T> adapter = new ListInLayoutAdapter<T>();
		adapter.items = new ArrayList<T>();
		adapter.itemViews = new ArrayList<View>();
		adapter.layoutInflater = ContextHelper.getLayoutInflater(contexto);
		adapter.linearLayout = containerLayout;
		adapter.childLayoutId = childLayoutId;
		return adapter;
	}

	/**
	 * Agrega un item a este adapter agregando una vista en el linear layout para representarlo
	 * 
	 * @param item
	 *            El elemento a agregar
	 */
	public void addItem(T item) {
		View itemView = createViewFor(item);
		items.add(item);
		itemViews.add(itemView);
		linearLayout.addView(itemView);
	}

	/**
	 * Elimina del conjunto el elemento pasado
	 * 
	 * @param erasedItem
	 *            El elemento a borrar con su vista
	 */
	public void removeItem(T erasedItem) {
		int erasedIndex = getPositionOf(erasedItem);
		if (erasedIndex == -1) {
			// No borramos nada
			return;
		}
		items.remove(erasedIndex);
		View erasedView = itemViews.remove(erasedIndex);
		linearLayout.removeView(erasedView);
	}

	/**
	 * Devuelve la posicion que ocupa el elemento indicado o -1 si no es encuentra en este adapter
	 * 
	 * @param searchedItem
	 *            El item buscado
	 * @return La posición si alguno de los items es el mismo que el pasado
	 */
	public int getPositionOf(T searchedItem) {
		for (int i = 0; i < items.size(); i++) {
			if (searchedItem == items.get(i)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Crea la vista para el item pasado, populandola previamente
	 * 
	 * @param item
	 *            El item utilizado para generar la vista
	 * @return La vista creada
	 */
	private View createViewFor(T item) {
		View childView = layoutInflater.inflate(childLayoutId, null);
		if (itemRenderer != null) {
			itemRenderer.render(childView, item, layoutInflater);
		}
		return childView;
	}

	/**
	 * Elimina todos los elementos de este adapter junto con sus vistas
	 */
	public void removeAll() {
		for (View removedView : itemViews) {
			linearLayout.removeView(removedView);
		}
		itemViews.clear();
		items.clear();
	}

	/**
	 * Reemplaza todos los elementos de este adapter por los nuevos pasados, generando las vistas
	 * apropiadas
	 * 
	 * @param newElements
	 *            los nuevos elementos
	 */
	public void replaceAll(Iterable<T> newElements) {
		removeAll();
		for (T newElement : newElements) {
			addItem(newElement);
		}
	}

	public List<T> getItems() {
		return items;
	}

	public void setItemRenderer(RenderBlock<T> itemRenderer) {
		this.itemRenderer = itemRenderer;
	}

}
