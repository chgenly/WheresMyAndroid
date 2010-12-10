package us.genly.wheresmyandroid.cmds;

import us.genly.wheresmyandroid.Cmd;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;

@SuppressWarnings("deprecation")
public class ReplyWithLocationCmd implements Cmd {
	private LocationManager mLocationManager;

	public String getCmdName() {
		return "location";
	}
	
	public void execCmd(Context context, final String address) {
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
					sendSMS(address, "Location provider out of service: "
							+ provider);
				}
			}

			public void onProviderEnabled(String provider) {
				Log.i("us.genly.wheresmyandroid", "onProviderEnabled provider="
						+ provider);
			}

			public void onProviderDisabled(String provider) {
				Log.i("us.genly.wheresmyandroid",
						"onProviderDisabled provider=" + provider);
				mLocationManager.removeUpdates(this);
				sendSMS(address, "Location provider disabled: " + provider);
			}
		};
		mLocationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
		} else if (mLocationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			mLocationManager
					.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							1000, 0, locationListener);
		} else {
			sendSMS(address, "No location service provider enabled.");
		}
	}

	private void sendReply(String phoneNumber, Location l) {
		String s = "(" + l.getLatitude() + ", " + l.getLongitude()
				+ ")\naltitude=" + l.getAltitude() + " speed(m/s)="
				+ l.getSpeed() + "\naccuracy: " + l.getAccuracy();
		sendSMS(phoneNumber, s);
	}

	private void sendSMS(String phoneNumber, String message) {
		Log.i("us.genly.wheresmyandroid", "Sending " + phoneNumber + ": "
				+ message);

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}

}