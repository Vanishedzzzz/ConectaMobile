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
import com.google.firebase.firestore.DocumentSnapshot;

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

        // Cargar los mensajes desde Firestore
        cargarMensajes();

        // Configuramos el botón de enviar
        sendButton.setOnClickListener(view -> enviarMensaje());

        // Configuramos el botón de contactos
        contactosButton.setOnClickListener(view -> abrirContactos());

        // Configuramos el botón de cerrar sesión
        logoutButton.setOnClickListener(view -> cerrarSesion());
    }

    private void enviarMensaje() {
        String mensaje = messageField.getText().toString();
        if (!mensaje.isEmpty()) {
            // Asegúrate de pasar todos los datos correctamente
            Mensaje mensajeNuevo = new Mensaje(mensaje, emailUsuarioActual, emailUsuarioActual, System.currentTimeMillis());

            db.collection("mensajes").add(mensajeNuevo)
                    .addOnSuccessListener(documentReference -> {
                        messageField.setText(""); // Limpiar el campo de mensaje
                        cargarMensajes(); // Recargar los mensajes
                    })
                    .addOnFailureListener(e -> Toast.makeText(ConversacionActivity.this, "Error al enviar el mensaje", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Por favor escribe un mensaje", Toast.LENGTH_SHORT).show();
        }
    }




    // Método para agregar el mensaje a la vista
    private void agregarMensajeALaVista(String mensaje, String autor) {
        // Creamos un nuevo TextView para el mensaje
        TextView mensajeView = new TextView(this);
        mensajeView.setText(autor.equals("yo") ? "Yo: " + mensaje : autor + ": " + mensaje);
        mensajeView.setPadding(8, 8, 8, 8);
        mensajeView.setTextSize(16);
        messageContainer.addView(mensajeView);
    }

    // Método para cargar los mensajes desde Firestore
    private void cargarMensajes() {
        // Limpiar el contenedor antes de recargar los mensajes
        messageContainer.removeAllViews();

        db.collection("mensajes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String mensaje = documentSnapshot.getString("texto");
                        String emisor = documentSnapshot.getString("nombreUsuario");  // Usar el campo "nombreUsuario"
                        if (mensaje != null && emisor != null) {
                            mostrarMensajeConEtiqueta(mensaje, emisor);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(ConversacionActivity.this, "Error al cargar los mensajes", Toast.LENGTH_SHORT).show());
    }


    // Método para mostrar los mensajes con la etiqueta "yo" o "amigo"
    private void mostrarMensajeConEtiqueta(String mensaje, String emisor) {
        // Verificamos si el emisor es null antes de comparar
        if (emisor != null && emisor.equals(emailUsuarioActual)) {
            agregarMensajeALaVista(mensaje, "yo");
        } else {
            agregarMensajeALaVista(mensaje, "amigo");
        }
    }

    // Método para abrir la pantalla de contactos
    private void abrirContactos() {
        Intent intent = new Intent(this, ContactosActivity.class);  // Asegúrate de tener una actividad ContactosActivity
        startActivity(intent);
    }

    // Método para cerrar sesión
    private void cerrarSesion() {
        // Implementar la lógica de cerrar sesión, como eliminar el usuario de la sesión o hacer logout en Firebase
        Intent intent = new Intent(this, MainActivity.class);  // Asegúrate de tener una actividad MainActivity o LoginActivity
        startActivity(intent);
        finish();  // Cierra la actividad actual
    }
}
