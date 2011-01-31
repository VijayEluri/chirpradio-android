package org.chirpradio.mobile;

import android.util.Log;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.text.Html;

import android.os.AsyncTask;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;

import org.chirpradio.mobile.Track;

/** NotificationUpdateTask encapsulates the background job that runs to fetch 
* things like track information from the CHIRP server. It also includes callbacks
* to do things like update the UI from the info received from the background job.
*
* @author Jim Benton, Chirag Patel
*/
public class NotificationUpdateTask extends AsyncTask<Context, Void, Track> {

	private static final String LOG_TAG = NotificationUpdateTask.class.toString();
	private static final int NOTIFICATION_ID = 1;
	
	private Context context;
	
    protected Track doInBackground(Context... contexts) {
		android.os.Debug.waitForDebugger();
    	this.context = contexts[0];
    	Track track = Track.getCurrentTrack();
		return track;
    }

    protected void onPostExecute(Track track) {
	    int icon = R.drawable.icon;
	    long when = System.currentTimeMillis();
	    
		try {
			if (track != null) {
				String notificationString = track.getArtist() + " - " + track.getTrack() + " from " + '"'+ track.getRelease() + '"';
	
			    CharSequence title = context.getString(R.string.app_name) + " (DJ" + " " + track.getDj()+ ")";
			 
			    Intent notificationIntent = new Intent(context, MainMenu.class);
			    notificationIntent.setAction(Intent.ACTION_VIEW);
			    notificationIntent.addCategory(Intent.CATEGORY_DEFAULT);
			    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    
			    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			    
			    Notification notification = new Notification(icon, notificationString, when);
			    notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
			    notification.setLatestEventInfo(context, title, notificationString, contentIntent);
			    
			    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			    notificationManager.notify(NOTIFICATION_ID, notification);
			}
		} catch (Exception e) {
	        Log.e(LOG_TAG, "", e);
		}

    }
}