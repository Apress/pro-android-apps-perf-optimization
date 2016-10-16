package com.apress.proandroid.power;

import static android.os.BatteryManager.*; // note the 'static' keyword (remove it and see what happens)

import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class BatteryInfoActivity extends Activity {
	private static final String TAG = "BatteryInfo";
	
	private BroadcastReceiver mBatteryChangedReceiver;
	private BroadcastReceiver mConnectivityChangedReceiver;
	private BroadcastReceiver mBackgroundDataSettingReceiver;
	private LocationListener mLocationListener;
	private SensorEventListener mSensorEventListener;
	
	private TextView mBatteryTextView;
	private TextView mConnectivityTextView;

	private void runInWakeLock(Runnable runnable, int flags) {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(flags, "My WakeLock");
		wl.acquire();
		runnable.run();
		wl.release();
	}
	
	public void onClickCancelAlarms(View v) {
		setupAlarm(true);
	}

    private void setupInexactAlarm(boolean cancel) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, MyService.class);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        if (cancel) {
            am.cancel(pendingIntent); // will cancel all alarms whose intent matches this one
        } else {
            long interval = AlarmManager.INTERVAL_HOUR;
            long firstInterval = DateUtils.MINUTE_IN_MILLIS * 30;

            am.setInexactRepeating(AlarmManager.RTC, firstInterval, interval, pendingIntent);
        }
    }

	private void setupAlarm(boolean cancel) {
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		
		Intent intent = new Intent(this, MyService.class);
		
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
		
		if (cancel) {
			am.cancel(pendingIntent);
		} else {
			long interval = DateUtils.HOUR_IN_MILLIS * 1;
			long firstInterval = DateUtils.MINUTE_IN_MILLIS * 30;

			am.setRepeating(AlarmManager.RTC_WAKEUP, firstInterval, interval, pendingIntent);
		}
	}
	
	private static String powerRequirementCodeToString(int powerRequirement) {
		switch (powerRequirement) {
		case Criteria.POWER_LOW: return "Low";
		case Criteria.POWER_MEDIUM: return "Medium";
		case Criteria.POWER_HIGH: return "High";
		default: return String.format("Unknown (%d)", powerRequirement);
		}
	}

	private void showLocationProvidersPowerRequirement() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		List<String> providers = lm.getAllProviders();
		
		if (providers != null) {
			for (String name : providers) {
				LocationProvider provider = lm.getProvider(name);
				if (provider != null) {
					int powerRequirement = provider.getPowerRequirement();
					Log.i(TAG, name + " location provider power requirement: " + powerRequirementCodeToString(powerRequirement));
				}
			}
		}
	}
	
	private LocationProvider getMyLocationProvider() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		LocationProvider provider = null;
		
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setAltitudeRequired(true);
		criteria.setBearingAccuracy(Criteria.NO_REQUIREMENT);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true); // most likely you want the user to be able to set that
		criteria.setHorizontalAccuracy(Criteria.ACCURACY_LOW);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setSpeedAccuracy(Criteria.ACCURACY_MEDIUM);
		criteria.setSpeedRequired(false);
		criteria.setVerticalAccuracy(Criteria.NO_REQUIREMENT);
		
		List<String> names = lm.getProviders(criteria, false); // perfect matches only
		
		if ((names != null) && ! names.isEmpty()) {
			for (String name : names) {
				provider = lm.getProvider(name);
				Log.d(TAG, "[getMyLocationProvider] " + provider.getName() + " " + provider);
			}
			provider = lm.getProvider(names.get(0));
		} else {
			Log.d(TAG, "Could not find perfect match for location provider");
			
			String name = lm.getBestProvider(criteria, false);
			
			if (name != null) {
				provider = lm.getProvider(name);
				Log.d(TAG, "[getMyLocationProvider] " + provider.getName() + " " + provider);
			}
		}
		
		return provider;
	}
	
	private LocationProvider getMyLocationProvider2() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		LocationProvider provider = null;
		
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setAltitudeRequired(true);
		criteria.setBearingAccuracy(Criteria.NO_REQUIREMENT);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true); // most likely you want the user to be able to set that
		criteria.setHorizontalAccuracy(Criteria.ACCURACY_LOW);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setSpeedAccuracy(Criteria.ACCURACY_MEDIUM);
		criteria.setSpeedRequired(false);
		criteria.setVerticalAccuracy(Criteria.NO_REQUIREMENT);
		
		String name = lm.getBestProvider(criteria, false);

		if (name != null) {
			provider = lm.getProvider(name);
			
			if (! provider.meetsCriteria(criteria)) {
				Log.d(TAG, "[getMyLocationProvider] Provider does not meet criteria");
				
				if (provider.getPowerRequirement() == Criteria.POWER_HIGH) {
					// we only accept high power requirement if we get better accuracy too
					
					if (provider.getAccuracy() != Criteria.ACCURACY_FINE) {
						// find another provider here
					}
				}
			}
			
			Log.d(TAG, "[getMyLocationProvider] " + provider.getName() + " " + provider);
		}
		
		return provider;
	}
	
	private Location getLastKnownLocation() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> names = lm.getAllProviders();
		Location location = null;
		if (names != null) {
			for (String name : names) {
				if (! LocationManager.PASSIVE_PROVIDER.equals(name)) {
					Location l = lm.getLastKnownLocation(name);
					if (location == null || l.getTime() > location.getTime()) {
						location = l;
						
						/*
						 * Warning: GPS and network providers' clocks may be out of sync so
						 * comparing the times may not be such a good idea...
						 */
					}
				}
			}
		}
		return location;
	}
	
	private void unregisterSensorEventListener(SensorEventListener listener) {
		SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sm.unregisterListener(listener);
	}
	
	private SensorEventListener registerWithAccelerometer() {
		SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensors != null && ! sensors.isEmpty()) {
			SensorEventListener listener = new SensorEventListener() {

				@Override
				public void onAccuracyChanged(Sensor sensor, int accuracy) {
					Log.i(TAG, "Accuracy changed to " + accuracy);
				}

				@Override
				public void onSensorChanged(SensorEvent event) {
					/*
					 * Accelerometer: array of 3 values
					 * 
					 * values[0] = Acceleration minus Gx on the x-axis 
					 * values[1] = Acceleration minus Gy on the y-axis 
					 * values[2] = Acceleration minus Gz on the z-axis 
					 */
					
					//Log.i(TAG, String.format("x:%.2f y:%.2f z:%.2f ", event.values[0], event.values[1], event.values[2]));
					
					// do something interesting here
				}
			};
			
			Sensor sensor = sensors.get(0);
			Log.d(TAG, "Using sensor " + sensor.getName() + " from " + sensor.getVendor());
			sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
			return listener;
		}
		return null;
	}
	
	private void requestLocationUpdates() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		List<String> providers = lm.getAllProviders();
		
		if (providers != null && ! providers.isEmpty()) {
			for (String name : providers) {
				if (! LocationManager.PASSIVE_PROVIDER.equals(name)) {
					Log.i(TAG, "Requesting location updates on " + name);
					lm.requestLocationUpdates(name, 0, 0, mLocationListener);
					//lm.requestLocationUpdates(name, DateUtils.HOUR_IN_MILLIS * 1, 100, listener); // not as frequently
				}
			}
		}
	}

	private void requestPassiveLocationUpdates() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener listener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				Log.i(TAG, "[PASSIVE] " + location.toString());
				
				// let's say you only care about GPS location updates
				if (LocationManager.GPS_PROVIDER.equals(location.getProvider())) {
					
					// if you care about accuracy, make sure you check whether the location reports accuracy information
					if (location.hasAccuracy() && (location.getAccuracy() < 10.0f)) {
						
						// do something here
					}
				}
			}

			@Override
			public void onProviderDisabled(String provider) {
				Log.i(TAG, "[PASSIVE] " + provider + " location provider disabled");
			}

			@Override
			public void onProviderEnabled(String provider) {
				Log.i(TAG, "[PASSIVE] " + provider + " location provider enabled");
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				Log.i(TAG, "[PASSIVE] " + provider + " location provider status changed to " + status);
			}
		};

		Log.i(TAG, "Requesting passive location updates");
		lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, DateUtils.SECOND_IN_MILLIS * 30, 100, listener);
	}

	private void disableLocationListener() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.removeUpdates(mLocationListener);
    }

	private void showPowerUsageSummary() {
		try {
			Intent intent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void onClickShowPowerUsageSummary(View v) {
		showPowerUsageSummary();
	}
	
	private void enableBatteryReceiver(boolean enabled) {
		PackageManager pm = getPackageManager();
		ComponentName receiverName = new ComponentName(this, BatteryReceiver.class);
		int newState;
		if (enabled) {
			newState = PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
		} else {
			newState = PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
		}
		pm.setComponentEnabledSetting(receiverName, newState, PackageManager.DONT_KILL_APP);
	}
	
	private static String healthCodeToString(int health) {
		switch (health) {
		//case BATTERY_HEALTH_COLD: return "Cold"; // API level 11 only
		case BATTERY_HEALTH_DEAD: return "Dead";
		case BATTERY_HEALTH_GOOD: return "Good";
		case BATTERY_HEALTH_OVERHEAT: return "Overheat";
		case BATTERY_HEALTH_OVER_VOLTAGE: return "Over voltage";
		case BATTERY_HEALTH_UNSPECIFIED_FAILURE: return "Unspecified failure";
		case BATTERY_HEALTH_UNKNOWN:
		default: return "Unknown";
		}
	}
	
	private static String pluggedCodeToString(int plugged) {
		switch (plugged) {
		case 0: return "Battery";
		case BATTERY_PLUGGED_AC: return "AC";
		case BATTERY_PLUGGED_USB: return "USB";
		default: return "Unknown";
		}
	}
	
	private static String statusCodeToString(int status) {
		switch (status) {
		case BATTERY_STATUS_CHARGING: return "Charging";
		case BATTERY_STATUS_DISCHARGING: return "Discharging";
		case BATTERY_STATUS_FULL: return "Full";
		case BATTERY_STATUS_NOT_CHARGING: return "Not charging";
		case BATTERY_STATUS_UNKNOWN:
		default: return "Unknown";
		}
	}
	
	private void showBatteryInfo(Intent intent) {
		if (intent != null) {
			int health = intent.getIntExtra(EXTRA_HEALTH, BATTERY_HEALTH_UNKNOWN);
			String healthString = "Health: " + healthCodeToString(health);
			Log.i(TAG, "Health: " + healthString);
			
			int level = intent.getIntExtra(EXTRA_LEVEL, 0);
			int scale = intent.getIntExtra(EXTRA_SCALE, 100);
			float percentage = (scale != 0) ? (100.f * (level / (float)scale)) : 0.0f;
			String levelString = String.format("Level: %d/%d (%.2f%%)", level, scale, percentage);
			Log.i(TAG, levelString);
			
			int plugged = intent.getIntExtra(EXTRA_PLUGGED, 0);
			String pluggedString = "Power source: " + pluggedCodeToString(plugged);
			Log.i(TAG, pluggedString);
			
			boolean present = intent.getBooleanExtra(EXTRA_PRESENT, false);
			String presentString = "Present? " + (present ? "Yes" : "No");
			Log.i(TAG, presentString);
			
			int status = intent.getIntExtra(EXTRA_STATUS, BATTERY_STATUS_UNKNOWN);
			String statusString = "Status: " + statusCodeToString(status);
			Log.i(TAG, statusString);
			
			String technology = intent.getStringExtra(EXTRA_TECHNOLOGY);
			String technologyString = "Technology: " + technology;
			Log.i(TAG, technologyString);
			
			int temperature = intent.getIntExtra(EXTRA_STATUS, Integer.MIN_VALUE);
			String temperatureString = "Temperature: " + temperature;
			Log.i(TAG, temperatureString);
			
			int voltage = intent.getIntExtra(EXTRA_VOLTAGE, Integer.MIN_VALUE);
			String voltageString = "Voltage: " + voltage;
			Log.i(TAG, voltageString);
			
			String s = healthString + "\n";
			s += levelString + "\n";
			s += pluggedString + "\n";
			s += presentString + "\n";
			s += statusString + "\n";
			s += technologyString + "\n";
			s += temperatureString + "\n";
			s += voltageString;
			mBatteryTextView.setText(s);
			
			int id = intent.getIntExtra(EXTRA_ICON_SMALL, 0);
			setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, id);
		} else {
			String s = "No battery information";
			Log.i(TAG, s);
			mBatteryTextView.setText(s);
			
			setFeatureDrawable(Window.FEATURE_LEFT_ICON, null);
		}
	}
	
	private void showBatteryInfo() {
		// no receiver needed
		Intent intent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		showBatteryInfo(intent);
	}
	
	private void showNetworkInfoToast(final NetworkInfo info) {
		Toast.makeText(this, info.toString(), Toast.LENGTH_LONG).show();
	}

	private void showNetworkInfoToast() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		// to show only the active connection
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null) {
			Toast.makeText(this, "Active: " + info.toString(), Toast.LENGTH_LONG).show();
		}
		
		// to show all connections
		NetworkInfo[] array = cm.getAllNetworkInfo();
		if (array != null) {
			String s = "All: ";
			for (NetworkInfo i: array) {
				s += i.toString() + "\n";
			}
			Toast.makeText(this, s, Toast.LENGTH_LONG).show();
		}
		
		Log.i(TAG, "Background data setting: " + cm.getBackgroundDataSetting());
	}
	
	private void transferData(byte[] array) {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean backgroundDataSetting = cm.getBackgroundDataSetting(); // deprecated in Android 4.0
		if (backgroundDataSetting) {
			// transfer data
		} else {
			// honor setting and do not transfer data
		}
	}
	
	private void updateConnectivityTextView() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] infoArray = cm.getAllNetworkInfo();
		if (infoArray != null) {
			String s = "";
			for (NetworkInfo info: infoArray) {
				s += info.toString() + "\n";
			}
			mConnectivityTextView.setText(s);
		}
	}
	
	private void showConnectivityInfo(Intent intent) {
		NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		showNetworkInfoToast(info);
		updateConnectivityTextView();
	}
	
	private void createBatteryReceiver() {
		mBatteryChangedReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				showBatteryInfo(intent);
			}
        };
	}
	
	private void createConnectivityReceiver() {
		mConnectivityChangedReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				showConnectivityInfo(intent);
			}
        };
	}
	
	private void backgroundDataSettingChanged() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean backgroundDataSetting = cm.getBackgroundDataSetting();
		
		if (backgroundDataSetting) {
			// resume/start transfers
		} else {
			// pause/cancel transfers
		}
	}
	
	private void createBackgroundDataSettingReceiver() {
		mBackgroundDataSettingReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				backgroundDataSettingChanged();
			}
        };
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.main);
        
        mBatteryTextView = (TextView) findViewById(R.id.battery);
        mConnectivityTextView = (TextView) findViewById(R.id.connectivity);
        
        transferData(null);
        
        showBatteryInfo(); // no receiver needed
        
        showNetworkInfoToast();
        
		mLocationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				Log.i(TAG, location.toString());
			}

			@Override
			public void onProviderDisabled(String provider) {
				Log.i(TAG, provider + " location provider disabled");
			}

			@Override
			public void onProviderEnabled(String provider) {
				Log.i(TAG, provider + " location provider enabled");
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				Log.i(TAG, provider + " location provider status changed to " + status);
			}
		};

		Location lastKnownLocation = getLastKnownLocation();
		if (lastKnownLocation != null) {
			long age = (System.currentTimeMillis() - lastKnownLocation.getTime() + 500) / 1000;
			Log.i(TAG, "Last known location " + age + " seconds ago: " + lastKnownLocation);
		}
        //requestLocationUpdates();
        //requestPassiveLocationUpdates();
        
        showLocationProvidersPowerRequirement();
        
        getMyLocationProvider();
    }

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mBatteryChangedReceiver);
		unregisterReceiver(mConnectivityChangedReceiver);
		unregisterReceiver(mBackgroundDataSettingReceiver);
		unregisterSensorEventListener(mSensorEventListener);
		mSensorEventListener = null;
		enableBatteryReceiver(false);
		
		// unregistering the receivers when the application is not in the foreground saves power
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mBatteryChangedReceiver == null) {
			createBatteryReceiver();
		}
		if (mConnectivityChangedReceiver == null) {
			createConnectivityReceiver();
		}
		if (mBackgroundDataSettingReceiver == null) {
			createBackgroundDataSettingReceiver();
		}
		registerReceiver(mBatteryChangedReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		registerReceiver(mConnectivityChangedReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		registerReceiver(mBackgroundDataSettingReceiver, new IntentFilter(ConnectivityManager.ACTION_BACKGROUND_DATA_SETTING_CHANGED));
		mSensorEventListener = registerWithAccelerometer();
		enableBatteryReceiver(true);
		updateConnectivityTextView();
		
		setupAlarm(false);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		unregisterReceiver(mBatteryChangedReceiver);
		mBatteryChangedReceiver = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		disableLocationListener();
	}
}