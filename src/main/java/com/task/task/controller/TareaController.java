package com.task.task.controller;

import com.task.task.model.Tarea;
import com.task.task.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public void crearTarea(@RequestBody Tarea tarea) {
        tareaService.CrearTarea(tarea);
    }

    @GetMapping
    public List<Tarea> consultarTareas() {
        return tareaService.consultarTareas();
    }

    @DeleteMapping("/{idTarea}")
    public void eliminarTarea(@PathVariable String idTarea) {
        tareaService.EliminarTarea(idTarea);
    }

    @PutMapping("/{idTarea}")
    public void actualizarTarea(@RequestBody Tarea tarea, @PathVariable String idTarea) {
        tareaService.ActualizarTarea(tarea, idTarea);
    }

    @PostMapping("/{idTarea}/asignar/{idUsuario}")
    public void asignarTareaUsuario(@PathVariable String idTarea, @PathVariable String idUsuario) {
        tareaService.asignarTareaUsuario(idTarea, idUsuario);
    }
}
