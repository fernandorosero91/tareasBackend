package com.task.task.service;

import com.task.task.model.Notificacion;
import com.task.task.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {
    private NotificacionRepository notificacionRepository;

    public NotificacionService() {
    }

    @Autowired
    public void setNotificacionRepository(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public NotificacionRepository getNotificacionRepository() {
        return notificacionRepository;
    }

    public void CrearNotificacion(Notificacion notificacion) {
        notificacionRepository.save(notificacion);
    }

    public void EliminarNotificacion(String idNotificacion) {
        notificacionRepository.deleteById(idNotificacion);
    }

    public List<Notificacion> consultarNotificaciones() {
        return notificacionRepository.findAll();
    }

    public Notificacion consultarNotificacionPorId(String idNotificacion) {
        return notificacionRepository.findById(idNotificacion).orElse(null);
    }

    public void enviarNotificacion(String idNotificacion) {
        Notificacion notificacion = notificacionRepository.findById(idNotificacion).orElse(null);
        if (notificacion != null) {
            notificacion.enviarNotificacion();
            notificacionRepository.save(notificacion);
        }
    }
}
