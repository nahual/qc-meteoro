/**
 * 15/05/2011 03:20:09 Copyright (C) 2011 Darío L. García
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

import ar.com.iron.android.extensions.adapters.RenderBlock;

/**
 * Esta clase representa la información de configuración de la paginación de manera que el adapter
 * sepa como mostrar el feedback de paginación
 * 
 * @author D. García
 */
public class PaginationConfiguration<T, P> {

	/**
	 * El id del recurso para el item que representa la paginación
	 */
	private int paginatedItemLayoutId;

	/**
	 * Bloque de código que sabe como popular la vista del item paginado
	 */
	private RenderBlock<T> paginatedItemRenderBlock;

	private PageFetchingCode<T, P> fetchingCode;

	public int getPaginatedItemLayoutId() {
		return paginatedItemLayoutId;
	}

	public void setPaginatedItemLayoutId(final int paginatedItemLayoutId) {
		this.paginatedItemLayoutId = paginatedItemLayoutId;
	}

	public RenderBlock<T> getPaginatedItemRenderBlock() {
		return paginatedItemRenderBlock;
	}

	public void setPaginatedItemRenderBlock(final RenderBlock<T> paginatedItemRenderBlock) {
		this.paginatedItemRenderBlock = paginatedItemRenderBlock;
	}

	public PageFetchingCode<T, P> getFetchingCode() {
		return fetchingCode;
	}

	public void setFetchingCode(final PageFetchingCode<T, P> fetchingCode) {
		this.fetchingCode = fetchingCode;
	}

	public static <T, P> PaginationConfiguration<T, P> create(final int paginatedItemLayoutId,
			final RenderBlock<T> paginatedRenderBlock, final PageFetchingCode<T, P> pageFetchingBlock) {
		final PaginationConfiguration<T, P> name = new PaginationConfiguration<T, P>();
		name.fetchingCode = pageFetchingBlock;
		name.paginatedItemLayoutId = paginatedItemLayoutId;
		name.paginatedItemRenderBlock = paginatedRenderBlock;
		return name;
	}
}
