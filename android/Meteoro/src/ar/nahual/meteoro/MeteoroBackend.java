package ar.nahual.meteoro;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class MeteoroBackend {
    private static String PREFS_NAME = "meteoro-prefs";
    public final Context context;

    public MeteoroBackend(final Context context) {
        this.context = context;
    }

    public Uri getCitiesUri() {
        return new Uri.Builder().scheme("http").authority(getServerUrl())
                .path("get_cities").build();
    }

    public Uri getForecastUri(final String cityCode) {
        return new Uri.Builder().scheme("http"). authority(getServerUrl())
                .path("get_forecast").appendQueryParameter("city", cityCode).build();
    }

    public void changeUrl(String url) {
        SharedPreferences settings = this.context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("server-url", url);
        editor.commit();
    }

    private String getServerUrl() {
        SharedPreferences settings = this.context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString("server-url", "meteoro.herokuapp.com");
    }
}
