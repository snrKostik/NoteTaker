package com.example.notetaker_java.ui.properties;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.notetaker_java.API.OpenWeatherApiService;
import com.example.notetaker_java.API.WeatherResponse;
import com.example.notetaker_java.API.keys;
import com.example.notetaker_java.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PropertiesFragment extends Fragment {

    private MapView map = null;
    private MyLocationNewOverlay myLocationOverlay;
    private CompassOverlay mCompassOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    private TextView weatherInfoTextView;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String OPEN_WEATHER_API_KEY = keys.OPEN_WEATHER_API_KEY;
    private OpenWeatherApiService openWeatherApiService;

    public PropertiesFragment() {
        // Специально пустой конструктор
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_properties, container, false);

        Configuration.getInstance().setUserAgentValue(requireActivity().getPackageName());

        map = view.findViewById(R.id.map);
        weatherInfoTextView = view.findViewById(R.id.weather_info_text_view);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        map.getController().setZoom(12.0);
        GeoPoint startPoint = new GeoPoint(55.751244, 37.617494);
        map.getController().setCenter(startPoint);

        mCompassOverlay = new CompassOverlay(requireContext(), new InternalCompassOrientationProvider(requireContext()), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);

        final DisplayMetrics dm = getResources().getDisplayMetrics();
        mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        map.getOverlays().add(mScaleBarOverlay);

        Marker marker = new Marker(map);
        marker.setPosition(new GeoPoint(55.7558, 37.6176));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle("Тест");
        map.getOverlays().add(marker);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        openWeatherApiService = retrofit.create(OpenWeatherApiService.class);

        fetchWeatherData(startPoint.getLatitude(), startPoint.getLongitude());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestLocationPermissions();
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            setupMyLocationOverlay();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMyLocationOverlay();
            } else {
                Toast.makeText(requireContext(), "Разрешение на местоположение отклонено. Некоторые функции могут быть недоступны.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setupMyLocationOverlay() {
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(requireContext()), map);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableFollowLocation();
        map.getOverlays().add(myLocationOverlay);

        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (myLocationOverlay.getMyLocation() != null) {
                            GeoPoint userLocation = myLocationOverlay.getMyLocation();
                            map.getController().animateTo(userLocation);
                            map.getController().setZoom(15.0);

                            fetchWeatherData(userLocation.getLatitude(), userLocation.getLongitude());
                        }
                    }
                });
            }
        });
    }

    private void fetchWeatherData(double lat, double lon) {
        if (openWeatherApiService == null) {
            Toast.makeText(requireContext(), "API Service not initialized.", Toast.LENGTH_SHORT).show();
            return;
        }

        openWeatherApiService.getCurrentWeather(lat, lon, OPEN_WEATHER_API_KEY, "metric")
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            WeatherResponse weatherResponse = response.body();
                            String cityName = weatherResponse.getName();
                            double temperature = weatherResponse.getMain().getTemp();
                            String description = weatherResponse.getWeather().get(0).getDescription();

                            String weatherInfo = "Город: " + cityName +
                                    "\nТемпература: " + temperature + "°C" +
                                    "\nОписание: " + description;

                            if (weatherInfoTextView != null) {
                                weatherInfoTextView.setText(weatherInfo);
                                weatherInfoTextView.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(requireContext(), weatherInfo, Toast.LENGTH_LONG).show();
                            }

                        } else {
                            if (weatherInfoTextView != null) {
                                weatherInfoTextView.setText("Не удалось получить данные о погоде: " + response.message());
                            }
                            Toast.makeText(requireContext(), "Не удалось получить данные о погоде: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                        if (weatherInfoTextView != null) {
                            weatherInfoTextView.setText("Ошибка при получении данных о погоде.");
                        }
                        Toast.makeText(requireContext(), "Ошибка при получении данных о погоде: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });
    }


    private void addWeatherMarker(double lat, double lon, String snippet) {
        Marker weatherMarker = new Marker(map);
        weatherMarker.setPosition(new GeoPoint(lat, lon));
        weatherMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        weatherMarker.setTitle("Текущая погода");
        weatherMarker.setSnippet(snippet);
        map.getOverlays().add(weatherMarker);
        map.invalidate();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (map != null) {
            map.onResume();
        }
        if (myLocationOverlay != null) {
            myLocationOverlay.enableMyLocation();
        }
        if (mCompassOverlay != null) {
            mCompassOverlay.enableCompass();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) {
            map.onPause();
        }
        if (myLocationOverlay != null) {
            myLocationOverlay.disableMyLocation();
        }
        if (mCompassOverlay != null) {
            mCompassOverlay.disableCompass();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (map != null) {
            map.onDetach();
            map = null;
        }
        weatherInfoTextView = null;
    }
}