package us.genly.wheresmyandroid;

import android.content.Context;

public interface Cmd {
	String getCmdName();
	void execCmd(Context context, final String address);
}
