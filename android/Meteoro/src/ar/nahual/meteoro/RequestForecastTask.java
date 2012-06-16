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
import ar.nahual.meteoro.model.Pronostico;

/**
 * 
 * @author D. García
 */
public class RequestForecastTask extends AsyncTask<CiudadPersistida, Void, List<Pronostico>> {

	private final VerCiudadActivity activity;
	private DefaultHttpClient httpClient;
	private CiudadPersistida ciudadElegida;
	private static int failedAttempts = 0;

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

	public RequestForecastTask(final VerCiudadActivity activity) {
		this.activity = activity;
	}

	/**
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected List<Pronostico> doInBackground(final CiudadPersistida... params) {
		ciudadElegida = params[0];
		final HttpGet request = new HttpGet(MeteoroApplication.getBackend().getForecastUri(ciudadElegida.getCityCode())
				.toString());
		HttpResponse response;
		try {
			response = httpClient.execute(request);
		} catch (final Exception e) {
			failedAttempts++;
			if (failedAttempts == 1) {
				// Solo la primera vez
				showToast("Error. Datos no disponibles al acceder al servidor: " + e.getMessage());
			}
			return Collections.emptyList();
		}
		// Reseteamos el contador
		failedAttempts = 0;
		final StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
            showToast("El servidor no pudo responder al pedido para la ciudad: " + ciudadElegida.getCityCode());
            return Collections.emptyList();
        }
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
		final ArrayList<Pronostico> pronosticos = new ArrayList<Pronostico>();
		try {
			final JSONArray results = new JSONArray(result);
			for (int i = 0; i < results.length(); i++) {
				final JSONObject object = results.getJSONObject(i);
				final String status = object.get("status").toString();
				final String temperature = object.get("temperature").toString();
				final String min = object.get("min").toString();
				final String date = object.get("date").toString();
				final String max = object.get("max").toString();
				final String humidity = object.get("humidity").toString();
				final String pressure = object.get("pressure").toString();
				final Pronostico pronostico = new Pronostico();
				pronostico.setPressure(pressure);
				pronostico.setDate(date);
				pronostico.setHumidity(humidity);
				pronostico.setMax(max);
				pronostico.setMin(min);
				pronostico.setStatus(status);
				pronostico.setTemperature(temperature);
				pronosticos.add(pronostico);
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
		return pronosticos;
	}

	private void showToast(final String texto) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ToastHelper.create(activity).showShort(texto);
			}
		});
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

	/**
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(final List<Pronostico> result) {
		if (result.isEmpty()) {
			// No tenemos pronostico
			return;
		}
		final Pronostico pronosticoActual = result.get(0);
		final ArrayList<Pronostico> futuros = new ArrayList<Pronostico>(result.size() - 1);
		for (int i = 1; i < result.size(); i++) {
			final Pronostico pronostico = result.get(i);
			futuros.add(pronostico);
		}
		this.ciudadElegida.setActual(pronosticoActual);
		this.ciudadElegida.setFuturos(futuros);

		activity.onPronosticoDisponible();
	}
}
