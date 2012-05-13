package ar.com.iron.android.helpers;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Esta clase representa un Helper sobre los {@link EditText}.
 * 
 * @author Maximiliano Vázquez
 */
public abstract class EditTextHelper {

	/**
	 * Linkea un {@link EditText} a un {@link Button}.<br>
	 * Esto implica que cuando se oprima la tecla enter estando con el foco en el {@link EditText}
	 * se propagará la acción al {@link Button} simulando un click sobre el mismo.<br>
	 */
	public static void linkToButtonOnEnter(EditText editText, final Button button) {

		OnKeyListener l = new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					button.performClick();
					return true;

				}
				return false;
			}
		};
		editText.setOnKeyListener(l);
	}

}
