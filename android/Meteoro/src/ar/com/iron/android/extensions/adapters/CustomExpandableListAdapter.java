/**
 * 08/03/2011 01:53:16 Copyright (C) 2011 Darío L. García
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import ar.com.iron.android.helpers.ContextHelper;
import ar.com.iron.colls.TreeMultiValueMap;

/**
 * Esta clase es una implementación custom del {@link ExpandableListAdapter} que asume que los
 * elementos de cada grupo son obtenibles con una función aplicada a cada uno de los grupos.<br>
 * Este adapter cachea las listas de elementos de cada grupo de manera que no es apta para
 * demasiados datos, y debe notificarse cada vez que se produce un cambio en los datos<br>
 * Se utiliza una lista de listas para contener la info de los grupos.<br>
 * <br>
 * Existen dos modos de uso, o se definen los grupos y un {@link GroupExpander} para obtener sus
 * elementos, o se definen los elementos y se utiliza un {@link ElementGrouper} para obtener sus
 * grupos
 * 
 * @author D. García
 * @param <G>
 *            Tipo de los objetos usados como grupo
 * @param <E>
 *            Tipo de los objetos usados como elementos de cada grupo
 */
public class CustomExpandableListAdapter<G, E> extends BaseExpandableListAdapter {

	/**
	 * Layout usado por defecto cuando no se especifica para mostrar un texto
	 */
	public static final int DEFAULT_TEXT_LAYOUT = android.R.layout.simple_list_item_1;

	private List<List<E>> elementosDeGrupo;

	private int groupLayoutId;
	private GroupRenderBlock<G> groupRenderBlock;
	private int elementLayoutId;
	private RenderBlock<E> elementRenderBlock;

	private List<G> groups;
	private List<E> elements;

	/**
	 * Opcional para obtener los elementos a partir de los grupos
	 */
	private GroupExpander<G, E> expander;

	/**
	 * Opcional para obtener los grupos a partir de los elementos
	 */
	private ElementGrouper<G, E> grouper;

	private LayoutInflater layoutInflater;

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
	public CustomExpandableListAdapter(Context contexto, int groupLayoutId, GroupRenderBlock<G> groupRenderBlock,
			int elementLayoutId, RenderBlock<E> elementRenderBlock, List<G> groups, GroupExpander<G, E> expander) {
		init(contexto, groupLayoutId, groupRenderBlock, elementLayoutId, elementRenderBlock, null, null, expander,
				groups);
	}

