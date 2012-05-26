/**
 * 03/07/2011 01:46:36 Copyright (C) 2011 Darío García
 */
package ar.com.iron.persistence.persistibles;

/**
 * Esta clase es base para todas las entidades persistentes
 * 
 * @author D. García
 */
public class PersistibleSupport implements Persistible {

	private Long id;

	/**
	 * @see ar.com.iron.persistence.Persistible#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @see ar.com.iron.persistence.Persistible#setId(java.lang.Long)
	 */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

}
