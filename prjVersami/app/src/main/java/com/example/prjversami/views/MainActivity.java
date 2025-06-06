package com.example.prjversami.views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.prjversami.R;
import com.example.prjversami.controllers.NotificacaoController;
import com.example.prjversami.util.Compartilha;
import com.example.prjversami.util.NavigationUtil;



public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    int idUsuarioLogado;
    long ultimoVoltarPressionado = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        personalizarActionBar("");

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = findViewById(R.id.fragment_container);
        idUsuarioLogado = getSharedPreferences("login", MODE_PRIVATE).getInt("id", 0);

        if(savedInstanceState == null){
            NavigationUtil.carregarFragment(getSupportFragmentManager(),R.id.fragment_container, new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.menu_home);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;

                switch (menuItem.getItemId()){
                    case R.id.menu_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.menu_search:
                        fragment = new SearchFragment();
                        break;
                    case R.id.menu_add:
                        fragment = new PostsFragment();
                        break;
                    case R.id.menu_notification:
                        fragment = new NotificationFragment();
                        break;
                    case R.id.menu_profile:
                        fragment = ProfileFragment.newInstance(idUsuarioLogado);
                        break;
                }

                if (fragment != null){
                    NavigationUtil.carregarFragment(getSupportFragmentManager(),R.id.fragment_container, fragment);
                }

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Compartilha.isEditouPerfil()){
            Compartilha.setEditouPerfil(false);
            NavigationUtil.carregarFragment(getSupportFragmentManager(),R.id.fragment_container, ProfileFragment.newInstance(idUsuarioLogado));
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(!(fragment instanceof HomeFragment)){
            NavigationUtil.carregarFragment(getSupportFragmentManager(), R.id.fragment_container, new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.menu_home);
        }else{
            long agora = System.currentTimeMillis();
            if(agora - ultimoVoltarPressionado < 2000){
                super.onBackPressed();
            }else{
                ultimoVoltarPressionado = agora;
                Toast.makeText(this, "Pressione voltar novamente para sair", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void personalizarActionBar(String title){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.versamilogoblue);
    }
}