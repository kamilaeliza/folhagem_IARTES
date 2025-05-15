package com.example.folhagem.ui.estante;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.folhagem.R;
import com.example.folhagem.database.DatabaseHelper;
import com.example.folhagem.database.ResenhaDAO;
import com.example.folhagem.model.ResenhaEntity;

public class AvaliacaoActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText editComentario;
    private Button btnSalvar;
    private int livroId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avaliacao_activity);

        ratingBar = findViewById(R.id.rating_bar);
        editComentario = findViewById(R.id.edit_comentario);
        btnSalvar = findViewById(R.id.btn_salvar_resenha);

        livroId = getIntent().getIntExtra("livro_id", -1);
        if (livroId == -1) {
            Toast.makeText(this, "Livro inválido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ResenhaDAO dao = new ResenhaDAO(dbHelper.getWritableDatabase());

        // Carrega resenha existente
        ResenhaEntity existente = dao.buscarResenhaPorLivroId(livroId);
        if (existente != null) {
            ratingBar.setRating(existente.getNota());
            editComentario.setText(existente.getComentario());
        }

        btnSalvar.setOnClickListener(v -> {
            float nota = ratingBar.getRating();
            String comentario = editComentario.getText().toString().trim();

            if (comentario.isEmpty()) {
                comentario = "Sem comentário.";
            }

            ResenhaEntity novaResenha = new ResenhaEntity(livroId, nota, comentario);
            ResenhaEntity atual = dao.buscarResenhaPorLivroId(livroId); // Recarrega para garantir

            if (atual != null) {
                dao.atualizarResenha(novaResenha);
                Toast.makeText(this, "Resenha atualizada!", Toast.LENGTH_SHORT).show();
            } else {
                dao.inserirResenha(novaResenha);
                Toast.makeText(this, "Resenha salva!", Toast.LENGTH_SHORT).show();
            }

            finish(); // Fecha e volta à estante
        });
    }
}
