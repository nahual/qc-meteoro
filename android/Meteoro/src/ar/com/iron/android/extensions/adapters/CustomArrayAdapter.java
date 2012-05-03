package ar.com.iron.android.extensions.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import ar.com.iron.android.extensions.adapters.paging.PaginationAdapter;
import ar.com.iron.android.extensions.adapters.paging.PaginationConfiguration;
import ar.com.iron.android.helpers.ContextHelper;
import ar.com.iron.annotations.CantBeNull;
import ar.com.iron.annotations.MayBeNull;

/**
 * Esta clase es un adaptador entre el origen de datos y la interfaz que necesita la vista pero está
 * customizada al uso que le damos nosotros
 * 
 * @author D. García
 * @param <T>
 *            Tipo del elemento mostrado
 */
public class CustomArrayAdapter<T> extends ArrayAdapter<T> {

	/**
	 * Identificador del layout utilizado para mostrar cada item
	 */
	private final int itemLayoutId;

	/**
	 * Bloque de código que popula la vista de cada item
	 */
	private final RenderBlock<T> renderBlock;

	/**
	 * Items de la lista. Creamos esta referencia porque la del ArrayAdapter es privada y no tenemos
	 * acceso.
	 */
	private final List<T> objects;

	/**
	 * Parser de los XMLs de vistas que permite generar las vistas
	 */
	private LayoutInflater inflater;

	/**
	 * Datos de paginado
	 */
	private PaginationAdapter<T, ?> paginationAdapter;

	/**
	 * Establece que este adapter utilizará paginación para mostrar los datos
	 * 
	 * @param pagination
	 *            La configuración de la paginación a utilizar
	 */
	public void usePagination(@CantBeNull final PaginationConfiguration<T, ?> pagination) {
		paginationAdapter = PaginationAdapter.create(pagination, this);
	}

	/**
	 * Devuelve el adapter usado para paginación, sólo si se indicó una configuración de paginado
	 * 
	 * @return El adapter o null si no se indicó configuración con
	 *         {@link #usePagination(PaginationConfiguration)}
	 */
	@MayBeNull
	@SuppressWarnings("unchecked")
	public PaginationAdapter<T, Object> getPaginationAdapter() {
		return (PaginationAdapter<T, Object>) paginationAdapter;
	}

	/**
	 * Constructor típico de este adapter
	 * 
	 * @param context
	 *            Contexto utilizado para la resolucion de las vistas
	 * @param itemLayoutId
	 *            Identificador del layout utilizado para la vista de cada item
	 * @param objects
	 *            Lista de objetos usados como fuente de datos
	 * @param renderBlock
	 *            Bloque de código utilizado para popular la vista de cada elemento
	 */
	public CustomArrayAdapter(final Context context, final int itemLayoutId, final List<T> objects,
			final RenderBlock<T> renderBlock) {
		super(context, itemLayoutId, objects);
		this.objects = objects;
		this.itemLayoutId = itemLayoutId;
		this.renderBlock = renderBlock;
	}

	public LayoutInflater getInflater() {
		if (inflater == null) {
			inflater = ContextHelper.getLayoutInflater(getContext());
		}
		return inflater;
	}

	/**
	 * Sobrescribimos este método que es el que define cómo se debe ver la vista de un elemento.<br>
	 * Para ello creamos vistas a partir del id pasado y las populamos con el bloque de código
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, final View itemView, final ViewGroup parent) {
		int inflatedLayoutId = this.itemLayoutId;
		RenderBlock<T> populationBlock = this.renderBlock;
		boolean viewIsGoingToBeUsedForPaginationItem = false;
		if (usingPagination()) {
			// Vemos si corresponde dibujar la pseudo vista
			viewIsGoingToBeUsedForPaginationItem = (position == getObjects().size());
			if (viewIsGoingToBeUsedForPaginationItem) {
				inflatedLayoutId = this.paginationAdapter.getConfig().getPaginatedItemLayoutId();
				populationBlock = this.paginationAdapter.getConfig().getPaginatedItemRenderBlock();
			}
		}

		final View populatedView = populateItemView(position, itemView, inflatedLayoutId, populationBlock);
		if (viewIsGoingToBeUsedForPaginationItem) {
			this.paginationAdapter.requestMoreItems();
		}
		return populatedView;
	}

	/**
	 * Popula los datos de la vista, creándola si es necesario
	 * 
	 * @param position
	 *            Posición del elemento a popular
	 * @param itemView
	 *            La vista actual reutilizable
	 * @param inflatedLayoutId
	 *            El id del layout a utilizar para el item
	 * @param populationBlock
	 *            El bloque para popular la vista
	 * @param forceDiscardView
	 *            flag que indica si se debe generar una nueva vista para el elemento
	 * @return La vista creada
	 */
	private View populateItemView(final int position, View itemView, final int inflatedLayoutId,
			final RenderBlock<T> populationBlock) {
		if (itemView == null) {
			// Crea la instancia de la vista del item a partir del id
			itemView = getInflater().inflate(inflatedLayoutId, null);
		}

		T item = null;
		if (position < getObjects().size()) {
			item = this.getItem(position);
		}
		populationBlock.render(itemView, item, getInflater());
		return itemView;
	}

	/**
	 * Devuelve los items de la lista.
	 * 
	 * @return
	 */
	public List<T> getObjects() {
		return objects;
	}

	/**
	 * Agrega todos los items de la lista pasada a la lista interna de este adapter.<br>
	 * Este método desactiva la notificación, agrega todos, y la vuelve a activar
	 * 
	 * @param elements
	 *            Los elementos a agregar
	 */
	public void addAll(final Iterable<T> elements) {
		setNotifyOnChange(false);
		for (final T element : elements) {
			add(element);
		}
		setNotifyOnChange(true);
		notifyDataSetChanged();
	}

	/**
	 * Reemplaza los elementos actuales de la lista subyacente con los elementos pasados,
	 * actualizando el estado al final
	 * 
	 * @param elements
	 *            Elementos que reemplazarán a los otros
	 */
	public void replaceAll(final Iterable<T> elements) {
		setNotifyOnChange(false);
		clear();
		addAll(elements);
		notifyDataSetChanged();
	}

	/**
	 * @see android.widget.ArrayAdapter#getCount()
	 */
	@Override
	public int getCount() {
		final int realCount = super.getCount();
		if (usingPagination() && this.paginationAdapter.hasMoreItems()) {
			return realCount + 1;
		}
		return realCount;
	}

	/**
	 * Indica si este adapter está usando paginación para mostrar resultados
	 * 
	 * @return true si se muestra un pseudo elemento al final
	 */
	public boolean usingPagination() {
		return this.paginationAdapter != null;
	}

	/**
	 * @see android.widget.BaseAdapter#getViewTypeCount()
	 */
	@Override
	public int getViewTypeCount() {
		// Uno es el pseudo elemento de paginado
		return 2;
	}

	/**
	 * @see android.widget.BaseAdapter#getItemViewType(int)
	 */
	@Override
	public int getItemViewType(final int position) {
		if (position == getObjects().size()) {
			// Usamos este tipo para el pseudo elemento
			return 1;
		}
		return super.getItemViewType(position);
	}

}
