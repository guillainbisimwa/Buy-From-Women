package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.AvailableResources;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui.update.UpdateAvailableResourcesFragment;

import java.util.ArrayList;

public class UpdateAvailableResourcesPage extends Page {

    public static final String AVAILABLE_RES_KEY = "key";

    private AvailableResources availableResources = new AvailableResources();

    public UpdateAvailableResourcesPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return UpdateAvailableResourcesFragment.create(getKey());
    }

    @Override
    public Bundle getData() {
        return super.getData();
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public boolean isRequired() {
        return super.isRequired();
    }

    @Override
    void setParentKey(String parentKey) {
        super.setParentKey(parentKey);
    }

    @Override
    public Page findByKey(String key) {
        return super.findByKey(key);
    }

    @Override
    public void flattenCurrentPageSequence(ArrayList<Page> dest) {
        super.flattenCurrentPageSequence(dest);
    }

    @Override
    public String getKey() {
        return super.getKey();
    }

    @Override
    public boolean isCompleted() {
        return super.isCompleted();
    }

    @Override
    public void resetData(Bundle data) {
        super.resetData(data);
    }

    @Override
    public void notifyDataChanged() {
        super.notifyDataChanged();
    }

    @Override
    public Page setRequired(boolean required) {
        return super.setRequired(required);
    }

    public UpdateAvailableResourcesPage setValue(AvailableResources availableResources) {
        mData.putParcelable(AVAILABLE_RES_KEY, availableResources);
        return this;
    }
}
