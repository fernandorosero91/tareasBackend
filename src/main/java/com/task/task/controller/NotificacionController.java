package com.task.task.controller;

import com.task.task.model.Notificacion;
import com.task.task.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {
    private NotificacionService notificacionService;

    @Autowired
    public void setNotificacionService(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    public NotificacionService getNotificacionService() {
        return notificacionService;
    }

    @PostMapping
    public void crearNotificacion(@RequestBody Notificacion notificacion) {
        notificacionService.CrearNotificacion(notificacion);
    }

    @GetMapping
    public List<Notificacion> consultarNotificaciones() {
        return notificacionService.consultarNotificaciones();
    }

    @DeleteMapping("/{idNotificacion}")
    public void eliminarNotificacion(@PathVariable String idNotificacion) {
        notificacionService.EliminarNotificacion(idNotificacion);
    }

    @PostMapping("/{idNotificacion}/enviar")
    public void enviarNotificacion(@PathVariable String idNotificacion) {
        notificacionService.enviarNotificacion(idNotificacion);
    }
}
