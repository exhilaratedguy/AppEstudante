package com.example.marco.appestudante;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class adicionarEvento extends AppCompatActivity {

    private SharedPreferences sharedPrefs; //ficheiro dos dados da apicação
    private Spinner spinner_disciplinas; //select box com as disciplinas disponíveis
    private SharedPreferences.Editor editor;
    private int request_code, id_notification;
    private TextView textview_evento_nome;
    private TextView textview_data;
    private TextView textview_hora;
    private long delay, delay_dia, delay_hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ler os estado do tema
        sharedPrefs = getSharedPreferences(getString(R.string.preferences_file_name), 0);
        editor = sharedPrefs.edit();
        request_code = sharedPrefs.getInt("request_code", 0);
        id_notification = sharedPrefs.getInt("id_notification", 0);
        boolean bool_1 = sharedPrefs.getBoolean("switch_dark_mode", false);

        if(bool_1)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);

        setContentView(R.layout.activity_adicionar_evento);

        spinner_disciplinas = findViewById(R.id.spinner_disciplinas);
        textview_evento_nome = findViewById(R.id.editText_nome_evento);
        textview_data = findViewById(R.id.textView_mostrar_data);
        textview_hora = findViewById(R.id.textView_mostrar_hora);

        fillSpinner(); //preencher a select box

        //mudar a cor do texto devido ao tema custom
        if(bool_1)
            textview_evento_nome.setTextColor(getResources().getColor(R.color.colorPrimary));
        else
            textview_evento_nome.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    /** Called when the user taps the Adicionar Evento button*/
    public void adicionar(View v)
    {
        //obter dados inseridos nos campos
        String nome_disciplina = spinner_disciplinas.getSelectedItem().toString();
        String nome_evento = textview_evento_nome.getText().toString();
        String data = textview_data.getText().toString();
        String hora = textview_hora.getText().toString();

        //validar dados
        if(nome_evento.equals("") || data.equals("") || hora.equals(""))
            Toast.makeText(getApplicationContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();

        else
        {
            //obter data inserida
            String[] dia_mes_ano = data.split(" / ");
            int dia_inserido = Integer.parseInt(dia_mes_ano[0]);
            int mes_inserido = Integer.parseInt(dia_mes_ano[1]);
            int ano_inserido = Integer.parseInt(dia_mes_ano[2]);

            //obter hora inserida
            String[] hora_minuto = hora.split(" : ");
            int hora_inserida = Integer.parseInt(hora_minuto[0]);
            int minuto_inserido = Integer.parseInt(hora_minuto[1]);

            try
            {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm");
                String str = dia_inserido+"-"+mes_inserido+"-"+ano_inserido+" "+ hora_inserida+":"+minuto_inserido;
                Date date = sdf.parse(str);
                delay = date.getTime() - System.currentTimeMillis();
                delay_hora = delay - 3600000;
                delay_dia = delay - 86400000;
            }
            catch(ParseException e)
            {
                e.printStackTrace();
            }

            //verificar se a data é válida
            if(delay <= 0)
                Toast.makeText(getApplicationContext(), "Insira uma data/hora válidas!", Toast.LENGTH_SHORT).show();

            else
            {
                List<Disciplina> disciplinas_array;
                String json = sharedPrefs.getString("disciplinas", "");
                Gson gson = new Gson();

                if (json.equals(""))
                    disciplinas_array = new ArrayList<>();
                else
                {
                    Type type = new TypeToken<ArrayList<Disciplina>>() {}.getType();
                    disciplinas_array = gson.fromJson(json, type);
                }

                ArrayList<Evento> eventos_temp = new ArrayList<>();

                //obter o arrayList de eventos correspondente à disciplina selecionada
                for (Disciplina disc : disciplinas_array)
                {
                    if (disc.getNome().equals(nome_disciplina))
                    {
                        eventos_temp = disc.getEventos();
                        break;
                    }
                }

                //verificar se o evento já existe
                boolean jaExiste = false;
                Evento evento_temp = new Evento(nome_evento, data, hora);

                for(Evento ev : eventos_temp)
                {
                    if(ev.getComparador() == evento_temp.getComparador() && ev.getNome().toLowerCase().equals(evento_temp.getNome().toLowerCase()))
                    {
                        jaExiste = true;
                        break;
                    }
                }

                if(jaExiste)
                    Toast.makeText(getApplicationContext(), "Este evento já existe!", Toast.LENGTH_SHORT).show();

                else
                {
                    //adicionar ao array de disciplinas
                    for (Disciplina disc : disciplinas_array)
                    {
                        if (disc.getNome().equals(nome_disciplina))
                        {
                            disc.adicionarEvento(new Evento(nome_evento, data, hora));
                            break;
                        }
                    }

                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    String disciplinas_add = gson.toJson(disciplinas_array);
                    editor.putString("disciplinas", disciplinas_add);
                    Toast.makeText(getApplicationContext(), "Evento adicionado!\n" + nome_disciplina + "\n" + nome_evento + "\n" + data + " às " + hora, Toast.LENGTH_SHORT).show();

                    if(delay_dia > 0)
                    {
                        scheduleNotification(getNotification(nome_disciplina + " - " + nome_evento + " às " + hora, true), delay_dia);
                    }

                    if(delay_hora > 0)
                        scheduleNotification(getNotification(nome_disciplina + " - " + nome_evento + " às " + hora, false), delay_hora);


                    editor.commit();
                    finish();
                }
            }
        }
    }

    //mostra o calendário quando se carrega no botão Data
    public void showDatePicker(View v)
    {
        setTheme(R.style.AppThemeLight);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "date picker");
    }

    //mostra o relógio quando se carrega no botão Hora
    public void showTimePicker(View v)
    {
        setTheme(R.style.AppThemeLight);
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "time picker");
    }

    /**Preenche o spinner com os nomes das disciplinas inseridas*/
    public void fillSpinner()
    {
        List<Disciplina> disciplinas_array;

        //get array from sharedprefs
        Gson gson = new Gson();
        String json = sharedPrefs.getString("disciplinas", "");

        if(Objects.equals(json, ""))
            disciplinas_array = new ArrayList<>();
        else
        {
            Type type = new TypeToken<ArrayList<Disciplina>>(){}.getType();
            disciplinas_array = gson.fromJson(json, type);
        }

        if(disciplinas_array.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Não há disciplinas registadas!\nAdicione disciplinas antes de adicionar eventos!", Toast.LENGTH_LONG).show();
            finish();
        }
        else
        {
            ArrayList<String> nomes_das_disciplinas = new ArrayList<>();
            for (Disciplina disc : disciplinas_array)
                nomes_das_disciplinas.add(disc.getNome());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nomes_das_disciplinas);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_disciplinas = findViewById(R.id.spinner_disciplinas);
            spinner_disciplinas.setAdapter(adapter);
        }
    }

    private void scheduleNotification(Notification notification, long delay)
    {
        request_code++;
        id_notification++;
        editor.putInt("id_notification", id_notification);
        editor.putInt("request_code", request_code);
        editor.commit();

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra("id", id_notification);
        notificationIntent.putExtra("notification", notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, request_code + 123, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content, boolean dia_hora)
    {
        Notification.Builder builder = new Notification.Builder(this);

        if(dia_hora)
            builder.setContentTitle("Aviso! falta 1 dia!");
        else
            builder.setContentTitle("Aviso! falta 1 hora!");

        long[] pattern = {0, 500, 200, 500}; //delay, vibra, sleep, vibra
        builder.setVibrate(pattern);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.logo);
        return builder.build();
    }
}
