/*
 * Copyright (C) 2019-2020 The exTHmUI Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.exthmui.theme.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.preference.PreferenceManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SoundUtil {

    private static final String TAG = "SoundUtil";

    public final static int TYPE_ALARM = RingtoneManager.TYPE_ALARM;
    public final static int TYPE_NOTIFICATION = RingtoneManager.TYPE_NOTIFICATION;
    public final static int TYPE_RINGTONE = RingtoneManager.TYPE_RINGTONE;

    public static boolean setRingtone(Context context, String fileName, InputStream inputStream, int type) {

        // create new file
        File mediaFile = new File(context.getExternalFilesDir("sounds") + "/" + System.currentTimeMillis() + "_" + fileName);
        FileUtil.createPath(mediaFile);
        try {
            FileUtil.saveInputStream(mediaFile.getAbsolutePath(), inputStream, false);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        int extPos = fileName.indexOf(".");
        if (extPos == -1) extPos = fileName.length() - 1;
        String title = fileName.substring(0, extPos);

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, mediaFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, title);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
        values.put(MediaStore.Audio.AudioColumns.IS_RINGTONE, false);
        values.put(MediaStore.Audio.AudioColumns.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.AudioColumns.IS_ALARM, false);
        values.put(MediaStore.Audio.AudioColumns.IS_MUSIC, false);

        String typeString = "";
        switch (type) {
            case TYPE_ALARM:
                typeString = "alarm";
                values.put(MediaStore.Audio.AudioColumns.IS_ALARM, true);
                break;
            case TYPE_NOTIFICATION:
                typeString = "notification";
                values.put(MediaStore.Audio.AudioColumns.IS_NOTIFICATION, true);
                break;
            case TYPE_RINGTONE:
                typeString = "ringtone";
                values.put(MediaStore.Audio.AudioColumns.IS_RINGTONE, true);
                break;
            default:
                return false;
        }

        // remove old ringtone
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String oldRingtoneTitle = preferences.getString("applied_title_" + typeString, "no_ringtone");
        String oldRingtoneFile = preferences.getString("applied_file_" + typeString, "no_ringtone");
        context.getContentResolver().delete(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStore.MediaColumns.TITLE + " = ?",
                new String[] {oldRingtoneTitle});
        new File(oldRingtoneFile).delete();

        Uri uri = context.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
        RingtoneManager.setActualDefaultRingtoneUri(context, type, uri);

        preferences.edit().putString("applied_title_" + typeString, title).apply();
        preferences.edit().putString("applied_file_" + typeString, mediaFile.getAbsolutePath()).apply();

        return true;
    }

}
