package com.nijus.alino.bfwcoopmanagement.vendors.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.fragment.UpdateVendorFragment;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.wizard.VendorWizardModelVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.wizard.UpdateWizardModelVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.PageFragmentCallbacksVendor;

public class UpdateVendor extends AppCompatActivity implements PageFragmentCallbacksVendor, UpdateVendorFragment.OnFragmentInteractionListener {
    private UpdateWizardModelVendorVendor vendorWizardModel = new UpdateWizardModelVendorVendor(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vendor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public UpdateWizardModelVendorVendor onGetUpdateWizard() {
        return vendorWizardModel;
    }

    @Override
    public VendorWizardModelVendor onGetFarmerWizard() {
        return null;
    }

    @Override
    public PageVendorVendor onGetPage(String key) {
        return vendorWizardModel.findByKey(key);
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
    public void onFragmentInteraction(Uri uri) {

    }
}