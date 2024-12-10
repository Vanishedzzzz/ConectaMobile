package com.example.proyectofirebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ContactosActivity extends AppCompatActivity {

    private LinearLayout contactosLayout;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactos);

        // Inicializamos las vistas
        contactosLayout = findViewById(R.id.contactosLayout);
        db = FirebaseFirestore.getInstance();

        // Obtener los contactos de la base de datos
        obtenerContactos();
    }

    private void obtenerContactos() {
        // Obtener los contactos de la colección "usuarios"
        db.collection("usuarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Comprobar si se obtuvo la lista de usuarios
                    if (queryDocumentSnapshots != null) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) { // Cambiado var a DocumentSnapshot
                            String email = document.getString("email"); // Asegúrate de que "email" existe en la base de datos
                            String nombre = document.getString("usuario"); // Asegúrate de que "nombre" existe en la base de datos

                            // Asegurarse de que el nombre no sea null
                            if (nombre != null) {
                                agregarContactoALaVista(nombre, email);
                            } else {
                                // Si no se encuentra el nombre, usar el email
                                agregarContactoALaVista(email, email);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ContactosActivity.this, "Error al obtener los contactos", Toast.LENGTH_SHORT).show();
                });
    }


    // Método para agregar un contacto a la vista
    private void agregarContactoALaVista(String nombre, String email) {
        // Inflar el layout del item de contacto
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contactoView = inflater.inflate(R.layout.item_usuario, null);

        // Obtener referencia del TextView donde se muestra el nombre
        TextView txtNombre = contactoView.findViewById(R.id.txtNombre);

        // Establecer el nombre del usuario en el TextView
        txtNombre.setText(nombre);

        // Agregar el item de contacto al LinearLayout principal
        LinearLayout contactosLayout = findViewById(R.id.contactosLayout);
        contactosLayout.addView(contactoView);
    }

    // Método para volver atrás
    public void volverAlChat(View view) {
        // Inicia la actividad de conversación (Chat)
        Intent intent = new Intent(ContactosActivity.this, ConversacionActivity.class);
        startActivity(intent);
        finish();  // Opcional, para asegurarse de que la actividad actual se cierre
    }

    // Método para abrir la actividad de registro (RegisterActivity)
    public void abrirRegistro(View view) {
        // Inicia la actividad de registro para agregar un nuevo contacto
        Intent intent = new Intent(ContactosActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

}
