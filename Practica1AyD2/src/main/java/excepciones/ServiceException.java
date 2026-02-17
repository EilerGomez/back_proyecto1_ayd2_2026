/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 *
 * @author eiler
 */
public class ServiceException extends Exception{
    public ServiceException(){
        
    }
    
    public ServiceException(String mensaje){
        super(mensaje);
    }
}
