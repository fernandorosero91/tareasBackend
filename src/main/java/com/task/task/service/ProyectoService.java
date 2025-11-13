package com.task.task.service;

import com.task.task.model.Proyecto;
import com.task.task.model.Tarea;
import com.task.task.repository.ProyectoRepository;
import com.task.task.repository.TareaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProyectoService {
    private static final Logger logger = LoggerFactory.getLogger(ProyectoService.class);
    
    private ProyectoRepository proyectoRepository;
    
    @Autowired
    private TareaRepository tareaRepository;

    public ProyectoService() {
    }

    @Autowired
    public void setProyectoRepository(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    public ProyectoRepository getProyectoRepository() {
        return proyectoRepository;
    }

    public void CrearProyecto(Proyecto proyecto) {
        proyectoRepository.save(proyecto);
    }

    public void ActualizarProyecto(Proyecto proyectoActualizado, String idProyecto) {
        logger.info("Actualizando proyecto con ID: {}", idProyecto);
        logger.debug("Datos recibidos: {}", proyectoActualizado);
        
        // Buscar el proyecto existente
        Proyecto proyectoExistente = proyectoRepository.findById(idProyecto)
            .orElseThrow(() -> {
                logger.error("Proyecto no encontrado con id: {}", idProyecto);
                return new RuntimeException("Proyecto no encontrado con id: " + idProyecto);
            });
        
        logger.debug("Proyecto existente antes de actualizar: {}", proyectoExistente);
        
        // Actualizar solo los campos que vienen en la petición
        if (proyectoActualizado.getNombre() != null) {
            logger.debug("Actualizando nombre: {} -> {}", proyectoExistente.getNombre(), proyectoActualizado.getNombre());
            proyectoExistente.setNombre(proyectoActualizado.getNombre());
        }
        if (proyectoActualizado.getDescripcion() != null) {
            logger.debug("Actualizando descripción: {} -> {}", proyectoExistente.getDescripcion(), proyectoActualizado.getDescripcion());
            proyectoExistente.setDescripcion(proyectoActualizado.getDescripcion());
        }
        if (proyectoActualizado.getResponsable() != null) {
            logger.debug("Actualizando responsable");
            proyectoExistente.setResponsable(proyectoActualizado.getResponsable());
        }
        if (proyectoActualizado.getFechaFinalizacion() != null) {
            logger.debug("Actualizando fecha de finalización");
            proyectoExistente.setFechaFinalizacion(proyectoActualizado.getFechaFinalizacion());
        }
        
        // Guardar el proyecto actualizado
        Proyecto proyectoGuardado = proyectoRepository.save(proyectoExistente);
        logger.info("Proyecto actualizado exitosamente con ID: {}", proyectoGuardado.getId());
        logger.debug("Proyecto después de actualizar: {}", proyectoGuardado);
    }

    public void EliminarProyecto(String idProyecto) {
        logger.info("Eliminando proyecto con ID: {}", idProyecto);
        
        // Verificar que el proyecto existe
        Proyecto proyecto = proyectoRepository.findById(idProyecto)
            .orElseThrow(() -> {
                logger.error("Proyecto no encontrado con id: {}", idProyecto);
                return new RuntimeException("Proyecto no encontrado con id: " + idProyecto);
            });
        
        // Buscar todas las tareas asociadas al proyecto
        List<Tarea> tareasDelProyecto = tareaRepository.findAll().stream()
            .filter(tarea -> tarea.getProyecto() != null && 
                    idProyecto.equals(tarea.getProyecto().getId()))
            .toList();
        
        // Eliminar todas las tareas asociadas
        if (!tareasDelProyecto.isEmpty()) {
            logger.info("Eliminando {} tareas asociadas al proyecto {}", tareasDelProyecto.size(), idProyecto);
            for (Tarea tarea : tareasDelProyecto) {
                logger.debug("Eliminando tarea: {} (ID: {})", tarea.getTitulo(), tarea.getId());
                tareaRepository.deleteById(tarea.getId());
            }
        } else {
            logger.info("No hay tareas asociadas al proyecto {}", idProyecto);
        }
        
        // Eliminar el proyecto
        proyectoRepository.deleteById(idProyecto);
        logger.info("Proyecto {} eliminado exitosamente", idProyecto);
    }

    public List<Proyecto> consultarProyectos() {
        logger.debug("Consultando todos los proyectos");
        List<Proyecto> proyectos = proyectoRepository.findAll();
        logger.debug("Proyectos encontrados: {}", proyectos.size());
        
        // Obtener todas las tareas
        List<Tarea> todasLasTareas = tareaRepository.findAll();
        logger.debug("Tareas totales en BD: {}", todasLasTareas.size());
        
        // Asignar tareas a cada proyecto
        for (Proyecto proyecto : proyectos) {
            List<Tarea> tareasDelProyecto = todasLasTareas.stream()
                .filter(tarea -> {
                    if (tarea.getProyecto() == null) {
                        logger.debug("Tarea '{}' no tiene proyecto asociado", tarea.getTitulo());
                        return false;
                    }
                    if (proyecto.getId() == null) {
                        logger.warn("Proyecto sin ID encontrado");
                        return false;
                    }
                    boolean coincide = proyecto.getId().equals(tarea.getProyecto().getId());
                    if (coincide) {
                        logger.debug("Tarea '{}' pertenece al proyecto '{}'", tarea.getTitulo(), proyecto.getNombre());
                    }
                    return coincide;
                })
                .toList();
            
            logger.debug("Proyecto '{}' tiene {} tareas", proyecto.getNombre(), tareasDelProyecto.size());
            proyecto.setTareas(tareasDelProyecto);
        }
        
        return proyectos;
    }

    public Proyecto consultarProyectoPorId(String idProyecto) {
        Proyecto proyecto = proyectoRepository.findById(idProyecto).orElse(null);
        if (proyecto != null) {
            // Obtener tareas del proyecto
            List<Tarea> tareas = tareaRepository.findAll().stream()
                .filter(tarea -> tarea.getProyecto() != null && 
                        idProyecto.equals(tarea.getProyecto().getId()))
                .toList();
            proyecto.setTareas(tareas);
        }
        return proyecto;
    }
}
