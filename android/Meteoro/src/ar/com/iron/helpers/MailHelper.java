/**
 * 18/07/2011 21:53:34 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package ar.com.iron.helpers;

import android.content.Context;
import android.content.Intent;

/**
 * Clase que define algunos métodos para enviar el mail
 * 
 * @author D. García
 */
public class MailHelper {

	/**
	 * Genera un intent y comienza el activity para el envio de mail
	 * 
	 * @param contexto
	 *            El contexto para el envío
	 * @param mailRecipient
	 *            El receptor del mail
	 * @param mailSubject
	 *            El asunto del tema
	 * @param mailBody
	 *            El cuerpo del mail
	 */
	public static void sendPlainTextMail(Context contexto, String mailRecipient, CharSequence mailSubject,
			String mailBody) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { mailRecipient });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mailSubject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mailBody);
		emailIntent.setType("text/plain");
		contexto.startActivity(emailIntent);
	}

}
