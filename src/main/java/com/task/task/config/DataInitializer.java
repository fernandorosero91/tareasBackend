package com.task.task.config;

import com.task.task.model.RolUsuario;
import com.task.task.model.Usuario;
import com.task.task.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existe un administrador
        Usuario adminExistente = usuarioRepository.findByCorreo("admin@taskflow.com");
        
        if (adminExistente == null) {
            // Crear usuario administrador por defecto
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setCorreo("admin@taskflow.com");
            admin.setContraseña(passwordEncoder.encode("admin123"));
            admin.setRol(RolUsuario.ADMINISTRADOR);
            
            usuarioRepository.save(admin);
            
            logger.info("========================================");
            logger.info("Usuario administrador creado exitosamente");
            logger.info("Correo: admin@taskflow.com");
            logger.info("Contraseña: admin123");
            logger.info("========================================");
        } else {
            logger.info("Usuario administrador ya existe");
        }
    }
}
