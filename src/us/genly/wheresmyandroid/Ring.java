package us.genly.wheresmyandroid;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class Ring implements Cmd {
	
	public String getCmdName() {
		return "ring";
	}
	
	public void execCmd(Context context, String address) {
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

}
