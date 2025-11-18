package com.task.task.service;

import com.task.task.model.Usuario;
import com.task.task.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    
    private UsuarioRepository usuarioRepository;

    public UsuarioService() {
    }

    @Autowired
    public void setUsuarioRepository(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }

    public void CrearUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void ActualizarUsuario(Usuario usuarioActualizado, String idUsuario) {
        logger.info("Actualizando usuario con ID: {}", idUsuario);
        logger.debug("Datos recibidos: {}", usuarioActualizado);
        
        // Buscar el usuario existente
        Usuario usuarioExistente = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> {
                logger.error("Usuario no encontrado con id: {}", idUsuario);
                return new RuntimeException("Usuario no encontrado con id: " + idUsuario);
            });
        
        logger.debug("Usuario existente antes de actualizar: {}", usuarioExistente);
        
        // Actualizar solo los campos que vienen en la petición
        if (usuarioActualizado.getNombre() != null) {
            logger.debug("Actualizando nombre: {} -> {}", usuarioExistente.getNombre(), usuarioActualizado.getNombre());
            usuarioExistente.setNombre(usuarioActualizado.getNombre());
        }
        if (usuarioActualizado.getCorreo() != null) {
            logger.debug("Actualizando correo: {} -> {}", usuarioExistente.getCorreo(), usuarioActualizado.getCorreo());
            usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
        }
        if (usuarioActualizado.getContraseña() != null && !usuarioActualizado.getContraseña().isEmpty()) {
            logger.debug("Actualizando contraseña");
            usuarioExistente.setContraseña(usuarioActualizado.getContraseña());
        }
        if (usuarioActualizado.getRol() != null) {
            logger.debug("Actualizando rol: {} -> {}", usuarioExistente.getRol(), usuarioActualizado.getRol());
            usuarioExistente.setRol(usuarioActualizado.getRol());
        }
        
        // Guardar el usuario actualizado
        Usuario usuarioGuardado = usuarioRepository.save(usuarioExistente);
        logger.info("Usuario actualizado exitosamente con ID: {}", usuarioGuardado.getId());
        logger.debug("Usuario después de actualizar: {}", usuarioGuardado);
    }

    public void EliminarUsuario(String idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }

    public List<Usuario> consultarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario consultarUsuarioPorId(String idUsuario) {
        return usuarioRepository.findById(idUsuario).orElse(null);
    }

    public Usuario autenticarUsuario(String correo, String contraseña) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            return usuario;
        }
        return null;
    }

    public Usuario consultarUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}
