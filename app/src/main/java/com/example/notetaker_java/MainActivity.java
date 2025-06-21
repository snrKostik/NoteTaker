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

		toolbar = binding.appBarDrawer.toolbar; // appBarDrawer - это id вашего <include> для app_bar_main.xml?
		setSupportActionBar(toolbar);

		drawerLayout = binding.drawerLayout; // Предполагаем, что drawer_layout это ID вашего DrawerLayout в activity_main.xml

		// Получаем NavigationView для Drawer. Убедитесь, что ID правильный.
		// Если nav_view используется и для BottomNavigationView, вам нужен другой ID для Drawer NavigationView
		navigationDrawerView = binding.navView; // Или findViewById(R.id.id_вашего_drawer_navigation_view);

		// Настройка NavController
		navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

		// Настройка AppBarConfiguration. Важно указать DrawerLayout здесь.
		// Перечислите ID верхнеуровневых экранов вашего приложения.
		// Гамбургер-иконка будет отображаться для этих экранов.
		appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_spaced_repetition, R.id.navigation_add_note, R.id.navigation_properties) // Добавьте сюда ID ваших фрагментов из графа навигации
				.setOpenableLayout(drawerLayout) // Связываем с DrawerLayout
				.build();

		// Связываем ActionBar с NavController и AppBarConfiguration
		NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

		// Связываем NavigationView (Drawer) с NavController
		// Эта строка заменяет ваш setNavigationItemSelectedListener
		if (navigationDrawerView != null) {
			NavigationUI.setupWithNavController(navigationDrawerView, navController);
		} else {
			// Логирование или обработка ошибки, если navigationDrawerView не найден
			System.out.println("Ошибка: navigationDrawerView не найден!");
		}

		// Старый ActionBarDrawerToggle - NavigationUI.setupActionBarWithNavController его частично заменяет,
		// но ActionBarDrawerToggle все еще нужен для анимации гамбургер-иконки и обработки открытия/закрытия.
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawerLayout.addDrawerListener(toggle);
		toggle.syncState();


		// Настройка FAB (если есть)
		if (binding.appBarDrawer.fab != null) {
			binding.appBarDrawer.fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAnchorView(R.id.fab).show();
				}
			});
		}

		// Если у вас также есть BottomNavigationView
		BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav_view); // Убедитесь, что это ID вашего BottomNavigationView
		if (bottomNavView != null) {
			// Вы можете настроить его с тем же NavController, если хотите,
			// чтобы и Drawer, и BottomNav управляли одним и тем же хостом фрагментов.
			NavigationUI.setupWithNavController(bottomNavView, navController);
		}


		// Этот метод важен для корректной работы кнопки "назад" и гамбургер-иконки с Navigation Component


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

	// Опционально: для закрытия Drawer кнопкой "назад", если он открыт
	@Override
	public void onBackPressed() {
		if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
			drawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

}

