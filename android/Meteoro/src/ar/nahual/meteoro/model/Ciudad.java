/**
 * 30/04/2012 16:58:35 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa la ciudad mostrada
 * 
 * @author D. García
 */
public class Ciudad {

	private String code;
	private String name;

	public Ciudad(final String code, final String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	private List<PronosticoDiario> pronosticos;

	public List<PronosticoDiario> getPronosticos() {
		if (pronosticos == null) {
			pronosticos = new ArrayList<PronosticoDiario>();
		}
		return pronosticos;
	}

	public void setPronosticos(final List<PronosticoDiario> pronosticos) {
		this.pronosticos = pronosticos;
	}

}
