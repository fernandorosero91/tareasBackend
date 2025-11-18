package com.task.task.controller;

import com.task.task.model.Usuario;
import com.task.task.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_PROYECTO')")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            // Verificar si el correo ya existe
            Usuario existente = usuarioService.consultarUsuarioPorCorreo(usuario.getCorreo());
            if (existente != null) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "El correo ya está registrado");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }

            // Encriptar la contraseña
            usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
            usuarioService.CrearUsuario(usuario);
            
            // No devolver la contraseña
            usuario.setContraseña(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al crear usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping
    public List<Usuario> consultarUsuarios() {
        return usuarioService.consultarUsuarios();
    }

    @GetMapping("/{idUsuario}")
    public Usuario consultarUsuarioPorId(@PathVariable String idUsuario) {
        return usuarioService.consultarUsuarioPorId(idUsuario);
    }

    @DeleteMapping("/{idUsuario}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_PROYECTO')")
    public void eliminarUsuario(@PathVariable String idUsuario) {
        usuarioService.EliminarUsuario(idUsuario);
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<?> actualizarUsuario(@RequestBody Usuario usuario, @PathVariable String idUsuario) {
        try {
            // Si viene contraseña, encriptarla
            if (usuario.getContraseña() != null && !usuario.getContraseña().isEmpty()) {
                usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
            }
            
            usuarioService.ActualizarUsuario(usuario, idUsuario);
            Usuario actualizado = usuarioService.consultarUsuarioPorId(idUsuario);
            actualizado.setContraseña(null);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al actualizar usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
