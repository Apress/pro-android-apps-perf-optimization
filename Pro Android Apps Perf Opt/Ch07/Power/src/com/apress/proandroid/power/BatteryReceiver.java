package com.apress.proandroid.power;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BatteryReceiver extends BroadcastReceiver {
	private static final String TAG = "BatteryReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		String text;
		
		if (Intent.ACTION_BATTERY_LOW.equals(action)) {
			text = "Low power";
		} else if (Intent.ACTION_BATTERY_OKAY.equals(action)) {
			text = "Power okay (not low anymore)";
		} else if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
			text = "Power now connected";
		} else if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
			text = "Power now disconnected";
		} else {
			return;
		}
		
		Log.i(TAG, text);
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
