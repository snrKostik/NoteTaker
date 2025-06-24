package com.example.notetaker_java.API;

import java.util.List;

public class WeatherResponse {
	private Main          main;
	private List<Weather> weather;
	private String        name;

	public Main getMain() {
		return main;
	}

	public List<Weather> getWeather() {
		return weather;
	}

	public String getName() {
		return name;
	}
}