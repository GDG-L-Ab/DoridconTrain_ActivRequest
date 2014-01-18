package com.droidcon.codelabtreno;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Intent;



public class MainActivity extends FragmentActivity implements
			GooglePlayServicesClient.ConnectionCallbacks,
			GooglePlayServicesClient.OnConnectionFailedListener{
	
	private boolean mUpdatesRequested = false;
	
	private TextView teVi_currentAct;
	private TextView teVi_conn;
	private EditText edTe_place;
	
	static Handler mHandler;
	
	//private LocationClient mLocationClient;
	//private Location mCurrentLocation;
    private DetectionRequester mDetectionRequester;
    private DetectionRemover mDetectionRemover;
    	
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		teVi_currentAct = (TextView) findViewById(R.id.teViRecongnAct);
		teVi_conn = (TextView) findViewById(R.id.teViConn);
		edTe_place = (EditText) findViewById(R.id.EdTePlace);
		
		mDetectionRequester = new DetectionRequester(getBaseContext());
        mDetectionRemover = new DetectionRemover();	
        
        //mLocationClient = new LocationClient(this, this, this);
       
        
        edTe_place.setText("Insert a place.", TextView.BufferType.EDITABLE);
        
        
        
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String text = (String)msg.obj;
                teVi_currentAct.setText(text);
                /*
                if (text.equals("still")) {
                	mCurrentLocation = mLocationClient.getLastLocation();
					Toast.makeText(getBaseContext(), String.valueOf(mCurrentLocation), Toast.LENGTH_SHORT).show();
				}
				*/
                //call setText here
            }
    	};
	}
	
	
	
	//********************* FUNZIONI LISTENER ********************************************************************
	//****************************************************************************************************************
	@Override
    public void onStart() {
        super.onStart();        
        //rootView.getContext().startService(new Intent(rootView.getContext(), ActivityRecognitionIntentService.class));
        mUpdatesRequested = true;
        //mLocationClient.connect();
        if (servicesConnected()) {
			startPeriodicUpdates();
		}
        /*
        mActivRequest = true;
        mActivityRecognitionClient.connect();
        */
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	
	@Override
    public void onStop() {
	    stopPeriodicUpdates();
        super.onStop();
    }

	

    @Override
    public void onPause() {
        super.onPause();
    }

    
    public void onResume() {
    	startPeriodicUpdates();
    	super.onResume();
    }
    
	
    private void startPeriodicUpdates() {
    	Log.i("startperioc", "inside");
    	teVi_conn.setText(getString(R.string.bullet_on));
    	mDetectionRequester.requestUpdates();
    	//mActivityRecognitionClient.requestActivityUpdates(ControllerLocations.MILLISECONDS_PER_SECOND, mActivityRecognitionPendingIntent);
         
    }

    private void stopPeriodicUpdates() {  
    	teVi_conn.setText(getString(R.string.bullet_off));
        mDetectionRemover.removeUpdates(mDetectionRequester.getRequestPendingIntent());
        mDetectionRequester.getRequestPendingIntent().cancel();
    }


	private boolean servicesConnected() {
        Boolean temp = false;
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d(ControllerLocations.APPTAG, "play services avaible");
            temp =  true;
            
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                //errorFragment.show(, LocationUtils.APPTAG);
                temp = false;
            }
            
        }
		return temp;
    }
	

	
	public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }


        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case ControllerLocations.CONNECTION_FAILURE_RESOLUTION_REQUEST :

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:

                        // Log the result
                        Log.d(ControllerLocations.APPTAG, "result ok");

                        // Display the result
                       
                    break;

                    // If any other result was returned by Google Play services
                    default:
                        // Log the result
                        Log.d(ControllerLocations.APPTAG, "no resolution");

                        // Display the result

                    break;
                }

            // If any other request code was received
            default:
               // Report that this Activity received an unknown requestCode
               Log.d(ControllerLocations.APPTAG, "unknown request code");

               break;
        }
    }
		

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {	
		Log.i("FALLITA", "FALLITA"); 
	}



	@Override
	public void onConnected(Bundle arg0) {
		Log.i("RIUSCITA", "RIUSCITA");
		if (mUpdatesRequested) {
			startPeriodicUpdates();
			mUpdatesRequested = false;
		}
	}



	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
	}

}