package com.task.task.controller;

import com.task.task.dto.LoginRequest;
import com.task.task.dto.LoginResponse;
import com.task.task.dto.RecuperarPasswordRequest;
import com.task.task.dto.RegisterRequest;
import com.task.task.model.Usuario;
import com.task.task.security.JwtUtil;
import com.task.task.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody RegisterRequest request) {
        try {
            // Verificar si el correo ya existe
            Usuario existente = usuarioService.consultarUsuarioPorCorreo(request.getCorreo());
            if (existente != null) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "El correo ya está registrado");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }

            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(request.getNombre());
            nuevoUsuario.setCorreo(request.getCorreo());
            nuevoUsuario.setContraseña(passwordEncoder.encode(request.getContraseña()));
            nuevoUsuario.setRol(request.getRol());

            usuarioService.CrearUsuario(nuevoUsuario);

            // Generar token
            String token = jwtUtil.generateToken(nuevoUsuario.getCorreo(), nuevoUsuario.getRol().name());
            
            // No devolver la contraseña
            nuevoUsuario.setContraseña(null);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token, nuevoUsuario));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al registrar usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Usuario usuario = usuarioService.consultarUsuarioPorCorreo(request.getCorreo());
            
            if (usuario == null) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Credenciales inválidas");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            if (!passwordEncoder.matches(request.getContraseña(), usuario.getContraseña())) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Credenciales inválidas");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            // Generar token
            String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol().name());
            
            // No devolver la contraseña
            usuario.setContraseña(null);
            
            return ResponseEntity.ok(new LoginResponse(token, usuario));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al iniciar sesión: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<?> recuperarPassword(@Valid @RequestBody RecuperarPasswordRequest request) {
        try {
            Usuario usuario = usuarioService.consultarUsuarioPorCorreo(request.getCorreo());
            
            if (usuario == null) {
                // Por seguridad, no revelar si el correo existe o no
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "Si el correo existe, recibirás instrucciones para recuperar tu contraseña");
                return ResponseEntity.ok(response);
            }

            // Aquí implementarías el envío de correo con token de recuperación
            // Por ahora, solo generamos una contraseña temporal
            String passwordTemporal = "Temp" + System.currentTimeMillis();
            usuario.setContraseña(passwordEncoder.encode(passwordTemporal));
            usuarioService.ActualizarUsuario(usuario, usuario.getId());

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Si el correo existe, recibirás instrucciones para recuperar tu contraseña");
            response.put("passwordTemporal", passwordTemporal); // En producción, esto se enviaría por correo
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al recuperar contraseña: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> obtenerUsuarioActual(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.substring(7);
            String correo = jwtUtil.extractUsername(jwt);
            Usuario usuario = usuarioService.consultarUsuarioPorCorreo(correo);
            
            if (usuario == null) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            usuario.setContraseña(null);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al obtener usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
