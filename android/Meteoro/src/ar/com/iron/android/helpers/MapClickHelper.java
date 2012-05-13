package ar.com.iron.android.helpers;

import android.view.MotionEvent;

/**
 * Esta clase ayuda a detectar los clicks en el mapa, que no son arrastres
 * 
 * @author D. García
 */
public class MapClickHelper {

	/**
	 * Indica a que distancia máxima puede estar el evento up del down para ser considerado un click
	 */
	private static final int MAX_PIXEL_DISTANCE_FOR_CLICK = 10;

	private boolean ultimoEventoFueClick = false;
	/**
	 * Posición de la última interacción con el usuario
	 */
	private int ultimoX;

	/**
	 * Posición de la última interacción con el usuario
	 */
	private int ultimoY;

	/**
	 * Indica si aún es factible considerar los eventos como clicks
	 */
	private boolean clickEnProgreso = false;

	/**
	 * Llamado por la pantalla para saber si se produjo un click o no
	 * 
	 * @param event
	 *            Evento de contacto
	 */
	public void analizarTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			analizarDown(event);
			break;
		case MotionEvent.ACTION_UP:
			analizarUp(event);
			break;
		case MotionEvent.ACTION_CANCEL:
			analizarCancel();
			break;
		case MotionEvent.ACTION_MOVE:
			analizarMove(event);
			break;
		case MotionEvent.ACTION_OUTSIDE:
			analizarOutside();
			break;
		}
	}

	private void analizarOutside() {
		ultimoEventoFueClick = false;
		clickEnProgreso = false;
	}

	private void analizarMove(MotionEvent event) {
		if (!estaDentroDeUmbral(event)) {
			// Si se mueve por fuera del umbral, seguro que no es click, si se mueve por dentro,
			// todavía no lo sabemos
			clickEnProgreso = false;
		}
		ultimoEventoFueClick = false;
	}

	private void analizarCancel() {
		ultimoEventoFueClick = false;
		clickEnProgreso = false;
	}

	private void analizarUp(MotionEvent event) {
		// Si está dentro de los rangos entonces es un click
		ultimoEventoFueClick = estaDentroDeUmbral(event);
		if (!ultimoEventoFueClick) {
			// Si no es un click no importan las coordenadas
			return;
		}
		ultimoX = (int) event.getX();
		ultimoY = (int) event.getY();
	}

	/**
	 * Indica si el evento pasado está dentro de los umbrales esperados
	 * 
	 * @param event
	 *            Evento a analizar
	 * @return false si se determino que no es un evento click
	 */
	private boolean estaDentroDeUmbral(MotionEvent event) {
		if (!clickEnProgreso) {
			return false;
		}
		// Podría ser un click
		int xPixelDistance = (int) (event.getX() - ultimoX);
		if (Math.abs(xPixelDistance) > MAX_PIXEL_DISTANCE_FOR_CLICK) {
			// Esta demasiado lejos para ser considerado un click
			ultimoEventoFueClick = false;
			return false;
		}
		int yPixelDistance = (int) (event.getY() - ultimoY);
		if (Math.abs(yPixelDistance) > MAX_PIXEL_DISTANCE_FOR_CLICK) {
			// Esta demasiado lejos para ser considerado un click
			ultimoEventoFueClick = false;
			return false;
		}
		return true;
	}

	/**
	 * @param event
	 */
	private void analizarDown(MotionEvent event) {
		ultimoEventoFueClick = false;
		clickEnProgreso = true;
		ultimoX = (int) event.getX();
		ultimoY = (int) event.getY();
	}

	/**
	 * Indica si el último evento analizado indica un click por parte del usuario
	 * 
	 * @return true si se puede consultar el X, Y del usuario
	 */
	public boolean ultimoEventoIndicaClick() {
		return ultimoEventoFueClick;
	}

	/**
	 * @return Devuelve la posición X en píxeles del último click. Se debe consultar primero
	 *         {@link #ultimoEventoIndicaClick()}
	 */
	public int getXUltimoClick() {
		return ultimoX;
	}

	/**
	 * @return Devuelve la posición Y en píxeles del último click. Se debe consultar primero
	 *         {@link #ultimoEventoIndicaClick()}
	 */
	public int getYUltimoClick() {
		return ultimoY;
	}

}
