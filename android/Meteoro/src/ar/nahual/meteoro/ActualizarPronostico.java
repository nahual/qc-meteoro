/**
 * 07/06/2012 09:30:17 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro;

import android.content.Intent;

/**
 * Esta clase representa el mensaje enviado por el servicio en bg para solicitar actualizacion del
 * frontend
 * 
 * @author D. García
 */
public class ActualizarPronostico extends Intent {
	public static final String ACTION = "ar.nahual.meteoro.ActualizarPronostico";

	public ActualizarPronostico(final Intent other) {
		super(other);
	}

	public ActualizarPronostico() {
		super(ACTION);
	}

}
