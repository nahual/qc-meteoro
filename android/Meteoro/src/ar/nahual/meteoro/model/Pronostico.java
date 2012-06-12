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
	private String status = "N/D";
	private String temperature = "N/D";
	private String min = "N/D";
	private String date = "N/D";
	private String max = "N/D";
	private String humidity = "N/D";
	private String pressure = "N/D";

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

	public String getPressure() {
		return pressure;
	}

	public void setPressure(final String pressure) {
		this.pressure = pressure;
	}

}
