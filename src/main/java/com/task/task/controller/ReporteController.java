package com.task.task.controller;

import com.task.task.model.EstadoTarea;
import com.task.task.model.Proyecto;
import com.task.task.model.Tarea;
import com.task.task.model.Usuario;
import com.task.task.service.ProyectoService;
import com.task.task.service.TareaService;
import com.task.task.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private TareaService tareaService;

    @Autowired
    private ProyectoService proyectoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/productividad-empleado/{idUsuario}")
    public Map<String, Object> reporteProductividadEmpleado(@PathVariable String idUsuario) {
        Usuario usuario = usuarioService.consultarUsuarioPorId(idUsuario);
        List<Tarea> todasLasTareas = tareaService.consultarTareas();
        
        List<Tarea> tareasUsuario = todasLasTareas.stream()
            .filter(t -> t.getResponsable() != null && t.getResponsable().getId().equals(idUsuario))
            .collect(Collectors.toList());

        long completadas = tareasUsuario.stream()
            .filter(t -> t.getEstado() == EstadoTarea.COMPLETADA)
            .count();
        
        long enProceso = tareasUsuario.stream()
            .filter(t -> t.getEstado() == EstadoTarea.ENPROCESO)
            .count();
        
        long pendientes = tareasUsuario.stream()
            .filter(t -> t.getEstado() == EstadoTarea.PENDIENTE)
            .count();
        
        long retrasadas = tareasUsuario.stream()
            .filter(t -> t.getEstado() == EstadoTarea.RETRASADA || t.estaRetrasada())
            .count();

        Map<String, Object> reporte = new HashMap<>();
        reporte.put("usuario", usuario);
        reporte.put("totalTareas", tareasUsuario.size());
        reporte.put("completadas", completadas);
        reporte.put("enProceso", enProceso);
        reporte.put("pendientes", pendientes);
        reporte.put("retrasadas", retrasadas);
        reporte.put("porcentajeCompletadas", tareasUsuario.isEmpty() ? 0 : (completadas * 100.0 / tareasUsuario.size()));
        reporte.put("tareas", tareasUsuario);

        return reporte;
    }

    @GetMapping("/productividad-general")
    public Map<String, Object> reporteProductividadGeneral() {
        List<Usuario> usuarios = usuarioService.consultarUsuarios();
        List<Map<String, Object>> reportesPorUsuario = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            Map<String, Object> reporte = reporteProductividadEmpleado(usuario.getId());
            reportesPorUsuario.add(reporte);
        }

        return Map.of(
            "totalUsuarios", usuarios.size(),
            "reportes", reportesPorUsuario
        );
    }

    @GetMapping("/proyecto/{idProyecto}")
    public Map<String, Object> reporteProyecto(@PathVariable String idProyecto) {
        Proyecto proyecto = proyectoService.consultarProyectoPorId(idProyecto);
        List<Tarea> tareas = proyecto.getTareas() != null ? proyecto.getTareas() : new ArrayList<>();

        long completadas = tareas.stream()
            .filter(t -> t.getEstado() == EstadoTarea.COMPLETADA)
            .count();
        
        long enProceso = tareas.stream()
            .filter(t -> t.getEstado() == EstadoTarea.ENPROCESO)
            .count();
        
        long pendientes = tareas.stream()
            .filter(t -> t.getEstado() == EstadoTarea.PENDIENTE)
            .count();
        
        long retrasadas = tareas.stream()
            .filter(t -> t.getEstado() == EstadoTarea.RETRASADA || t.estaRetrasada())
            .count();

        Map<String, Object> reporte = new HashMap<>();
        reporte.put("proyecto", proyecto);
        reporte.put("totalTareas", tareas.size());
        reporte.put("completadas", completadas);
        reporte.put("enProceso", enProceso);
        reporte.put("pendientes", pendientes);
        reporte.put("retrasadas", retrasadas);
        reporte.put("porcentajeAvance", tareas.isEmpty() ? 0 : (completadas * 100.0 / tareas.size()));

        return reporte;
    }

    @GetMapping("/tareas-retrasadas")
    public List<Tarea> tareasRetrasadas() {
        List<Tarea> todasLasTareas = tareaService.consultarTareas();
        return todasLasTareas.stream()
            .filter(t -> t.estaRetrasada() || t.getEstado() == EstadoTarea.RETRASADA)
            .collect(Collectors.toList());
    }

    @GetMapping("/tareas-proximas-vencer")
    public List<Tarea> tareasProximasVencer(@RequestParam(defaultValue = "7") int dias) {
        List<Tarea> todasLasTareas = tareaService.consultarTareas();
        return todasLasTareas.stream()
            .filter(t -> t.getEstado() != EstadoTarea.COMPLETADA)
            .filter(t -> t.diasRestantes() >= 0 && t.diasRestantes() <= dias)
            .sorted(Comparator.comparingInt(Tarea::diasRestantes))
            .collect(Collectors.toList());
    }

    @GetMapping("/estadisticas-generales")
    public Map<String, Object> estadisticasGenerales() {
        List<Proyecto> proyectos = proyectoService.consultarProyectos();
        List<Tarea> tareas = tareaService.consultarTareas();
        List<Usuario> usuarios = usuarioService.consultarUsuarios();

        long tareasCompletadas = tareas.stream()
            .filter(t -> t.getEstado() == EstadoTarea.COMPLETADA)
            .count();
        
        long tareasRetrasadas = tareas.stream()
            .filter(t -> t.estaRetrasada() || t.getEstado() == EstadoTarea.RETRASADA)
            .count();

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalProyectos", proyectos.size());
        estadisticas.put("totalTareas", tareas.size());
        estadisticas.put("totalUsuarios", usuarios.size());
        estadisticas.put("tareasCompletadas", tareasCompletadas);
        estadisticas.put("tareasRetrasadas", tareasRetrasadas);
        estadisticas.put("porcentajeCompletadas", tareas.isEmpty() ? 0 : (tareasCompletadas * 100.0 / tareas.size()));

        return estadisticas;
    }
}
