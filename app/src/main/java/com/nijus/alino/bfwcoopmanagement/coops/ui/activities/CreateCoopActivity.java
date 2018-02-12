package com.nijus.alino.bfwcoopmanagement.coops.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.fragment.CreateCoopFragment;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.wizard.CoopWizardModel;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.wizard.UpdateWizardModel;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui.PageFragmentCallbacks;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;

public class CreateCoopActivity extends AppCompatActivity implements CreateCoopFragment.OnFragmentInteractionListener, PageFragmentCallbacks {

    //private VendorWizardModelVendor farmerWizardModel = new VendorWizardModelVendor(this);
    private CoopWizardModel coopWizardModel = new CoopWizardModel(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_coop_fragment);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
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
    public Page onGetPage(String key) {
        //return null;
        return coopWizardModel.findByKey(key);
    }

    @Override
    public CoopWizardModel onGetFarmerWizard() {
        //return null;
        return coopWizardModel;
    }

    @Override
    public UpdateWizardModel onGetUpdateWizard() {
        return null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
