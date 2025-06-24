package com.example.notetaker_java.API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherApiService {
	@GET("data/2.5/weather")
	Call<WeatherResponse> getCurrentWeather(
			@Query("lat") double lat,
			@Query("lon") double lon,
			@Query("appid") String apiKey,
			@Query("units") String units
	);
}