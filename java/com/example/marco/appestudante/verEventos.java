package com.example.marco.appestudante;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class verEventos extends AppCompatActivity
{
    private SharedPreferences sharedPrefs;
    private String aux;
    private ListView lista_eventos;
    private List<Disciplina> disciplinas_array;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // ler os estado do tema
        sharedPrefs = getSharedPreferences(getString(R.string.preferences_file_name), 0);
        boolean bool_1 = sharedPrefs.getBoolean("switch_dark_mode", false);

        if (bool_1)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);

        setContentView(R.layout.activity_ver_eventos);

        lista_eventos = findViewById(R.id.listView_eventos);
        TextView subtitulo = findViewById(R.id.textView_disciplina_subtitulo);
        Intent intent = getIntent();
        aux = intent.getStringExtra("disciplina_nome");
        subtitulo.setText(aux); //coloca o nome da disciplina como subtitulo

        preencheLista(); //preenche o listView com os eventos da disciplina
    }

    public void preencheLista()
    {
        List<Evento> eventos_array = new ArrayList<>();

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

        //verifica se há eventos guardados
        boolean found = false;

        for(Disciplina disc : disciplinas_array)
        {
            if(disc.getNome().equals(aux))
            {
                found = true;
                eventos_array = disc.getEventos();
                break;
            }
        }

        if(!found || eventos_array.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Não há eventos registados nesta disciplina!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            //cria uma lista de Strings com a informação do evento
            ArrayList<String> nomes_dos_eventos = new ArrayList<>();
            String texto;

            for (Evento event : eventos_array)
            {
                texto = event.getData() + "   às   " + event.getHora() + "\n" + event.getNome();
                nomes_dos_eventos.add(texto);
            }

            ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nomes_dos_eventos);
            lista_eventos.setAdapter(adapter);

            //quando se faz um clique longo sobre um evento
            lista_eventos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
            {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3)
                {
                    setTheme(R.style.AppThemeLight);
                    String item = (String) parent.getItemAtPosition(position);
                    AlertDialog diaBox = AskOption(item); //mostra a opção de eliminar
                    diaBox.show();

                    return true;
                }
            });

            //quando se faz um clique sobre um evento
            lista_eventos.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
                {
                    String item = (String) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getApplicationContext(), verNotas.class);
                    intent.putExtra("evento_nome", item);
                    intent.putExtra("disciplina_nome", aux);
                    startActivity(intent); //iniciar a próxima activity
                }
            });
        }
    }

    /**Mostra um diálogo para confirmar a eliminação de um evento*/
    private AlertDialog AskOption(String item)
    {
        final String aux_item = item;

        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Eliminar evento?")
                .setMessage(item)

                //botão Eliminar
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //encontra a disciplina correta
                        for(Disciplina disc : disciplinas_array)
                        {
                            if(disc.getNome().equals(aux))
                            {
                                String temp_aux;

                                //encontra o evento correto e elimina
                                for(int i = 0; i < disc.getEventos().size(); i++)
                                {
                                    temp_aux = disc.getEventos().get(i).getData() + "   às   " + disc.getEventos().get(i).getHora() + "\n" + disc.getEventos().get(i).getNome();
                                    if(temp_aux.equals(aux_item))
                                    {
                                        disc.getEventos().remove(i);
                                        Gson gson = new Gson();
                                        String disciplinas_add = gson.toJson(disciplinas_array);
                                        SharedPreferences.Editor editor = sharedPrefs.edit();
                                        editor.putString("disciplinas", disciplinas_add);
                                        Toast.makeText(getApplicationContext(), "Evento eliminado!", Toast.LENGTH_SHORT).show();
                                        editor.commit();

                                        if(disc.getEventos().isEmpty())
                                            finish();
                                        else
                                            recreate();

                                        break;
                                    }
                                }
                                break;
                            }
                        }

                        dialog.dismiss();
                    }
                })

                //botao Cancelar
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
