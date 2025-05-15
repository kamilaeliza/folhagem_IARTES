package com.example.folhagem.ui.perfil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.folhagem.R;
import com.example.folhagem.database.DatabaseHelper;
import com.example.folhagem.database.UsuarioDAO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditarPerfilActivity extends AppCompatActivity {

    private static final int REQUEST_GALERIA = 1001;

    private ImageView imagePerfil;
    private EditText inputNome, inputEmail;
    private Button btnTrocarFoto, btnSalvar;

    private String caminhoFotoSalva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        imagePerfil = findViewById(R.id.image_editar_perfil);
        inputNome = findViewById(R.id.input_nome);
        inputEmail = findViewById(R.id.input_email);
        btnTrocarFoto = findViewById(R.id.btn_trocar_foto);
        btnSalvar = findViewById(R.id.btn_salvar);

        SharedPreferences prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE);
        String nomeSalvo = prefs.getString("nome", "");
        String emailSalvo = prefs.getString("email", "");
        String fotoSalva = prefs.getString("foto", null);

        inputNome.setText(nomeSalvo);
        inputEmail.setText(emailSalvo);
        caminhoFotoSalva = fotoSalva;

        if (fotoSalva != null) {
            File file = new File(fotoSalva);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(fotoSalva);
                imagePerfil.setImageBitmap(bitmap);
            }
        }

        btnTrocarFoto.setOnClickListener(v -> abrirGaleria());
        btnSalvar.setOnClickListener(v -> salvarPerfil());
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALERIA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALERIA && resultCode == RESULT_OK && data != null) {
            Uri imagemSelecionada = data.getData();
            if (imagemSelecionada != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imagemSelecionada);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imagePerfil.setImageBitmap(bitmap);

                    File file = new File(getFilesDir(), "foto_perfil.jpg");
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }

                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                    fos.close();

                    caminhoFotoSalva = file.getAbsolutePath();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void salvarPerfil() {
        String nome = inputNome.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Preencha nome e e-mail", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("usuario_prefs", MODE_PRIVATE);
        int userId = prefs.getInt("id", -1);

        if (userId == -1) {
            Toast.makeText(this, "Erro: usuário não identificado", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Atualiza no banco
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        UsuarioDAO usuarioDAO = new UsuarioDAO(dbHelper.getWritableDatabase());
        usuarioDAO.atualizarNomeEmail(userId, nome, email);

        if (caminhoFotoSalva != null) {
            usuarioDAO.atualizarFoto(userId, caminhoFotoSalva);
        }

        // ✅ Atualiza também no SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nome", nome);
        editor.putString("email", email);

        if (caminhoFotoSalva != null) {
            editor.putString("foto", caminhoFotoSalva);
        }

        editor.apply();

        Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
