package com.example.folhagem.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.folhagem.R;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        inputEmail = findViewById(R.id.input_email_recuperar);
        btnEnviar = findViewById(R.id.btn_enviar_email);

        btnEnviar.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Informe seu email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simulação de envio de email
            Toast.makeText(this, "Instruções enviadas para: " + email, Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
