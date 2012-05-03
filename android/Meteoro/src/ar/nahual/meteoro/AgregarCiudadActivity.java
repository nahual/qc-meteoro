/**
 * 30/04/2012 17:35:18 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import ar.com.iron.android.extensions.activities.CustomActivity;
import ar.com.iron.android.extensions.adapters.CustomArrayAdapter;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.helpers.ViewHelper;
import ar.nahual.meteoro.model.Ciudad;

/**
 * 
 * @author D. García
 */
public class AgregarCiudadActivity extends CustomActivity {

	private AutoCompleteTextView ciudadAutoComplete;
	private final List<Ciudad> ciudades = new ArrayList<Ciudad>();
	private CustomArrayAdapter<Ciudad> customArrayAdapter;

	private Handler currentHandler;
	private Runnable textChangeHandler;

	public class OnTextChangeHandler implements Runnable {

		@Override
		public void run() {
			if (this != textChangeHandler) {
				// No somos nosotros el que podemos ejecutar
				return;
			}
			onTextChangedAndNoInput();
		}

	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	@Override
	public int getLayoutIdForActivity() {
		return R.layout.add_city_view;
	}

	/**
	 * Invocado después de que el usuario no tuvo input
	 */
	public void onTextChangedAndNoInput() {
		requestCiudades();
	}

	/**
	 * 
	 */
	private void requestCiudades() {
		final String partialName = ciudadAutoComplete.getText().toString();
		final RequestCitiesTask requestCitiesTask = new RequestCitiesTask(this, customArrayAdapter);
		requestCitiesTask.execute(partialName);
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		currentHandler = new Handler();
		ciudadAutoComplete = ViewHelper.findAutoComplete(R.id.nombreParcialCiudad, getContentView());
		ciudadAutoComplete.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
			}

			@Override
			public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
			}

			@Override
			public void afterTextChanged(final Editable s) {
				if (textChangeHandler != null) {
					currentHandler.removeCallbacks(textChangeHandler);
				}
				textChangeHandler = new OnTextChangeHandler();
				currentHandler.postDelayed(textChangeHandler, 1500);
				onTextChangedAndNoInput();
			}
		});

		ciudades.add(new Ciudad("Buenos", "Aires"));
		ciudades.add(new Ciudad("Bulga", "Aires"));
		ciudades.add(new Ciudad("Bumurin", "Aires"));
		customArrayAdapter = new CustomArrayAdapter<Ciudad>(this, android.R.layout.simple_dropdown_item_1line,
				ciudades, new RenderBlock<Ciudad>() {
					@Override
					public void render(final View itemView, final Ciudad item, final LayoutInflater inflater) {
						final TextView textView = (TextView) itemView;
						textView.setText(item.getName());
					}
				});
		ciudadAutoComplete.setAdapter(customArrayAdapter);
	}

}
