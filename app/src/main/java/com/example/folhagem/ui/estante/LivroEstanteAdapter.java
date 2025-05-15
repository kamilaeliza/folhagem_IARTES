package com.example.folhagem.ui.estante;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.folhagem.R;
import com.example.folhagem.database.DatabaseHelper;
import com.example.folhagem.database.EstanteDAO;
import com.example.folhagem.database.ResenhaDAO;
import com.example.folhagem.model.LivroEntity;
import com.example.folhagem.model.ResenhaEntity;
import com.example.folhagem.ui.detalhes.DetalheLivroActivity;

import java.util.List;

public class LivroEstanteAdapter extends RecyclerView.Adapter<LivroEstanteAdapter.LivroViewHolder> {

    public interface OnLivroRemovidoListener {
        void onLivroRemovido();
    }

    private final List<LivroEntity> livros;
    private final OnLivroRemovidoListener removerListener;

    public LivroEstanteAdapter(List<LivroEntity> livros, OnLivroRemovidoListener listener) {
        this.livros = livros;
        this.removerListener = listener;
    }

    static class LivroViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, autores, statusText;
        ImageView capa;
        Button btnRemover, btnAvaliar;
        RatingBar ratingEstrela;
        ImageButton btnEditarStatus;

        LivroViewHolder(View view) {
            super(view);
            titulo = view.findViewById(R.id.titulo_livro);
            autores = view.findViewById(R.id.autores_livro);
            capa = view.findViewById(R.id.capa_livro);
            btnRemover = view.findViewById(R.id.btn_remover);
            btnAvaliar = view.findViewById(R.id.btn_avaliar);
            ratingEstrela = view.findViewById(R.id.rating_estrela);
            statusText = view.findViewById(R.id.text_status);
            btnEditarStatus = view.findViewById(R.id.btn_editar_status);
        }
    }

    @NonNull
    @Override
    public LivroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_livro_estante, parent, false);
        return new LivroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LivroViewHolder holder, int position) {
        LivroEntity livro = livros.get(position);
        Context context = holder.itemView.getContext();

        holder.titulo.setText(livro.getTitulo());
        holder.autores.setText(livro.getAutores());

        // Capa
        String imagemUrl = livro.getImagemUrl();
        if (imagemUrl != null && imagemUrl.startsWith("http://")) {
            imagemUrl = imagemUrl.replace("http://", "https://");
        }

        if (imagemUrl != null) {
            Glide.with(context)
                    .load(imagemUrl)
                    .placeholder(R.drawable.ic_book_placeholder)
                    .error(R.drawable.ic_book_placeholder)
                    .into(holder.capa);
        } else {
            holder.capa.setImageResource(R.drawable.ic_book_placeholder);
        }

        // Cor do status de leitura
        String status = livro.getStatus();
        int cor;

        switch (status) {
            case "Lendo":
                cor = Color.parseColor("#FFC107"); // amarelo
                break;
            case "Lido":
                cor = Color.parseColor("#4CAF50"); // verde
                break;
            default:
                cor = Color.parseColor("#1976D2"); // azul (Quero ler)
        }

        holder.statusText.setText(status);
        holder.statusText.setBackgroundTintList(ColorStateList.valueOf(cor));

        // Editar status
        holder.btnEditarStatus.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, holder.btnEditarStatus);
            popup.getMenu().add("Quero ler");
            popup.getMenu().add("Lendo");
            popup.getMenu().add("Lido");

            popup.setOnMenuItemClickListener(item -> {
                String novoStatus = item.getTitle().toString();
                livro.setStatus(novoStatus);

                // Atualizar no banco
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                EstanteDAO estanteDAO = new EstanteDAO(dbHelper.getWritableDatabase());
                estanteDAO.atualizarStatus(livro.getId(), novoStatus);

                notifyItemChanged(position);
                return true;
            });

            popup.show();
        });

        // Avaliação (resenha)
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        ResenhaDAO resenhaDAO = new ResenhaDAO(dbHelper.getReadableDatabase());
        ResenhaEntity resenha = resenhaDAO.buscarResenhaPorLivroId(livro.getId());
        holder.ratingEstrela.setRating(resenha != null ? resenha.getNota() : 0f);

        // Remover
        holder.btnRemover.setOnClickListener(v -> {
            EstanteDAO dao = new EstanteDAO(dbHelper.getWritableDatabase());
            dao.removerLivroPorId(livro.getId());
            livros.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Livro removido da estante", Toast.LENGTH_SHORT).show();
            removerListener.onLivroRemovido();
        });

        // Avaliar
        holder.btnAvaliar.setOnClickListener(v -> {
            Intent intent = new Intent(context, AvaliacaoActivity.class);
            intent.putExtra("livro_id", livro.getId());
            context.startActivity(intent);
        });

        // Ver detalhes
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalheLivroActivity.class);
            intent.putExtra("titulo", livro.getTitulo());
            intent.putExtra("autores", livro.getAutores());
            intent.putExtra("descricao", livro.getDescricao());
            intent.putExtra("imagem", livro.getImagemUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return livros.size();
    }
}
