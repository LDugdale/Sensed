package com.example.android.sensed.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by mnt_x on 05/06/2017.
 */

public class EventIntentService extends IntentService {

    public EventIntentService() {
        super("EventIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
