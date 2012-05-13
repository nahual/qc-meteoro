/**
 * 13/03/2011 19:09:27 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.persistence.messages;

import android.content.Intent;
import ar.com.iron.annotations.CantBeNull;
import ar.com.iron.persistence.persistibles.Persistible;

/**
 * Esta clase representa el mensaje enviado desde el servicio de persistencia para notificar que el
 * estado de un persistible fue guardado
 * 
 * @author D. García
 */
public class PersistibleChangedMessage extends Intent {
	public static final String ACTION = "ar.com.iron.persistence.messages.CHANGED";

	public static final String ID_EXTRA = "persistible_id";
	public static final String CLASS_NAME_EXTRA = "class_name";
	public static final String CHANGE_TYPE_EXTRA = "persistible_change_type";

	/**
	 * Constante que indica que el tipo de cambio fue la creación de la entidad
	 */
	public static final int CREATED_CHANGE_TYPE = 0;
	/**
	 * Constante que indica que el tipo de cambio fue la actualización de los datos
	 */
	public static final int UPDATED_CHANGE_TYPE = 2;
	/**
	 * Constante que indica que el tipo de cambio fue el borrado de la entidad
	 */
	public static final int DELETED_CHANGE_TYPE = 4;

	/**
	 * Constructor por copia
	 */
	public PersistibleChangedMessage(Intent other) {
		super(other);
	}

	public PersistibleChangedMessage(Persistible persisitble, int changeType) {
		super(ACTION);
		putExtra(CLASS_NAME_EXTRA, persisitble.getClass().getName());
		putExtra(ID_EXTRA, persisitble.getId());
		putExtra(CHANGE_TYPE_EXTRA, changeType);
	}

	/**
	 * Devuelve el id de la entidad guardada (puede ser que antes no tuviera ID en cuyo caso es una
	 * entidad nueva). Se puede consultar si el ID es nuevo con {@link #isNewId()}
	 * 
	 * @return El ID de la entidad guardada
	 */
	@CantBeNull
	public Long getPersistibleId() {
		long persistibleId = getLongExtra(ID_EXTRA, -1);
		if (persistibleId == -1) {
			throw new IllegalStateException("Este mensaje debería tener ID como dato extra");
		}
		return persistibleId;
	}

	/**
	 * Indica el tipo de cambio sobre la entidad persistible.<br>
	 * Las constantes {@link #CREATED_CHANGE_TYPE}, {@link #UPDATED_CHANGE_TYPE},
	 * {@link #DELETED_CHANGE_TYPE} pueden usarse para verificar si este mensaje representa la
	 * creación, la modificación, o borrado de una entidad
	 * 
	 * @return true si la entidad es nueva en la base
	 */
	public int getChangeType() {
		int changeType = getIntExtra(CHANGE_TYPE_EXTRA, 0);
		if (changeType == 0) {
			throw new IllegalStateException("Este mensaje debería indicar el tipo de cambio como dato extra");
		}
		return changeType;
	}

	/**
	 * Devuelve el nombre de la clase cuya entidad fue persistida
	 * 
	 * @return El nombre de la clase persistible
	 */
	public String getPersistibleClassName() {
		String className = getStringExtra(CLASS_NAME_EXTRA);
		if (className == null) {
			throw new IllegalStateException(
					"Este mensaje debería tener el nombre de la clase persisitda como dato extra");
		}
		return className;
	}

}
