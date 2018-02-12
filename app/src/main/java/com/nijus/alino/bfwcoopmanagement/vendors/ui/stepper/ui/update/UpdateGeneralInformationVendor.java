package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.update;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventB;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventR;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.GeneralVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.GeneralInformationVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.PageFragmentCallbacksVendor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class UpdateGeneralInformationVendor extends Fragment implements /* AdapterView.OnItemSelectedListener,*/ LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = GeneralInformationVendor.class.getSimpleName();
    private String mKey;

    private GeneralVendor generalVendor = new GeneralVendor();

    private PageVendorVendor mPageVendor;
    private PageFragmentCallbacksVendor mCallbacks;
    private AutoCompleteTextView names;
    private AutoCompleteTextView phoneNumber;
    private RadioButton male;
    private RadioButton female;
    private RadioGroup mGenderGroup;
    //private Spinner spinner;
    private Uri mUri;
    private long mFarmerId;
    private int coopId;
    private LinearLayout content_spiner_coop;


    public UpdateGeneralInformationVendor() {
        super();
    }

    public static UpdateGeneralInformationVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateGeneralInformationVendor fragment = new UpdateGeneralInformationVendor();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPageVendor = mCallbacks.onGetPage(mKey);

        Intent intent = getActivity().getIntent();

        if (intent.hasExtra("farmerId")) {
            mFarmerId = intent.getLongExtra("farmerId", 0);
            mUri = BfwContract.Farmer.buildFarmerUri(mFarmerId);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(1, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String farmerSelection = BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer._ID + " =  ? ";

        if (mUri != null) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    null,
                    farmerSelection,
                    new String[]{Long.toString(mFarmerId)},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            String name = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_NAME));
            String phone = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_PHONE));
            String gender = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_GENDER));

            coopId = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_COOP_USER_ID));
            if (gender.equals("male")) {
                male.setChecked(true);
            } else {
                female.setChecked(true);
            }
            names.setText(name);
            phoneNumber.setText(phone);
            generalVendor.setName(name);
            generalVendor.setPhoneNumber(phone);
            mPageVendor.setData("generalVendor", generalVendor);

            setDefaultGender();

            //setSpinnerItemById(spinner, coopId);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDataValidEventB(DataValidEventB validEventB) {

        String nameInfo = names.getText().toString();
        String phoneInfo = phoneNumber.getText().toString();

        //Add Regex
        if (!TextUtils.isEmpty(nameInfo) && nameInfo.length() >= 4 && !TextUtils.isEmpty(phoneInfo)) {
            EventBus.getDefault().post(new DataValidEventR(true));
        } else {
            names.setError("Name is Required");
            phoneNumber.setError("Phone Number is required or not Valid");
            EventBus.getDefault().post(new DataValidEventR(false));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.general_information, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.gen_title));

        names = rootView.findViewById(R.id.name_f);
        phoneNumber = rootView.findViewById(R.id.name_phone);
        male = rootView.findViewById(R.id.radio_male);
        female = rootView.findViewById(R.id.radio_female);
        //spinner = rootView.findViewById(R.id.spinner_coops_infos);
        mGenderGroup = rootView.findViewById(R.id.gender_group);
        content_spiner_coop = rootView.findViewById(R.id.content_spiner_coop);
        content_spiner_coop.setVisibility(View.GONE);


        //set default gender
        setDefaultGender();

        //listen for change on gender
        mGenderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.radio_male) {
                    generalVendor.setGender(true);
                    mPageVendor.getData().putParcelable("generalVendor", generalVendor);
                } else if (i == R.id.radio_female) {
                    generalVendor.setGender(false);
                    mPageVendor.getData().putParcelable("generalVendor", generalVendor);
                }
            }
        });

        names.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generalVendor.setName(charSequence.toString());
                mPageVendor.setData("generalVendor", generalVendor);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generalVendor.setPhoneNumber(charSequence.toString());
                mPageVendor.setData("generalVendor", generalVendor);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //populateSpinner();
        //spinner.setOnItemSelectedListener(this);
        return rootView;
    }

    public void setDefaultGender() {
        if (mGenderGroup.getCheckedRadioButtonId() == R.id.radio_male) {
            generalVendor.setGender(true);
            mPageVendor.getData().putParcelable("generalVendor", generalVendor);
        } else if (mGenderGroup.getCheckedRadioButtonId() == R.id.radio_female) {
            generalVendor.setGender(false);
            mPageVendor.getData().putParcelable("generalVendor", generalVendor);
        }
    }

/*    public void populateSpinner() {
        String[] fromColumns = {BfwContract.Coops.COLUMN_COOP_NAME};

        // View IDs to map the columns (fetched above) into
        int[] toViews = {
                android.R.id.text1
        };
        Cursor cursor = getActivity().getContentResolver().query(
                BfwContract.Coops.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    getContext(), // context
                    android.R.layout.simple_spinner_item, // layout file
                    cursor, // DB cursor
                    fromColumns, // data to bind to the UI
                    toViews, // views that'll represent the data from `fromColumns`
                    0
            );

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Create the list view and bind the adapter
            spinner.setAdapter(adapter);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (PageFragmentCallbacksVendor) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

  /*  @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Cursor cursor = (Cursor) spinner.getSelectedItem();
        generalVendor.setCoopsName(cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_COOP_NAME)));
        mPageVendor.setData("generalVendor", generalVendor);
    }

    public void setSpinnerItemById(Spinner spinner, int _id) {
        int spinnerCount = spinner.getCount();
        for (int i = 0; i < spinnerCount; i++) {
            Cursor value = (Cursor) spinner.getItemAtPosition(i);
            long id = value.getLong(value.getColumnIndex(BfwContract.Coops.COLUMN_COOP_SERVER_ID));
            if (id == _id) {
                spinner.setSelection(i);
                generalVendor.setCoopsName(value.getString(value.getColumnIndex(BfwContract.Coops.COLUMN_COOP_NAME)));
                mPageVendor.setData("generalVendor", generalVendor);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }*/
}
