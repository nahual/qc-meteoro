/**
 * 26/05/2012 16:39:29 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro.model;

/**
 * Esta clase representa la proyeccion de datos climaticos para una ciudad
 * 
 * @author D. García
 */
public class Pronostico {
	private String status;
	private String temperature;
	private String min;
	private String date;
	private String max;
	private String humidity;
	private String wind;
	private String chill;

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(final String temperature) {
		this.temperature = temperature;
	}

	public String getMin() {
		return min;
	}

	public void setMin(final String min) {
		this.min = min;
	}

	public String getDate() {
		return date;
	}

	public void setDate(final String date) {
		this.date = date;
	}

	public String getMax() {
		return max;
	}

	public void setMax(final String max) {
		this.max = max;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(final String humidity) {
		this.humidity = humidity;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(final String wind) {
		this.wind = wind;
	}

	public String getChill() {
		return chill;
	}

	public void setChill(final String chill) {
		this.chill = chill;
	}

}
