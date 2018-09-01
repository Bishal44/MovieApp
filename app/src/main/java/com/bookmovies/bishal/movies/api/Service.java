package com.bookmovies.bishal.movies.api;

import com.bookmovies.bishal.movies.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Service {
    @GET("movie/popular")
    Call<MovieResponse> getpopularMovies(@Query("api_key")String api_key);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key")String api_key);
}
