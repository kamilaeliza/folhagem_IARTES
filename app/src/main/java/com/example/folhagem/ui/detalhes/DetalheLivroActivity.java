package com.example.folhagem.ui.detalhes;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.folhagem.R;
import com.example.folhagem.database.DatabaseHelper;
import com.example.folhagem.database.EstanteDAO;
import com.example.folhagem.database.ResenhaDAO;
import com.example.folhagem.model.LivroEntity;
import com.example.folhagem.model.ResenhaEntity;

public class DetalheLivroActivity extends AppCompatActivity {

    ImageView imageCapa;
    TextView textTitulo, textAutores, textDescricao, textVerMais, textResenha;
    RatingBar ratingBar;
    Button btnAdicionar;

    boolean descricaoExpandida = false;
    String descricaoCompleta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_livro);

        imageCapa = findViewById(R.id.image_capa);
        textTitulo = findViewById(R.id.text_titulo);
        textAutores = findViewById(R.id.text_autores);
        textDescricao = findViewById(R.id.text_descricao);
        textVerMais = findViewById(R.id.text_ver_mais);
        textResenha = findViewById(R.id.text_resenha);
        ratingBar = findViewById(R.id.rating_detalhe);
        btnAdicionar = findViewById(R.id.btn_adicionar_estante);

        // Receber dados do Intent e marcar como final
        final String titulo = getIntent().getStringExtra("titulo");
        final String autores = getIntent().getStringExtra("autores");
        descricaoCompleta = getIntent().getStringExtra("descricao"); // não precisa ser final (não usada na lambda)
        final String imagemUrlRaw = getIntent().getStringExtra("imagem");

        textTitulo.setText(titulo);
        textAutores.setText(autores);
        textDescricao.setText(descricaoCompleta);

        String imagemUrl = imagemUrlRaw;
        if (imagemUrl != null && imagemUrl.startsWith("http://")) {
            imagemUrl = imagemUrl.replace("http://", "https://");
        }

        if (imagemUrl != null && !imagemUrl.isEmpty()) {
            Glide.with(this).load(imagemUrl).into(imageCapa);
        } else {
            imageCapa.setImageResource(R.drawable.ic_book_placeholder);
        }

        textVerMais.setOnClickListener(v -> toggleDescricao());

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ResenhaDAO resenhaDAO = new ResenhaDAO(dbHelper.getReadableDatabase());
        EstanteDAO estanteDAO = new EstanteDAO(dbHelper.getWritableDatabase());

        // livroSalvo também final
        final LivroEntity livroSalvo = estanteDAO.buscarLivroPorTitulo(titulo);

        if (livroSalvo != null) {
            ResenhaEntity resenha = resenhaDAO.buscarResenhaPorLivroId(livroSalvo.getId());
            if (resenha != null) {
                ratingBar.setRating(resenha.getNota());
                textResenha.setText(resenha.getComentario());
            } else {
                ratingBar.setRating(0f);
                textResenha.setText("Nenhuma resenha.");
            }
        }

        // Botão adicionar com todas as variáveis finais e this corrigido
        final String finalImagemUrl = imagemUrl; // marcar como final para uso na lambda
        btnAdicionar.setOnClickListener(v -> {
            final LivroEntity livro = new LivroEntity(titulo, autores, descricaoCompleta, finalImagemUrl);

            if (livroSalvo != null) {
                Toast.makeText(DetalheLivroActivity.this, "Esse livro já está na estante!", Toast.LENGTH_SHORT).show();
            } else {
                estanteDAO.inserirLivro(livro);
                Toast.makeText(DetalheLivroActivity.this, "Livro adicionado à estante!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void toggleDescricao() {
        if (descricaoExpandida) {
            textDescricao.setMaxLines(5);
            textDescricao.setEllipsize(TextUtils.TruncateAt.END);
            textVerMais.setText("Mais");
        } else {
            textDescricao.setMaxLines(Integer.MAX_VALUE);
            textDescricao.setEllipsize(null);
            textVerMais.setText("Menos");
        }
        descricaoExpandida = !descricaoExpandida;
    }
}
