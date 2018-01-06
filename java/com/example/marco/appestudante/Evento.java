package com.example.marco.appestudante;

import java.util.ArrayList;

public class Evento {

    private String nome;
    private String data;
    private String hora;
    private long comparador;
    private ArrayList<String> notas;

    public Evento(String nome, String data, String hora)
    {
        notas = new ArrayList<>();
        this.nome = nome;
        this.data = data;
        this.hora = hora;
        comparador = criaComparador(data, hora);
    }

    public String getNome()
    {
        return nome;
    }

    public ArrayList<String> getNotas()
    {
        return notas;
    }

    public long getComparador() {
        return comparador;
    }

    public String getData()
    {
        return data;
    }

    public String getHora()
    {
        return hora;
    }

    public long criaComparador(String data, String hora)
    {
        String[] aux_hora = hora.split(" : ");
        String[] aux_data = data.split(" / ");
        String aux_comp = "" + aux_data[2] + aux_data[1] + aux_data[0] + aux_hora[0] + aux_hora[1];

        return Long.parseLong(aux_comp);
    }
}
