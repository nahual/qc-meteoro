/**
 * 07/03/2011 21:19:37 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package ar.com.iron.android.extensions.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import ar.com.iron.helpers.ViewHelper;

/**
 * Esta clase es una extensión a los diálogos que agrega métodos templates y operaciones comunes
 * 
 * @author D. García
 */
public abstract class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
		if (context instanceof Activity) {
			this.setOwnerActivity((Activity) context);
		}
	}

	/**
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Debe llamarse antes de establecer otro contenido de la vista
		setupDialogTitle();
		this.setContentView(getDialogLayoutId());
		setUpOkCancelButtons();
		setUpComponents();
	}

	/**
	 * Configura los listeners de los botones indicados como OK/Cancel.<br>
	 * Al ejecutarse este método se define un listener del click de los botones (si existen) para
	 * recibir las notificaciones de OK o cancel de este diálogo
	 */
	protected void setUpOkCancelButtons() {
		Integer cancelButtonId = getCancelButtonId();
		if (cancelButtonId != null) {
			View cancelButton = ViewHelper.findInnerView(cancelButtonId, getContentView());
			cancelButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					onBotonCancelarApretado();
				}
			});
		}
		Integer okButtonId = getOkButtonId();
		if (okButtonId != null) {
			View okButton = ViewHelper.findInnerView(okButtonId, getContentView());
			okButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					onBotonOkApretado();
				}
			});
		}
	}

	/**
	 * Este método es invocado al ser presionado el botón de aceptar este diálogo. Se encarga de
	 * llamar a {@link #dismiss()}
	 */
	protected void onBotonOkApretado() {
		this.dismiss();
	}

	/**
	 * Este método es invocado al ser presionado el botón de cancelar el diálogo y se encarga de
	 * cancelar efectivamente este diálogo llamando a {@link #cancel()}
	 */
	protected void onBotonCancelarApretado() {
		this.cancel();
	}

	/**
	 * Establece el titulo para el diálogo en base a lo que defina la subclase
	 */
	protected void setupDialogTitle() {
		Object title = getDialogTitleOrId();
		if (title == null) {
			// Le quitamos el borde de título antes de establecer ningún contenido
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		} else if (title instanceof CharSequence) {
			CharSequence text = (CharSequence) title;
			setTitle(text);
		} else if (title instanceof Number) {
			int resourceId = ((Number) title).intValue();
			setTitle(resourceId);
		} else {
			throw new RuntimeException("El metodo getDialogTitleOrId() no devolvio un valor valido: " + title);
		}
	}

	/**
	 * Devuelve el texto que será parte del título, o el ID que lo identifica en los recursos.<br>
	 * Si se devuelve null, el diálogo no tendrá titulo
	 * 
	 * @return Un String con el titulo, un Integer con el id o null
	 */
	protected Object getDialogTitleOrId() {
		return null;
	}

	/**
	 * Devuelve el id del layout que será utilizado para todo el diálogo.<br>
	 * Debe contener un {@link ListView} con id="@+id/android:list", y un View para mostrar cuando
	 * no hay elementos con id="@+id/android:empty"
	 * 
	 * @return El id del layout usado para armar el diálogo
	 */
	protected abstract int getDialogLayoutId();

	/**
	 * Define comportamiento adicional para configurar otros componentes que pueda tener el diálogo
	 */
	protected void setUpComponents() {
		// No hacemos nada para que no sea necesaria la implementación
	}

	/**
	 * @return Devuelve la vista que contiene a este diálogo
	 */
	protected View getContentView() {
		return getWindow().getDecorView();
	}

	/**
	 * Establece el listener de dos opciones (OK o cancel) para diálogos que tiene esas opciones
	 * como botones que generan un dismiss como OK, o un cancel como cancelación. Este listener
	 * reemplaza al de dismiss y cancel que pueda tener este diálogo
	 * 
	 * @param onOkCancelDialogListener
	 *            El listener para ser notificado de los eventos
	 */
	public void setOnOkCancelDialogListener(OnOkCancelDialogListener<?> onOkCancelDialogListener) {
		this.setOnCancelListener(onOkCancelDialogListener);
		this.setOnDismissListener(onOkCancelDialogListener);
	}

	/**
	 * Devuelve el ID del botón usado para la opción de confirmación.<br>
	 * El botón debe ser accesible desde el {@link #getContentView()}
	 * 
	 * @return El Id del botón o null, si no existe un botón de confirmación
	 */
	public Integer getOkButtonId() {
		return null;
	}

	/**
	 * Devuelve el ID del botón usado para la opción de cancelación.<br>
	 * El botón debe ser accesible desde el {@link #getContentView()}
	 * 
	 * @return El Id del botón o null, si no existe un botón de cancelación
	 */
	public Integer getCancelButtonId() {
		return null;
	}

}