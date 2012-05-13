/**
 * 03/07/2011 18:59:48 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.persistence.db4o.filters;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

/**
 * Esta clase representa un filtro de búsqueda en DB4o utilizando un predicado para buscar
 * 
 * @author D. García
 */
public abstract class PredicateFilter<T> extends Predicate<T> implements Db4oFilter {
	private static final long serialVersionUID = 7725078354496833418L;

	/**
	 * @see ar.com.iron.persistence.db4o.filters.Db4oFilter#executeOn(com.db4o.ObjectContainer)
	 */
	public ObjectSet<?> executeOn(ObjectContainer container) {
		ObjectSet<?> results = container.query(this);
		return results;
	}

	/**
	 * El tipo base que se va a filtrar
	 */
	public PredicateFilter(Class<T> filteredType) {
		super(filteredType);
	}
}
