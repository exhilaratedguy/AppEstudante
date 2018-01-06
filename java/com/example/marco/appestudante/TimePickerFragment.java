package com.example.marco.appestudante;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), timeSetListener, hour, minute, true);
    }

    private TimePickerDialog.OnTimeSetListener timeSetListener =
            new TimePickerDialog.OnTimeSetListener()
            {

                public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                {
                    //em versoes antigas (5.1 e abaixo), temos de usar as funções adequadas
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        String minutos;
                        String horas = Integer.toString(view.getHour());

                        if (view.getMinute() < 10)
                            minutos = "0" + Integer.toString(view.getMinute());
                        else
                            minutos = Integer.toString(view.getMinute());

                        if (view.getHour() < 10)
                            horas = "0" + view.getHour();

                        TextView tv = getActivity().findViewById(R.id.textView_mostrar_hora);
                        String hora = horas + " : " + minutos;
                        tv.setText(hora);
                    }
                    else
                    {
                        String minutos;
                        String horas = Integer.toString(view.getCurrentHour());

                        if (view.getCurrentMinute() < 10)
                            minutos = "0" + Integer.toString(view.getCurrentMinute());
                        else
                            minutos = Integer.toString(view.getCurrentMinute());

                        if (view.getCurrentHour() < 10)
                            horas = "0" + view.getCurrentHour();

                        TextView tv = getActivity().findViewById(R.id.textView_mostrar_hora);
                        String hora = horas + " : " + minutos;
                        tv.setText(hora);
                    }
                }
            };
}