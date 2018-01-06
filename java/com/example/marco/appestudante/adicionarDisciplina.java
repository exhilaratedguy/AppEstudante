package com.example.marco.appestudante;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class adicionarDisciplina extends AppCompatActivity {

    private SharedPreferences sharedPrefs; //ficheiro com os dados da aplicação
    private EditText disciplina_nome; //caixa de input de texto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ler os estado do tema
        sharedPrefs = getSharedPreferences(getString(R.string.preferences_file_name), 0);
        boolean bool_1 = sharedPrefs.getBoolean("switch_dark_mode", false);

        if(bool_1)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);

        setContentView(R.layout.activity_adicionar_disciplina);

        disciplina_nome = findViewById(R.id.editText_inserir_nome);

        //mudar a cor do texto da caixa, devido ao tema custom
        if(bool_1)
            disciplina_nome.setTextColor(getResources().getColor(R.color.colorPrimary));
        else
            disciplina_nome.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    /** Called when the user taps the Acidionar Disciplina button*/
    public void adicionar(View v)
    {
        String str = disciplina_nome.getText().toString(); //nome escrito no campo
        List<Disciplina> disciplinas_array; //array a ser lido do ficheiro
        boolean soNumeros = true; //boolean auxiliar

        //validações do texto inserido
        if(str.equals(""))
            Toast.makeText(getApplicationContext(), "Nome não pode ser vazio!", Toast.LENGTH_SHORT).show();

        else if(str.length() > 25)
            Toast.makeText(getApplicationContext(), "Nome não pode ter mais de 25 caracteres!", Toast.LENGTH_SHORT).show();

        else
        {
            for(char c : str.toCharArray()) //verificar se só foram inseridos números
            {
                if(Character.isLetter(c))
                {
                    soNumeros = false;
                    break;
                }
            }

            if(soNumeros)
                Toast.makeText(getApplicationContext(), "Nome não pode ter apenas números!", Toast.LENGTH_SHORT).show();

            else //se o texto foi validado com sucesso
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

                //check if array contains disciplina inserida
                boolean found = false;

                for(Disciplina disc : disciplinas_array)
                {
                    if(disc.getNome().toLowerCase().equals(str.toLowerCase()))
                    {
                        found = true;
                        break;
                    }
                }

                if(found) //se a disciplina já existe no ficheiro
                    Toast.makeText(getApplicationContext(), "Esta disciplina já existe!", Toast.LENGTH_SHORT).show();
                else
                {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Disciplina disciplina = new Disciplina(str); //criar nova disciplina
                    disciplinas_array.add(disciplina); //adicionar ao array

                    //ordenar alfabeticamente
                    Collections.sort(disciplinas_array, new Comparator<Disciplina>()
                    {
                        @Override
                        public int compare(Disciplina disc1, Disciplina disc2)
                        {
                            return disc1.getNome().compareTo(disc2.getNome());
                        }
                    });

                    String disciplinas_add = gson.toJson(disciplinas_array);
                    editor.putString("disciplinas", disciplinas_add);
                    Toast.makeText(getApplicationContext(), "Disciplina adicionada!", Toast.LENGTH_SHORT).show();
                    editor.commit(); //guardar no ficheiro
                    finish(); //terminar atividade
                }
            }
        }
    }
}
