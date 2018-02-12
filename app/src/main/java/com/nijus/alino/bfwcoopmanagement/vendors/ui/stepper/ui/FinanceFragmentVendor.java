package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.FinanceVendor;

public class FinanceFragmentVendor extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String ARG_KEY = "key";
    private String mKey;
    private PageVendorVendor mPageVendor;
    private PageFragmentCallbacksVendor mCallbacks;

    private FinanceVendor financeVendor = new FinanceVendor();

    private CheckBox outstandingLoan;
    private AutoCompleteTextView totLoanAmount;
    private AutoCompleteTextView totOutstanding;
    private AutoCompleteTextView interestRate;
    private AutoCompleteTextView duration;
    private Spinner loanProvider;
    private CheckBox mobileMoneyAccount;
    private CheckBox inPut;
    private CheckBox aggregation;
    private CheckBox other;

    public FinanceFragmentVendor() {

    }

    public static FinanceFragmentVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        FinanceFragmentVendor fragment = new FinanceFragmentVendor();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPageVendor = mCallbacks.onGetPage(mKey);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.finance_layout, container, false);
        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.finance));

        outstandingLoan = rootView.findViewById(R.id.outstanding_loan);

        totLoanAmount = rootView.findViewById(R.id.tot_loan_amount);
        totOutstanding = rootView.findViewById(R.id.tot_outstanding);
        interestRate = rootView.findViewById(R.id.interest_rate);
        duration = rootView.findViewById(R.id.duration_month);
        loanProvider = rootView.findViewById(R.id.loan_provider);

        mobileMoneyAccount = rootView.findViewById(R.id.mobile_m_account);
        inPut = rootView.findViewById(R.id.l_input);
        aggregation = rootView.findViewById(R.id.l_aggregation);
        other = rootView.findViewById(R.id.l_other);

        //set default outstanding loan
        boolean isOutstandingLoan = outstandingLoan.isChecked();
        financeVendor.setOutstandingLoan(isOutstandingLoan);

        //set default mobile money account and attach a listener
        boolean isMobileMoney = mobileMoneyAccount.isChecked();
        financeVendor.setHasMobileMoneyAccount(isMobileMoney);

        //set default input
        boolean isInput = inPut.isChecked();
        financeVendor.setInput(isInput);

        //set default aggregation
        boolean isAggregation = aggregation.isChecked();
        financeVendor.setAggregation(isAggregation);

        //set other
        boolean isOther = other.isChecked();
        financeVendor.setOtherLp(isOther);

        mPageVendor.getData().putParcelable("financeVendor", financeVendor);

        outstandingLoan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    financeVendor.setOutstandingLoan(true);
                } else {
                    financeVendor.setOutstandingLoan(false);
                }
                mPageVendor.getData().putParcelable("financeVendor", financeVendor);
            }
        });

        mobileMoneyAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    financeVendor.setHasMobileMoneyAccount(true);
                } else {
                    financeVendor.setHasMobileMoneyAccount(false);
                }
                mPageVendor.getData().putParcelable("financeVendor", financeVendor);
            }
        });

        inPut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    financeVendor.setInput(true);
                } else {
                    financeVendor.setInput(false);
                }
                mPageVendor.getData().putParcelable("financeVendor", financeVendor);
            }
        });

        aggregation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    financeVendor.setInput(true);
                } else {
                    financeVendor.setInput(false);
                }
                mPageVendor.getData().putParcelable("financeVendor", financeVendor);
            }
        });


        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    financeVendor.setOtherLp(true);
                } else {
                    financeVendor.setOtherLp(false);
                }
                mPageVendor.getData().putParcelable("financeVendor", financeVendor);

            }
        });

        totLoanAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    financeVendor.setTotLoanAmount(Integer.parseInt(charSequence.toString()));
                    mPageVendor.getData().putParcelable("financeVendor", financeVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totOutstanding.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    financeVendor.setTotOutstanding(Integer.parseInt(charSequence.toString()));
                    mPageVendor.getData().putParcelable("financeVendor", financeVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        interestRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    financeVendor.setInterestRate(Double.parseDouble(charSequence.toString()));
                    mPageVendor.getData().putParcelable("financeVendor", financeVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    financeVendor.setDurationInMonth(Integer.parseInt(charSequence.toString()));
                    mPageVendor.getData().putParcelable("financeVendor", financeVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.loan_provider, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loanProvider.setAdapter(adapter);

        loanProvider.setOnItemSelectedListener(this);

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        financeVendor.setLoanProvider(adapterView.getItemAtPosition(i).toString());
        mPageVendor.getData().putParcelable("financeVendor", financeVendor);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

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
}