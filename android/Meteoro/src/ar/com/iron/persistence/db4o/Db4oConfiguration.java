/**
 * 02/07/2011 23:37:48 Copyright (C) 2011 Darío García
 */
package ar.com.iron.persistence.db4o;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ar.com.iron.persistence.db4o.annotations.CascadeField;
import ar.com.iron.persistence.db4o.annotations.IndexField;
import ar.com.iron.persistence.persistibles.Persistible;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.config.ObjectClass;
import com.db4o.config.ObjectField;

/**
 * Esta clase representa la configuración de la base db4o
 * 
 * @author D. García
 */
public class Db4oConfiguration {

	/**
	 * Nombre de archivo para la base de la aplicación
	 */
	private static final String DATABASE_NAME = "database.db4o";

	/**
	 * Directorio dentro de la app donde se guarda la base
	 */
	public static final String DATABASE_DIR = "persistence";

	private String databaseDir = DATABASE_DIR;
	private String databaseName = DATABASE_NAME;

	private List<Class<? extends Persistible>> persistibleClasses;

	public List<Class<? extends Persistible>> getPersistibleClasses() {
		if (persistibleClasses == null) {
			persistibleClasses = new ArrayList<Class<? extends Persistible>>();
		}
		return persistibleClasses;
	}

	public String getDatabaseDir() {
		return databaseDir;
	}

	public void setDatabaseDir(final String databaseDir) {
		this.databaseDir = databaseDir;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(final String databaseName) {
		this.databaseName = databaseName;
	}

	/**
	 * Agrega la clase pasada como próximo persistible. Antes deben registrarse las clases de las
	 * que esta depende, ya que serán creadas en orden
	 * 
	 * @param nextPersistible
	 *            Clase persistible marcada con annotations de ORMLite
	 */
	public void addPersistibleClass(final Class<? extends Persistible> nextPersistible) {
		getPersistibleClasses().add(nextPersistible);
	}

	/**
	 * Devuelve la configuración para abrir la base
	 * 
	 * @return La configuración para Db4o
	 */
	public EmbeddedConfiguration getInternalConfig() {
		// Las configuraciones no son reutilizables
		return calculateConfig();
	}

	/**
	 * Realiza la configuracion de una entidad basado en los metadatos que expone la entidad.<br>
	 * Se configuran los fields que corresponden ser indexados basados en el annotation
	 * {@link IndexedFields} que contiene la entidad.
	 */
	protected void configureEntity(final EmbeddedConfiguration config, final Class<? extends Persistible> clazz) {
		final ObjectClass objectClass = config.common().objectClass(clazz);

		// Recorremos también sus superclases
		Class<?> currentClass = clazz;
		while (!Object.class.equals(currentClass)) {
			final Field[] classFields = currentClass.getDeclaredFields();
			// Por cada atributo verificamos si tiene annotations
			for (final Field currentField : classFields) {
				final CascadeField cascadeAnnotation = currentField.getAnnotation(CascadeField.class);
				if (cascadeAnnotation != null) {
					final ObjectField objectField = objectClass.objectField(currentField.getName());
					objectField.cascadeOnActivate(cascadeAnnotation.cascadeOnActivate());
					objectField.cascadeOnDelete(cascadeAnnotation.cascadeOnDelete());
					objectField.cascadeOnUpdate(cascadeAnnotation.cascadeOnUpdate());
				}

				final IndexField indexAnnotation = currentField.getAnnotation(IndexField.class);
				if (indexAnnotation != null) {
					final ObjectField objectField = objectClass.objectField(currentField.getName());
					objectField.indexed(true);
				}
			}
			// Pasamos al parent
			currentClass = currentClass.getSuperclass();
		}
	}

	/**
	 * Crea una nueva configuración de db4o para ser usado con las sesiones a la base
	 * 
	 * @return La configuración creada
	 */
	private EmbeddedConfiguration calculateConfig() {
		final EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		final List<Class<? extends Persistible>> allPersistibleClasses = getPersistibleClasses();
		for (final Class<? extends Persistible> persistibleClass : allPersistibleClasses) {
			configureEntity(config, persistibleClass);
		}
		return config;
	}

	public static Db4oConfiguration create() {
		final Db4oConfiguration config = new Db4oConfiguration();
		return config;
	}
}
