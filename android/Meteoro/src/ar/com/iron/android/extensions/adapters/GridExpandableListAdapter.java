/**
 * 08/03/2011 12:02:53 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.adapters;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.GridView;
import android.widget.TextView;
import ar.com.iron.android.extensions.widgets.drawer.IconTextRenderBlock;
import ar.com.iron.android.extensions.widgets.drawer.ItemizedGridAdapter;
import ar.com.iron.android.helpers.ContextHelper;

/**
 * Esta clase representa un adapter para utilizar en una lista anidada pero en vez de mostrar los
 * sub elementos como lista, los muestra como grilla debajo de cada grupo.<br>
 * La grilla asume un {@link TextView} para cada elemento al que se le puede agregar un ícono
 * 
 * @author D. García
 */
public class GridExpandableListAdapter<G, E> extends CustomExpandableListAdapter<G, E> {

	private final Context contexto;

	private OnChildClickListener onChildClickListener;

	public OnChildClickListener getOnChildClickListener() {
		return onChildClickListener;
	}

	public void setOnChildClickListener(OnChildClickListener childClickListener) {
		this.onChildClickListener = childClickListener;
	}

	/**
	 * Crea un adapter para aplicar en un {@link ExpandableListView} que toma la lista de grupos y
	 * un {@link GroupExpander} que permite obtener los elementos de cada grupo.<br>
	 * Este constructor asume que la base son los grupos y a partir de ellos se obtienen los
	 * elementos
	 * 
	 * @param contexto
	 *            Contexto del cual obtener las vistas
	 * @param groupLayoutId
	 *            Identificador del layout para representar cada grupo
	 * @param groupRenderBlock
	 *            El código para popular la vista de los grupos
	 * @param elementLayoutId
	 *            El id del layout para representar cada elemento
	 * @param elementRenderBlock
	 *            El bloque para popular la vista del elemento
	 * @param groups
	 *            La lista de grupos que se tomará como referencia
	 * @param expander
	 *            La función que permite obtener los elementos de cada grupo
	 */
	public GridExpandableListAdapter(Context contexto, int groupLayoutId, GroupRenderBlock<G> groupRenderBlock,
			IconTextRenderBlock<E> elementRenderBlock, List<G> groups, GroupExpander<G, E> expander) {
		// No se permite un layout custom para los elementos por eso el 0
		super(contexto, groupLayoutId, groupRenderBlock, 0, elementRenderBlock, groups, expander);
		this.contexto = contexto;
	}

	/**
	 * Crea un adapter típico para aplicar en un {@link ExpandableListView} que toma la lista de
	 * grupos y un {@link GroupExpander} que permite obtener los elementos de cada grupo.<br>
	 * Este constructor asume que la base son los grupos y a partir de ellos se obtienen los
	 * elementos.<br>
	 * Para las vistas de grupos se asume que se quieren mostrar textos y se utiliza la default de
	 * android R.layout.simple_list_item_1
	 * 
	 * @param contexto
	 *            Contexto del cual obtener las vistas
	 * @param textRenderBlock
	 *            El código que indica de dónde obtener el texto para el la descripción del grupo
	 * @param elementRenderBlock
	 *            El bloque para popular la vista del elemento
	 * @param groups
	 *            La lista de grupos que se tomará como referencia
	 * @param expander
	 *            La función que permite obtener los elementos de cada grupo
	 */
	public GridExpandableListAdapter(Context contexto, TextGroupRenderBlock<G> textRenderBlock,
			IconTextRenderBlock<E> elementRenderBlock, List<G> groups, GroupExpander<G, E> expander) {
		// No se permite un layout custom para los elementos por eso el 0
		this(contexto, DEFAULT_TEXT_LAYOUT, textRenderBlock, elementRenderBlock, groups, expander);
	}

	/**
	 * Crea un adapter para una vista expandida que agrupa los elementos por un grupo determinado en
	 * cada uno.<br>
	 * Este constructor asume que la base son los elementos a partir de los cuales se obtienen sus
	 * grupos con un {@link ElementGrouper} y que el grupo es comparable
	 * 
	 * @param contexto
	 *            Contexto del cual obtener las vistas
	 * @param groupLayoutId
	 *            Identificador del layout para representar cada grupo
	 * @param groupRenderBlock
	 *            El código para popular la vista de los grupos
	 * @param elementLayoutId
	 *            El id del layout para representar cada elemento
	 * @param elementRenderBlock
	 *            El bloque para popular la vista del elemento
	 * @param elementGrouper
	 *            El código que permite obtener el grupo de cada elemento
	 * @param elementos
	 *            Los elementos para la lista
	 */
	public GridExpandableListAdapter(Context contexto, int groupLayoutId, GroupRenderBlock<G> groupRenderBlock,
			IconTextRenderBlock<E> elementRenderBlock, ElementGrouper<G, E> elementGrouper, List<E> elementos) {
		// No se permite un layout custom para los elementos por eso el 0
		super(contexto, groupLayoutId, groupRenderBlock, 0, elementRenderBlock, elementGrouper, elementos);
		this.contexto = contexto;
	}

