package com.example.folhagem.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.folhagem.model.LivroEntity;

import java.util.ArrayList;
import java.util.List;

public class EstanteDAO {

    private final SQLiteDatabase db;

    public EstanteDAO(SQLiteDatabase db) {
        this.db = db;
    }

    // ➕ Inserir novo livro
    public void inserirLivro(LivroEntity livro) {
        ContentValues values = new ContentValues();
        values.put("titulo", livro.getTitulo());
        values.put("autores", livro.getAutores());
        values.put("descricao", livro.getDescricao());
        values.put("imagem_url", livro.getImagemUrl());
        values.put("status", livro.getStatus());

        db.insert("estante", null, values);
    }

    // ❌ Remover livro por ID
    public void removerLivroPorId(int id) {
        db.delete("estante", "id = ?", new String[]{String.valueOf(id)});
    }

    // 🔍 Buscar livro por título
    public LivroEntity buscarLivroPorTitulo(String titulo) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM estante WHERE titulo = ?",
                new String[]{titulo}
        );

        LivroEntity livro = null;
        if (cursor.moveToFirst()) {
            livro = criarLivroDoCursor(cursor);
        }

        cursor.close();
        return livro;
    }

    // 🔁 Atualizar status de leitura
    public void atualizarStatus(int id, String novoStatus) {
        ContentValues values = new ContentValues();
        values.put("status", novoStatus);

        db.update("estante", values, "id = ?", new String[]{String.valueOf(id)});
    }

    // 📚 Buscar todos os livros
    public List<LivroEntity> listarTodos() {
        List<LivroEntity> lista = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM estante", null);

        while (cursor.moveToNext()) {
            lista.add(criarLivroDoCursor(cursor));
        }

        cursor.close();
        return lista;
    }

    // 🔢 Contar livros na estante
    public int contarLivros() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM estante", null);
        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    // 🔁 Método auxiliar para criar LivroEntity
    private LivroEntity criarLivroDoCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
        String autores = cursor.getString(cursor.getColumnIndexOrThrow("autores"));
        String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
        String imagemUrl = cursor.getString(cursor.getColumnIndexOrThrow("imagem_url"));
        String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

        LivroEntity livro = new LivroEntity(titulo, autores, descricao, imagemUrl);
        livro.setId(id);
        livro.setStatus(status);
        return livro;
    }
}
