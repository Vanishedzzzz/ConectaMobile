
package com.example.proyectofirebase;

public class Mensaje {
    private String texto;
    private String nombreUsuario;
    private String emailUsuario;
    private long timestamp;

    public Mensaje(String texto, String nombreUsuario, String emailUsuario, long timestamp) {
        this.texto = texto;
        this.nombreUsuario = nombreUsuario;
        this.emailUsuario = emailUsuario;
        this.timestamp = timestamp;
    }

    // Métodos getters y setters
    public String getTexto() {
        return texto;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public long getTimestamp() {
        return timestamp;
    }
}


