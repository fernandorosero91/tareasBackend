package com.task.task.controller;

import com.task.task.model.Usuario;
import com.task.task.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private UsuarioService usuarioService;

    @Autowired
    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    @PostMapping
    public void crearUsuario(@RequestBody Usuario usuario) {
        usuarioService.CrearUsuario(usuario);
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
    public void eliminarUsuario(@PathVariable String idUsuario) {
        usuarioService.EliminarUsuario(idUsuario);
    }

    @PutMapping("/{idUsuario}")
    public Usuario actualizarUsuario(@RequestBody Usuario usuario, @PathVariable String idUsuario) {
        usuarioService.ActualizarUsuario(usuario, idUsuario);
        return usuarioService.consultarUsuarioPorId(idUsuario);
    }

    @PostMapping("/autenticar")
    public Usuario autenticarUsuario(@RequestParam String correo, @RequestParam String contraseña) {
        return usuarioService.autenticarUsuario(correo, contraseña);
    }
}
