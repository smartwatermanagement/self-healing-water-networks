package reports.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.example.android.swn.R;

/**
 * Created by kempa on 30/9/14.
 */
public class WaterQualityDetails extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_water_quality_details, null);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView)
                .setTitle("Main Tank - Water Quality Details");

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
