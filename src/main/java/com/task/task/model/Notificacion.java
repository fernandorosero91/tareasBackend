package com.task.task.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "notificaciones")
public class Notificacion {
    @Id
    private String id;
    private String mensaje;
    private Usuario destinatario;
    private Date fechaEnvio;
    private TipoNotificacion tipo;

    public Notificacion() {
    }

    public Notificacion(String mensaje, Usuario destinatario, Date fechaEnvio, TipoNotificacion tipoNotificacion) {
        this.mensaje = mensaje;
        this.destinatario = destinatario;
        this.fechaEnvio = fechaEnvio;
        this.tipo = tipoNotificacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacion tipo) {
        this.tipo = tipo;
    }

    public void enviarNotificacion() {
        this.fechaEnvio = new Date();
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "id=" + id +
                ", mensaje='" + mensaje + '\'' +
                ", destinatario=" + destinatario +
                ", fechaEnvio=" + fechaEnvio +
                ", tipo=" + tipo +
                '}';
    }
}
