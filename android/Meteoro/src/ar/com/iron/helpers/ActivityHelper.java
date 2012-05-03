/**
 * 09/12/2009 1:12:39<br>
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
package ar.com.iron.helpers;

import android.app.Activity;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import ar.com.iron.android.extensions.activities.model.CustomableActivity;
import ar.com.iron.android.helpers.ContextHelper;
import ar.com.iron.menues.ActivityMenuItem;
import ar.com.iron.menues.DynamicMenuItem;
import ar.com.iron.menues.ScreenMenuHelper;

/**
 * Esta clase reune varios métodos que son implementación de métodos custom en los activities
 * 
 * @author D.L. García
 */
public class ActivityHelper {

	/**
	 * Devuelve la vista raíz de toda la jerarquía de vistas del activity pasado
	 * 
	 * @param activity
	 *            Actividad de la que se sacará la vista
	 * @return Vista raíz
	 */
	public static View getContentViewFrom(final Activity activity) {
		return activity.getWindow().getDecorView();
	}

	/**
	 * Oculta el teclado virtual en el activity indicado
	 * 
	 * @param activity
	 *            Actividad que esta mostrando el teclado virtual
	 */
	public static void hideSoftKeyboardFor(final Activity activity) {
		final InputMethodManager keyboardManager = ContextHelper.getKeyboardManager(activity);
		final Window window = activity.getWindow();
		final View rootView = window.getDecorView();
		final IBinder viewTokenId = rootView.getWindowToken();
		keyboardManager.hideSoftInputFromWindow(viewTokenId, 0);
	}

	/**
	 * Muestra el teclado virtual en el activity indicado
	 * 
	 * @param activity
	 *            Actividad que esta mostrando el teclado virtual
	 */
	public static void showSoftKeyboardFor(final Activity activity, final View viewWithTextInput) {
		final InputMethodManager keyboardManager = ContextHelper.getKeyboardManager(activity);
		keyboardManager.showSoftInput(viewWithTextInput, 0);
	}

	/**
	 * Indica si el activity pasado tiene en alguna de sus opciones de menú un item dinámico, que
	 * requiere regeneración del menú.<br>
	 * Si se devuelve true este activity regenerará el menú cada vez que deba ser mostrado
	 * 
	 * @param activity
	 *            Activity a comprobar
	 * @return false si el menú es fijo
	 */
	public static boolean usesDynamicMenuOptions(final CustomableActivity activity) {
		final ActivityMenuItem<? extends CustomableActivity>[] menuItems = activity.getMenuItems();
		if (menuItems == null) {
			return false;
		}
		for (final ActivityMenuItem<? extends CustomableActivity> menuItem : menuItems) {
			if (menuItem instanceof DynamicMenuItem) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Prepara el menú para mostrar las opciones dinámicas actualizadas a su estado actual.<br>
	 * La implementación actual limpia el menú
	 * 
	 * @param customActivity
	 *            Activity para el que se mostrará el menú
	 * @param menu
	 *            El menú a mostrar
	 * @return boolean si se debe mostrar el menú
	 */
	public static boolean prepareDynamicOptionsMenuFor(final CustomableActivity customActivity, final Menu menu) {
		menu.clear();
		return createActivityOptionsFor(customActivity, menu);
	}

	/**
	 * Crea las opciones de menú para la actividad pasada
	 * 
	 * @param menu
	 *            El menú en el que se crearán
	 * @return true si se crearon opciones para el menú
	 */
	@SuppressWarnings("unchecked")
	public static boolean createActivityOptionsFor(final CustomableActivity activity, final Menu menu) {
		final ActivityMenuItem<Activity>[] menuItems = (ActivityMenuItem<Activity>[]) activity.getMenuItems();
		return ScreenMenuHelper.createActivityOptionsFor(menu, (Activity) activity, menuItems);
	}
}
