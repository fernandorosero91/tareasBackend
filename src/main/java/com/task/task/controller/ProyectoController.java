package com.task.task.controller;

import com.task.task.model.Proyecto;
import com.task.task.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {
    private ProyectoService proyectoService;

    @Autowired
    public void setProyectoService(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    public ProyectoService getProyectoService() {
        return proyectoService;
    }

    @PostMapping
    public void crearProyecto(@RequestBody Proyecto proyecto) {
        proyectoService.CrearProyecto(proyecto);
    }

    @GetMapping
    public List<Proyecto> consultarProyectos() {
        return proyectoService.consultarProyectos();
    }

    @GetMapping("/{idProyecto}")
    public Proyecto consultarProyectoPorId(@PathVariable String idProyecto) {
        return proyectoService.consultarProyectoPorId(idProyecto);
    }

    @DeleteMapping("/{idProyecto}")
    public void eliminarProyecto(@PathVariable String idProyecto) {
        proyectoService.EliminarProyecto(idProyecto);
    }

    @PutMapping("/{idProyecto}")
    public Proyecto actualizarProyecto(@RequestBody Proyecto proyecto, @PathVariable String idProyecto) {
        proyectoService.ActualizarProyecto(proyecto, idProyecto);
        return proyectoService.consultarProyectoPorId(idProyecto);
    }
}
