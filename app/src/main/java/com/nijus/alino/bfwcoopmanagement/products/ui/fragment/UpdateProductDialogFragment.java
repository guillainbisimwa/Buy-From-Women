package com.nijus.alino.bfwcoopmanagement.products.ui.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;

import java.util.Calendar;

public class UpdateProductDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, View.OnClickListener {

    private LinearLayout mProductContainer;
    private Button addProductItem;
    private Spinner vendor;
    private Spinner harvsetSeason;
    private Spinner grade;

    AutoCompleteTextView product_name;
    AutoCompleteTextView quantity;
    AutoCompleteTextView cost;
    AutoCompleteTextView sale_price;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View viewContainer = getActivity().getLayoutInflater().inflate(R.layout.product_order_detail, null);

        mProductContainer = viewContainer.findViewById(R.id.productContainer);
        addProductItem = viewContainer.findViewById(R.id.add_po_proposal);
        addProductItem.setOnClickListener(this);

        vendor = viewContainer.findViewById(R.id.vendor);
        harvsetSeason = viewContainer.findViewById(R.id.harvsetSeason);
        grade = viewContainer.findViewById(R.id.grade);

        product_name = viewContainer.findViewById(R.id.product_name);
        quantity = viewContainer.findViewById(R.id.quantity);
        cost = viewContainer.findViewById(R.id.cost);
        sale_price = viewContainer.findViewById(R.id.sale_price);

        builder.setView(viewContainer)
                .setPositiveButton(R.string.msg_ok, this)
                .setNegativeButton(R.string.msg_cancel, this);

        populateSpinner();
        return builder.create();
    }

    public void populateSpinner() {
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
            //coops.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_po_proposal) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            //add product datastructure

            Spinner spinner = new Spinner(getContext());

            EditText quantity = new EditText(getContext());
            quantity.setWidth(AppBarLayout.LayoutParams.MATCH_PARENT);
            quantity.setHeight(AppBarLayout.LayoutParams.WRAP_CONTENT);
            quantity.setHint(getResources().getString(R.string.quantity));
            quantity.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

            EditText unitPrice = new EditText(getContext());
            unitPrice.setWidth(AppBarLayout.LayoutParams.MATCH_PARENT);
            unitPrice.setHeight(AppBarLayout.LayoutParams.WRAP_CONTENT);
            unitPrice.setHint(getResources().getString(R.string.price_rwf));
            unitPrice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

            linearLayout.addView(spinner);
            linearLayout.addView(quantity);
            linearLayout.addView(unitPrice);

            mProductContainer.addView(linearLayout, 1);
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (Dialog.BUTTON_POSITIVE == i) {
            Toast.makeText(getContext(), "Update Purchase Order Coming soon !!!", Toast.LENGTH_LONG).show();
        }
    }

}
