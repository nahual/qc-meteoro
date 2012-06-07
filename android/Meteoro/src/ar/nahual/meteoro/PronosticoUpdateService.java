/**
 * 07/06/2012 09:26:46 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro;

import java.util.Calendar;

import ar.com.iron.android.extensions.services.CronBgService;
import ar.com.iron.android.extensions.services.FailedTaskExecutionException;

/**
 * Este servicio se encarga de pedir al frontend que actualice la vista
 * 
 * @author D. García
 */
public class PronosticoUpdateService extends CronBgService {

	public PronosticoUpdateService() {
		super("PronosticoService");
	}

	/**
	 * @see ar.com.iron.android.extensions.services.CronBgService#executeTask()
	 */
	@Override
	protected void executeTask() throws FailedTaskExecutionException {
		sendBroadcast(new ActualizarPronostico());
	}

	/**
	 * @see ar.com.iron.android.extensions.services.CronBgService#setTodayFireMoment(java.util.Calendar)
	 */
	@Override
	protected void setTodayFireMoment(final Calendar calendar) {
		final long now = calendar.getTimeInMillis();
		// Debería ejecutar dentro de 20s
		calendar.setTimeInMillis(now + 20 * 1000);
	}

	/**
	 * @see ar.com.iron.android.extensions.services.CronBgService#getStateFileName()
	 */
	@Override
	protected String getStateFileName() {
		return "update-service";
	}

}
