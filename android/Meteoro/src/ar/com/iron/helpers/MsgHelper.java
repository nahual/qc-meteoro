package ar.com.iron.helpers;

import android.content.Context;
import android.widget.Toast;

/**
 * Esta clase ofrece algunos método para acceder fácil a los mensajes para el usuario
 * 
 * @author D. Garcia
 */
public class MsgHelper {

	/**
	 * Muestra un mensaje toast por poco tiempo
	 * 
	 * @param messageId
	 *            Entero que identifica el id del texto como recurso, o un String con el texto a
	 *            mostrar
	 * @param applicationContext
	 */
	public static void shortToast(Object messageId, Context applicationContext) {
		showToast(messageId, applicationContext, Toast.LENGTH_SHORT);
	}

	/**
	 * Muestra un toast con el texto indicado por la duracion de tiempo especificada y luego lo
	 * descarta
	 * 
	 * @param messageId
	 *            Idntificacion del recurso string o un String con el texto
	 * @param applicationContext
	 * @param duracion
	 */
	private static void showToast(Object messageId, Context applicationContext, int duracion) {
		Toast toast = null;
		if (messageId instanceof CharSequence) {
			CharSequence messageText = (CharSequence) messageId;
			toast = Toast.makeText(applicationContext, messageText, duracion);
		} else if (messageId instanceof Number) {
			int resourceId = ((Number) messageId).intValue();
			toast = Toast.makeText(applicationContext, resourceId, duracion);
		} else {
			throw new IllegalArgumentException("El messageId debe ser un CharSequence o Number: " + messageId);
		}
		toast.show();
	}

	/**
	 * Muestra un mensaje largo a partir de su id
	 * 
	 * @param messageId
	 *            Id del string a mostrar
	 * @param applicationContext
	 *            Contexto en el que se mostrará
	 */
	public static void longToast(Object messageId, Context applicationContext) {
		showToast(messageId, applicationContext, Toast.LENGTH_LONG);
	}

}
