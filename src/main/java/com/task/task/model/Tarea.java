package com.task.task.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Document(collection = "tareas")
public class Tarea {
    @Id
    private String id;
    private String titulo;
    private String descripcion;
    private Usuario responsable;
    
    @JsonIgnoreProperties({"tareas"})
    private Proyecto proyecto;
    private Date fechaInicio;
    private Date fechaEntrega;
    private EstadoTarea estado;

    public Tarea() {
    }

    public Tarea(String titulo, String descripcion, Usuario responsable, Proyecto proyecto, Date fechaInicio, Date fechaEntrega, EstadoTarea estado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.responsable = responsable;
        this.proyecto = proyecto;
        this.fechaInicio = fechaInicio;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }

    public void cambiarUsuario(Usuario usuario) {
        this.responsable = usuario;
    }

    public void editarTarea(String titulo, String descripcion, Date fechaEntrega, EstadoTarea estado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
    }

    public void eliminarTarea() {
        this.responsable = null;
        this.proyecto = null;
    }

    public boolean estaRetrasada() {
        if (fechaEntrega == null || estado == EstadoTarea.COMPLETADA) {
            return false;
        }
        return new Date().after(fechaEntrega);
    }

    public int diasRestantes() {
        if (fechaEntrega == null) {
            return 0;
        }
        long diff = fechaEntrega.getTime() - new Date().getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", responsable=" + responsable +
                ", fechaInicio=" + fechaInicio +
                ", fechaEntrega=" + fechaEntrega +
                ", estado=" + estado +
                '}';
    }
}
