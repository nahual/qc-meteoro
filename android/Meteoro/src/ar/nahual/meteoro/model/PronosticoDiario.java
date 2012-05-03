/**
 * 30/04/2012 17:00:34 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro.model;

/**
 * Esta clase representa el pronostico de un día para una ciudad
 * 
 * @author D. García
 */
public class PronosticoDiario {

	private String dia;
	private Integer temp;

	public String getDia() {
		return dia;
	}

	public void setDia(final String dia) {
		this.dia = dia;
	}

	public Integer getTemp() {
		return temp;
	}

	public void setTemp(final Integer temp) {
		this.temp = temp;
	}

	public static PronosticoDiario create(final String dia, final Integer temp) {
		final PronosticoDiario name = new PronosticoDiario();
		name.dia = dia;
		name.temp = temp;
		return name;
	}
}