	/**
	 * Crea un adapter para una vista expandida que agrupa los elementos por un grupo determinado en
	 * cada uno.<br>
	 * Este constructor asume que la base son los elementos a partir de los cuales se obtienen sus
	 * grupos con un {@link ElementGrouper} y que el grupo es comparable<br>
	 * Para las vistas de grupos se asume que se quieren mostrar textos y se utiliza la default de
	 * android R.layout.simple_list_item_1
	 * 
	 * @param contexto
	 *            Contexto del cual obtener las vistas
	 * @param textRenderBlock
	 *            El código para definir el texto para los grupos
	 * @param elementRenderBlock
	 *            El bloque para popular la vista del elemento
	 * @param elementGrouper
	 *            Agrupador que permite obtener el grupo de cada elemento
	 * @param elementos
	 *            Los elementos de base
	 */
	public GridExpandableListAdapter(Context contexto, TextGroupRenderBlock<G> textRenderBlock,
			IconTextRenderBlock<E> elementRenderBlock, ElementGrouper<G, E> elementGrouper, List<E> elementos) {
		this(contexto, DEFAULT_TEXT_LAYOUT, textRenderBlock, elementRenderBlock, elementGrouper, elementos);
	}

	/**
	 * Este método siempre retorna un elemento por la grilla
	 * 
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		// Siempre hay un sólo elemento del grupo que es la grilla
		return 1;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
	 *      android.view.ViewGroup)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView,
			final ViewGroup expandableListParent) {
		int pixelsPerColumn = ContextHelper.convertDpToPixels(contexto, 85);
		int cellSpacingPixels = ContextHelper.convertDpToPixels(contexto, 6);
		ItemizedGridAdapter<E> gridAdapter;
		GridView gridView = (GridView) convertView;
		if (gridView == null) {
			// Creamos la grilla para mostrar los elementos del grupo
			gridView = new GridView(contexto);
			gridAdapter = ItemizedGridAdapter.create(contexto, getElementRenderBlock());
			gridView.setAdapter(gridAdapter);

			// La configuramos para 3 columnas
			gridView.setColumnWidth(pixelsPerColumn);
			gridView.setNumColumns(GridView.AUTO_FIT);
			gridView.setVerticalSpacing(cellSpacingPixels);
			gridView.setHorizontalSpacing(cellSpacingPixels);
			gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			gridView.setGravity(Gravity.CENTER);
			LayoutParams layoutParams = new AbsListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			gridView.setLayoutParams(layoutParams);
		} else {
			gridAdapter = (ItemizedGridAdapter<E>) gridView.getAdapter();
		}

		// Quitamos los anteriores y le agregamos los elementos
		gridAdapter.setNotifyDataChanges(false);
		gridAdapter.removeAll();
		List<E> elementosDelGrupo = getElementosDeGrupo().get(groupPosition);
		for (E elemento : elementosDelGrupo) {
			gridAdapter.addItem(elemento);
		}

		// Dimensionamos la grilla
		int itemPerRow = expandableListParent.getWidth() / pixelsPerColumn;
		// El (itemPerRow - 1) asegura que se vean los últimos
		int rowCount = (gridAdapter.getCount() + (itemPerRow - 1)) / itemPerRow;
		// TODO: cambiar este número mágico. Es el ancho de cada fila que no sé como calcularlo
		int pixelsPerRow = 166;
		int gridHeight = pixelsPerRow * rowCount;
		gridView.getLayoutParams().height = gridHeight;

		if (onChildClickListener != null) {
			gridView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View textView, int childPosition, long id) {
					// Derivamos el click al listener indicado
					onChildClickListener.onChildClick((ExpandableListView) expandableListParent, textView,
							groupPosition, childPosition, id);
				}
			});

		}

		gridAdapter.notifyDataSetChanged();
		return gridView;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
