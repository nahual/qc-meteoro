package ar.com.iron.android.extensions.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Esta clase permite implementar una configuración persistente en archivo de preferencias
 * abstrayendo los detalles detrás de un objeto
 * 
 * @author D. García
 */
public abstract class PreferencesConfiguration {

	private final SharedPreferences preferences;
	private Editor preferenceEditor;

	public PreferencesConfiguration(Context context, String fileName) {
		preferences = context.getSharedPreferences(fileName, Context.MODE_WORLD_READABLE);
	}

	protected SharedPreferences getPreferences() {
		return preferences;
	}

	protected Editor getPreferenceEditor() {
		if (preferenceEditor == null) {
			preferenceEditor = preferences.edit();
		}
		return preferenceEditor;
	}

	/**
	 * Guarda los cambios realizados en esta instancia sobre el archivo de preferencias
	 */
	public void saveChanges() {
		boolean committed = getPreferenceEditor().commit();
		if (!committed) {
			throw new RuntimeException("No fue posible guardar el cambio de preferences");
		}
	}

}