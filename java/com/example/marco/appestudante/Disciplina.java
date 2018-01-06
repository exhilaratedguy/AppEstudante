package com.example.marco.appestudante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Disciplina {

    private String nome;
    private ArrayList<Evento> eventos;

    public Disciplina(String nome)
    {
        this.nome = nome;
        eventos = new ArrayList<>();
    }

    public ArrayList<Evento> getEventos()
    {
        return eventos;
    }

    public String getNome()
    {
        return nome;
    }

    public void adicionarEvento(Evento e)
    {
        eventos.add(e);

        //ordenar os eventos por data
        Collections.sort(eventos, new Comparator<Evento>() {
            @Override
            public int compare(Evento event1, Evento event2)
            {
                if (event1.getComparador() < (event2.getComparador()))
                    return -1;
                else if(event1.getComparador() == (event2.getComparador()))
                    return 0;
                else return 1;
            }
        });
    }
}
