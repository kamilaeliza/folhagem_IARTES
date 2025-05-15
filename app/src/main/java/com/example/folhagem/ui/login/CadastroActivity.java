package com.example.folhagem.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.folhagem.R;
import com.example.folhagem.database.DatabaseHelper;
import com.example.folhagem.database.UsuarioDAO;
import com.example.folhagem.model.UsuarioEntity;

public class CadastroActivity extends AppCompatActivity {

    private EditText inputNome, inputEmail, inputSenha;
    private Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inputNome = findViewById(R.id.input_nome);
        inputEmail = findViewById(R.id.input_email);
        inputSenha = findViewById(R.id.input_senha);
        btnCadastrar = findViewById(R.id.btn_cadastrar);

        btnCadastrar.setOnClickListener(v -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {
        String nome = inputNome.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String senha = inputSenha.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        UsuarioDAO usuarioDAO = new UsuarioDAO(dbHelper.getWritableDatabase());

        if (usuarioDAO.buscarUsuarioPorEmail(email) != null) {
            Toast.makeText(this, "Email já cadastrado", Toast.LENGTH_SHORT).show();
            return;
        }

        UsuarioEntity novoUsuario = new UsuarioEntity(nome, email, senha);
        usuarioDAO.inserirUsuario(novoUsuario);

        Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
