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
		// No tiene sentido
	}

	/**
	 * @see ar.com.iron.android.extensions.services.CronBgService#determineNextFireMoment(long,
	 *      long)
	 */
	@Override
	protected long determineNextFireMoment(final long lastFiredMoment, final long currentTime) {
		final long nextFireMoment = lastFiredMoment + 20 * 1000;
		return nextFireMoment;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.CronBgService#getStateFileName()
	 */
	@Override
	protected String getStateFileName() {
		return "update-service";
	}

}
