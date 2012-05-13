/**
 * 03/07/2011 02:40:14 Copyright (C) 2011 Darío García
 */
package ar.com.iron.persistence.db4o.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Este annotation es una marca para utilizar en clases persistentes de manera de indicarle a Db4o
 * que debe propagar las acciones de persistencia
 * 
 * @author D. García
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CascadeField {
	/**
	 * Indica si debe activarse el objeto del atributo al activarse la clase contenedora
	 * 
	 * @return true por default
	 */
	boolean cascadeOnActivate() default true;

	/**
	 * Indica si debe actualizarse el objeto del atributo al actualizarse la instancia contenedora
	 * 
	 * @return true por defecto
	 */
	boolean cascadeOnUpdate() default true;

	/**
	 * Indica que debe borrarse el objeto del atributo al borrarse la instancia contenedora
	 * 
	 * @return false por defecto
	 */
	boolean cascadeOnDelete() default false;
}
