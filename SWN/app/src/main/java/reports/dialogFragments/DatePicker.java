package reports.dialogFragments;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

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
        //datePickerDialog.getDatePicker().updateDate(defaultYear, defaultMonth, defaultDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        return datePickerDialog;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        textView.setText(Utils.getDateString(year, month, day, Utils.APP_DATE_FORMAT));
    }
}