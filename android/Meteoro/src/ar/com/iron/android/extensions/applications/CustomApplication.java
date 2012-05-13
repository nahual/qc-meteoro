/**
 * 18/01/2011 16:21:10 Darío L. García
 */
package ar.com.iron.android.extensions.applications;

import java.lang.ref.SoftReference;

import android.app.Application;
import ar.com.iron.helpers.ToastHelper;

/**
 * Esta clase es la base para aplicaciones android
 * 
 * @author D. García
 */
public abstract class CustomApplication extends Application {

	/**
	 * Usamos una softreference a modo de cache
	 */
	private SoftReference<ToastHelper> toastHelper;

	public ToastHelper toast() {
		if (toastHelper == null || toastHelper.get() == null) {
			ToastHelper helper = ToastHelper.create(this);
			toastHelper = new SoftReference<ToastHelper>(helper);
		}

		return toastHelper.get();
	}

	/**
	 * Define la lógica a ejecutar durante la inicialización de la aplicación.<br>
	 * En este punto se inicializa el repositorio de datos de DB4O.
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		// Le pongo nombre para identificarlo
		Thread.currentThread().setName(getMainThreadName());

		initializeGlobalComponents();
	}

	/**
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();

		// No tiene efecto, por que esta llamada nunca es realizada fuera del emulador
		shutdownGlobalComponents();
	}

	/**
	 * Devuelve el nombre utilizado para discriminar al thread principal de la aplicación
	 * 
	 * @return El nombre para el thread (no puede ser null)
	 */
	protected abstract String getMainThreadName();

	/**
	 * Inicializa los componentes de la aplicación que tienen un alcance global
	 */
	protected void initializeGlobalComponents() {
		// Por defecto no hay componentes globales
	}

	/**
	 * Cierra los recursos asociados a componentes globales. <br>
	 * Este método debería llamarse cuando se sabe que la aplicación ya no es utilizada.<br>
	 * No existe un hook predefinido en android
	 */
	public void shutdownGlobalComponents() {
		// Por defecto no hay componentes globales
	}

}