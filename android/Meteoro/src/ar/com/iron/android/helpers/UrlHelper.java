/**
 * 28/01/2011 13:55:29 Copyright (C) 2006 Darío L. García
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package ar.com.iron.android.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.net.ConnectivityManager;
import ar.com.iron.helpers.CantCopyException;
import ar.com.iron.helpers.FileHelper;
import ar.com.iron.httpclient.ConnectionClientException;
import ar.com.iron.httpclient.BadStatusCodeException;
import ar.com.iron.httpclient.ConnectionClient;

/**
 * Esta clase ofrece algunos métodos que facilitan la conexión y obtención de datos desde servidores
 * remotos
 * 
 * @author D. García
 */
public class UrlHelper {

	/**
	 * Realiza un request http y toma de la respuesta el cuerpo principal como si fuera un string.<br>
	 * Se asume que el servidor responde un texto pequeño como contenido de la respuesta.<br>
	 * Utilizar este método con precaución ya que dependiendo de la respuesta puede excederse la
	 * memoria disponible para la aplicación
	 * 
	 * @param remoteUrl
	 *            Dirección donde se hará un pedido GET y se obtendrá la respuesta
	 * @return La cadena que representa el cuerpo de la respuesta HTTP devuelta
	 */
	public static String getStringFrom(String remoteUrl) throws FailedConnectionException {
		InputStream contentStream = getContentStreamFrom(remoteUrl);
		ByteArrayOutputStream outputStream;
		try {
			outputStream = new ByteArrayOutputStream();
			FileHelper.copyTo(outputStream, contentStream);
		} catch (CantCopyException e) {
			throw new FailedConnectionException(
					"No fue posible construir el String con el contenido bajado del servidor", e);
		} finally {
			FileHelper.cerrarStream(contentStream);
		}
		String responseString = outputStream.toString();
		FileHelper.cerrarStream(outputStream);
		return responseString;
	}

	/**
	 * Realiza un request a la URL remota y devuelve un stream de entrada con el contenido devuelto
	 * dentro de la respuesta
	 * 
	 * @param remoteUrl
	 *            URL a la que se le realizará el request
	 * @return El stream abierto con el contenido (debe cerrarse después de usarlo)
	 * @throws FailedConnectionException
	 *             Si se produjo un error en la conexión
	 */
	public static InputStream getContentStreamFrom(String remoteUrl) throws FailedConnectionException {
		ConnectionClient remoteConnection;
		try {
			remoteConnection = new ConnectionClient();
		} catch (ConnectionClientException e) {
			throw new FailedConnectionException("No se pudo crear la conexión cliente antes de conectarse", e);
		}
		HttpResponse remoteResponse;
		try {
			remoteResponse = remoteConnection.executeGetMethod(remoteUrl);
		} catch (BadStatusCodeException e) {
			throw new FailedConnectionException("El servidor no respondió correctamente: " + e.getHttpStatusCode(), e);
		} catch (ConnectionClientException e) {
			throw new FailedConnectionException("Error en la comunicación HTTP con el servidor", e);
		}
		HttpEntity entity = remoteResponse.getEntity();
		if (entity == null) {
			throw new FailedConnectionException("El servidor no envió contenido en la respuesta");
		}
		InputStream contentStream;
		try {
			contentStream = entity.getContent();
		} catch (IllegalStateException e) {
			throw new FailedConnectionException(
					"No se puede bajar el contenido de la respuesta del servidor en el estado actual", e);
		} catch (IOException e) {
			throw new FailedConnectionException(
					"No se pudo bajar el contenido de la respuesta del servidor. Desconexión?", e);
		}
		return contentStream;
	}

	/**
	 * Realiza un request a la url remota indicada y baja el contenido recibido como archivo en el
	 * archivo que se indique.<br>
	 * El archivo debe existir antes de llamar a este método. El contenido del archivo indicado será
	 * reemplazado
	 * 
	 * @param remoteUrl
	 *            Url desde la cual bajar el archivo
	 * @param destinationFile
	 *            Archivo en el que se escribirá el contenido del archivo bajado
	 * @throws FailedConnectionException
	 *             Si se produce un error en la conexión y no se puede bajar el archivo
	 * @throws FileNotFoundException
	 *             Si el archivo pasado no existe
	 */
	public static void getFileFrom(String remoteUrl, File destinationFile) throws FailedConnectionException,
			FileNotFoundException {
		InputStream contentStream = getContentStreamFrom(remoteUrl);
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(destinationFile, false);
		} catch (FileNotFoundException e) {
			FileHelper.cerrarStream(contentStream);
			throw e;
		}
		try {
			FileHelper.copyTo(outputStream, contentStream);
		} catch (CantCopyException e) {
			throw new FailedConnectionException("No se pudo copiar el contenido de la respuesta al archivo", e);
		} finally {
			FileHelper.cerrarStream(contentStream);
			FileHelper.cerrarStream(outputStream);
		}
	}

	/**
	 * Tomado de http://stackoverflow.com/questions/2295998/requestroutetohost-ip-argument
	 * 
	 * @param hostname
	 *            Url del host
	 * @return int que lo representa para {@link ConnectivityManager#requestRouteToHost(int, int)}
	 */
	public static int lookupHost(String hostname) {
		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getByName(hostname);
		} catch (UnknownHostException e) {
			return -1;
		}
		byte[] addrBytes;
		int addr;
		addrBytes = inetAddress.getAddress();
		addr = ((addrBytes[3] & 0xff) << 24) | ((addrBytes[2] & 0xff) << 16) | ((addrBytes[1] & 0xff) << 8)
				| (addrBytes[0] & 0xff);
		return addr;
	}

}
