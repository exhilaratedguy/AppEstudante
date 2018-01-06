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
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class verDisciplinas extends AppCompatActivity {

    private SharedPreferences sharedPrefs;
    private ListView lista_disciplinas;
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

        setContentView(R.layout.activity_ver_disciplinas);

        lista_disciplinas = findViewById(R.id.listView_disciplinas);

        preencheLista();  //preenche o listView com as disciplinas registadas
    }

    public void preencheLista()
    {
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
            Toast.makeText(getApplicationContext(), "Não há disciplinas registadas!", Toast.LENGTH_LONG).show();
            finish();
        }
        else
        {
            //criar uma lista de Strings com os nomes das disciplinas
            ArrayList<String> nomes_das_disciplinas = new ArrayList<>();
            for (Disciplina disc : disciplinas_array)
                nomes_das_disciplinas.add(disc.getNome());

            ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nomes_das_disciplinas);
            lista_disciplinas.setAdapter(adapter);

            //quando se faz um clique longo numa disciplina
            lista_disciplinas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
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

            //quando se clica numa disciplina
            lista_disciplinas.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
                {
                    String item = (String) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getApplicationContext(), verEventos.class);
                    intent.putExtra("disciplina_nome", item);
                    startActivity(intent); //mostra a próxima activity
                }
            });
        }
    }


    /**Mostra um diálogo para confirmar a eliminação de uma disciplina*/
    private AlertDialog AskOption(String item)
    {
        final String aux_item = item;
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Eliminar " + item + "?")
                .setMessage("Isto irá eliminar todos os eventos associados a esta disciplina.")

                //botão Eliminar
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        //encontra a disciplina certa e elinima
                        for(int i = 0; i < disciplinas_array.size(); i++)
                        {
                            if(disciplinas_array.get(i).getNome().equals(aux_item))
                            {
                                disciplinas_array.remove(i);
                                Gson gson = new Gson();
                                String disciplinas_add = gson.toJson(disciplinas_array);
                                SharedPreferences.Editor editor = sharedPrefs.edit();
                                editor.putString("disciplinas", disciplinas_add);
                                Toast.makeText(getApplicationContext(), "Disciplina eliminada!", Toast.LENGTH_SHORT).show();
                                editor.commit();
                                recreate();
                                break;
                            }
                        }

                        dialog.dismiss();
                    }
                })

                //botão Cancelar
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
