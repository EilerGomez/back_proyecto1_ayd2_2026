/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repositorios;

import java.util.Optional;
import modelos.EntidadRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author eiler
 */

@Repository
public interface RolRepositorio extends JpaRepository<EntidadRol, Integer>{
        Optional<EntidadRol> findByNombre(String nombre);
        EntidadRol findByIdRol(Integer id);
        boolean existsById(Integer id);
}
