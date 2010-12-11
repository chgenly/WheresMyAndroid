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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import us.genly.wheresmyandroid.cmds.ReplyWithLocationCmd;
import us.genly.wheresmyandroid.cmds.RingCmd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	private String keyword;
	static private Map<String, Cmd> cmds = new HashMap<String, Cmd>();
	static {
		initCmds();
	}
	static private void initCmds() {
		for(Cmd cmd : new Cmd[] {new RingCmd(), new ReplyWithLocationCmd()}) {
			cmds.put(cmd.getCmdName(),cmd);
		}
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		keyword = prefs.getString("keyword", "").toLowerCase();
		if (keyword.equals("") || !prefs.getBoolean("enabled", true))
			return;

		List<SmsMessage> smsMessages = getSmsMessages(intent);
		for(SmsMessage smsMessage : smsMessages) {
			String address = smsMessage.getOriginatingAddress();
			String body = smsMessage.getMessageBody();
			processCmd(context, body, address);
		}
	}
	
	private void processCmd(Context context, String body, String address) {
		// If the message starts with a paren, we are probably seeing
		// our own reply from ReplyWithLocation.  To prevent an SMS message loop we ignore it.
		if (body.startsWith("("))
			return;

		// If the message came from an email gateway, the command
		// could be embedded anywhere in the message.
		int ix = body.toLowerCase().indexOf(keyword);
		if (ix == -1)
			return;
		
		String rest = body.substring(ix+keyword.length());
		String cmdName = "";
		StringTokenizer st = new StringTokenizer(rest);
	
		if (st.hasMoreTokens()) {
			cmdName = st.nextToken();
		}
		Cmd cmd = cmds.get(cmdName);
		if (cmd == null)
			cmd = cmds.get("location");
		cmd.execCmd(context, address, st);
	}
	
	private List<SmsMessage> getSmsMessages(Intent intent) {
		List<SmsMessage> smsMessages = new ArrayList<SmsMessage>();
		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			for (int i = 0; i < pdus.length; i++) {
				SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdus[i]);
				smsMessages.add(msg);
			}
		}
		return smsMessages;
	}

}