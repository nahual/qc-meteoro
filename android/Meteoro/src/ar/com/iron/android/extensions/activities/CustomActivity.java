package ar.com.iron.android.extensions.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import ar.com.iron.android.extensions.activities.model.CustomableActivity;
import ar.com.iron.android.extensions.messages.IntentReceptor;
import ar.com.iron.helpers.ActivityHelper;
import ar.com.iron.menues.ActivityMenuItem;
import ar.com.iron.menues.DynamicMenuItem;
import ar.com.iron.menues.ScreenMenuHelper;

/**
 * Esta clase define comportamiento general para todos los activities
 * 
 * @author D. García
 */
public abstract class CustomActivity extends Activity implements CustomableActivity {

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

	public void setIntentReceptor(final IntentReceptor intentReceptor) {
		this.intentReceptor = intentReceptor;
	}

	/**
	 * @return Devuelve el array de items para crear en el menu de opcioens accesible desde el botón
	 *         "menu". Null indica sin menu
	 */
	@Override
	public ActivityMenuItem<? extends CustomActivity>[] getMenuItems() {
		return null;
	}

	/**
	 * Crea y configura los controles que se usaran en la pantalla. Opcional para aquellas pantallas
	 * que no tengan controles
	 */
	@Override
	public void setUpComponents() {
	}

	/**
	 * @return Devuelve la vista raiz de toda la pantalla
	 */
	@Override
	public View getContentView() {
		return ActivityHelper.getContentViewFrom(this);
	}

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(this.getLayoutIdForActivity());

		this.initDependencies();
		this.initMessageReceivers();
		this.setUpComponents();

		this.afterOnCreate();
	}

	/**
	 * Obtiene los managers o servicios que sean necesarios para el funcionamiento de esta pantalla
	 */
	@Override
	public void initDependencies() {
	}

	/**
	 * Ejecuta código específico de la subclase para configurar cosas adicionales (es opcional)
	 */
	@Override
	public void afterOnCreate() {
	}

	/**
	 * Crea el menu de opciones basándose en el array definido por la subclase
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		if (usesDynamicMenuOptions()) {
			// No creamos el menú ahora, ya que en el prepare se hará de nuevo por ser dinámico
			return true;
		}
		return createActivityOptions(menu);
	}

	/**
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		if (usesDynamicMenuOptions()) {
			menu.clear();
			return createActivityOptions(menu);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Indica si este activity usa opciones de menú dinámicos (que pueden cambiar de estado).<br>
	 * Si se devuelve true este activity regenerará el menú cada vez que deba ser mostrado
	 * 
	 * @return false por defecto ya que la mayoría de los menúes son fijos
	 */
	public boolean usesDynamicMenuOptions() {
		final ActivityMenuItem<? extends CustomActivity>[] menuItems = getMenuItems();
		if (menuItems == null) {
			return false;
		}
		for (final ActivityMenuItem<? extends CustomActivity> menuItem : menuItems) {
			if (menuItem instanceof DynamicMenuItem) {
				return true;
			}
		}
		return false;
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
	 * Metodo para la subclase que indica el momento adecuado para registrar los receivers de este
	 * activity que sirven para la comunicación de fondo.<br>
	 * Al registrar los receivers en este metodo se asegura que se recibirán los mensajes aún cuando
	 * la pantalla no es visible
	 */
	@Override
	public void initMessageReceivers() {
	}

	/**
	 * Se devuelve a sí misma como contexto para ser utilizada en métodos que requieren acceso a
	 * recursos
	 * 
	 * @return Esta actividad como contexto
	 */
	public Context getContext() {
		return this;
	}
}
