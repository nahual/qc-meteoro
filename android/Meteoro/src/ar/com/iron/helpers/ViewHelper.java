package ar.com.iron.helpers;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android.widget.VideoView;
import android.widget.ZoomButton;
import android.widget.ZoomControls;

/**
 * Esta clase es un helper de las vistas que facilita algunas operaciones comunes con las vistas
 * 
 * @author D. Garcia
 */
public abstract class ViewHelper {

	/**
	 * Obtiene el {@link TextView} de la vista indicado por id. Se produce un error inesperado si no
	 * existe, ya que se asume que debería existir
	 * 
	 * @param textViewId
	 *            Id del textView a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static TextView findTextView(int textViewId, View view) {
		return getControlFromView(view, textViewId, TextView.class);
	}

	/**
	 * Obtiene el {@link CheckedTextView} de la vista indicado por id. Se produce un error
	 * inesperado si no existe, ya que se asume que debería existir
	 * 
	 * @param textViewId
	 *            Id del textView a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static CheckedTextView findCheckedTextView(int textViewId, View view) {
		return getControlFromView(view, textViewId, CheckedTextView.class);
	}

	/**
	 * Obtiene el {@link Button} de la vista indicado por id. Se produce un error inesperado si no
	 * existe, ya que se asume que debería existir
	 * 
	 * @param buttonId
	 *            Id del Button a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static Button findButton(int buttonId, View view) {
		return getControlFromView(view, buttonId, Button.class);
	}

	/**
	 * Obtiene el {@link AutoCompleteTextView} de la vista indicado por id. Se produce un error
	 * inesperado si no existe, ya que se asume que debería existir
	 * 
	 * @param autoCompleteId
	 *            Id del AutoCompleteTextView a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static AutoCompleteTextView findAutoComplete(int autoCompleteId, View view) {
		return getControlFromView(view, autoCompleteId, AutoCompleteTextView.class);
	}

	/**
	 * Obtiene el {@link CheckBox} de la vista indicado por id. Se produce un error inesperado si no
	 * existe, ya que se asume que debería existir
	 * 
	 * @param checkBoxId
	 *            Id del CheckBox a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static CheckBox findCheckBox(int checkBoxId, View view) {
		return getControlFromView(view, checkBoxId, CheckBox.class);
	}

	/**
	 * Obtiene el {@link Chronometer} de la vista indicado por id. Se produce un error inesperado si
	 * no existe, ya que se asume que debería existir
	 * 
	 * @param chronometerId
	 *            Id del Chronometer a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static Chronometer findChronometer(int chronometerId, View view) {
		return getControlFromView(view, chronometerId, Chronometer.class);
	}

	/**
	 * Obtiene el {@link DatePicker} de la vista indicado por id. Se produce un error inesperado si
	 * no existe, ya que se asume que debería existir
	 * 
	 * @param datePickerId
	 *            Id del DatePicker a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static DatePicker findDatePicker(int datePickerId, View view) {
		return getControlFromView(view, datePickerId, DatePicker.class);
	}

	/**
	 * Obtiene el {@link EditText} de la vista indicado por id. Se produce un error inesperado si no
	 * existe, ya que se asume que debería existir
	 * 
	 * @param editTextId
	 *            Id del EditText a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static EditText findEditText(int editTextId, View view) {
		return getControlFromView(view, editTextId, EditText.class);
	}

	/**
	 * Obtiene el {@link Gallery} de la vista indicado por id. Se produce un error inesperado si no
	 * existe, ya que se asume que debería existir
	 * 
	 * @param galleryId
	 *            Id del Gallery a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static Gallery findGallery(int galleryId, View view) {
		return getControlFromView(view, galleryId, Gallery.class);
	}

	/**
	 * Obtiene el {@link ImageButton} de la vista indicado por id. Se produce un error inesperado si
	 * no existe, ya que se asume que debería existir
	 * 
	 * @param imageButtonId
	 *            Id del ImageButton a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static ImageButton findImageButton(int imageButtonId, View view) {
		return getControlFromView(view, imageButtonId, ImageButton.class);
	}

	/**
	 * Obtiene el {@link ImageView} de la vista indicado por id. Se produce un error inesperado si
	 * no existe, ya que se asume que debería existir
	 * 
	 * @param imageViewId
	 *            Id del ImageView a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static ImageView findImageView(int imageViewId, View view) {
		return getControlFromView(view, imageViewId, ImageView.class);
	}

	/**
	 * Obtiene el {@link MultiAutoCompleteTextView} de la vista indicado por id. Se produce un error
	 * inesperado si no existe, ya que se asume que debería existir
	 * 
	 * @param multiAutoCompleteId
	 *            Id del MultiAutoCompleteTextView a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static MultiAutoCompleteTextView findMultiAutoComplete(int multiAutoCompleteId, View view) {
		return getControlFromView(view, multiAutoCompleteId, MultiAutoCompleteTextView.class);
	}

	/**
	 * Obtiene el {@link ProgressBar} de la vista indicado por id. Se produce un error inesperado si
	 * no existe, ya que se asume que debería existir
	 * 
	 * @param progressBarId
	 *            Id del ProgressBar a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static ProgressBar findProgressBar(int progressBarId, View view) {
		return getControlFromView(view, progressBarId, ProgressBar.class);
	}

	/**
	 * Obtiene el {@link RadioButton} de la vista indicado por id. Se produce un error inesperado si
	 * no existe, ya que se asume que debería existir
	 * 
	 * @param radioButtonId
	 *            Id del RadioButton a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static RadioButton findRadioButton(int radioButtonId, View view) {
		return getControlFromView(view, radioButtonId, RadioButton.class);
	}

	/**
	 * Obtiene el {@link RatingBar} de la vista indicado por id. Se produce un error inesperado si
	 * no existe, ya que se asume que debería existir
	 * 
	 * @param ratingBarId
	 *            Id del RatingBar a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static RatingBar findRatingBar(int ratingBarId, View view) {
		return getControlFromView(view, ratingBarId, RatingBar.class);
	}

	/**
	 * Obtiene el {@link SeekBar} de la vista indicado por id. Se produce un error inesperado si no
	 * existe, ya que se asume que debería existir
	 * 
	 * @param seekBarId
	 *            Id del SeekBar a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static SeekBar findSeekBar(int seekBarId, View view) {
		return getControlFromView(view, seekBarId, SeekBar.class);
	}

	/**
	 * Obtiene el {@link Spinner} de la vista indicado por id. Se produce un error inesperado si no
	 * existe, ya que se asume que debería existir
	 * 
	 * @param spinnerId
	 *            Id del Spinner a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static Spinner findSpinner(int spinnerId, View view) {
		return getControlFromView(view, spinnerId, Spinner.class);
	}

	/**
	 * Obtiene el {@link TimePicker} de la vista indicado por id. Se produce un error inesperado si
	 * no existe, ya que se asume que debería existir
	 * 
	 * @param timePickerId
	 *            Id del TimePicker a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static TimePicker findTimePicker(int timePickerId, View view) {
		return getControlFromView(view, timePickerId, TimePicker.class);
	}

	/**
	 * Obtiene el {@link ToggleButton} de la vista indicado por id. Se produce un error inesperado
	 * si no existe, ya que se asume que debería existir
	 * 
	 * @param toggleButtonId
	 *            Id del ToggleButton a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static ToggleButton findToggleButton(int toggleButtonId, View view) {
		return getControlFromView(view, toggleButtonId, ToggleButton.class);
	}

	/**
	 * Obtiene el {@link VideoView} de la vista indicado por id. Se produce un error inesperado si
	 * no existe, ya que se asume que debería existir
	 * 
	 * @param videoViewId
	 *            Id del VideoView a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static VideoView findVideoView(int videoViewId, View view) {
		return getControlFromView(view, videoViewId, VideoView.class);
	}

	/**
	 * Obtiene el {@link ZoomButton} de la vista indicado por id. Se produce un error inesperado si
	 * no existe, ya que se asume que debería existir
	 * 
	 * @param zoomButtonId
	 *            Id del ZoomButton a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static ZoomButton findZoomButton(int zoomButtonId, View view) {
		return getControlFromView(view, zoomButtonId, ZoomButton.class);
	}

	/**
	 * Obtiene el {@link ZoomControls} de la vista indicado por id. Se produce un error inesperado
	 * si no existe, ya que se asume que debería existir
	 * 
	 * @param zoomControlsId
	 *            Id del ZoomControls a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static ZoomControls findZoomControls(int zoomControlsId, View view) {
		return getControlFromView(view, zoomControlsId, ZoomControls.class);
	}

	/**
	 * Obtiene el {@link View} de la vista indicado por id. Se produce un error inesperado si no
	 * existe, ya que se asume que debería existir
	 * 
	 * @param viewId
	 *            Id del View a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static View findInnerView(int viewId, View view) {
		return getControlFromView(view, viewId, View.class);
	}

	/**
	 * Obtiene el {@link ScrollView} de la vista indicado por id. Se produce un error inesperado si
	 * no existe, ya que se asume que debería existir
	 * 
	 * @param viewId
	 *            Id del View a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static ScrollView findScrollView(int viewId, View view) {
		return getControlFromView(view, viewId, ScrollView.class);
	}

	/**
	 * Obtiene el {@link LinearLayout} de la vista indicado por id. Se produce un error inesperado
	 * si no existe, ya que se asume que debería existir
	 * 
	 * @param viewId
	 *            Id del View a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static LinearLayout findLinearLayout(int viewId, View view) {
		return getControlFromView(view, viewId, LinearLayout.class);
	}

	/**
	 * Obtiene el {@link SlidingDrawer} de la vista indicado por id. Se produce un error inesperado
	 * si no existe, ya que se asume que debería existir
	 * 
	 * @param viewId
	 *            Id del View a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static SlidingDrawer findSlidingDrawer(int viewId, View view) {
		return getControlFromView(view, viewId, SlidingDrawer.class);
	}

	/**
	 * Obtiene el {@link GridView} de la vista indicado por id. Se produce un error inesperado si no
	 * existe, ya que se asume que debería existir
	 * 
	 * @param viewId
	 *            Id del View a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static GridView findGridView(int viewId, View view) {
		return getControlFromView(view, viewId, GridView.class);
	}

	/**
	 * Obtiene el {@link ExpandableListView} de la vista indicado por id. Se produce un error
	 * inesperado si no existe, ya que se asume que debería existir
	 * 
	 * @param viewId
	 *            Id del View a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static ExpandableListView findExpandableListView(int viewId, View view) {
		return getControlFromView(view, viewId, ExpandableListView.class);
	}

	/**
	 * Obtiene el {@link View} de la vista indicado por id. Se produce un error inesperado si no
	 * existe, ya que se asume que debería existir
	 * 
	 * @param viewId
	 *            Id del View a obtener
	 * @param view
	 *            Vista de la que obtendrá el control
	 * @return El control existente en la vista
	 */
	public static <V extends View> V findInnerViewAs(Class<V> tipoVista, int viewId, View view) {
		return getControlFromView(view, viewId, tipoVista);
	}

	/**
	 * Obtiene de la vista pasada el control identificado por su id, que debe ser del tipo esperado.
	 * Si no existe o no es assignable al tipo pasado, se produce un error.
	 * 
	 * @param <V>
	 *            Tipo del control esperado
	 * @param view
	 *            Vista de la que se obtendrá el control
	 * @param textViewId
	 *            Identificador del control
	 * @param expectedControlType
	 *            Tipo o super tipo del control esperado
	 * @return El control esperado
	 */
	@SuppressWarnings("unchecked")
	private static <V extends View> V getControlFromView(View view, int textViewId, Class<V> expectedControlType) {
		View control = view.findViewById(textViewId);
		if (control == null) {
			throw new RuntimeException("La vista[" + view + "] no posee el control que se esperaba[" + textViewId
					+ "] de tipo[" + expectedControlType + "]");
		}
		if (!expectedControlType.isAssignableFrom(control.getClass())) {
			throw new RuntimeException("Se obtuvo el control[" + control + "] pero no es del tipo esperado["
					+ expectedControlType + "]");
		}
		// El casteo es seguro porque lo comprobé con el if
		return (V) control;
	}
}
