/**
 * 27/02/2011 23:53:14 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.receivers;

import android.content.BroadcastReceiver;

/**
 * Esta clase es un receiver que permite recibir los intents de booteo.<br>
 * En realidad sirve más como recordatorio del intent.<br>
 * <br>
 * Se debe declarar el filter:<br>
 * &lt;intent-filter&gt; <br>
 * &lt;action android:name="android.intent.action.BOOT_COMPLETED" /&gt;<br>
 * &lt;category android:name="android.intent.category.HOME" /&gt; <br>
 * &lt;/intent-filter&gt;
 * 
 * 
 * @author D. García
 */
public abstract class SystemBootReceiver extends BroadcastReceiver {
	public static final String ACTION = "android.intent.action.BOOT_COMPLETED";

}
