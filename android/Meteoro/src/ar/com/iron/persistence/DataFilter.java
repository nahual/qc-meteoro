/**
 * 
 */
package ar.com.iron.persistence;


/**
 * Esta interfaz representa un filtro de los datos buscados. Normalmente es un conjunto de
 * restricciones definidas sobre los datos para restringir los resultados devueltos por el dao.<br>
 * Esta interfaz es sólo un super tipo vacío ya que la manera de especificar esas restricciones
 * dependerá del motor de persistencia utilizado.<br>
 * <br>
 * Normalmente los filtros sólo deben recopilar los datos necesarios en su creación, y recién
 * procesarlos cuando interactúan con el motor de persistencia, de esta manera roban la menor
 * cantidad de tiempo posible del thread principal, y hacen el trabajo pesado en el thread del
 * servicio de persistencia
 * 
 * 
 * @author D. García
 */
public interface DataFilter {

}
