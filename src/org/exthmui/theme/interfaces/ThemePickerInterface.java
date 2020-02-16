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

package org.exthmui.theme.interfaces;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;

import org.exthmui.theme.models.ThemeBase;

import java.util.List;

public interface ThemePickerInterface {
    Drawable getThemeImage(String packageName);
    List<ThemeBase> getThemeList();
    void updateThemeList();
    void onThemeItemClick(AdapterView<?> parent, View view, int position, long id);
    boolean onThemeItemLongClick(AdapterView<?> parent, View view, int position, long id);
}
