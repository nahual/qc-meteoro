package ar.com.iron.managers;

/**
 * Esta interfaz representa un Manager.<br>
 * Los managers son uno de los componentes principales de la arquitectura y representan la logica
 * que se realiza por atras de una pantalla o caso de uso.<br>
 * Siendo que las pantallas se modelan con un Activity, cada uno de estos tendra asociado por lo
 * menos un manager sobre el cual se delegaran las operaciones como: <br>
 * <ul>
 * <li>queries a la base de datos</li>
 * <li>conversion de objetos de negocio a Dtos</li>
 * <li>operaciones transaccionales</li>
 * <li>comunicacion con el servidor</li>
 * <li>etc</li>
 * </ul>
 * <br>
 * Estos objetos deberian ser accedidos desde el {@link ManagerLocator} y para la construccion de
 * los mismos, el locator invocara el constructor niladico.
 * 
 * 
 * @author Maximiliano Vazquez
 * 
 */
public interface Manager {

}
