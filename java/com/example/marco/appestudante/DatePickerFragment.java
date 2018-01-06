package com.example.marco.appestudante;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
        new DatePickerDialog.OnDateSetListener()
        {
            public void onDateSet(DatePicker view, int year, int month, int day)
            {
                String dia;
                String mes;

                if(view.getDayOfMonth() < 10)
                    dia = "0" + Integer.toString(view.getDayOfMonth());
                else
                    dia = Integer.toString(view.getDayOfMonth());

                if((view.getMonth()+1) < 10)
                    mes = "0" + Integer.toString((view.getMonth()+1));
                else
                    mes = Integer.toString((view.getMonth()+1));

                TextView tv = getActivity().findViewById(R.id.textView_mostrar_data);
                String data = dia + " / " + mes + " / " + view.getYear();
                tv.setText(data);
            }
        };
}