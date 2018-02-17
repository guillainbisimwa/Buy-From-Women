package com.nijus.alino.bfwcoopmanagement.farmers.ui.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.CreateFarmerFragment;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.wizard.FarmerWizardModel;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.wizard.UpdateWizardModel;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.PageFragmentCallbacks;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;

public class CreateFarmerActivity extends AppCompatActivity implements CreateFarmerFragment.OnFragmentInteractionListener,
        PageFragmentCallbacks {

    private FarmerWizardModel farmerWizardModel = new FarmerWizardModel(this);
    /*private ActionBar actionBar;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        getSupportActionBar().setElevation(0.0f);
        //AppBarLayout appBarLayout = actionBar.getCustomView();
        //getSupportActionBar().setElevation(0);
        //getActionBar().setElevation(0);


        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setElevation(0);

        setContentView(R.layout.create_farmer_fragment);

        //actionBar.setElevation(0);
        if (actionBar != null) {
            //actionBar.setElevation(0);
        }

        // chek si orientation est paysage pour supprimer l'eleveation du toolbar
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            // Portrait Mode
            //Toast.makeText(this,"P",Toast.LENGTH_LONG).show();
            //actionBar.setElevation(0);
        } else {
            // Landscape Mode
            //Toast.makeText(this,"L",Toast.LENGTH_LONG).show();
            //actionBar.setElevation(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public FarmerWizardModel onGetFarmerWizard() {
        return farmerWizardModel;
    }

    @Override
    public UpdateWizardModel onGetUpdateWizard() {
        return null;
    }

    @Override
    public Page onGetPage(String key) {
        return farmerWizardModel.findByKey(key);
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
