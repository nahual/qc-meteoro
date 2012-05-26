package ar.com.iron.persistence;

import android.content.Context;
import ar.com.iron.helpers.ToastHelper;

public abstract class DefaultOnFailurePersistenceOperationListener<T> implements
		PersistenceOperationListener<T> {
	private final Context context;

	public DefaultOnFailurePersistenceOperationListener(Context context) {
		this.context = context;
	}

	@Override
	public void onFailure(final Exception exceptionThrown) {
		ToastHelper.create(context).showShort(
				"Se produjo un error al acceder a las bases guardadas: "
						+ exceptionThrown.getMessage());
	}
}
