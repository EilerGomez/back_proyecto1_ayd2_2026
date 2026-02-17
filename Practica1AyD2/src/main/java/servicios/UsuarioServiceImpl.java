/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import dtoUsuarios.NuevoUsuarioRequest;
import dtoUsuarios.UsuarioResponse;
import dtoUsuarios.UsuarioUpdateRequest;
import excepciones.ExcepcionEntidadDuplicada;
import excepciones.ExcepcionNoExiste;
import java.util.List;
import lombok.Value;
import modelos.EntidadPerfil;
import modelos.EntidadRol;
import modelos.EntidadUsuario;
import modelos.Entidad_Usuario_Rol;
import modelos.UsuarioRolId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositorios.PerfilRepositorio;
import repositorios.RolRepositorio;
import repositorios.UsuarioRepositorio;
import repositorios.UsuarioRolRepositorio;
import utilities.GeneratePassword;

/**
 *
 * @author eiler
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {
    
   private final UsuarioRepositorio uRepositorio;
   private final RolRepositorio rolRepository;
   private final PerfilRepositorio perfilRepository;
   private final UsuarioRolRepositorio usuarioRolRepository;
   private GeneratePassword genP;
   @Autowired
   public UsuarioServiceImpl(UsuarioRepositorio urepo, RolRepositorio rolrepo, PerfilRepositorio perfilrepo, UsuarioRolRepositorio usuariorolrepo){
       this.uRepositorio=urepo;
       this.rolRepository=rolrepo;
       this.perfilRepository=perfilrepo;
       this.usuarioRolRepository=usuariorolrepo;
       this.genP= new GeneratePassword();
   }

    @Override
    public EntidadUsuario getById(Integer id) throws ExcepcionNoExiste{
        return uRepositorio.findById(id)
            .orElseThrow(() -> new ExcepcionNoExiste("Usuario no encontrado"));
    }


    @Override
    public EntidadUsuario crearUsuario(NuevoUsuarioRequest nuevoU) throws ExcepcionEntidadDuplicada,ExcepcionNoExiste {
        //validad si existe el correo
        if(uRepositorio.existsByCorreo(nuevoU.getCorreo())){
            throw new ExcepcionEntidadDuplicada("El correo ya existe");
        }
        
        //validad el username
        
        if(uRepositorio.existsByUsername(nuevoU.getUsername())){
            throw new ExcepcionEntidadDuplicada("El username ya existe");
        }
        
        if(!rolRepository.existsById(nuevoU.getId_rol())){
            throw  new ExcepcionNoExiste("El Rol no existe");
        }
        
        // crear usuario
        EntidadUsuario u = nuevoU.crearEntidad();
        u.setPassword_hash(genP.hashPassword(nuevoU.getPassword()));
        EntidadUsuario usuarioGuardado = uRepositorio.save(u);

        // crear perfil con usuario_id
        EntidadPerfil perfil = new EntidadPerfil();
        perfil.setUsuario_id(usuarioGuardado.getId());
        perfilRepository.save(perfil);

        // crear registro en usuario_roles
        UsuarioRolId pk = new UsuarioRolId(usuarioGuardado.getId(), nuevoU.getId_rol());
        Entidad_Usuario_Rol ur = new Entidad_Usuario_Rol(pk);
        usuarioRolRepository.save(ur);

        return usuarioGuardado;
    }

    @Override
    public List<EntidadUsuario> findAll() {
        return uRepositorio.findAll();
    }
    

    @Override
    public void eliminarUsuario(Integer id) throws ExcepcionNoExiste {
        EntidadUsuario usr = getById(id);
        uRepositorio.deleteById(usr.getId());
    }

    @Override
    public UsuarioResponse actializarUsuario(Integer id, UsuarioUpdateRequest dataAActualizar) throws ExcepcionEntidadDuplicada, ExcepcionNoExiste {
        EntidadUsuario usuarioUpdate = getById(id);
        
        boolean existUsername = uRepositorio.existeUsuarioAActualizarPorUsername(usuarioUpdate.getId(), usuarioUpdate.getUsername());
        boolean existCorreo =uRepositorio.existeUsuarioAActualizarPorCorreo(usuarioUpdate.getId(), usuarioUpdate.getCorreo());
        
        if(existUsername){
            throw new ExcepcionEntidadDuplicada("EL Username ya existe");
        }
        
        if(existCorreo){
            throw new ExcepcionEntidadDuplicada("EL Correo ya existe");
        }
        
        usuarioUpdate.setNombre(dataAActualizar.getNombre());
        usuarioUpdate.setApellido(dataAActualizar.getApellido());
        usuarioUpdate.setUsername(dataAActualizar.getUsername());
        usuarioUpdate.setCorreo(dataAActualizar.getCorreo());
        usuarioUpdate.setEstado(dataAActualizar.getEstado());
        
        uRepositorio.save(usuarioUpdate);
        
        return new UsuarioResponse(usuarioUpdate);
        
    }
    
}
