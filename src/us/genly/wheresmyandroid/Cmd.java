package us.genly.wheresmyandroid;

import java.util.StringTokenizer;

import android.content.Context;

public interface Cmd {
	String getCmdName();
	/**
	 * Execute the command.
	 * 
	 * @param context The Android context.
	 * @param address The SMS address to reply to.
	 * @param args A StringTokenizer which can be used to retrieve optional
	 *             command arguments.
	 */
	void execCmd(Context context, String address, StringTokenizer stringTokenizer);
}
