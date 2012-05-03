/**
 * 09/12/2009 1:21:37<br>
 * Copyright (C) 2009 YAH Group<br>
 * <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by-nc-sa/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/" property="dc:title">YAH Tasks</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">YAH Group</span> is
 * licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by-nc-sa/2.5/ar/">Creative Commons
 * Attribution-Noncommercial-Share Alike 2.5 Argentina License</a>.
 */
package ar.com.iron.android.extensions.messages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;

/**
 * Esta clase permite agregar comportamiento de recepción de intents como si fueran mensajes a
 * cualquier otra instancia de la aplicación
 * 
 * @author D.L. García
 */
public class IntentReceptor {

	/**
	 * Contexto utilizado para el intercambio de mensajes
	 */
	private final Context messageContext;

	/**
	 * Handler opcional a utilizar para el encolado de mensajes
	 */
	private Handler customHandler;

	/**
	 * @param customActivity
	 */
	public IntentReceptor(final Context context) {
		this.messageContext = context;
	}

	/**
	 * Receptores que permiten la comunicacion mediante intents
	 */
	private List<BroadcastReceiver> messageReceivers;

	/**
	 * Registra en este activity un receiver que será disparado al recibir un intent del action
	 * declarado.<br>
	 * Al recibir el intent con el action declarado, se ejecuta el listener pasado
	 * 
	 * @param expectedAction
	 *            Action que declara el mensaje esperado
	 * @param executedWhenReceived
	 *            Listener a ejecutar cuando se recibe el mensaje
	 */
	public void registerMessageReceiver(final String expectedAction, final BroadcastReceiver executedWhenReceived) {
		final IntentFilter filtroMensajes = new IntentFilter(expectedAction);
		if (customHandler != null) {
			messageContext.registerReceiver(executedWhenReceived, filtroMensajes, null, customHandler);
		} else {
			messageContext.registerReceiver(executedWhenReceived, filtroMensajes);
		}
		getMessageReceivers().add(executedWhenReceived);
	}

	public List<BroadcastReceiver> getMessageReceivers() {
		if (messageReceivers == null) {
			// El tamaño viene dado porque rara vez se supera esa cantidad
			messageReceivers = new ArrayList<BroadcastReceiver>(2);
		}
		return messageReceivers;
	}

	/**
	 * Desregistra todos los receptores en background. Normalmente queremos que suceda esto al
	 * finalizar el activity
	 */
	public void unregisterMessageReceivers() {
		if (messageReceivers == null) {
			return;
		}
		for (final Iterator<BroadcastReceiver> iterator = messageReceivers.iterator(); iterator.hasNext();) {
			final BroadcastReceiver broadcastReceiver = iterator.next();
			messageContext.unregisterReceiver(broadcastReceiver);
			iterator.remove();
		}
	}

	/**
	 * Indica que este receptor tiene que encolar los mensajes recibidos en una cola especial
	 * 
	 * @param customHandler
	 *            Handler custom que debe utilizarse en vez del default del contexto
	 */
	public void setCustomHandler(final Handler customHandler) {
		this.customHandler = customHandler;
	}

}
