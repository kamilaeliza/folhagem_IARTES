package com.example.folhagem.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.folhagem.R;
import com.example.folhagem.api.GoogleBooksApiService;
import com.example.folhagem.model.BookItem;
import com.example.folhagem.model.BookResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private BookAdapter adapter;
    private RecyclerView recyclerView;
    private EditText searchInput;
    private GoogleBooksApiService apiService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchInput = view.findViewById(R.id.search_input);
        Button searchBtn = view.findViewById(R.id.search_button);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);

        // Inicializa Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(GoogleBooksApiService.class);

        // Buscar livros populares ao abrir a tela
        searchBooks("best seller");

        // Buscar livros personalizados ao clicar
        searchBtn.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                searchBooks(query);
            }
        });

        return view;
    }

    private void searchBooks(String query) {
        apiService.searchBooks(query, 10).enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                List<BookItem> books = response.body() != null ? response.body().getItems() : null;
                if (books != null) {
                    adapter.setBooks(books);
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
