/**
 * 15/05/2011 03:30:41 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.adapters.paging;

import java.util.List;

import ar.com.iron.android.extensions.adapters.CustomArrayAdapter;

/**
 * Esta clase mantiene información de estado para el adapter de manera que pueda interactuar con el
 * paginado
 * 
 * @author D. García
 */
public class PaginationAdapter<T, P> {

	private PaginationConfiguration<T, P> config;

	private P nextPageRequest;

	private boolean isPageRequested;

	private CustomArrayAdapter<T> listAdapter;

	public P getNextPageRequest() {
		return nextPageRequest;
	}

	/**
	 * Establece cuál es el siguiente request que debe utilizarse para la búsqueda de página. Si se
	 * pasa null se indica que no hay más resultados para buscar
	 * 
	 * @param nextPageRequest
	 *            El objeto que sirve para buscar los próximos items en el fetching code
	 */
	public void setNextPageRequest(final P nextPageRequest) {
		this.nextPageRequest = nextPageRequest;
		this.isPageRequested = false;
		this.listAdapter.notifyDataSetChanged();
	}

	public PaginationConfiguration<T, P> getConfig() {
		return config;
	}

	public void setConfig(final PaginationConfiguration<T, P> config) {
		this.config = config;
	}

	public static <T, P> PaginationAdapter<T, P> create(final PaginationConfiguration<T, P> config,
			final CustomArrayAdapter<T> listaAdapter) {
		final PaginationAdapter<T, P> name = new PaginationAdapter<T, P>();
		name.config = config;
		name.listAdapter = listaAdapter;
		return name;
	}

	/**
	 * Indica si existen más items para mostrar en próximas páginas
	 */
	public boolean hasMoreItems() {
		return this.nextPageRequest != null;
	}

	/**
	 * Dispara la búsqueda de más items para el paginado en caso de que no se haya disparado ya una
	 * búsqueda previa
	 */
	public void requestMoreItems() {
		if (this.isPageRequested) {
			// Ya pedimos una
			return;
		}
		if (!hasMoreItems()) {
			return;
		}
		getConfig().getFetchingCode().fetchNextItems(this, this.nextPageRequest);
		this.isPageRequested = true;
	}

	/**
	 * Indica si el request pasado aún es válido, o si ya sé disparó un nuevo y por lo tanto los
	 * resultados que se recibieron para el request pasado deben ser descartados
	 * 
	 * @param testedRequest
	 *            El request a verificar
	 * @return false si este adapter tiene un nuevo request como actual
	 */
	public boolean isStillValid(final P testedRequest) {
		return testedRequest == this.nextPageRequest;
	}

	/**
	 * Invocado cuando se reciben resultados del servidor disponibles para agregar al conjunto.<br>
	 * Si el request para el que se recibieron ya no es el actual, los resultados se descartan
	 * 
	 * @param originalPageRequest
	 *            Request para el que se recibieron los resultados
	 * @param result
	 *            Los resultados
	 * @return true si los elementos fueron agregados, false si el request ya no es valido
	 */
	public boolean onItemsReceivedFor(final P originalPageRequest, final List<T> result) {
		if (!isStillValid(originalPageRequest)) {
			// Dispararon otra busqueda antes que llegaran los resultados,
			// los descartamos
			return false;
		}
		// Agregamos los resultados recibidos
		listAdapter.addAll(result);
		this.setNextPageRequest(null);
		return true;
	}

	/**
	 * Inicia la próxima búsqueda de elementos borrando el contenido de los resultados anteriores
	 * 
	 * @param firstPageRequest
	 *            El primer pedido de página
	 */
	public void startNextSearch(final P firstPageRequest) {
		listAdapter.clear();
		setNextPageRequest(firstPageRequest);
	}

}
