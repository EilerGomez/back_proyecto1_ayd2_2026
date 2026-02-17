/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorios;

import java.util.List;
import modelos.EntidadUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author eiler
 */

@Repository
public interface UsuarioRepositorio extends JpaRepository<EntidadUsuario,Integer> {
    List<EntidadUsuario> findByCorreo(String correo);
    boolean existsByCorreo (String correo);
    
    List<EntidadUsuario> findByUsername(String username);
    boolean existsByUsername (String username);
    
    List<EntidadUsuario> getUsuarios();
    
    EntidadUsuario getUsuario(Integer id);
    
    
    @Query("SELECT CASE WHEN COUNT(us) > 0 THEN true ELSE false END FROM usuarios us WHERE us.id <> :id AND us.username = :username")
    boolean existeUsuarioAActualizarPorUsername(@Param("id") Integer id, @Param("username") String username);
    
    
    @Query("SELECT CASE WHEN COUNT(us) > 0 THEN true ELSE false END FROM usuarios us WHERE us.id <> :id AND us.correo = :correo")
    boolean existeUsuarioAActualizarPorCorreo(@Param("id") Integer id, @Param("correo") String correo);

}
