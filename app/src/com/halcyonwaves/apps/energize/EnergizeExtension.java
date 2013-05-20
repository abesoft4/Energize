package com.halcyonwaves.apps.energize;

import android.content.Intent;
import android.net.Uri;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

public class EnergizeExtension extends DashClockExtension {

	protected void onUpdateData(int reason) {
		publishUpdate(new ExtensionData()
				.visible(true)
				.icon(R.drawable.ic_launcher)
				.status("Hello")
				.expandedTitle("Hello, world!")
				.expandedBody("This is an example.")
				.clickIntent(
						new Intent(Intent.ACTION_VIEW, Uri
								.parse("http://www.google.com"))));
	}

}
