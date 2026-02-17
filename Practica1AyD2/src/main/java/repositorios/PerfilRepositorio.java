/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repositorios;

import java.util.List;
import modelos.EntidadPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author eiler
 */
@Repository
public interface PerfilRepositorio extends JpaRepository<EntidadPerfil, Integer>{
    List<EntidadPerfil> findByusuario_id(Integer id);
}
