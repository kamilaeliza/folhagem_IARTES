package com.example.folhagem.model;

public class ResenhaEntity {
    private int id;
    private int livroId;
    private float nota;
    private String comentario;

    // Construtor completo
    public ResenhaEntity(int id, int livroId, float nota, String comentario) {
        this.id = id;
        this.livroId = livroId;
        this.nota = nota;
        this.comentario = comentario;
    }

    // Construtor sem ID (para inserção)
    public ResenhaEntity(int livroId, float nota, String comentario) {
        this(0, livroId, nota, comentario);
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getLivroId() {
        return livroId;
    }

    public float getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }
}
