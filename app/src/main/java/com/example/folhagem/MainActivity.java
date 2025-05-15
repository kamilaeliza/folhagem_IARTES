package com.example.folhagem;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.folhagem.ui.estante.EstanteFragment;
import com.example.folhagem.ui.home.HomeFragment;
import com.example.folhagem.ui.perfil.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        // Carrega o fragmento inicial (Home)
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    // Listener de navegação inferior
    private final BottomNavigationView.OnItemSelectedListener navListener = new BottomNavigationView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_estante) {
                selectedFragment = new EstanteFragment();
            } else if (itemId == R.id.nav_perfil) {
                selectedFragment = new PerfilFragment();
            }

            return loadFragment(selectedFragment);
        }
    };

    // Método auxiliar para carregar fragments
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
