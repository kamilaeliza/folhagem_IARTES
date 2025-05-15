package com.example.folhagem.model;

public class LivroEntity {

    private int id;
    private String titulo;
    private String autores;
    private String descricao;
    private String imagemUrl;
    private String status;

    // Construtor padrão sem ID (usado ao inserir no banco)
    public LivroEntity(String titulo, String autores, String descricao, String imagemUrl) {
        this.titulo = titulo;
        this.autores = autores;
        this.descricao = descricao;
        this.imagemUrl = imagemUrl;
        this.status = "Quero ler"; // valor padrão
    }

    // Construtor completo com ID (opcional se quiser usar em DAOs)
    public LivroEntity(int id, String titulo, String autores, String descricao, String imagemUrl, String status) {
        this.id = id;
        this.titulo = titulo;
        this.autores = autores;
        this.descricao = descricao;
        this.imagemUrl = imagemUrl;
        this.status = status;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutores() {
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
