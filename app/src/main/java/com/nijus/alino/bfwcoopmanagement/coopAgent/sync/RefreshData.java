package com.nijus.alino.bfwcoopmanagement.coopAgent.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class RefreshData extends IntentService {

    public RefreshData() {
        super("");
    }

    public RefreshData(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //Check non sync data and sync
        startService(new Intent(this, SyncAgentBackground.class));

    }
}
