package com.droidcon.codelabtreno;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import android.app.IntentService;
import android.content.Intent;
import android.os.Message;

public class ActivityRecognitionIntentService extends IntentService{

	public ActivityRecognitionIntentService() {
		super("ActivityRecognitionIntentService");
		// TODO Auto-generated constructor stub
	}

	@Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
           
            DetectedActivity mostProbableActivity = result.getMostProbableActivity();
            
            int activityType = mostProbableActivity.getType();
            String activityName = getNameFromType(activityType);
            
            Message msg = new Message();
            msg.obj = activityName;
            MainActivity.mHandler.sendMessage(msg);
            
        } else {
            
        }
   
    }
	
	private String getNameFromType(int activityType) {
        switch(activityType) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.UNKNOWN:
                return "unknown";
            case DetectedActivity.TILTING:
                return "tilting";
        }
        return "unknown";
    }


	
}
