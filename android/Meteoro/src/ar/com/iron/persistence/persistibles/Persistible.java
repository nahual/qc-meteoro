/**
 * 05/03/2011 20:14:59 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.persistence.persistibles;

/**
 * Esta interfaz es una marca para todas las entidades que son persistibles
 * 
 * @author D. García
 */
public interface Persistible {

	/**
	 * Nombre que los adapter con cursor de android requieren
	 */
	public static final String ANDROID_PREFERRED_ID_COLUMN_NAME = "_id";

	/**
	 * Devuelve el ID identificador de esta entidad
	 * 
	 * @return El identificador de la entidad otorgado al persistirla
	 */
	public Long getId();

	/**
	 * Establece el id de esta entidad al persistirla
	 * 
	 * @param id
	 *            El identificador que permite relacionarla con su estado persistido
	 */
	public void setId(Long id);

}
