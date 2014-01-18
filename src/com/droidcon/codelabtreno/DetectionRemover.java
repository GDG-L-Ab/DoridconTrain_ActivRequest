package com.droidcon.codelabtreno;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.ActivityRecognitionClient;

import android.app.PendingIntent;
import android.os.Bundle;

public class DetectionRemover
        implements ConnectionCallbacks, OnConnectionFailedListener {
	
    private ActivityRecognitionClient mActivityRecognitionClient;
    private PendingIntent mCurrentIntent;

    public DetectionRemover() {
        mActivityRecognitionClient = null;
    }
    
    public void removeUpdates(PendingIntent requestIntent) {
        mCurrentIntent = requestIntent;
    }

    @Override
    public void onConnected(Bundle connectionData) {
        mActivityRecognitionClient.removeActivityUpdates(mCurrentIntent);
        mCurrentIntent.cancel();
        mActivityRecognitionClient.disconnect();
    	mActivityRecognitionClient = null;
    }
    
    @Override
    public void onDisconnected() {
        mActivityRecognitionClient = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
