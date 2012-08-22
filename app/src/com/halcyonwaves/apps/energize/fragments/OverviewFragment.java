package com.halcyonwaves.apps.energize.fragments;

import com.halcyonwaves.apps.energize.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OverviewFragment extends Fragment {

	private static final String TAG = "OverviewFragment";

	private TextView textViewCurrentLoadingLevel = null;
	private TextView textViewCurrentChargingState = null;
	private TextView textViewTemp = null;
	private SharedPreferences sharedPref = null;

	// private boolean batteryDischarging = false;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		this.sharedPref = PreferenceManager.getDefaultSharedPreferences( this.getActivity().getApplicationContext() );

		// inflate the static part of the view
		View inflatedView = inflater.inflate( R.layout.fragment_maininformation, container, false );

		// get the handles to some important controls
		this.textViewCurrentLoadingLevel = (TextView) inflatedView.findViewById( R.id.textview_text_current_charginglvl );
		this.textViewCurrentChargingState = (TextView) inflatedView.findViewById( R.id.textview_text_current_chargingstate );
		this.textViewTemp = (TextView) inflatedView.findViewById( R.id.textview_text_temperature );

		// get the current battery state and show it on the main activity
		BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {

			public void onReceive( Context context, Intent intent ) {
				try {
					context.unregisterReceiver( this );
					int rawlevel = intent.getIntExtra( BatteryManager.EXTRA_LEVEL, -1 );
					int scale = intent.getIntExtra( BatteryManager.EXTRA_SCALE, -1 );
					int status = intent.getIntExtra( BatteryManager.EXTRA_STATUS, -1 );
					float temp = ((float) intent.getIntExtra( BatteryManager.EXTRA_TEMPERATURE, -1 )) / 10.0f;
					int level = -1;
					if( rawlevel >= 0 && scale > 0 ) {
						level = (rawlevel * 100) / scale;
					}
					switch( status ) {
						case BatteryManager.BATTERY_STATUS_CHARGING:
							OverviewFragment.this.textViewCurrentChargingState.setText( OverviewFragment.this.getString( R.string.battery_state_charging ) );
							break;
						case BatteryManager.BATTERY_STATUS_DISCHARGING:
							OverviewFragment.this.textViewCurrentChargingState.setText( OverviewFragment.this.getString( R.string.battery_state_discharging ) );
							// OverviewFragment.this.batteryDischarging = true;
							break;
						case BatteryManager.BATTERY_STATUS_FULL:
							OverviewFragment.this.textViewCurrentChargingState.setText( OverviewFragment.this.getString( R.string.battery_state_full ) );
							break;
						default:
							OverviewFragment.this.textViewCurrentChargingState.setText( OverviewFragment.this.getString( R.string.battery_state_unknown ) );
							break;
					}

					OverviewFragment.this.textViewCurrentLoadingLevel.setText( level + "" ); // TODO
					String prefUsedUnit = OverviewFragment.this.sharedPref.getString( "display.temperature_unit", "Celsius" );
					if( prefUsedUnit.compareToIgnoreCase( "celsius" ) == 0 ) {
						OverviewFragment.this.textViewTemp.setText( OverviewFragment.this.getString( R.string.textview_text_temperature_celsius, temp ) );
					} else if( prefUsedUnit.compareToIgnoreCase( "fahrenheit" ) == 0 ) {
						final float newTemp = temp * 1.8f + 32.0f;
						OverviewFragment.this.textViewTemp.setText( OverviewFragment.this.getString( R.string.textview_text_temperature_fahrenheit, newTemp ) );
					}
				} catch( IllegalStateException e ) {
					Log.e( OverviewFragment.TAG, "The fragment was in an illegal state while it received the battery information. This should be handled in a different (and better way), The exception message was: ", e ); // TODO
				}
			}
		};
		IntentFilter batteryLevelFilter = new IntentFilter( Intent.ACTION_BATTERY_CHANGED );
		this.getActivity().registerReceiver( batteryLevelReceiver, batteryLevelFilter );

		// update the label how long the device is running on battery
		/*
		 * TextView onBatteryTextView = (TextView)inflatedView.findViewById( R.id.textview_text_time_on_battery ); onBatteryTextView.setText( this.getText( R.string.textview_text_time_on_battery_not_on_battery ) ); if( !batteryDischarging ) { BatteryStatisticsDatabaseOpenHelper batteryDbOpenHelper = new BatteryStatisticsDatabaseOpenHelper( this.getActivity().getApplicationContext() ); SQLiteDatabase batteryStatisticsDatabase = batteryDbOpenHelper.getReadableDatabase(); Cursor lastEntryMadeCursor = batteryStatisticsDatabase.query( RawBatteryStatisicsTable.TABLE_NAME, new String[] { RawBatteryStatisicsTable.COLUMN_EVENT_TIME }, RawBatteryStatisicsTable.COLUMN_CHARGING_STATE + " LIKE ?", new String[] { String.valueOf( RawBatteryStatisicsTable.CHARGING_STATE_DISCHARGING ) }, null, null, RawBatteryStatisicsTable.COLUMN_EVENT_TIME + " DESC" ); //Cursor lastEntryMadeCursor = batteryStatisticsDatabase.query( RawBatteryStatisicsTable.TABLE_NAME, new String[] { RawBatteryStatisicsTable.COLUMN_EVENT_TIME, RawBatteryStatisicsTable.COLUMN_CHARGING_STATE }, null, null, null, null, RawBatteryStatisicsTable.COLUMN_EVENT_TIME + " DESC" );
		 * 
		 * // if( lastEntryMadeCursor.moveToFirst() ) { int lastUnixTime = lastEntryMadeCursor.getInt( lastEntryMadeCursor.getColumnIndex( RawBatteryStatisicsTable.COLUMN_EVENT_TIME ) ); long diff = System.currentTimeMillis() - ( lastUnixTime * 1000 ); diff /= 1000; diff /= 60;
		 * 
		 * // Log.v( OverviewFragment.TAG, String.format( "The unix time where the battery was unplugged: %d (%d now, %d minutes ago)", lastUnixTime, System.currentTimeMillis() / 1000, diff ) ); }
		 * 
		 * // close our connection to the database lastEntryMadeCursor.close(); lastEntryMadeCursor = null; batteryDbOpenHelper.close(); batteryStatisticsDatabase = null; batteryDbOpenHelper = null;
		 * 
		 * 
		 * // onBatteryTextView.setText( this.getString( R.string.textview_text_time_on_battery, 0, 0 ) ); }
		 */

		// return the inflated view
		return inflatedView;
	}
}
