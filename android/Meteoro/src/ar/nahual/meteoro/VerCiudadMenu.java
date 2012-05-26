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
	AGREGAR_CIUDAD;

	/**
	 * @see ar.com.iron.menues.CustomMenuItem#getItemTitleOrResId()
	 */
	@Override
	public Object getItemTitleOrResId() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("El método no fue implementado");
	}

	/**
	 * @see ar.com.iron.menues.ActivityMenuItem#getFiredActivityIntent(android.content.Context)
	 */
	@Override
	public Intent getFiredActivityIntent(final Context contexto) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("El método no fue implementado");
	}

	/**
	 * @see ar.com.iron.menues.ActivityMenuItem#onSelection(android.app.Activity)
	 */
	@Override
	public boolean onSelection(final VerCiudadActivity activity) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("El método no fue implementado");
	}

	/**
	 * @see ar.com.iron.menues.ActivityMenuItem#getIconId()
	 */
	@Override
	public Integer getIconId() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("El método no fue implementado");
	}

}
