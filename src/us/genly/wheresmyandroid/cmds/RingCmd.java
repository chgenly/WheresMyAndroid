package us.genly.wheresmyandroid.cmds;

import us.genly.wheresmyandroid.Cmd;
import us.genly.wheresmyandroid.Ring;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;

public class RingCmd implements Cmd {

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
		Intent intent = new Intent(context, Ring.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("time", 5);
		context.startActivity(intent);
	}
}
