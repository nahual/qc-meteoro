/**
 * 27/02/2011 21:12:06 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.widgets.drawer;

/**
 * Esta clase representa un item seleccionable por el usuario que tiene una imagen asociada en el
 * drawer y una descripción
 * 
 * @author D. García
 */
public class DrawerItem<T> {

	private String description;
	private T item;
	private int drawableIconId;

	public static <T> DrawerItem<T> create(T item, int drawableId, String description) {
		DrawerItem<T> drawerItem = new DrawerItem<T>();
		drawerItem.item = item;
		drawerItem.drawableIconId = drawableId;
		drawerItem.description = description;
		return drawerItem;
	}

	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}

	public int getDrawableIconId() {
		return drawableIconId;
	}

	/**
	 * Define el ID del recurso drawable para utilizar como ícono de este item
	 * 
	 * @param drawableIconId
	 *            El ID de recurso drawable
	 */
	public void setDrawableIconId(int iconId) {
		this.drawableIconId = iconId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String itemDescription) {
		this.description = itemDescription;
	}

}
