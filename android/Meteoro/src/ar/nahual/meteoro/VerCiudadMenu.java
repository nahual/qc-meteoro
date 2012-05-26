/**
 * 26/05/2012 15:16:06 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro;

import android.content.Context;
import android.content.Intent;
import ar.com.iron.menues.ActivityMenuItem;

/**
 * 
 * @author D. García
 */
public enum VerCiudadMenu implements ActivityMenuItem<VerCiudadActivity> {
	AGREGAR_CIUDAD {
		@Override
		public Object getItemTitleOrResId() {
			return "Agregar ciudad";
		}

		@Override
		public boolean onSelection(final VerCiudadActivity activity) {
			activity.onAgregarCiudadSelected();
			return true;
		}
	};

	/**
	 * @see ar.com.iron.menues.CustomMenuItem#getItemTitleOrResId()
	 */
	@Override
	public Object getItemTitleOrResId() {
		return null;
	}

	/**
	 * @see ar.com.iron.menues.ActivityMenuItem#getFiredActivityIntent(android.content.Context)
	 */
	@Override
	public Intent getFiredActivityIntent(final Context contexto) {
		return null;
	}

	/**
	 * @see ar.com.iron.menues.ActivityMenuItem#onSelection(android.app.Activity)
	 */
	@Override
	public boolean onSelection(final VerCiudadActivity activity) {
		return false;
	}

	/**
	 * @see ar.com.iron.menues.ActivityMenuItem#getIconId()
	 */
	@Override
	public Integer getIconId() {
		return null;
	}

}
