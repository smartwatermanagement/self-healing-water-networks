package reports.tabhostFragments.usageBreakUp.subFragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import utils.StorageArrayAdapter;
import utils.Utils;

public class Filter extends Fragment {

    private final static String LOG_TAG = Filter.class.getSimpleName();
    private OnFilterFragmentInteractionListener mListener;
    private View rootView;
    private StorageArrayAdapter storageArrayAdapter = null;
    private int position;

    public Filter() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            mListener = (OnFilterFragmentInteractionListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getParentFragment().toString()
                    + " must implement OnFilterFragmentInteractionListener");
        }

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_filter, container, false);

        setUpFromDateFilter(rootView);
        setUpToDateFilter(rootView);

        rootView.findViewById(R.id.refreshButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from = ((TextView) rootView.findViewById(R.id.from)).getText().toString();
                String to = ((TextView) rootView.findViewById(R.id.to)).getText().toString();
                mListener = (OnFilterFragmentInteractionListener) getParentFragment();
                mListener.onFilterFragmentInteraction(storageArrayAdapter.getStorageId(position),
                        Utils.getDateString(from, Utils.APP_DATE_FORMAT, Utils.REST_API_DATE_FORMAT),
                        Utils.getDateString(to, Utils.APP_DATE_FORMAT, Utils.REST_API_DATE_FORMAT));

            }
        });
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
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);

        final TextView fromDatetextView = (TextView) rootView.findViewById(R.id.from);
        fromDatetextView.setText(Utils.getDateString(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                Utils.APP_DATE_FORMAT));

        rootView.findViewById(R.id.from).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Utils.getCalender(fromDatetextView.getText().toString(),
                        Utils.APP_DATE_FORMAT);
                DialogFragment dialogFragment = new DatePicker(fromDatetextView,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });
    }

    private void setUpToDateFilter(View rootView) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        final TextView toDatetextView = (TextView) rootView.findViewById(R.id.to);
        toDatetextView.setText(Utils.getDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH),
                Utils.APP_DATE_FORMAT));

        toDatetextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Utils.getCalender(toDatetextView.getText().toString(),
                        Utils.APP_DATE_FORMAT);
                DialogFragment dialogFragment = new DatePicker(toDatetextView,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialogFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });
    }

    public void populateStorageFilter(List<Asset> storageAssets) {
        storageArrayAdapter
                = new StorageArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
        storageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (Asset storageAsset : storageAssets) {
            storageArrayAdapter.add(storageAsset.getName(), storageAsset.getAsset_id());
        }
        Spinner spinner = (Spinner) rootView.findViewById(R.id.storage_spinner);
        spinner.setAdapter(storageArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Filter.this.position = position;
                String from = ((TextView) rootView.findViewById(R.id.from)).getText().toString();
                String to = ((TextView) rootView.findViewById(R.id.to)).getText().toString();
                mListener = (OnFilterFragmentInteractionListener) getParentFragment();
                mListener.onFilterFragmentInteraction(storageArrayAdapter.getStorageId(position),
                        Utils.getDateString(from, Utils.APP_DATE_FORMAT, Utils.REST_API_DATE_FORMAT),
                        Utils.getDateString(to, Utils.APP_DATE_FORMAT, Utils.REST_API_DATE_FORMAT));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
