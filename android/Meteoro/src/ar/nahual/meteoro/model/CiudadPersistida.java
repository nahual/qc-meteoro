/**
 * 13/05/2012 18:07:26 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro.model;

import ar.com.iron.persistence.persistibles.PersistibleSupport;

/**
 * Esta clase representa la ciudad persistida en la base
 * 
 * @author D. García
 */
public class CiudadPersistida extends PersistibleSupport {

	private String cityCode;
	private String cityName;

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

	public static CiudadPersistida create(final Ciudad ciudadBase) {
		final CiudadPersistida name = new CiudadPersistida();
		name.cityCode = ciudadBase.getCode();
		name.cityName = ciudadBase.getName();
		return name;
	}
}
