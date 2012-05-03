/**
 * 09/12/2009 10:19:12<br>
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
package ar.com.iron.android.extensions.activities.model;

import android.content.BroadcastReceiver;
import android.view.View;
import ar.com.iron.menues.ActivityMenuItem;

/**
 * Esta interfaz reune los métodos custom que agregamos a los activities. Y sirve de referencia para
 * hacer custom otros activities
 * 
 * @author D.L. García
 */
public interface CustomableActivity {

	/***************************************
	 * Métodos del ciclo de vida
	 ***************************************/

	/**
	 * Obtiene los managers o servicios que sean necesarios para el funcionamiento de esta pantalla
	 */
	void initDependencies();

	/**
	 * Metodo para la subclase que indica el momento adecuado para registrar los receivers de este
	 * activity que sirven para la comunicación de fondo.<br>
	 * Al registrar los receivers en este metodo se asegura que se recibirán los mensajes aún cuando
	 * la pantalla no es visible
	 */
	void initMessageReceivers();

	/**
	 * Crea y configura los controles que se usaran en la pantalla. Opcional para aquellas pantallas
	 * que no tengan controles
	 */
	void setUpComponents();

	/**
	 * Ejecuta código específico de la subclase para configurar cosas adicionales (es opcional)
	 */
	void afterOnCreate();

	/***************************************
	 * Métodos de estructura
	 ***************************************/

	/**
	 * Devuelve el array de items para crear en el menu de opciones accesible desde el botón "menu"
	 * 
	 * @return Un array de opciones para el menu. Null indica sin menu
	 */
	public ActivityMenuItem<? extends CustomableActivity>[] getMenuItems();

	/**
	 * @return Devuelve la vista raiz de toda la pantalla
	 */
	public View getContentView();

	/**
	 * Devuelve el identificador del recurso layout utilizado para definir la vista del activity
	 * 
	 * @return El id del recurso
	 */
	public int getLayoutIdForActivity();

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
	void registerMessageReceiver(String expectedAction, BroadcastReceiver executedWhenReceived);

}
