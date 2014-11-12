package reports;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by kempa on 11/11/14.
 */
public class DatePickerFragment extends DialogFragment implements  DatePickerDialog.OnDateSetListener {

    private static final String LOG_TAG = DatePickerFragment.class.getSimpleName();

    private int defaultYear;
    private int defaultMonth;
    private int defaultDay;
    private TextView textView;

    public DatePickerFragment() {}

    public DatePickerFragment(TextView textView, int defaultYear, int defaultMonth, int defaultDay) {
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
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        String dateString = null;
        try {
            SimpleDateFormat fromFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat toFormat = new SimpleDateFormat("MMM dd, yyyy");
            dateString = toFormat.format(fromFormat.parse(new Integer(day).toString() + "/" + new Integer(month).toString() + "/" + new Integer(year)));
        } catch (ParseException e) {
            // This shouldn't happen as we are using a date picker
            e.printStackTrace();
        }
        textView.setText(dateString.toCharArray(), 0, dateString.length());
    }
}