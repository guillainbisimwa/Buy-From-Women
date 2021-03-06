package com.nijus.alino.bfwcoopmanagement.loans.sync;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.nijus.alino.bfwcoopmanagement.BuildConfig;
import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UpdateSyncLoanBkgrnd extends IntentService {

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");
    public final String LOG_TAG = UpdateSyncLoanBkgrnd.class.getSimpleName();

    public UpdateSyncLoanBkgrnd() {
        super("");
    }

    public UpdateSyncLoanBkgrnd(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        //get non sync farmer to the server (is_sync)
        int dataCount = 0;
        int loanServerId;
        long id;
        Cursor cursor = null;

        String selection = BfwContract.Loan.TABLE_NAME + "." +
                BfwContract.Loan.COLUMN_IS_SYNC + " =  1 AND " +
                BfwContract.Loan.TABLE_NAME + "." +
                BfwContract.Loan.COLUMN_IS_UPDATE + " = 0";

        String selectionLoan_id = BfwContract.Loan.TABLE_NAME + "." +
                BfwContract.Loan._ID + " =  ? ";

        try {
            cursor = getContentResolver().query(BfwContract.Loan.CONTENT_URI, null, selection, null, null);
            if (cursor != null) {
                dataCount = cursor.getCount();

                while (cursor.moveToNext()) {
                    id = cursor.getLong(cursor.getColumnIndex(BfwContract.Loan._ID));
                    loanServerId = cursor.getInt(cursor.getColumnIndex(BfwContract.Loan.COLUMN_SERVER_ID));

                    int local_farmer_id = cursor.getInt(cursor.getColumnIndex(BfwContract.Loan.COLUMN_FARMER_ID));
                    Long start_date_long = cursor.getLong(cursor.getColumnIndex(BfwContract.Loan.COLUMN_START_DATE));

                    Date start_date_date = new Date(start_date_long);
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    String date_string = DateFormat.getDateInstance().format(start_date_date);

                    Double amount = cursor.getDouble(cursor.getColumnIndex(BfwContract.Loan.COLUMN_AMOUNT));
                    Double interest_rate = cursor.getDouble(cursor.getColumnIndex(BfwContract.Loan.COLUMN_INTEREST_RATE));
                    Double duration = cursor.getDouble(cursor.getColumnIndex(BfwContract.Loan.COLUMN_DURATION));
                    String purpose = cursor.getString(cursor.getColumnIndex(BfwContract.Loan.COLUMN_PURPOSE));
                    String financial_institution = cursor.getString(cursor.getColumnIndex(BfwContract.Loan.COLUMN_FINANCIAL_INSTITUTION));

                    //Get farmer id from server
                    String selectFarmerId = BfwContract.Farmer.TABLE_NAME + "." +
                            BfwContract.Farmer._ID + " =  ? ";

                    cursor = getContentResolver().query(BfwContract.Farmer.CONTENT_URI, null,
                            selectFarmerId,
                            new String[]{Long.toString(local_farmer_id)},
                            null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            local_farmer_id = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_FARMER_SERVER_ID));
                        }
                    }

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(240, TimeUnit.SECONDS)
                            .writeTimeout(240, TimeUnit.SECONDS)
                            .readTimeout(240, TimeUnit.SECONDS)
                            .build();

                    //Construct body
                    String bodyContent;

                    String userType = Utils.getUserType(getApplicationContext());

                    // if agent show farmer with coop server user id
                    bodyContent = "{" +
                            "\"purpose\": \"" + purpose + "\", " +
                            "\"financial_institution\": \"" + financial_institution + "\"," +
                            "\"amount\": " + amount + "," +
                            "\"duration\": " + duration + ", " +
                            "\"interest_rate\": " + interest_rate + ", " +
                            "\"start_date\": \"" + date_string + "\", ";

                    if (userType.equals("Admin") || userType.equals("Agent")) {
                        bodyContent += "\"partner_id\": " + local_farmer_id + "" +
                                "}";
                    } else if (userType.equals("Vendor")) {
                        int vendorId = Utils.getVendorServerId(getApplicationContext());
                        bodyContent += "\"vendor_id\": " + vendorId + "" +
                                "}";
                    }

                    String API_INFO = BuildConfig.DEV_API_URL + "res.partner.loan" + "/" + loanServerId;

                    RequestBody bodyLoan = RequestBody.create(JSON, bodyContent);

                    Request requesLoan = new Request.Builder()
                            .url(API_INFO)
                            .header("Content-Type", "text/html")
                            .header("Access-Token", appToken)
                            .method("PUT", bodyLoan)
                            .build();
                    try {
                        Response responseLoan = client.newCall(requesLoan).execute();
                        ResponseBody responseBodyLoan = responseLoan.body();
                        if (responseBodyLoan != null) {
                            String farmerDataInfo = responseBodyLoan.string();
                            if (farmerDataInfo.equals("{}")) {

                                //update localId
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(BfwContract.Loan.COLUMN_SERVER_ID, loanServerId);
                                contentValues.put(BfwContract.Loan.COLUMN_IS_SYNC, 1);
                                contentValues.put(BfwContract.Loan.COLUMN_IS_UPDATE, 1);

                                getContentResolver().update(BfwContract.Loan.CONTENT_URI, contentValues, selectionLoan_id, new String[]{Long.toString(id)});
                            }
                        }

                    } catch (IOException e) {
                        EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.syncing_error_loan), false));
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        //post event sync after
        if (dataCount > 0)
            EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.add_loan_msg_sync), true));
    }
}
