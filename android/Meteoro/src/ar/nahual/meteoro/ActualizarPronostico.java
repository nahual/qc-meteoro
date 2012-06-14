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
