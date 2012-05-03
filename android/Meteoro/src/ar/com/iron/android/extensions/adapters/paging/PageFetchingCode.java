/**
 * 15/05/2011 12:07:10 Copyright (C) 2011 Darío L. García
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

/**
 * Esta interfaz representa un código que sabe como obtener nuevos elementos para agregar como
 * siguiente página en una lista con paginación.<br>
 * Al conseguir los nuevos elementos, este código debe verificar si cambió la búsqueda original (y
 * por lo tanto descartar los resultados), o en caso de que sigan siendo válidos agregarlos al
 * {@link PaginationAdapter}
 * 
 * @author D. García
 */
public interface PageFetchingCode<T, P> {

	/**
	 * Invocado al mostrarse la vista de la siguiente página, lo que solicita nuevos elementos
	 * 
	 * @param paginationAdapter
	 *            El adapter que recibe los elementos recibidos
	 * @param pageRequest
	 *            objeto que indica la página que debe solicitarse al servidor
	 */
	public void fetchNextItems(PaginationAdapter<T, P> paginationAdapter, P pageRequest);
}
