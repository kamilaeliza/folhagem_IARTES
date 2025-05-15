package com.example.folhagem.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.folhagem.model.UsuarioEntity;

public class UsuarioDAO {

    private final SQLiteDatabase db;

    public UsuarioDAO(SQLiteDatabase db) {
        this.db = db;
    }

    // 🔍 Login: buscar por email e senha
    public UsuarioEntity buscarUsuarioPorEmailSenha(String email, String senha) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuario WHERE email = ? AND senha = ?",
                new String[]{email, senha}
        );

        UsuarioEntity usuario = null;

        if (cursor.moveToFirst()) {
            usuario = criarUsuarioDoCursor(cursor);
        }

        cursor.close();
        return usuario;
    }

    // 🔍 Buscar por email (para evitar duplicidade)
    public UsuarioEntity buscarUsuarioPorEmail(String email) {
        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuario WHERE email = ?",
                new String[]{email}
        );

        UsuarioEntity usuario = null;

        if (cursor.moveToFirst()) {
            usuario = criarUsuarioDoCursor(cursor);
        }

        cursor.close();
        return usuario;
    }

    // ➕ Inserir novo usuário
    public void inserirUsuario(UsuarioEntity usuario) {
        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());

        // Só salva a foto se não for nula
        if (usuario.getFoto() != null) {
            values.put("foto", usuario.getFoto());
        }

        db.insert("usuario", null, values);
    }

    // ✏️ Atualizar nome e email
    public void atualizarNomeEmail(int id, String novoNome, String novoEmail) {
        ContentValues values = new ContentValues();
        values.put("nome", novoNome);
        values.put("email", novoEmail);
        db.update("usuario", values, "id = ?", new String[]{String.valueOf(id)});
    }

    // ✏️ Atualizar foto de perfil
    public void atualizarFoto(int id, String caminhoFoto) {
        ContentValues values = new ContentValues();
        values.put("foto", caminhoFoto);
        db.update("usuario", values, "id = ?", new String[]{String.valueOf(id)});
    }

    // 🔁 Criar objeto UsuarioEntity a partir do cursor
    private UsuarioEntity criarUsuarioDoCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
        String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
        String senha = cursor.getString(cursor.getColumnIndexOrThrow("senha"));

        // Se a coluna "foto" existe e não for nula
        String foto = null;
        int fotoIndex = cursor.getColumnIndex("foto");
        if (fotoIndex != -1 && !cursor.isNull(fotoIndex)) {
            foto = cursor.getString(fotoIndex);
        }

        return new UsuarioEntity(id, nome, email, senha, foto);
    }
}
