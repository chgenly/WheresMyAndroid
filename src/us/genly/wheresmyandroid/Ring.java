package us.genly.wheresmyandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Ring extends Activity {
	private static final int DIALOG_ERROR = 1;
	private Ringtone ringtone;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ring);
		Button stopButton = (Button) findViewById(R.id.stop);
		stopButton.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				if (ringtone != null) {
					ringtone.stop();
				}
				Ring.this.finish();
			}
		});
	}
	
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		int ringTime = intent.getExtras().getInt("time");
		Uri uri = Settings.System.DEFAULT_RINGTONE_URI;
		ringtone = RingtoneManager.getRingtone(this, uri);
		if (ringtone == null) {
			showDialog(DIALOG_ERROR);
			return;
		}
		ringtone.play();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case DIALOG_ERROR:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Please set a default ringtone.");
			builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                Ring.this.finish();
		           }
		       });
			AlertDialog d = builder.create();
			return d;
		default:
			return super.onCreateDialog(id);
		}
	}
}
