package com.example.marco.appestudante;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ler os estado do tema
        sharedPrefs = getSharedPreferences(getString(R.string.preferences_file_name), 0);
        boolean bool_1 = sharedPrefs.getBoolean("switch_dark_mode", false);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        if(bool_1)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);

        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRestart() //quando se volta a esta atividade
    {
        super.onRestart();  // Always call the superclass method first
        recreate(); //para atualizar o tema
    }

    /** Called when the user taps the Ver Disciplinas button */
    public void verDisciplinas(View view)
    {
        Intent intent = new Intent(this, verDisciplinas.class);
        startActivity(intent);
    }

    /** Called when the user taps the Adicionar Disciplina button */
    public void adicionarDisciplina(View view)
    {
        Intent intent = new Intent(this, adicionarDisciplina.class);
        startActivity(intent);
    }

    /** Called when the user taps the Adicionar Evento button */
    public void adicionarEvento(View view)
    {
        Intent intent = new Intent(this, adicionarEvento.class);
        startActivity(intent);
    }

    /** Called when the user taps the Definiçõesbutton */
    public void definicoes(View view)
    {
        Intent intent = new Intent(this, Definicoes.class);
        startActivity(intent);
    }
}
