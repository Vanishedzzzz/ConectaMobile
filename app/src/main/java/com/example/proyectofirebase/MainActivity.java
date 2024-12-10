package com.example.proyectofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private EditText txtEmail, txtPassword;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Asegúrate de usar el layout correcto

        // Inicializamos la referencia de FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        // Inicializamos los campos del formulario
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtContra);
    }

    // Método para iniciar sesión
    public void loginUser(View view) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        // Validamos que los campos no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Por favor ingrese correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Consultamos Firestore para encontrar el usuario con el correo ingresado
        db.collection("usuarios")  // Asegúrate de que tu colección de usuarios se llame "usuarios"
                .whereEqualTo("email", email)  // Comparamos por el correo electrónico
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Si se encuentra el usuario
                        boolean userFound = false;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String storedPassword = document.getString("contraseña");

                            // Comparamos la contraseña ingresada con la almacenada
                            if (storedPassword != null && storedPassword.equals(password)) {
                                userFound = true;
                                Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                                // Redirigimos al usuario a la pantalla de conversación
                                Intent intent = new Intent(MainActivity.this, ConversacionActivity.class);
                                startActivity(intent);
                                finish();  // Finaliza la actividad para que no pueda regresar con el botón atrás
                                break;
                            }
                        }

                        // Si el usuario no fue encontrado o las contraseñas no coinciden
                        if (!userFound) {
                            Toast.makeText(MainActivity.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para ir a la pantalla de registro
    public void goToRegister(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
