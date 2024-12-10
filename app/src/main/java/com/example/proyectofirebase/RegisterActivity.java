package com.example.proyectofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtUsuario, txtEmail, txtContra, txtConfirmContra;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrarse); // Asegúrate de usar el layout correcto

        db = FirebaseFirestore.getInstance();

        // Inicializar los componentes de la interfaz
        txtUsuario = findViewById(R.id.txtUsuario);
        txtEmail = findViewById(R.id.txtEmail);
        txtContra = findViewById(R.id.txtContra);
        txtConfirmContra = findViewById(R.id.txtConfirmContra);
    }

    // Método para enviar datos a Firestore
    public void registerUser(View view) {
        // Obtenemos los campos ingresados en el formulario
        String usuario = txtUsuario.getText().toString();
        String email = txtEmail.getText().toString();
        String contraseña = txtContra.getText().toString();
        String confirmarContraseña = txtConfirmContra.getText().toString();

        // Validar que todos los campos están llenos
        if (usuario.isEmpty() || email.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que las contraseñas coincidan
        if (!contraseña.equals(confirmarContraseña)) {
            Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un mapa con los datos del usuario
        Map<String, Object> usuarioData = new HashMap<>();
        usuarioData.put("usuario", usuario);
        usuarioData.put("email", email);
        usuarioData.put("contraseña", contraseña);

        // Enviar los datos a Firestore
        db.collection("usuarios")
                .document(usuario)  // Usamos el nombre de usuario como ID del documento
                .set(usuarioData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Datos enviados a Firestore correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Error al enviar datos a Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Método para ir al login
    public void goToLogin(View view) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
