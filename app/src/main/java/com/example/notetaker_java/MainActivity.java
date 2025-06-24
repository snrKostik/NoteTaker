package com.example.notetaker_java;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notetaker_java.DB.DBHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.notetaker_java.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
	private DBHelper            myDB;
	private ActivityMainBinding binding;
	private AppBarConfiguration myAppBarConfiguration;

	private DrawerLayout   drawerLayout;
	private NavigationView navigationView;
	private Toolbar        toolbar;


	private AppBarConfiguration appBarConfiguration; // Изменено с myAppBarConfiguration для соответствия общепринятым именам
	private NavigationView      navigationDrawerView; // Переименовано для ясности, если есть BottomNavigationView
	private NavController       navController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		binding = ActivityMainBinding.inflate(getLayoutInflater());

		setContentView(binding.getRoot());

		toolbar = binding.appBarDrawer.toolbar;
		setSupportActionBar(toolbar);

		drawerLayout = binding.drawerLayout;


		navigationDrawerView = binding.navView;

		navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

		appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_spaced_repetition, R.id.navigation_add_note, R.id.navigation_properties) // Добавьте сюда ID ваших фрагментов из графа навигации
				.setOpenableLayout(drawerLayout) // Связываем с DrawerLayout
				.build();

		NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

		if (navigationDrawerView != null) {
			NavigationUI.setupWithNavController(navigationDrawerView, navController);
		} else {
			System.out.println("Ошибка: navigationDrawerView не найден!");
		}

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawerLayout.addDrawerListener(toggle);
		toggle.syncState();


		if (binding.appBarDrawer.fab != null) {
			binding.appBarDrawer.fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
// I definitely need to make some things here
				}
			});
		}

		BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav_view);
		if (bottomNavView != null) {
			NavigationUI.setupWithNavController(bottomNavView, navController);
		}


		NavigationView      navView             = findViewById(R.id.nav_view);
		AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_spaced_repetition, R.id.navigation_add_note, R.id.navigation_properties).build();
		NavController       navController       = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
		BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);

		NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
		NavigationUI.setupWithNavController(navView, navController);


	}

	@Override
	public boolean onSupportNavigateUp() {
		return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
	}

	@Override
	public void onBackPressed() {
		if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
			drawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

}

