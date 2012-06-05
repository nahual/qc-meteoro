/**
 * 13/05/2012 18:07:26 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro.model;

import java.util.ArrayList;
import java.util.List;

import ar.com.iron.persistence.persistibles.PersistibleSupport;

/**
 * Esta clase representa la ciudad persistida en la base
 * 
 * @author D. García
 */
public class CiudadPersistida extends PersistibleSupport {

	private String cityCode;
	private String cityName;
	private Pronostico actual;
	private List<Pronostico> futuros;
	private long ultimoTimestampDeUpdate = 0;

	public CiudadPersistida(final String name, final String code) {
		this.cityName = name;
		this.cityCode = code;
	}

	public Pronostico getActual() {
		return actual;
	}

	public void setActual(final Pronostico actual) {
		this.actual = actual;
	}

	public List<Pronostico> getFuturos() {
		if (futuros == null) {
			futuros = new ArrayList<Pronostico>();
		}
		return futuros;
	}

	public void setFuturos(final List<Pronostico> futuros) {
		this.futuros = futuros;
		this.ultimoTimestampDeUpdate = getNow();
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(final String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(final String cityName) {
		this.cityName = cityName;
	}

	/**
	 * Indica si esta ciudad posee datos actualizados del pronostico (no paso una hora desde el
	 * ultimo update)
	 * 
	 * @return true si tiene datos de pronostico, y aun no paso una hora desde que se actualizaron
	 */
	public boolean tienePronosticoActualizado() {
		if (getFuturos().isEmpty()) {
			return false;
		}
		final long unaHora = 60 * 60 * 1000;
		final long momentoDeProximoUpdate = ultimoTimestampDeUpdate + unaHora;
		final long now = getNow();
		if (momentoDeProximoUpdate <= now) {
			// Ya paso el momento en el que deberíamos actualizar
			return false;
		}
		return true;
	}

	private long getNow() {
		return System.currentTimeMillis();
	}
}
