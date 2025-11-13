package com.task.task.service;

import com.task.task.model.Tarea;
import com.task.task.model.Usuario;
import com.task.task.repository.TareaRepository;
import com.task.task.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TareaService {
    private static final Logger logger = LoggerFactory.getLogger(TareaService.class);
    
    private TareaRepository tareaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public TareaService() {
    }

    @Autowired
    public void setTareaRepository(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public TareaRepository getTareaRepository() {
        return tareaRepository;
    }

    public void CrearTarea(Tarea tarea) {
        logger.info("Creando tarea: {}", tarea.getTitulo());
        if (tarea.getProyecto() != null) {
            logger.info("Tarea asociada al proyecto ID: {}", tarea.getProyecto().getId());
        } else {
            logger.warn("Tarea creada sin proyecto asociado");
        }
        tareaRepository.save(tarea);
        logger.info("Tarea creada exitosamente con ID: {}", tarea.getId());
    }

    public void ActualizarTarea(Tarea tareaActualizada, String idTarea) {
        // Buscar la tarea existente
        Tarea tareaExistente = tareaRepository.findById(idTarea)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada con id: " + idTarea));
        
        // Actualizar solo los campos que vienen en la petición
        if (tareaActualizada.getTitulo() != null) {
            tareaExistente.setTitulo(tareaActualizada.getTitulo());
        }
        if (tareaActualizada.getDescripcion() != null) {
            tareaExistente.setDescripcion(tareaActualizada.getDescripcion());
        }
        if (tareaActualizada.getResponsable() != null) {
            tareaExistente.setResponsable(tareaActualizada.getResponsable());
        }
        if (tareaActualizada.getProyecto() != null) {
            tareaExistente.setProyecto(tareaActualizada.getProyecto());
        }
        if (tareaActualizada.getFechaInicio() != null) {
            tareaExistente.setFechaInicio(tareaActualizada.getFechaInicio());
        }
        if (tareaActualizada.getFechaEntrega() != null) {
            tareaExistente.setFechaEntrega(tareaActualizada.getFechaEntrega());
        }
        if (tareaActualizada.getEstado() != null) {
            tareaExistente.setEstado(tareaActualizada.getEstado());
        }
        
        // Guardar la tarea actualizada
        tareaRepository.save(tareaExistente);
    }

    public void EliminarTarea(String idTarea) {
        tareaRepository.deleteById(idTarea);
    }

    public List<Tarea> consultarTareas() {
        return tareaRepository.findAll();
    }

    public Tarea consultarTareaPorId(String idTarea) {
        return tareaRepository.findById(idTarea).orElse(null);
    }

    public void asignarTareaUsuario(String idTarea, String idUsuario) {
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (tarea != null && usuario != null) {
            tarea.cambiarUsuario(usuario);
            tareaRepository.save(tarea);
        }
    }
}
