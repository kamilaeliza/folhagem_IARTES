package com.example.folhagem.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.folhagem.MainActivity;
import com.example.folhagem.R;
import com.example.folhagem.database.DatabaseHelper;
import com.example.folhagem.database.UsuarioDAO;
import com.example.folhagem.model.UsuarioEntity;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputSenha;
    private Button btnLogin;
    private TextView linkCriarConta, linkEsqueciSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.input_email);
        inputSenha = findViewById(R.id.input_senha);
        btnLogin = findViewById(R.id.btn_login);
        linkCriarConta = findViewById(R.id.link_criar_conta);
        linkEsqueciSenha = findViewById(R.id.link_esqueci_senha);

        btnLogin.setOnClickListener(v -> fazerLogin());

        linkCriarConta.setOnClickListener(v -> {
            startActivity(new Intent(this, CadastroActivity.class));
        });

        linkEsqueciSenha.setOnClickListener(v -> {
            startActivity(new Intent(this, RecuperarSenhaActivity.class));
        });

        // Se já estiver logado, vai direto para a Home
        SharedPreferences prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE);
        if (prefs.contains("id")) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void fazerLogin() {
        String email = inputEmail.getText().toString().trim();
        String senha = inputSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        UsuarioDAO usuarioDAO = new UsuarioDAO(dbHelper.getReadableDatabase());

        UsuarioEntity usuario = usuarioDAO.buscarUsuarioPorEmailSenha(email, senha);

        if (usuario != null) {
            // ✅ Salva todos os dados no SharedPreferences
            SharedPreferences prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("id", usuario.getId());
            editor.putString("nome", usuario.getNome());
            editor.putString("email", usuario.getEmail());
            if (usuario.getFoto() != null) {
                editor.putString("foto", usuario.getFoto());
            }
            editor.apply();

            Toast.makeText(this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else {
            Toast.makeText(this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show();
        }
    }
}
