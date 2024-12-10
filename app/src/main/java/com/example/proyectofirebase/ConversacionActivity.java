package com.example.proyectofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ConversacionActivity extends AppCompatActivity {

    private EditText messageField;
    private Button sendButton, contactosButton, logoutButton;
    private LinearLayout messageContainer;
    private FirebaseFirestore db;
    private String emailUsuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversacion);

        // Inicializamos las vistas
        messageField = findViewById(R.id.messageField);
        sendButton = findViewById(R.id.sendButton);
        messageContainer = findViewById(R.id.messageContainer);
        contactosButton = findViewById(R.id.contactosButton);
        logoutButton = findViewById(R.id.logoutButton);

        db = FirebaseFirestore.getInstance();

        // Obtener el email del usuario que inició sesión
        emailUsuarioActual = getIntent().getStringExtra("emailUsuario");

        // Configuramos el botón de enviar
        sendButton.setOnClickListener(view -> enviarMensaje());

        // Configuramos el botón de contactos
        contactosButton.setOnClickListener(view -> abrirContactos());

        // Configuramos el botón de cerrar sesión
        logoutButton.setOnClickListener(view -> cerrarSesion());
    }

    // Método para enviar el mensaje
    private void enviarMensaje() {
        String mensaje = messageField.getText().toString();
        if (!mensaje.isEmpty()) {
            // Crear el mensaje en la colección de mensajes
            db.collection("mensajes")
                    .add(new Mensaje(mensaje, emailUsuarioActual, "receptor_email", System.currentTimeMillis()))
                    .addOnSuccessListener(documentReference -> {
                        // Limpiar el campo de mensaje
                        messageField.setText("");
                        // Agregar el mensaje a la vista
                        agregarMensajeALaVista(mensaje);
                    })
                    .addOnFailureListener(e -> Toast.makeText(ConversacionActivity.this, "Error al enviar el mensaje", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Por favor escribe un mensaje", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para agregar el mensaje a la vista
    private void agregarMensajeALaVista(String mensaje) {
        // Creamos un nuevo TextView para el mensaje
        TextView mensajeView = new TextView(this);
        mensajeView.setText(mensaje);
        mensajeView.setPadding(8, 8, 8, 8);
        mensajeView.setTextSize(16);
        messageContainer.addView(mensajeView);
    }

    // Método para abrir la pantalla de contactos
    private void abrirContactos() {
        Intent intent = new Intent(this, ContactosActivity.class);  // Asegúrate de tener una actividad ContactosActivity
        startActivity(intent);
    }

    // Método para cerrar sesión
    private void cerrarSesion() {
        // Implementar la lógica de cerrar sesión, como eliminar el usuario de la sesión o hacer logout en Firebase
        Intent intent = new Intent(this, MainActivity.class);  // Asegúrate de tener una actividad LoginActivity
        startActivity(intent);
        finish();  // Cierra la actividad actual
    }
}
