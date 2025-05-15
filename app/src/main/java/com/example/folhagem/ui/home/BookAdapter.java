package com.example.folhagem.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.folhagem.R;
import com.example.folhagem.model.BookItem;
import com.example.folhagem.model.VolumeInfo;
import com.example.folhagem.ui.detalhes.DetalheLivroActivity;

import java.util.*;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<BookItem> books = new ArrayList<>();

    public void setBooks(List<BookItem> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title, authors;
        ImageView cover;

        BookViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title_text);
            authors = view.findViewById(R.id.authors_text);
            cover = view.findViewById(R.id.cover_image);
        }
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookItem item = books.get(position);
        VolumeInfo info = item.getVolumeInfo();

        String title = info.getTitle() != null ? info.getTitle() : "Sem título";
        String authorText = (info.getAuthors() != null && !info.getAuthors().isEmpty())
                ? String.join(", ", info.getAuthors())
                : "Autor desconhecido";
        String description = info.getDescription() != null ? info.getDescription() : "Sem descrição disponível";

        String rawUrl = info.getImageLinks() != null ? info.getImageLinks().get("thumbnail") : null;
        String imageUrl = (rawUrl != null && rawUrl.startsWith("http://"))
                ? rawUrl.replace("http://", "https://")
                : rawUrl;

        holder.title.setText(title);
        holder.authors.setText(authorText);

        if (imageUrl != null) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_book_placeholder)
                    .error(R.drawable.ic_book_placeholder)
                    .into(holder.cover);
        } else {
            holder.cover.setImageResource(R.drawable.ic_book_placeholder);
        }

        // Proteção total contra crash ao abrir detalhes
        holder.itemView.setOnClickListener(v -> {
            try {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetalheLivroActivity.class);
                intent.putExtra("titulo", title);
                intent.putExtra("autores", authorText);
                intent.putExtra("descricao", description);
                intent.putExtra("imagem", imageUrl != null ? imageUrl : "");
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(holder.itemView.getContext(), "Erro ao abrir detalhes do livro", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
