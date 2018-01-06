package com.example.marco.appestudante;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class verCreditos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ler os estado do tema
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.preferences_file_name), 0);
        boolean bool_1 = sharedPrefs.getBoolean("switch_dark_mode", false);

        if(bool_1)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);

        setContentView(R.layout.activity_ver_creditos);
    }
}