	/**
	 * Crea un adapter para aplicar en un {@link ExpandableListView} que toma la lista de grupos y
	 * un {@link GroupExpander} que permite obtener los elementos de cada grupo.<br>
	 * Este constructor asume que la base son los grupos y a partir de ellos se obtienen los
	 * elementos.<br>
	 * Este constructor asume que la vista de los grupos será un texto usando
	 * R.layout.simple_list_item_1, que el texto se obtiene del grupo. Para los elementos del grupo
	 * se permite una vista custom
	 * 
	 * @param contexto
	 *            Contexto del cual obtener las vistas
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
	public CustomExpandableListAdapter(Context contexto, TextGroupRenderBlock<G> groupRenderBlock, int elementLayoutId,
			RenderBlock<E> elementRenderBlock, List<G> groups, GroupExpander<G, E> expander) {
		this(contexto, DEFAULT_TEXT_LAYOUT, groupRenderBlock, elementLayoutId, elementRenderBlock, groups, expander);
	}

	/**
	 * Crea un adapter para aplicar en un {@link ExpandableListView} que toma la lista de grupos y
	 * un {@link GroupExpander} que permite obtener los elementos de cada grupo.<br>
	 * Este constructor asume que la base son los grupos y a partir de ellos se obtienen los
	 * elementos.<br>
	 * Este constructor asume que tanto la vista de los grupos como de elementos será un texto
	 * usando R.layout.simple_list_item_1, que el texto se obtiene del grupo o elemento
	 * 
	 * @param contexto
	 *            Contexto del cual obtener las vistas
	 * @param groupRenderBlock
	 *            El código para popular la vista de los grupos
	 * @param elementRenderBlock
	 *            El bloque para popular la vista del elemento
	 * @param groups
	 *            La lista de grupos que se tomará como referencia
	 * @param expander
	 *            La función que permite obtener los elementos de cada grupo
	 */
	public CustomExpandableListAdapter(Context contexto, TextGroupRenderBlock<G> groupRenderBlock,
			TextRenderBlock<E> elementRenderBlock, List<G> groups, GroupExpander<G, E> expander) {
		this(contexto, DEFAULT_TEXT_LAYOUT, groupRenderBlock, DEFAULT_TEXT_LAYOUT, elementRenderBlock, groups, expander);
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
	 * @param elementos
	 */
	public CustomExpandableListAdapter(Context contexto, int groupLayoutId, GroupRenderBlock<G> groupRenderBlock,
			int elementLayoutId, RenderBlock<E> elementRenderBlock, ElementGrouper<G, E> elementGrouper,
			List<E> elementos) {
		init(contexto, groupLayoutId, groupRenderBlock, elementLayoutId, elementRenderBlock, elementGrouper, elementos,
				null, null);
	}

	/**
	 * Crea un adapter para una vista expandida que agrupa los elementos por un grupo determinado en
	 * cada uno.<br>
	 * Este constructor asume que la base son los elementos a partir de los cuales se obtienen sus
	 * grupos con un {@link ElementGrouper} y que el grupo es comparable.<br>
	 * Este constructor asume que la vista de los grupos será un texto usando
	 * R.layout.simple_list_item_1, que el texto se obtiene del grupo. Para los elementos del grupo
	 * se permite una vista custom
	 * 
	 * @param contexto
	 *            Contexto del cual obtener las vistas
	 * @param groupRenderBlock
	 *            El código para popular la vista de los grupos
	 * @param elementLayoutId
	 *            El id del layout para representar cada elemento
	 * @param elementRenderBlock
	 *            El bloque para popular la vista del elemento
	 * @param elementGrouper
	 * @param elementos
	 */
	public CustomExpandableListAdapter(Context contexto, TextGroupRenderBlock<G> groupRenderBlock, int elementLayoutId,
			RenderBlock<E> elementRenderBlock, ElementGrouper<G, E> elementGrouper, List<E> elementos) {
		this(contexto, DEFAULT_TEXT_LAYOUT, groupRenderBlock, elementLayoutId, elementRenderBlock, elementGrouper,
				elementos);
	}

	/**
	 * Crea un adapter para una vista expandida que agrupa los elementos por un grupo determinado en
	 * cada uno.<br>
	 * Este constructor asume que la base son los elementos a partir de los cuales se obtienen sus
	 * grupos con un {@link ElementGrouper} y que el grupo es comparable.<br>
	 * Este constructor asume que tanto la vista de los grupos como de elementos será un texto
	 * usando R.layout.simple_list_item_1, que el texto se obtiene del grupo o elemento
	 * 
	 * @param contexto
	 *            Contexto del cual obtener las vistas
	 * @param groupRenderBlock
	 *            El código para popular la vista de los grupos
	 * @param elementRenderBlock
	 *            El bloque para popular la vista del elemento
	 * @param elementGrouper
	 * @param elementos
	 */
	public CustomExpandableListAdapter(Context contexto, TextGroupRenderBlock<G> groupRenderBlock,
			RenderBlock<E> elementRenderBlock, ElementGrouper<G, E> elementGrouper, List<E> elementos) {
		this(contexto, DEFAULT_TEXT_LAYOUT, groupRenderBlock, DEFAULT_TEXT_LAYOUT, elementRenderBlock, elementGrouper,
				elementos);
	}

	/**
	 * Inicializa esta instancia para que sea consistente
	 */
	private void init(Context contexto, int groupLayoutId, GroupRenderBlock<G> groupRenderBlock, int elementLayoutId,
			RenderBlock<E> elementRenderBlock, ElementGrouper<G, E> elementGrouper, List<E> elementos,
			GroupExpander<G, E> expander, List<G> groups) {
		this.elementLayoutId = elementLayoutId;
		this.elementRenderBlock = elementRenderBlock;
		this.groupLayoutId = groupLayoutId;
		this.groupRenderBlock = groupRenderBlock;
		this.grouper = elementGrouper;
		this.elements = elementos;
		this.groups = groups;
		this.expander = expander;
		this.layoutInflater = ContextHelper.getLayoutInflater(contexto);
		reconstruirElementosDeCadaGrupo();
	}

	/**
	 * @see android.widget.BaseExpandableListAdapter#notifyDataSetChanged()
	 */
	@Override
	public void notifyDataSetChanged() {
		reconstruirElementosDeCadaGrupo();
		super.notifyDataSetChanged();
	}

	/**
	 * Reconstruye la información de elementos de cada grupo para adaptarse a los cambios en los
	 * grupos. Descartando la estructura anterior.<br>
	 * Si existe un expander se expanden los grupos, si no se agrupan los elementos
	 */
	private void reconstruirElementosDeCadaGrupo() {
		if (expander != null) {
			expandGroups();
		} else {
			groupElements();
		}
	}

	/**
	 * Obtiene el grupo de cada elemento y los ordena reflejandolos en la lista de grupos y de
	 * elementos por grupo
	 */
	private void groupElements() {
		// Primero los clasificamos por grupo y los ordenamos
		TreeMultiValueMap<Comparable<Comparable<?>>, E> groupings = new TreeMultiValueMap<Comparable<Comparable<?>>, E>();
		for (E element : elements) {
			G group = grouper.getGroupOf(element);
			if (!(group instanceof Comparable)) {
				throw new IllegalArgumentException("El grupo[" + group + "] para el elemento[" + element
						+ "] no es comparable");
			}
			// Forzamos el casteo sólo para que se entienda que estamos tomándolo como comparable
			@SuppressWarnings("unchecked")
			Comparable<Comparable<?>> comparableGroup = (Comparable<Comparable<?>>) group;
			groupings.putValue(comparableGroup, element);
		}
		// Creamos las estructuras necesarias
		int cantidadGrupos = groupings.size();
		groups = new ArrayList<G>(cantidadGrupos);
		elementosDeGrupo = new ArrayList<List<E>>(cantidadGrupos);
		Set<Entry<Comparable<Comparable<?>>, List<E>>> groupEntries = groupings.entrySet();
		for (Entry<Comparable<Comparable<?>>, List<E>> groupEntry : groupEntries) {
			// Volvemos a castear a G, para respetar los tipos (sabemos que era G de antes)
			@SuppressWarnings("unchecked")
			G group = (G) groupEntry.getKey();
			List<E> groupElements = groupEntry.getValue();
			// Como son listas (ordenadas) se mantiene la coherencia entre grupo y elementos sin
			// índice
			groups.add(group);
			elementosDeGrupo.add(groupElements);
		}
	}

	/**
	 * Obtiene los elementos de cada grupo y los agrega en la lista de elementos por grupo
	 */
	private void expandGroups() {
		this.elementosDeGrupo = new ArrayList<List<E>>(groups.size());
		// Como son listas y mantienen el orden no es necesario utilizar indices
		for (G grupo : groups) {
			ArrayList<E> elementos = new ArrayList<E>();
			expander.expand(grupo, elementos);
			this.elementosDeGrupo.add(elementos);
		}
	}

	/**
	 * @see android.widget.BaseExpandableListAdapter#notifyDataSetInvalidated()
	 */
	@Override
	public void notifyDataSetInvalidated() {
		reconstruirElementosDeCadaGrupo();
		super.notifyDataSetInvalidated();
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	public int getGroupCount() {
		return groups.size();
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	public int getChildrenCount(int groupPosition) {
		return elementosDeGrupo.get(groupPosition).size();
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	public G getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	public E getChild(int groupPosition, int childPosition) {
		return elementosDeGrupo.get(groupPosition).get(childPosition);
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	public boolean hasStableIds() {
		return false;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
	 *      android.view.ViewGroup)
	 */
	public View getGroupView(int groupPosition, boolean isExpanded, View reusableView, ViewGroup parent) {
		if (reusableView == null) {
			// Crea la instancia de la vista del item a partir del id
			reusableView = layoutInflater.inflate(groupLayoutId, parent, false);
		}
		G group = this.getGroup(groupPosition);
		if (group != null) {
			groupRenderBlock.render(reusableView, group, isExpanded, layoutInflater);
		}
		return reusableView;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
	 *      android.view.ViewGroup)
	 */
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View reusableView,
			ViewGroup parent) {
		if (reusableView == null) {
			// Crea la instancia de la vista del item a partir del id
			reusableView = layoutInflater.inflate(elementLayoutId, parent, false);
		}
		E element = this.getChild(groupPosition, childPosition);
		if (element != null) {
			elementRenderBlock.render(reusableView, element, layoutInflater);
		}
		return reusableView;
	}

	/**
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	protected List<List<E>> getElementosDeGrupo() {
		return elementosDeGrupo;
	}

	protected List<G> getGroups() {
		return groups;
	}

	public RenderBlock<E> getElementRenderBlock() {
		return elementRenderBlock;
	}

	/**
	 * Reemplaza todos los elementos vaciando la lista base y agregando los elementos pasados
	 * 
	 * @param results
	 */
	public void replaceAllElements(List<E> newElements) {
		this.elements.clear();
		this.elements.addAll(newElements);
		this.notifyDataSetChanged();
	}

	/**
	 * Elimina el elemento indicado de este adapter
	 * 
	 * @param element
	 *            Elemento a borrar
	 */
	public void removeElement(E element) {
		this.elements.remove(element);
		this.notifyDataSetChanged();
	}

}
