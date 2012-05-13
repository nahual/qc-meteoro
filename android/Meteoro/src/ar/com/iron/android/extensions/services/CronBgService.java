/**
 * 28/01/2011 13:09:59 Darío L. García
 */
package ar.com.iron.android.extensions.services;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import ar.com.iron.android.helpers.ContextHelper;

/**
 * Esta clase representa un servicio de ejecución en background (utiliza su propio thread, pero
 * dentro del mismo proceso), que es configurado para ejecutar una tarea periódicamente.<br>
 * Normalmente las tareas de la aplicación que deben ejecutarse diariamente implementan una subclase
 * de esta, definiendo el momento de ejecución, la tarea y opcionalmente el tiempo de reintento.<br>
 * Esta clase utiliza un {@link SharedPreferences} para mantener el registro de las ejecuciones
 * 
 * @author D. García
 */
public abstract class CronBgService extends IntentService {

	/**
	 * Constante para la variable de estado que registra el último momento de ejecución
	 */
	private static final String LAST_RUN_MOMENT = "lastRunMoment";

	/**
	 * Parámetro adicional del intent que indica que debe ejecutarse la tarea aunque no sea el
	 * momento correcto al recibir un intent
	 */
	public static final String SHOULD_EXECUTE_IF_NOT_ON_TIME_EXTRA = "SHOULD_EXECUTE_IF_NOT_ON_TIME_EXTRA";

	/**
	 * @param threadName
	 *            Nombre que se le dará al thread de este servicio
	 */
	public CronBgService(String threadName) {
		super(threadName);
	}

	/**
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		long currentTime = getCurrentTime();
		long lastFiredMoment = getLastFireMoment();
		long nextFireMoment = determineNextFireMoment(lastFiredMoment, currentTime);
		boolean isTimeToExecute = nextFireMoment <= currentTime;
		boolean forceExecution = intent.getBooleanExtra(SHOULD_EXECUTE_IF_NOT_ON_TIME_EXTRA, false);
		if (forceExecution || isTimeToExecute) {
			// Es hora de ejecutar, ya estamos pasados
			try {
				executeTask();
				// Recalculamos próxima ejecución
				lastFiredMoment = saveLastFireMoment();
				nextFireMoment = determineNextFireMoment(lastFiredMoment, getCurrentTime());
			} catch (FailedTaskExecutionException e) {
				Log.e("CronBgService", "Error en la ejecución background de la tarea["
						+ this.getClass().getSimpleName() + "]: " + e.getMessage(), e);
				// Reintentamos más tarde por haber fallado
				nextFireMoment = currentTime + getAfterFailRepeatInterval();
			}
		}
		scheduleNextExecution(nextFireMoment);
	}

	/**
	 * Agregamos una alarma al dispositivo para que ejecute este servicio en el momento indicado
	 * 
	 * @param nextFireMoment
	 *            Próximo momento de ejecución de este servicio (debe ser a futuro)
	 */
	protected void scheduleNextExecution(long nextFireMoment) {
		Intent serviceIntent = new Intent(this, this.getClass());
		int unusedCode = 0;
		PendingIntent pendingIntent = PendingIntent.getService(this, unusedCode, serviceIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarmManager = ContextHelper.getAlarmManager(this);
		alarmManager.set(AlarmManager.RTC, nextFireMoment, pendingIntent);
	}

	/**
	 * Devuelve el intervalo en milisegundos en el que se debe reintentar después de una ejecución
	 * fallida de la tarea.<br>
	 * Por defecto 1h después
	 * 
	 * @return El intervalo en milisegundos en el que una tarea se vuelve a ejecutar si falló la vez
	 *         anterior
	 */
	protected long getAfterFailRepeatInterval() {
		int unaHora = 1 * 60 * 60 * 1000;
		return unaHora;
	}

	/**
	 * @return Devuelve el momento considerado como actual para este servicio (varía entre
	 *         invocaciones)
	 */
	protected long getCurrentTime() {
		return System.currentTimeMillis();
	}

	/**
	 * Ejecuta la tarea designada para este servicio
	 * 
	 * @throws FailedTaskExecutionException
	 *             Si la tarea no pudo ser terminada exitósamente, y debe reintentarse
	 */
	protected abstract void executeTask() throws FailedTaskExecutionException;

	/**
	 * Determina el próximo momento en que se debería ejecutar la tarea dado que su último momento
	 * de ejecución es el pasado, y que estamos en el momento actual.<br>
	 * Este método debe devolver un momento pasado si se desea ejecutar ya, o un momento en el
	 * futuro si se debe planificar la ejecución con la alarma.<br>
	 * Si es la primera ejecución del servicio lastFireMoment será 0.<br>
	 * 
	 * @param lastFiredMoment
	 *            Momento de la última ejecución exitosa de la tarea
	 * @param currentTime
	 *            Momento considerado actual
	 * @return El momento de la próxima ejecución de la tarea
	 */
	protected long determineNextFireMoment(long lastFiredMoment, long currentTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(currentTime);
		setTodayFireMoment(calendar);
		long todayFireTime = calendar.getTimeInMillis();
		if (todayFireTime > lastFiredMoment) {
			// Todavía no ejecutamos hoy, ese es el momento de ejecución
			return todayFireTime;
		}
		// Si hoy ya ejecutamos, entonces tenemos que calcular el momento de ejecución para mañana
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		long tomorrowFireTime = calendar.getTimeInMillis();
		return tomorrowFireTime;
	}

	/**
	 * Establece el momento de ejecución de la tarea en el calendario que representa el día de hoy.<br>
	 * La subclase debe modificar el calendario para que indique el momento de ejecución para el día
	 * que representa el calendario
	 * 
	 * @param calendar
	 *            Calendario que representa el momento actual
	 */
	protected abstract void setTodayFireMoment(Calendar calendar);

	/**
	 * Devuelve el último momento de ejecución de la tarea encargada a este servicio
	 * 
	 * @return El último momento en que este servicio pudo ejecutar la tarea. 0 si es la primera vez
	 */
	protected long getLastFireMoment() {
		SharedPreferences serviceState = getServicePreferences();
		long lastRunMoment = serviceState.getLong(LAST_RUN_MOMENT, 0);
		return lastRunMoment;
	}

	/**
	 * Guarda el momento actual como última ejecución exitosa y devuelve su valor
	 * 
	 * @return El momento actualizado, considerado como última ejecución exitosa
	 */
	protected long saveLastFireMoment() {
		long currentTime = getCurrentTime();
		SharedPreferences serviceState = getServicePreferences();
		Editor editor = serviceState.edit();
		editor.putLong(LAST_RUN_MOMENT, currentTime);
		boolean commited = editor.commit();
		if (!commited) {
			Log.e("CronBgService", "Error al guardar el momento de última ejecución en el cron: " + getClass());
		}
		return currentTime;

	}

	/**
	 * Devuelve el archivo de preferences utilizado para mantener el estado interno de este servicio
	 * 
	 * @return El preferences editable
	 */
	protected SharedPreferences getServicePreferences() {
		return getSharedPreferences(getStateFileName(), MODE_WORLD_READABLE);
	}

	/**
	 * Devuelve el nombre del archivo que se utilizará para guardar el estado de ejecución de este
	 * servicio en las preferencias de la app
	 * 
	 * @return Un nombre de archivo que sea representativo (sin extensión)
	 */
	protected abstract String getStateFileName();

}