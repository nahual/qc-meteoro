package ar.com.iron.android.extensions.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;

/**
 * 
 * @author D.Garcia
 * @since 05/10/2009
 */
public class NotificationBuilder {

	private final Notification notificacion;

	public NotificationBuilder(Context contexto, int iconId, CharSequence textoEvento, CharSequence titulo,
			CharSequence descripcion, PendingIntent intent) {
		notificacion = new Notification(iconId, textoEvento, System.currentTimeMillis());
		notificacion.setLatestEventInfo(contexto, titulo, descripcion, intent);
	}

	/**
	 * Crea la notificacion
	 * 
	 * @return
	 */
	public Notification build() {
		return notificacion;
	}

	/**
	 * Hace que la notificación parezca de un evento en progreso. No cancebale en la sección de
	 * ongoing
	 */
	public void makePermanent() {
		notificacion.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
	}

	/**
	 * Hace que la notificación se vaya al clickearla
	 */
	public void makeCancelableOnClick() {
		notificacion.flags |= Notification.FLAG_AUTO_CANCEL;
	}

}
