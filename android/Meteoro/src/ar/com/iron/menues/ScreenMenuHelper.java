package ar.com.iron.menues;

import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ListAdapter;

/**
 * Esta clase intenta servir como una utilidad para configurar los menues de una pantalla.<br>
 * Generalmente el código de los métodos donde se configuran la creación del menú y menú contextual
 * de una pantalla termina siendo repetitivo.<br>
 * Esta clase permite implementar los menúes de opciones tanto contextuales como de activity usando
 * enums o cualquier otro objeto que permita definir los atributos del menú
 * 
 * @author Maximiliano Vázquez, S. Fernández, D. García
 * 
 */
public abstract class ScreenMenuHelper {

	/**
	 * Este método se utiliza para crear y configurar el menú de opciones para un activity.<br>
	 * 
	 * @param <A>
	 *            Tipo de actividad usado
	 * @param menu
	 *            El menú que contendrá las opciones a configurar
	 * @param hostActivity
	 *            El activity para el que se crean las opciones de menú
	 * @param menuOptionDefinitions
	 *            Un array con cada una de las definiciones para cada opción de menú, o null si no
	 *            hay opciones
	 * @return false si no se debe crear un menú (por que no hay opciones), true si se creó y
	 *         configuró cada opción
	 */
	public static <A extends Activity> boolean createActivityOptionsFor(final Menu menu, final A hostActivity,
			final ActivityMenuItem<A>[] menuOptionDefinitions) {
		if (menuOptionDefinitions == null || menuOptionDefinitions.length == 0) {
			return false;
		}

		for (int itemId = 0; itemId < menuOptionDefinitions.length; itemId++) {
			final ActivityMenuItem<A> menuOptionDefinition = menuOptionDefinitions[itemId];
			if (menuOptionDefinition instanceof DynamicMenuItem) {
				final DynamicMenuItem dynamicOptionDefinition = (DynamicMenuItem) menuOptionDefinition;
				if (!dynamicOptionDefinition.isVisible()) {
					// Si no es visible, no se incluye como opción
					continue;
				}
			}

			final MenuItem addedMenuItem = createMenuOptionFor(menuOptionDefinition, itemId, menu);
			if (!addedMenuItem.isEnabled()) {
				// Si no es seleccionable, no agregamos código para tratar su selección
				continue;
			}

			final Intent firedActivityIntent = menuOptionDefinition.getFiredActivityIntent(hostActivity);
			if (firedActivityIntent != null) {
				addedMenuItem.setIntent(firedActivityIntent);
				// Si tiene intent no se debe usar un click listener
				continue;
			}

			// Por defecto derivamos la invocación a su definición
			addedMenuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(final MenuItem item) {
					return menuOptionDefinition.onSelection(hostActivity);
				}
			});
		}
		return true;
	}

	/**
	 * Este método crea y configura las opciones contextuales de menú para una listado.<br>
	 * El menú contextual será construido a partir del array pasado y se usará el adapter
	 * correspondiente para acceder al item seleccionado cuando una opción del menú es elegida
	 * 
	 * @param <D>
	 *            Tipo de los elementos que tienen menú contextual
	 * @param <A>
	 *            Tipo de actividad involucrada
	 * @param menu
	 *            El menu sobre el cual se crearán las opciones
	 * @param menuOptionDefinitions
	 *            El array con una definición de opción por item del menú
	 * @param contextMenuHeaderTitleOrResourceId
	 *            El titulo para incluir en el menú contextual como texto o como id de texto, o null
	 * @param hostActivity
	 *            Actividad involucrada en el menú contextual. Siempre debe existir una que es la
	 *            que realmente recibe los eventos
	 * @param listAdapterOrExpandableListAdapter
	 *            Adapter de la lista o lista expandible utilizada para acceder a los elementos al
	 *            ser seleccionada una opción
	 */
	public static <A extends Activity> void createContextOptionsFor(final ContextMenu menu,
			final ContextMenuItem<A, ?>[] menuOptionDefinitions, final Object contextMenuHeaderTitleOrResourceId,
			final A hostActivity, final Object listAdapterOrExpandableListAdapter) {
		if (menuOptionDefinitions == null || menuOptionDefinitions.length == 0) {
			// Si no hay opciones no hacemos nada
			return;
		}

		configureMenuTitle(menu, contextMenuHeaderTitleOrResourceId);

		for (int itemId = 0; itemId < menuOptionDefinitions.length; itemId++) {
			final ContextMenuItem<A, ?> menuOptionDefinition = menuOptionDefinitions[itemId];

			if (menuOptionDefinition instanceof DynamicMenuItem) {
				final DynamicMenuItem dynamicOptionDefinition = (DynamicMenuItem) menuOptionDefinition;
				if (!dynamicOptionDefinition.isVisible()) {
					// Si no es visible, no se incluye como opción
					continue;
				}
			}

			final MenuItem addedMenuItem = createMenuOptionFor(menuOptionDefinition, itemId, menu);
			if (!addedMenuItem.isEnabled()) {
				// Si no es seleccionable, no agregamos código para tratar su selección
				continue;
			}

			// Agregamos el listener para que nos derive la invocación
			final MenuItem clickedItem = addedMenuItem;
			addedMenuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(final MenuItem selectedMenu) {
					return processContextItemClick(clickedItem, hostActivity, listAdapterOrExpandableListAdapter,
							menuOptionDefinition);
				}
			});
		}
	}

	/**
	 * Crea una opción dentro del menú indicado que refleja la configuración de la definición pasada
	 * 
	 * @param menuOptionDefinition
	 *            Definición que indica la configuración de la opción
	 * @param itemId
	 *            El identificador de la opción dentro del menú
	 * @param menu
	 *            El menú contenedor de todas las opciones
	 * @return La opción creada
	 */
	private static MenuItem createMenuOptionFor(final CustomMenuItem menuOptionDefinition, final int itemId,
			final Menu menu) {
		final Object itemTitleOrResId = menuOptionDefinition.getItemTitleOrResId();

		final MenuItem addedMenuItem = createMenuItemIn(menu, itemId, itemTitleOrResId);

		if (menuOptionDefinition instanceof DynamicMenuItem) {
			final DynamicMenuItem dynamicOptionDefinition = (DynamicMenuItem) menuOptionDefinition;
			addedMenuItem.setEnabled(dynamicOptionDefinition.isEnabled());
		}

		// Vemos si define un ícono
		if (menuOptionDefinition instanceof ActivityMenuItem) {
			final ActivityMenuItem<?> iconizedDefinition = (ActivityMenuItem<?>) menuOptionDefinition;
			final Integer iconId = iconizedDefinition.getIconId();
			if (iconId != null) {
				// Si tiene se lo definimos
				addedMenuItem.setIcon(iconId);
			}
		}

		return addedMenuItem;
	}

	/**
	 * Este método posee la lógica necesaria para procesar la selección de un item del menu
	 * contextual para cualquier activity. Permitiendo tratar tanto listas normales como expandibles<br>
	 * 
	 * @param <D>
	 *            Tipo de los elementos con menú contextual
	 * @param <A>
	 *            Activity en el que se aloja la vista
	 * @param item
	 *            Item seleccionado
	 * @param activity
	 *            Activity involucrada
	 * @param listAdapterOrExpandableListAdapter
	 *            Adapter que permite llegar al item clickeado (puede ser un list adapter o
	 *            expandableList adapter
	 * @param selectedItem
	 *            El item elegido de todas las opciones
	 * @return true si el evento fue tratado
	 */
	private static <D, A extends Activity> boolean processContextItemClick(final MenuItem item, final A activity,
			final Object listAdapterOrExpandableListAdapter, final ContextMenuItem<A, D> selectedItem) {
		Object contextualObject;
		final ContextMenuInfo menuInfo = item.getMenuInfo();
		if (menuInfo instanceof AdapterContextMenuInfo) {
			// Este tipo tiene la información de posición directamente
			final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			ListAdapter listAdapter;
			try {
				listAdapter = (ListAdapter) listAdapterOrExpandableListAdapter;
			} catch (final ClassCastException e) {
				throw new IllegalArgumentException(
						"El adapter pasado no es un ListAdapter pero el menu contextual si corresponde a uno", e);
			}
			contextualObject = listAdapter.getItem(info.position);
		} else if (menuInfo instanceof ExpandableListContextMenuInfo) {
			ExpandableListAdapter expandableAdapter;
			try {
				expandableAdapter = (ExpandableListAdapter) listAdapterOrExpandableListAdapter;
			} catch (final ClassCastException e) {
				throw new IllegalArgumentException(
						"El adapter pasado no es un ExpandableListAdapter pero el menu contextual si corresponde a uno",
						e);
			}

			// Este tipo tiene la posición empaquetada
			final ExpandableListContextMenuInfo contextMenu = (ExpandableListContextMenuInfo) menuInfo;
			final int positionType = ExpandableListView.getPackedPositionType(contextMenu.packedPosition);
			if (positionType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
				final int groupPosition = ExpandableListView.getPackedPositionGroup(contextMenu.packedPosition);
				contextualObject = expandableAdapter.getGroup(groupPosition);
			} else if (positionType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
				final int groupPosition = ExpandableListView.getPackedPositionGroup(contextMenu.packedPosition);
				final int childPosition = ExpandableListView.getPackedPositionChild(contextMenu.packedPosition);
				contextualObject = expandableAdapter.getChild(groupPosition, childPosition);
			} else {
				// Es un tipo de evento que no manejamos
				return false;
			}

		} else {
			throw new RuntimeException("Se esperaba un menuInfo instancia de " + AdapterContextMenuInfo.class
					+ " o de " + ExpandableListContextMenuInfo.class + ". Se obtuvo: " + menuInfo);
		}
		@SuppressWarnings("unchecked")
		final D selectedObject = (D) contextualObject;
		return selectedItem.onSelection(activity, selectedObject);
	}

	/**
	 * Agrega una opción al menú pasado identificada con el ID indicado y el usando el texto para el
	 * menú pasado. El item agregado no respeta un orden
	 * 
	 * @param menu
	 *            Menu en el que se agrega una opción.
	 * @param itemId
	 *            Identificador de la opción
	 * @param titleResId
	 *            El titulo como texto o como ID
	 * @return La opción del menú creada
	 */
	private static MenuItem createMenuItemIn(final Menu menu, final int itemId, final Object titleResId) {
		MenuItem addedMenuItem;
		if (titleResId instanceof CharSequence) {
			final CharSequence rawText = (CharSequence) titleResId;
			addedMenuItem = menu.add(0, itemId, ContextMenu.NONE, rawText);
		} else if (titleResId instanceof Number) {
			final int textId = ((Number) titleResId).intValue();
			addedMenuItem = menu.add(0, itemId, ContextMenu.NONE, textId);
		} else {
			throw new RuntimeException(
					"El titulo de la opción de menu debe ser un id de recurso string o un charSequence: " + titleResId);
		}
		return addedMenuItem;
	}

	/**
	 * Configura el titulo que se le asignará al menú
	 * 
	 * @param menu
	 *            El menú cuyo titulo se definirá
	 * @param contextMenuHeaderTitleResourceId
	 *            El texto para el titulo o el ID de recurso, o null si no se usa titulo
	 */
	private static void configureMenuTitle(final ContextMenu menu, final Object contextMenuHeaderTitleResourceId) {
		if (contextMenuHeaderTitleResourceId == null) {
			// No hay titulo para mostrar
			return;
		}
		if (contextMenuHeaderTitleResourceId instanceof CharSequence) {
			final CharSequence rawText = (CharSequence) contextMenuHeaderTitleResourceId;
			menu.setHeaderTitle(rawText);
		} else if (contextMenuHeaderTitleResourceId instanceof Number) {
			final int textId = ((Number) contextMenuHeaderTitleResourceId).intValue();
			menu.setHeaderTitle(textId);
		} else {
			throw new RuntimeException("El titulo del menu contextual debe ser un id o un texto: "
					+ contextMenuHeaderTitleResourceId);
		}
	}

}
