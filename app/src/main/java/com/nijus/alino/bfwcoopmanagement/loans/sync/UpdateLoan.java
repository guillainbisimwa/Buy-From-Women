package com.nijus.alino.bfwcoopmanagement.loans.sync;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.loans.pojo.PojoLoan;
import com.nijus.alino.bfwcoopmanagement.products.pojo.PojoProduct;
import com.nijus.alino.bfwcoopmanagement.products.sync.UpdateSyncProduct;
import com.nijus.alino.bfwcoopmanagement.products.sync.UpdateSyncProductBkgrnd;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

/**
 * Created by Guillain-B on 19/02/2018.
 */

public class UpdateLoan extends IntentService {
    public final String LOG_TAG = UpdateLoan.class.getSimpleName();
    private int id_loan;

    public UpdateLoan() {
        super("");
    }

    public UpdateLoan(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            id_loan = intent.getIntExtra("loan_id", 0);
            String loanSelect = BfwContract.Loan.TABLE_NAME + "." +
                    BfwContract.Loan._ID + " =  ? ";
            Bundle loanData = intent.getBundleExtra("loan_data");

            int isSyncProduct = 0;
            Cursor loanCursor = null;

            try {
                loanCursor = getContentResolver().query(BfwContract.Loan.CONTENT_URI, null, loanSelect,
                        new String[]{Long.toString(id_loan)}, null);
                if (loanCursor != null) {
                    while (loanCursor.moveToNext()) {
                        isSyncProduct = loanCursor.getInt(loanCursor.getColumnIndex(BfwContract.Loan.COLUMN_IS_SYNC));
                    }
                }
            } finally {
                if (loanCursor != null) loanCursor.close();
            }

            PojoLoan pojoLoan = loanData.getParcelable("loan");

            int farmer_id = 0;
            /*int coop_id = 0;
            int vendor_id = 0;*/
            Long start_date = null;
            Double amount = 0.0;
            Double interest_rate = 0.0;
            Double duration = 0.0;
            String purpose = "";
            String financial_institution = "";


            if (pojoLoan != null) {
                 /* coop_id = pojoLoan.getCoop_id();
                vendor_id = pojoLoan.getVendor_id(); */
                farmer_id = pojoLoan.getFarmer_id();

                start_date = pojoLoan.getStart_date();
                amount = pojoLoan.getAmount();
                interest_rate = pojoLoan.getInterest_rate();
                duration = pojoLoan.getDuration();
                purpose = pojoLoan.getPurpose();
                financial_institution = pojoLoan.getFinancial_institution();

                ContentValues contentValues = new ContentValues();
                contentValues.put(BfwContract.Loan.COLUMN_FARMER_ID, farmer_id);
                contentValues.put(BfwContract.Loan.COLUMN_START_DATE, start_date);
                contentValues.put(BfwContract.Loan.COLUMN_AMOUNT, amount);
                contentValues.put(BfwContract.Loan.COLUMN_INTEREST_RATE, interest_rate);
                contentValues.put(BfwContract.Loan.COLUMN_DURATION, duration);
                contentValues.put(BfwContract.Loan.COLUMN_PURPOSE, purpose);
                contentValues.put(BfwContract.Loan.COLUMN_FINANCIAL_INSTITUTION, financial_institution);

                contentValues.put(BfwContract.Loan.COLUMN_IS_SYNC, isSyncProduct);
                contentValues.put(BfwContract.Loan.COLUMN_IS_UPDATE, 0);

                getContentResolver().update(BfwContract.Loan.CONTENT_URI, contentValues,loanSelect,new String[]{Integer.toString(id_loan)});

                //Post event after saving data
                EventBus.getDefault().post(new SaveDataEvent());
                //sync if network available
                if (Utils.isNetworkAvailable(getApplicationContext())) {
                    //start job service
                   startService(new Intent(this, UpdateSyncLoanBkgrnd.class));
                } else {
                    //schedule a job if not network is available
                    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

                    Job job = dispatcher.newJobBuilder()
                            .setService(UpdateSyncLoan.class)
                            .setTag(UUID.randomUUID().toString())
                            .setConstraints(Constraint.ON_ANY_NETWORK)
                            .build();

                    dispatcher.mustSchedule(job);
                }
            }

        }
    }
}
