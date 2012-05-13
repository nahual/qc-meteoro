/**
 * 19/04/2011 23:13:11 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.adapters;

import android.widget.TextView;

/**
 * Esta clase es un render de elementos que sabe como renderizar de String a un {@link TextView}
 * 
 * @author D. García
 */
public class StringTextRenderBlock extends TextRenderBlock<String> {

	/**
	 * @see ar.com.iron.android.extensions.adapters.TextRenderBlock#getDescriptionFor(java.lang.Object)
	 */
	@Override
	protected CharSequence getDescriptionFor(String item) {
		return item;
	}

}
