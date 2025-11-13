package com.task.task.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "proyectos")
public class Proyecto {
    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private Usuario responsable;
    private Date fechaCreacion;
    private Date fechaFinalizacion;
    private List<Tarea> tareas;

    public Proyecto() {
        this.tareas = new ArrayList<>();
    }

    public Proyecto(String nombre, String descripcion, Usuario responsable, Date fechaCreacion, Date fechaFinalizacion, List<Tarea> tareas) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.responsable = responsable;
        this.fechaCreacion = fechaCreacion;
        this.fechaFinalizacion = fechaFinalizacion;
        this.tareas = tareas != null ? tareas : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public void editarProyecto(String nombre, String descripcion, Usuario responsable, Date fechaFinalizacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.responsable = responsable;
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public void eliminarProyecto() {
        this.tareas.clear();
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", responsable=" + responsable +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaFinalizacion=" + fechaFinalizacion +
                '}';
    }
}
