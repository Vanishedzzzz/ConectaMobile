
package com.example.proyectofirebase;

public class Mensaje {
    private String texto;
    private String emailUsuario;
    private String nombreUsuario;
    private long timestamp;

    // Constructor requerido para Firestore
    public Mensaje() {}

    public Mensaje(String texto, String emailUsuario, String nombreUsuario, long timestamp) {
        this.texto = texto;
        this.emailUsuario = emailUsuario;
        this.nombreUsuario = nombreUsuario;
        this.timestamp = timestamp;
    }

    public String getTexto() {
        return texto;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
