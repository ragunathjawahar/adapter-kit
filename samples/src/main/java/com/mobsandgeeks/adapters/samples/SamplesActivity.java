/*
 * Copyright � 2013 Mobs & Geeks
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

package com.mobsandgeeks.adapters.samples;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.os.Bundle;

public class SamplesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_samples);

		initTabs();
	}

	private void initTabs() {
		ActionBar actionBar = this.getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab tab1 = actionBar
				.newTab()
				.setText("Simple List")
				.setTabListener(
						new TabListener<SimpleListFragment>(this,
								"simple_list", SimpleListFragment.class));
		actionBar.addTab(tab1);

		Tab tab2 = actionBar
				.newTab()
				.setText("Sectioned List")
				.setTabListener(
						new TabListener<SectionedListFragment>(this,
								"sectioned_list", SectionedListFragment.class));
		actionBar.addTab(tab2);

		Tab tab3 = actionBar
				.newTab()
				.setText("Circular List")
				.setTabListener(
						new TabListener<CircularListFragment>(this,
								"circular_list", CircularListFragment.class));
		actionBar.addTab(tab3);

        Tab tab4 = actionBar
                .newTab()
                .setText("Image List")
                .setTabListener(
                        new TabListener<ImageListFragment>(this,
                                "image_list", ImageListFragment.class));
        actionBar.addTab(tab4);
	}



}
