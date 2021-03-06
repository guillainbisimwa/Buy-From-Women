package com.nijus.alino.bfwcoopmanagement.products.sync;


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
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UpdateSyncProductBkgrnd extends IntentService {

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");
    public final String LOG_TAG = UpdateSyncProductBkgrnd.class.getSimpleName();

    public UpdateSyncProductBkgrnd() {
        super("");
    }

    public UpdateSyncProductBkgrnd(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        //get non sync product to the server (is_sync)
        int dataCount = 0;
        int productServerId;
        long id;
        Cursor cursor = null, landCursor = null;

        String selection = BfwContract.ProductTemplate.TABLE_NAME + "." +
                BfwContract.ProductTemplate.COLUMN_IS_SYNC + " =  1 AND " +
                BfwContract.ProductTemplate.TABLE_NAME + "." +
                BfwContract.ProductTemplate.COLUMN_IS_UPDATE + " = 0";

        String selectionProduct = BfwContract.ProductTemplate.TABLE_NAME + "." +
                BfwContract.ProductTemplate.COLUMN_IS_SYNC + " =  0 ";

        String selectionProduct_id = BfwContract.ProductTemplate.TABLE_NAME + "." +
                BfwContract.ProductTemplate._ID + " =  ? ";

        String bankInfos = "\"bank_ids\": [],";
        try {
            cursor = getContentResolver().query(BfwContract.ProductTemplate.CONTENT_URI, null, selection, null, null);
            if (cursor != null) {
                dataCount = cursor.getCount();

                while (cursor.moveToNext()) {
                    id = cursor.getLong(cursor.getColumnIndex(BfwContract.ProductTemplate._ID));
                    productServerId = cursor.getInt(cursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_SERVER_ID));

                    String nameProduct = cursor.getString(cursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_PRODUCT_NAME));
                    Double productPrice = 0.0;
                    productPrice = cursor.getDouble(cursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_PRICE));

                    int productQty = cursor.getInt(cursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_VENDOR_QTY));
                    String grade = cursor.getString(cursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_HARVEST_GRADE));

                    int local_farmer_id = cursor.getInt(cursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_FARMER_ID));
                    int local_season_id = cursor.getInt(cursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_HARVEST_SEASON));


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

                    String selectHarvsetId = BfwContract.HarvestSeason.TABLE_NAME + "." +
                            BfwContract.HarvestSeason._ID + " =  ? ";

                    cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                            selectHarvsetId,
                            new String[]{Long.toString(local_season_id)},
                            null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            local_season_id = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                        }
                    }


                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(240, TimeUnit.SECONDS)
                            .writeTimeout(240, TimeUnit.SECONDS)
                            .readTimeout(240, TimeUnit.SECONDS)
                            .build();

                    String bodyContent;

                    bodyContent = "{" +
                            "\"name\": \"" + nameProduct + "\", " +
                            "\"harvest_grade\": \"" + grade + "\"," +
                            "\"harvest_season\": " + local_season_id + "," +
                            "\"vendor_qty\": " + productQty + "," +
                            "\"standard_price\": " + productPrice + ",";

                    String userType = Utils.getUserType(getApplicationContext());

                    if (userType.equals("Admin") || userType.equals("Agent")) {
                        bodyContent += "\"farmer_id\": " + local_farmer_id + "" +
                                "}";
                    } else if (userType.equals("Vendor")) {
                        int vendorId = Utils.getVendorServerId(getApplicationContext());
                        bodyContent += "\"vendor_farmer_id\": " + vendorId + "" +
                                "}";
                    }

                    String API_INFO = BuildConfig.DEV_API_URL + "product.template" + "/" + productServerId;

                    RequestBody bodyProduct = RequestBody.create(JSON, bodyContent);

                    Request requesProduct = new Request.Builder()
                            .url(API_INFO)
                            .header("Content-Type", "text/html")
                            .header("Access-Token", appToken)
                            .method("PUT", bodyProduct)
                            .build();
                    try {
                        Response responseProduct = client.newCall(requesProduct).execute();
                        ResponseBody responseBodyProduct = responseProduct.body();
                        if (responseBodyProduct != null) {
                            String farmerDataInfo = responseBodyProduct.string();
                            if (farmerDataInfo.equals("{}")) {

                                //update localId
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(BfwContract.ProductTemplate.COLUMN_SERVER_ID, productServerId);
                                contentValues.put(BfwContract.ProductTemplate.COLUMN_IS_SYNC, 1);
                                contentValues.put(BfwContract.ProductTemplate.COLUMN_IS_UPDATE, 1);

                                getContentResolver().update(BfwContract.ProductTemplate.CONTENT_URI, contentValues, selectionProduct_id, new String[]{Long.toString(id)});
                            }
                        }

                    } catch (IOException e) {
                        EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.syncing_error_product), false));
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (landCursor != null) {
                landCursor.close();
            }
        }

        //post event sync after
        if (dataCount > 0)
            EventBus.getDefault().post(new SyncDataEvent(getString(com.nijus.alino.bfwcoopmanagement.R.string.prod_sync), true));
    }
}
