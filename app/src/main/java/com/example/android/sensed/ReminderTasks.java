package com.example.android.sensed;
/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.Intent;

public class ReminderTasks {

    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    public static final String ACTION_ENTRY_REMINDER = "charging-reminder";

    public static void executeTask(Context context, String action) {

        if (ACTION_ENTRY_REMINDER.equals(action)) {
            issueChargingReminder(context);
        }


    }


    private static void issueChargingReminder(Context context) {
//        PreferenceUtilities.incrementChargingReminderCount(context);
//        NotificationUtils.remindUserBecauseCharging(context);
        System.out.println("issueChargingReminder");
        Intent intent = new Intent(context, ChatHeadService.class);
        context.startService(intent);
    }

}