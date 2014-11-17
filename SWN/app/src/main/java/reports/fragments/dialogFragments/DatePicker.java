package reports.fragments.dialogFragments;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import utils.Utils;

/**
 * Created by kempa on 11/11/14.
 */
public class DatePicker extends DialogFragment implements  DatePickerDialog.OnDateSetListener {

    private static final String LOG_TAG = DatePicker.class.getSimpleName();

    private int defaultYear;
    private int defaultMonth;
    private int defaultDay;
    private TextView textView;

    public DatePicker() {}

    public DatePicker(TextView textView, int defaultYear, int defaultMonth, int defaultDay) {
        this.textView = textView;
        this.defaultYear = defaultYear;
        this.defaultMonth = defaultMonth;
        this.defaultDay = defaultDay;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, defaultYear, defaultMonth, defaultDay);

        // TODO: For api level 10
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker().updateDate(defaultYear, defaultMonth, defaultDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        return datePickerDialog;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        String dateString = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateString = Utils.getFormattedDateString(simpleDateFormat.parse(new Integer(day).toString() + "/" + new Integer(month + 1).toString() + "/" + new Integer(year)));
        } catch (ParseException e) {
            // This shouldn't happen as we are using a date picker
            e.printStackTrace();
        }
        textView.setText(dateString);
    }
}