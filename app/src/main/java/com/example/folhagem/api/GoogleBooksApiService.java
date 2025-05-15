package com.example.folhagem.api;

import com.example.folhagem.model.BookResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleBooksApiService {
    @GET("volumes")
    Call<BookResponse> searchBooks(@Query("q") String query, @Query("maxResults") int maxResults);

}
