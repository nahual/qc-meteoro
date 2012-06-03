/**
 * 30/04/2012 20:27:44 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import ar.com.iron.helpers.ToastHelper;
import ar.nahual.meteoro.model.CiudadPersistida;

/**
 * 
 * @author D. García
 */
public class RequestCitiesTask extends AsyncTask<String, Void, List<CiudadPersistida>> {
	private DefaultHttpClient httpClient;
	private final AgregarCiudadActivity activity;

	/**
	 * @param customArrayAdapter
	 * 
	 */
	public RequestCitiesTask(final AgregarCiudadActivity context) {
		this.activity = context;
	}

	/**
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected List<CiudadPersistida> doInBackground(final String... params) {
		final HttpGet request = new HttpGet(MeteoroApplication.getBackend().getCitiesUri().toString());
		HttpResponse response;
		try {
			response = httpClient.execute(request);
		} catch (final Exception e) {
			showToast("Se produjo un error en la conexion: " + e.getMessage());
			return Collections.emptyList();
		}
		final StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
			showToast("El servidor no está disponible");
			return Collections.emptyList();
		}

		final HttpEntity entity = response.getEntity();
		if (entity == null) {
			showToast("Se recibio respuesta vacia del servidor");
			return Collections.emptyList();
		}
		// A Simple JSON Response Read
		InputStream instream;
		try {
			instream = entity.getContent();
		} catch (final Exception e) {
			showToast("Error al comunicar con servidor: " + e.getMessage());
			return Collections.emptyList();
		}
		final String result = convertStreamToString(instream);
		final ArrayList<CiudadPersistida> ciudades = new ArrayList<CiudadPersistida>();
		try {
			final JSONArray results = new JSONArray(result);
			for (int i = 0; i < results.length(); i++) {
				final JSONObject object = results.getJSONObject(i);
				final String nombre = (String) object.get("name");
				final String codigo = (String) object.get("code");
				ciudades.add(new CiudadPersistida(nombre, codigo));
			}
		} catch (final JSONException e) {
			showToast("Error en los datos recibidos: " + e.getMessage());
			return Collections.emptyList();
		}
		try {
			instream.close();
		} catch (final IOException e) {
			// Ya fue
		}
		return ciudades;
	}

	private void showToast(final String texto) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ToastHelper.create(activity).showShort(texto);
			}
		});
	}

	/**
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		createHttpClient();
	}

	/**
	 * 
	 */
	private void createHttpClient() {
		final int TIMEOUT_MILLISEC = 10000; // = 10 seconds
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		httpClient = new DefaultHttpClient(httpParams);
	}

	/**
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(final List<CiudadPersistida> result) {
		this.activity.onCiudadesDisponibles(result);
	}

	public static String convertStreamToString(final InputStream is) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		final StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
