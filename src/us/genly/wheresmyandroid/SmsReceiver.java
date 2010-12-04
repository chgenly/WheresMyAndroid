/*
 * WheresMyAndroid
 * 
 * Copyright 2010 Chris Genly
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 *        
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package us.genly.wheresmyandroid;

import java.util.StringTokenizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.telephony.gsm.SmsManager;
import android.util.Log;

@SuppressWarnings("deprecation")
public class SmsReceiver extends BroadcastReceiver {
	private LocationManager mLocationManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		if (!prefs.getBoolean("enabled", false))
			return;
		String keyword = prefs.getString("keyword", "xyzzy").toLowerCase();

		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		SmsMessage msg = null;
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			for (int i = 0; i < pdus.length; i++) {
				msg = SmsMessage.createFromPdu((byte[]) pdus[i]);
				String address = msg.getOriginatingAddress();
				String body = msg.getMessageBody();

				// If the message starts with a paren, we are probably seeing
				// our own reply.  To prevent an SMS message loop we ignore it.
				if (body.startsWith("("))
					return;
				
				int ix = body.toLowerCase().indexOf(keyword);
				if (ix == -1)
					return;
				String rest = body.substring(ix+keyword.length());
				String cmd = null;
				StringTokenizer st = new StringTokenizer(rest);
			
				if (st.hasMoreTokens()) {
					cmd = st.nextToken();
				}
				if (cmd == null)
					getLocationAndSendReply(context, address);
				else if (cmd.equals("ring"))
					ringCmd(context);
			}
		}
	}

	private Location getLocationAndSendReply(Context context, final String address) {
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				Log.i("us.genly.wheresmyandroid", "locationListener location="
						+ location);
				mLocationManager.removeUpdates(this);
				sendReply(address, location);
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				Log.i("us.genly.wheresmyandroid", "onStatusChanged provider="
						+ provider);
				if (status == LocationProvider.OUT_OF_SERVICE) {
					sendSMS(address, "Location provider out of service: "+provider);
				}
			}

			public void onProviderEnabled(String provider) {
				Log.i("us.genly.wheresmyandroid", "onProviderEnabled provider="
						+ provider);
			}

			public void onProviderDisabled(String provider) {
				Log.i("us.genly.wheresmyandroid", "onProviderDisabled provider="
						+ provider);
				mLocationManager.removeUpdates(this);
				sendSMS(address, "Location provider disabled: "+provider);
			}
		};
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 0, locationListener);
		} else if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0, locationListener);
		} else {
			sendSMS(address, "No location service provider enabled.");
		}
		return null;
	}

	private void ringCmd(Context context) {
		ringerOn(context);
		volumeUp(context);
		ring(context);
	}
	
	/**
	 * Make sure we are not in silent or vibrate mode.
	 * @param context
	 */
	private void ringerOn(Context context) {
		AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	}
	
	private void volumeUp(Context context) {
		AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
		audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, 0);
	}
	
	private void ring(Context context) {
		Uri uri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);
		Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
		ringtone.play();
	}
	private void sendReply(String phoneNumber, Location l) {
		String s = "("+l.getLatitude()+", "+l.getLongitude()+")\naltitude="+l.getAltitude()+" speed(m/s)="+l.getSpeed()+"\naccuracy: "+l.getAccuracy();
		sendSMS(phoneNumber, s);
	}

	private void sendSMS(String phoneNumber, String message) {
		Log.i("us.genly.wheresmyandroid", "Sending "+phoneNumber+": "+message);
		
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}
}