package ar.com.iron.daos;

import ar.com.iron.persistence.persistibles.Persistible;

/**
 * Esta interfaz representa los objetos que modelan un Dao.<br>
 * Los Daos son uno de los componentes principales de la arquitectura y modelan todo el acceso y
 * comunicacion con la base de datos.<br>
 * Estos objetos deberian contener logica relacionada a la insercion, actualizacion, remocion y
 * queries de las entidades declaradas como {@link Persistiblea entidad, la cual es declara por su tipo. Y todas
 * las operaciones del dao van a estar centradas en esa entidad.<br>
 * 
 * @author Maximiliano Vazquez
 * @param <P>
 *            Tipo de la entidad para la que hace este dao
 */
public interface Dao<P extends Persistible> {

	/**
	 * Obtiene un objeto a partir de su id
	 * 
	 * @param id
	 *            Id asignado al objeto al momento de guardarlo
	 * @return El objeto existente, o null si no existe objeto con ese id
	 */
	public P get(Long id);

	/**
	 * Persiste el objeto en la base
	 * 
	 * @param persistent
	 *            Objeto a persistir
	 */
	public void store(P persistent);

}
