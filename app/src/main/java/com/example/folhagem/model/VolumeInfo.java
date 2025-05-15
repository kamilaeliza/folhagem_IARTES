package com.example.folhagem.model;

import java.util.List;
import java.util.Map;

public class VolumeInfo {
    private String title;
    private List<String> authors;
    private Map<String, String> imageLinks;
    private String description; // ✅ Adiciona este campo

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public Map<String, String> getImageLinks() {
        return imageLinks;
    }

    public String getDescription() {
        return description;
    }
}
