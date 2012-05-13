/**
 * 27/02/2011 22:52:42 Copyright (C) 2011 Darío L. García
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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.android.helpers.ContextHelper;

/**
 * Esta clase sirve de adapter para los elementos mostrados en la grilla del drawer.<br>
 * A través de esta clase se pueden agregar o quitar elementos (o modificarlos)
 * 
 * @author D. García
 */
public class ItemizedGridAdapter<T> extends BaseAdapter {
	private Context mContext;

	private List<T> items;

	private RenderBlock<T> itemRenderer;

	private boolean notifyDataChanges = true;

	private LayoutInflater layoutInflater;

	public boolean isNotifyDataChanges() {
		return notifyDataChanges;
	}

	public void setNotifyDataChanges(final boolean notifyDataChanges) {
		this.notifyDataChanges = notifyDataChanges;
	}

	public RenderBlock<T> getItemRenderer() {
		return itemRenderer;
	}

	public void setItemRenderer(final RenderBlock<T> itemRenderer) {
		this.itemRenderer = itemRenderer;
	}

	/**
	 * Crea el adapter para manejar los elementos y generar sus vistas
	 * 
	 * @param <T>
	 *            el tipo de elementos de este adapter
	 * @param contexto
	 *            El contexto del cual obtener las imágenes
	 * @return El adapter creado
	 */
	public static <T> ItemizedGridAdapter<T> create(final Context contexto, final RenderBlock<T> elementRenderBlock) {
		final ItemizedGridAdapter<T> adapter = new ItemizedGridAdapter<T>();
		adapter.mContext = contexto;
		adapter.items = new ArrayList<T>();
		adapter.itemRenderer = elementRenderBlock;
		adapter.layoutInflater = ContextHelper.getLayoutInflater(contexto);
		return adapter;
	}

	/**
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return items.size();
	}

	/**
	 * @see android.widget.Adapter#getItem(int)
	 */
	public T getItem(final int position) {
		return items.get(position);
	}

	/**
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(final int position) {
		return position;
	}

	/**
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		TextView textView;
		if (convertView == null) {
			textView = new TextView(mContext);
			textView.setLayoutParams(new GridView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			textView.setEllipsize(TruncateAt.MARQUEE);
			textView.setSelected(true);
			textView.requestFocus();
			textView.setMaxLines(1);
		} else {
			textView = (TextView) convertView;
		}

		final T realItem = getItem(position);
		itemRenderer.render(textView, realItem, layoutInflater);
		return textView;
	}

	/**
	 * Agrega el elemento al conjunto disponible
	 * 
	 * @param templateItem
	 *            Item agregado al final en al grilla
	 */
	public void addItem(final T templateItem) {
		items.add(templateItem);
		if (notifyDataChanges) {
			notifyDataSetChanged();
		}
	}

	/**
	 * Quita de la grilla el elemento indicado. Si el elemento pasado no pertenece a la grilla, nada
	 * se hace
	 * 
	 * @param selectedItem
	 *            Elemento seleccionable de la grilla
	 */
	public void removeItem(final T selectedItem) {
		items.remove(selectedItem);
		if (notifyDataChanges) {
			notifyDataSetChanged();
		}
	}

	/**
	 * Elimina todos los elementos de la grilla
	 */
	public void removeAll() {
		items.clear();
		if (notifyDataChanges) {
			notifyDataSetChanged();
		}
	}

	/**
	 * @see android.widget.BaseAdapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {
		return false;
	}

	/**
	 * Agrega todos los elementos pasados reemplazando los actuales. Se notifica del cambio después
	 * de agregar todos
	 * 
	 * @param newItems
	 *            Los elementos a agregar
	 */
	public void replaceAll(final List<T> newItems) {
		setNotifyDataChanges(false);
		removeAll();
		for (final T newItem : newItems) {
			addItem(newItem);
		}
		setNotifyDataChanges(true);
		notifyDataSetChanged();
	}
}