package com.example.marco.appestudante;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class verNotas extends AppCompatActivity
{
    private SharedPreferences sharedPrefs;
    private String aux1, aux2;
    private ListView lista_notas;
    private List<Disciplina> disciplinas_array;
    private SharedPreferences.Editor editor;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // ler os estado do tema
        sharedPrefs = getSharedPreferences(getString(R.string.preferences_file_name), 0);
        editor = sharedPrefs.edit();
        boolean bool_1 = sharedPrefs.getBoolean("switch_dark_mode", false);

        if (bool_1)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);

        setContentView(R.layout.activity_ver_notas);

        lista_notas = findViewById(R.id.ListView_notas);
        TextView subtitulo1 = findViewById(R.id.textView_subtitulo1);
        TextView subtitulo2 = findViewById(R.id.textView_subtitulo2);
        Intent intent = getIntent();
        aux2 = intent.getStringExtra("evento_nome");
        aux1 = intent.getStringExtra("disciplina_nome");
        subtitulo1.setText(aux1);
        subtitulo2.setText(aux2);

        preencheLista(); //preenche o listView com as notas do evento selecionado
    }

    public void preencheLista()
    {
        List<Evento> eventos_array = new ArrayList<>();
        List<String> notas_array = new ArrayList<>();

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

        //encontra a disciplina certa
        for(Disciplina disc : disciplinas_array)
        {
            if(disc.getNome().equals(aux1))
            {
                eventos_array = disc.getEventos();
                break;
            }
        }

        //encontra o evento certo e obtem a lista de notas
        String temp_aux;
        for(Evento event_temp : eventos_array)
        {
            temp_aux = event_temp.getData() + "   às   " + event_temp.getHora() + "\n" + event_temp.getNome();
            if(temp_aux.equals(aux2))
            {
                notas_array = event_temp.getNotas();
                break;
            }
        }

        //cria uma lista de Strings com as notas
        ArrayList<String> lista_notas_temp = new ArrayList<>();

        for (String nota : notas_array)
            lista_notas_temp.add(nota);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista_notas_temp);
        lista_notas.setAdapter(adapter);

        //quando se faz um click longo sobre uma nota
        lista_notas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3)
            {
                setTheme(R.style.AppThemeLight);
                String item = (String) parent.getItemAtPosition(position);
                android.support.v7.app.AlertDialog diaBox = AskOption(item); //mostra a opção de Eliminar
                diaBox.show();

                return true;
            }
        });

        //quando se faz um clique sobre uma nota
        lista_notas.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                String item = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show(); //mostra o texto da nota
            }
        });
    }

    /**Quando o utilizador clica sobre o botão Adicionar nova nota*/
    public void adicionarNota(View v)
    {
        setTheme(R.style.AppThemeLight);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected;
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        //Botão OK
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //validar o input
                String in = input.getText().toString();
                if(in.equals(""))
                    Toast.makeText(getApplicationContext(), "Nota não pode ser vazia!", Toast.LENGTH_SHORT).show();

                else
                {
                    //encontrar a disciplina correta
                    String temp_aux;
                    for(Disciplina disc : disciplinas_array)
                    {
                        if(disc.getNome().equals(aux1))
                        {
                            //encontrar o evento correto e adicionar nova nota
                            ArrayList<Evento> eventos_temp = disc.getEventos();
                            for(Evento ev_temp : eventos_temp)
                            {
                                temp_aux = ev_temp.getData() + "   às   " + ev_temp.getHora() + "\n" + ev_temp.getNome();
                                if(temp_aux.equals(aux2))
                                {
                                    ev_temp.getNotas().add(in);
                                    Gson gson = new Gson();
                                    String disciplinas_add = gson.toJson(disciplinas_array);
                                    editor.putString("disciplinas", disciplinas_add);
                                    Toast.makeText(getApplicationContext(), "Nota adicionada!", Toast.LENGTH_SHORT).show();
                                    editor.commit();
                                    recreate();
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }

                Toast.makeText(getApplicationContext(), "Nota adicionada!", Toast.LENGTH_SHORT).show();
            }
        });

        //botão Cancelar
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**Mostra um diálogo para confirmar a eliminação de uma nota*/
    private android.support.v7.app.AlertDialog AskOption(String item)
    {
        final String aux = item;

        android.support.v7.app.AlertDialog myQuittingDialogBox = new android.support.v7.app.AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Eliminar nota?")
                .setMessage(item)

                //botao Eliminar
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //encontra a disciplina correta
                        for(Disciplina disc : disciplinas_array)
                        {
                            if(disc.getNome().equals(aux1))
                            {
                                //encontra o evento correto
                                String temp_aux;
                                for(Evento ev : disc.getEventos())
                                {
                                    temp_aux = ev.getData() + "   às   " + ev.getHora() + "\n" + ev.getNome();
                                    if(temp_aux.equals(aux2))
                                    {
                                        //encontra a nota correta e elimina
                                        for(int i = 0; i < ev.getNotas().size(); i++)
                                        {
                                            if(ev.getNotas().get(i).equals(aux))
                                            {
                                                ev.getNotas().remove(i);
                                                Gson gson = new Gson();
                                                String disciplinas_add = gson.toJson(disciplinas_array);
                                                editor.putString("disciplinas", disciplinas_add);
                                                Toast.makeText(getApplicationContext(), "Nota eliminada!", Toast.LENGTH_SHORT).show();
                                                editor.commit();
                                                recreate();
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                                break;
                            }
                        }

                        dialog.dismiss();
                    }
                })

                //botao cancelar
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }
}
