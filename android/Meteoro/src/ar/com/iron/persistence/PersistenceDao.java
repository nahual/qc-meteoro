/**
 * 05/03/2011 15:35:49 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.persistence;

import java.util.List;

import ar.com.iron.android.extensions.services.BackgroundProcess;
import ar.com.iron.persistence.messages.PersistibleChangedMessage;
import ar.com.iron.persistence.persistibles.Persistible;

/**
 * Esta clase representa el punto de contacto del activity con el servicio de persistencia.<br>
 * A través de implementaciones de esta clase el activity puede abstraerse del motor de persistencia
 * concreto.<br>
 * Esta clase sabe como convertir las llamadas a métodos, a tareas para procesar en background y
 * devolver el resultado al thread principal del activity
 * 
 * @author D. García
 */
public interface PersistenceDao {

	/**
	 * Guarda el objeto pasado creándolo en el motor de persistencia si no existía.<br>
	 * Al persistir la entidad el servicio de persistencia generará un intent con los datos de la
	 * entidad guardada: {@link PersistibleChangedMessage}
	 * 
	 * @param newItem
	 *            El item que contiene el estado a guardar
	 * @param persistenceOperationListener
	 *            El listener invocado al terminar la operación
	 */
	public <T extends Persistible> void save(T newItem, PersistenceOperationListener<T> persistenceOperationListener);

	/**
	 * Establece el proceso en el que se ejecutarán todas las operaciones de persistencia
	 * 
	 * @param process
	 *            El proceso de ejecución de las operaciones
	 */
	public void setBackgroundProcess(BackgroundProcess process);

	/**
	 * Realiza una búsqueda libre en la base con un criterio arbitrario. Los registros devueltos no
	 * tienen que ser necesariamente clases de dominio. Los objetos devueltos dependerán del filtro
	 * usado<br>
	 * Normalmente los filtros sólo deben recopilar los datos necesarios en su creación, y recién
	 * procesarlos cuando interactúan con el motor de persistencia, de esta manera roban la menor
	 * cantidad de tiempo posible del thread principal, y hacen el trabajo pesado en el thread del
	 * servicio de persistencia
	 * 
	 * @param filter
	 *            Instancia de un filtro que define condiciones para los objetos devueltos
	 * @param persistenceOperationListener
	 *            El listener para recibir los resultados
	 */
	public <T> void findAllMatching(DataFilter filter,
			PersistenceOperationListener<List<T>> persistenceOperationListener);

	/**
	 * Elimina el elemento pasado del motor de persistencia
	 * 
	 * @param element
	 *            El elemento que no será obtenible desde la capa de persistencia
	 * @param persistenceOperationListener
	 *            Listener notificado del resultado
	 */
	public <T extends Persistible> void delete(T element, PersistenceOperationListener<T> persistenceOperationListener);

	/**
	 * Busca el elemento de la clase indicada con el id pasado, devolviendo la entidad de la base o
	 * null si no existe
	 * 
	 * @param <T>
	 *            Tipo del elemento devuelto
	 * @param expectedClass
	 *            Clase a devolver
	 * @param id
	 *            El id del elemento buscado
	 * @param persistenceOperationListener
	 *            El listener notificado con el objeto
	 */
	public <T extends Persistible> void getOf(Class<T> expectedClass, Long id,
			PersistenceOperationListener<T> persistenceOperationListener);

}
