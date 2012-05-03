package ar.nahual.meteoro;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import ar.com.iron.android.extensions.activities.CustomListActivity;
import ar.com.iron.android.extensions.activities.model.CustomableListActivity;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.menues.ContextMenuItem;
import ar.nahual.meteoro.model.Ciudad;
import ar.nahual.meteoro.model.PronosticoDiario;

public class VerCiudadActivity extends CustomListActivity<PronosticoDiario> {

	public static final List<Ciudad> ciudades = new ArrayList<Ciudad>();

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (ciudades.isEmpty()) {
			startActivity(new Intent(this, AgregarCiudadActivity.class));
		}
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getContextMenuItems()
	 */
	@Override
	public ContextMenuItem<? extends CustomableListActivity<PronosticoDiario>, PronosticoDiario>[] getContextMenuItems() {
		return null;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementList()
	 */
	@Override
	public List<PronosticoDiario> getElementList() {
		final List<PronosticoDiario> pronosticos = new ArrayList<PronosticoDiario>();
		pronosticos.add(PronosticoDiario.create("Martes", 22));
		pronosticos.add(PronosticoDiario.create("Miercoles", 23));
		pronosticos.add(PronosticoDiario.create("jueves", 24));
		return pronosticos;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementRenderBlock()
	 */
	@Override
	public RenderBlock<PronosticoDiario> getElementRenderBlock() {
		return new RenderBlock<PronosticoDiario>() {
			@Override
			public void render(final View itemView, final PronosticoDiario item, final LayoutInflater inflater) {
				final TextView diaPronosticoText = ViewHelper.findTextView(R.id.diaPronostico_txt, itemView);
				diaPronosticoText.setText(item.getDia());
				final TextView tempPronostico = ViewHelper.findTextView(R.id.tempPronostico_txt, itemView);
				tempPronostico.setText(String.valueOf(item.getTemp()));
			}
		};
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getLayoutIdForElements()
	 */
	@Override
	public int getLayoutIdForElements() {
		return R.layout.city_day_item;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	@Override
	public int getLayoutIdForActivity() {
		return R.layout.city_view;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#getContextMenuHeaderTitleOrId()
	 */
	@Override
	public Object getContextMenuHeaderTitleOrId() {
		return null;
	}

}