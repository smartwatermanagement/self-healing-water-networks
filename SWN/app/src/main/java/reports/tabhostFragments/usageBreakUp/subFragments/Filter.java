package reports.tabhostFragments.usageBreakUp.subFragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.swn.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Asset;
import reports.dialogFragments.DatePicker;
import reports.tabhostFragments.usageBreakUp.UsageBreakUp;
import utils.StorageArrayAdapter;
import utils.Utils;

public class Filter extends Fragment {

    private final static String LOG_TAG = Filter.class.getSimpleName();
    private OnFilterFragmentInteractionListener mListener;
    private View rootView;

    public Filter() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            mListener = (UsageBreakUp) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getParentFragment().toString()
                    + " must implement OnFragmentInteractionListener");
        }

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_filter, container, false);

        if (savedInstanceState == null) {
            setUpFromDateFilter(rootView);
            setUpToDateFilter(rootView);
        }
        else
            Log.d(LOG_TAG, "There is a savedInstanceState");
        return rootView;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFilterFragmentInteractionListener {
        public void onFilterFragmentInteraction(int storageId, String from, String to);
    }

    /**
     * Initialize and set listeners for the the 'from' date field
     * @param rootView
     */
    private void setUpFromDateFilter(View rootView) {
        final TextView fromDatetextView = (TextView) rootView.findViewById(R.id.from);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        fromDatetextView.setText(Utils.getFormattedDateString(c.getTime()));
        rootView.findViewById(R.id.from).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // max date
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);

                DialogFragment dialogFragment = new DatePicker(fromDatetextView, year, month, day);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });
    }

    private void setUpToDateFilter(View rootView) {
        final TextView toDatetextView = (TextView) rootView.findViewById(R.id.to);
        toDatetextView.setText(Utils.getFormattedDateString(new Date()));
        toDatetextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //max fate
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);

                DialogFragment dialogFragment = new DatePicker(toDatetextView, year, month, day);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });
    }

    public void populateStorageFilter(List<Asset> storageAssets) {
        final StorageArrayAdapter storageArrayAdapter
                = new StorageArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
        storageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (Asset storageAsset : storageAssets) {
            storageArrayAdapter.add(storageAsset.getName(), storageAsset.getAsset_id());
        }
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        spinner.setAdapter(storageArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String from = ((TextView) rootView.findViewById(R.id.from)).getText().toString();
                String to = ((TextView) rootView.findViewById(R.id.to)).getText().toString();
                mListener = (UsageBreakUp) getParentFragment();
                mListener.onFilterFragmentInteraction(storageArrayAdapter.getStorageId(position),
                        from, to);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
