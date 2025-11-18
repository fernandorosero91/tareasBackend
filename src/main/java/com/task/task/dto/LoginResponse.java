package com.task.task.dto;

import com.task.task.model.Usuario;

public class LoginResponse {
    private String token;
    private String tipo = "Bearer";
    private Usuario usuario;

    public LoginResponse() {
    }

    public LoginResponse(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
