package com.nijus.alino.bfwcoopmanagement.products.ui.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.events.ProcessingFarmerEvent;
import com.nijus.alino.bfwcoopmanagement.events.RefreshProductLoader;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.products.ui.fragment.ProductDialogFragment;
import com.nijus.alino.bfwcoopmanagement.products.ui.fragment.ProductListFragment;
import com.nijus.alino.bfwcoopmanagement.ui.activities.BaseActivity;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ProductActivity extends BaseActivity implements
        ProductListFragment.OnListFragmentInteractionListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product_container);

        FloatingActionButton fab = findViewById(R.id.fab_product_orders);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(this);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences(getResources().getString(R.string.application_key),
                Context.MODE_PRIVATE);
        String groupName = prefs.getString(getResources().getString(R.string.g_name), "123");

        if (groupName.equals("Buyer")) {
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_product_orders) {
            ProductDialogFragment dialogFragment = new ProductDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "productDialog");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = super.getDrawerLayout();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            SharedPreferences prefs = getApplicationContext().getSharedPreferences(getResources().getString(R.string.application_key),
                    Context.MODE_PRIVATE);

            String groupName = prefs.getString(getResources().getString(R.string.g_name), "123");
            if (groupName.equals("Buyer")) {
                super.onBackPressed();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            } else {
                NavUtils.navigateUpFromSameTask(this);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    public void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY).trim();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProcessingFarmerEvent(ProcessingFarmerEvent processingFarmerEvent) {
        Toast.makeText(this, processingFarmerEvent.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        if (saveDataEvent.isSuccess()) {
            Toast.makeText(getApplicationContext(), saveDataEvent.getMessage(), Toast.LENGTH_LONG).show();
            EventBus.getDefault().post(new RefreshProductLoader());
        } else {
            Toast.makeText(getApplicationContext(), saveDataEvent.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncDataEvent(SyncDataEvent syncDataEvent) {
        if (syncDataEvent.isSuccess()) {
            Toast.makeText(getApplicationContext(), syncDataEvent.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), syncDataEvent.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
