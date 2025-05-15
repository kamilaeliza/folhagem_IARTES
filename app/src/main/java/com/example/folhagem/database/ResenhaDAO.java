package com.example.folhagem.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.folhagem.model.ResenhaEntity;

import java.util.ArrayList;
import java.util.List;

public class ResenhaDAO {
    private final SQLiteDatabase db;

    public ResenhaDAO(SQLiteDatabase db) {
        this.db = db;
    }

    // ➕ Inserir nova resenha
    public void inserirResenha(ResenhaEntity resenha) {
        ContentValues values = new ContentValues();
        values.put("livro_id", resenha.getLivroId());
        values.put("nota", resenha.getNota());
        values.put("comentario", resenha.getComentario());
        db.insert("resenha", null, values);
    }

    // ✏️ Atualizar resenha existente
    public void atualizarResenha(ResenhaEntity resenha) {
        ContentValues values = new ContentValues();
        values.put("nota", resenha.getNota());
        values.put("comentario", resenha.getComentario());

        db.update("resenha", values, "livro_id = ?", new String[]{String.valueOf(resenha.getLivroId())});
    }

    // 🔍 Buscar resenha por ID do livro
    public ResenhaEntity buscarResenhaPorLivroId(int livroId) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM resenha WHERE livro_id = ?",
                new String[]{String.valueOf(livroId)}
        );

        ResenhaEntity resenha = null;

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            float nota = cursor.getFloat(cursor.getColumnIndexOrThrow("nota"));
            String comentario = cursor.getString(cursor.getColumnIndexOrThrow("comentario"));

            resenha = new ResenhaEntity(id, livroId, nota, comentario);
        }

        cursor.close();
        return resenha;
    }

    // ✅ Listar todas as resenhas salvas no banco
    public List<ResenhaEntity> listarResenhas() {
        List<ResenhaEntity> lista = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM resenha", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            int livroId = cursor.getInt(cursor.getColumnIndexOrThrow("livro_id"));
            float nota = cursor.getFloat(cursor.getColumnIndexOrThrow("nota"));
            String comentario = cursor.getString(cursor.getColumnIndexOrThrow("comentario"));

            lista.add(new ResenhaEntity(id, livroId, nota, comentario));
        }

        cursor.close();
        return lista;
    }
}
