package com.example.marco.appestudante;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


public class Definicoes extends AppCompatActivity {

    private final String endereco_mail_1 = "diogohenriquecruz2202@gmail.com";
    private final String endereco_mail_2 = "marcopm1997@hotmail.com";
    private final String endereco_mail_3 = "diogonobrega6@gmail.com";
    private final String endereco_mail_4 = "miguelmafm16@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // ler os estados dos switches
        SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.preferences_file_name), 0);
        final SharedPreferences.Editor editor = sharedPrefs.edit();
        boolean bool_1 = sharedPrefs.getBoolean("switch_dark_mode", false);
        boolean bool_2 = sharedPrefs.getBoolean("switch_notifications", false);

        if(bool_1)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);

        setContentView(R.layout.activity_definicoes);

        //botoes switch
        final Switch sw_dark_mode, sw_notifications;

        sw_dark_mode = findViewById(R.id.switch_dark_mode);
        sw_notifications = findViewById(R.id.switch_notifications);

        //mudar a cor do texto dos botoes, necessário neste ecrã porque a cor
        //dos botões abaixo(azul) é custom. Nos outros ecrãs, a cor do texto é
        //alterada automaticamente com o tema
        if(bool_1)
        {
            sw_dark_mode.setTextColor(getResources().getColor(R.color.colorPrimary));
            sw_notifications.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            sw_dark_mode.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            sw_notifications.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        //colocar os botoes na posição lida (On ou Off)
        sw_dark_mode.setChecked(bool_1);
        sw_notifications.setChecked(bool_2);

        //quando se altera o botão Dark Mode
        sw_dark_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if(isChecked)
                {
                    Toast.makeText(getApplicationContext(), "Dark mode ON", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("switch_dark_mode", true);
                    editor.commit();
                    setTheme(R.style.AppThemeDark);
                    recreate();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Dark mode OFF", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("switch_dark_mode", false);
                    editor.commit();
                    setTheme(R.style.AppThemeLight);
                    recreate();
                }
            }
        });

        //quando se altera o botão Notificações
        sw_notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if(isChecked)
                {
                    Toast.makeText(getApplicationContext(), "Notificações ON", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("switch_notifications", true);
                    editor.commit();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Notificações OFF", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("switch_notifications", false);
                    editor.commit();
                }
            }
        });
    }

    /** Called when the user taps the Classificar button */
    public void classificar(View view)
    {
        //abrir página na app store
        Toast.makeText(this, "Esta funcionalidade ainda não foi implementada.", Toast.LENGTH_SHORT).show();
    }

    /** Called when the user taps the Créditos button */
    public void mostrarCreditos(View view)
    {
        Intent intent = new Intent(this, verCreditos.class);
        startActivity(intent);
    }

    /** Called when the user taps the Sugestões button */
    public void enviarSugestao(View view)
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");
        intent.setData(Uri.parse("mailto:" + endereco_mail_1 + "," + endereco_mail_2 + "," + endereco_mail_3 + "," + endereco_mail_4));
        intent.putExtra(Intent.EXTRA_SUBJECT, "AppEstudante - Sugestão");
        intent.putExtra(Intent.EXTRA_TEXT   , "");
        try
        {
            startActivity(Intent.createChooser(intent, "Escolha uma aplicação"));
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    /** Called when the user taps the Bug button */
    public void reportarBug(View view)
    {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");
        intent.setData(Uri.parse("mailto:" + endereco_mail_1 + "," + endereco_mail_2 + "," + endereco_mail_3 + "," + endereco_mail_4));
        intent.putExtra(Intent.EXTRA_SUBJECT, "AppEstudante - Report Bug");
        intent.putExtra(Intent.EXTRA_TEXT   , "");
        try {
            startActivity(Intent.createChooser(intent, "Escolha uma aplicação"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
