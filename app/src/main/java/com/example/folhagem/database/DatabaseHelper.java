package com.example.folhagem.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "folhagem.db";
    private static final int DATABASE_VERSION = 4; // atualizado para refletir nova coluna

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela da estante
        db.execSQL("CREATE TABLE estante (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT NOT NULL, " +
                "autores TEXT, " +
                "descricao TEXT, " +
                "imagem_url TEXT, " +
                "status TEXT DEFAULT 'Quero ler'" +
                ");");

        // Tabela de resenhas
        db.execSQL("CREATE TABLE resenha (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "livro_id INTEGER NOT NULL, " +
                "nota REAL, " +
                "comentario TEXT, " +
                "FOREIGN KEY(livro_id) REFERENCES estante(id)" +
                ");");

        // Tabela de usuários com campo de foto
        db.execSQL("CREATE TABLE usuario (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "email TEXT NOT NULL UNIQUE, " +
                "senha TEXT NOT NULL, " +
                "foto TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // ⚠️ Em produção, usar migração com ALTER TABLE em vez de apagar dados
        db.execSQL("DROP TABLE IF EXISTS estante");
        db.execSQL("DROP TABLE IF EXISTS resenha");
        db.execSQL("DROP TABLE IF EXISTS usuario");
        onCreate(db);
    }
}
