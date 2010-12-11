package us.genly.wheresmyandroid.cmds;

import java.util.StringTokenizer;

import us.genly.wheresmyandroid.Cmd;
import us.genly.wheresmyandroid.Ring;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class RingCmd implements Cmd {
	private int ringDuration;
	
	public String getCmdName() {
		return "ring";
	}

	public void execCmd(Context context, String address, StringTokenizer  args) {
		ringDuration = 60;
		if (args.hasMoreTokens()) {
			String arg = args.nextToken();
			try {
				ringDuration = Integer.parseInt(arg);
			} catch (NumberFormatException e) {
				// Just take the default if we don't find an integer.
			}
		}
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
		intent.putExtra("time", ringDuration);
		context.startActivity(intent);
	}
}
