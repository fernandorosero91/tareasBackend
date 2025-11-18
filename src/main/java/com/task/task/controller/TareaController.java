package com.task.task.controller;

import com.task.task.model.EstadoTarea;
import com.task.task.model.Tarea;
import com.task.task.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {
    private TareaService tareaService;

    @Autowired
    public void setTareaService(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    public TareaService getTareaService() {
        return tareaService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_PROYECTO')")
    public void crearTarea(@RequestBody Tarea tarea) {
        tareaService.CrearTarea(tarea);
    }

    @GetMapping
    public List<Tarea> consultarTareas(@RequestHeader("Authorization") String token) {
        return tareaService.consultarTareas();
    }
    
    @GetMapping("/mis-tareas")
    public List<Tarea> consultarMisTareas(@RequestHeader("Authorization") String token) {
        return tareaService.consultarTareasPorUsuarioAutenticado(token);
    }

    @DeleteMapping("/{idTarea}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_PROYECTO')")
    public void eliminarTarea(@PathVariable String idTarea) {
        tareaService.EliminarTarea(idTarea);
    }

    @PutMapping("/{idTarea}")
    public void actualizarTarea(@RequestBody Tarea tarea, @PathVariable String idTarea) {
        tareaService.ActualizarTarea(tarea, idTarea);
    }
    
    @PatchMapping("/{idTarea}/estado")
    public void actualizarEstadoTarea(@PathVariable String idTarea, @RequestBody Map<String, String> body) {
        String estado = body.get("estado");
        tareaService.actualizarEstadoTarea(idTarea, EstadoTarea.valueOf(estado));
    }

    @PostMapping("/{idTarea}/asignar/{idUsuario}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE_PROYECTO')")
    public void asignarTareaUsuario(@PathVariable String idTarea, @PathVariable String idUsuario) {
        tareaService.asignarTareaUsuario(idTarea, idUsuario);
    }
}
