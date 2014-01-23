package com.droidcon.codelabtreno;



import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;


import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class MainActivity extends FragmentActivity{
	
	public static String PREFERENCES_NAME = "locCodelabTrain";
	
	private boolean mUpdatesRequested = false;
	
	private ImageButton save;
	private EditText edTe_place;
	private TextView teVi_currentAct;
	private TextView teVi_conn;
	private static TextView teVi_choosedLoc;
	private static TextView teVi_loc;
	

	static Handler mHandler;
	
    private DetectionRequester mDetectionRequester;
    private DetectionRemover mDetectionRemover;
    private LocationRequest mLocationRequest;
    private static LocationClient mLocationClient;
    	
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
			

		save = (ImageButton) findViewById(R.id.imBut_save);
		teVi_currentAct = (TextView) findViewById(R.id.teViRecongnAct);
		teVi_conn = (TextView) findViewById(R.id.teViConn);
		teVi_choosedLoc = (TextView) findViewById(R.id.teViChoosedLoc);
		teVi_loc = (TextView) findViewById(R.id.teViLocation);
		edTe_place = (EditText) findViewById(R.id.EdTePlace);
		
		mLocationRequest = new LocationRequest();
		mLocationClient = new LocationClient(getBaseContext(), mLocationRequest, mLocationRequest);
		mDetectionRequester = new DetectionRequester(getBaseContext());
        mDetectionRemover = new DetectionRemover();	
        
        teVi_choosedLoc.setText(loadPreferences());
        
        save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				savePreferences(String.valueOf(edTe_place.getText()));
				teVi_choosedLoc.setText(loadPreferences());
				edTe_place.setText("");
			}
		});
        
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String text = (String)msg.obj;
                teVi_currentAct.setText(text);
                if (text.equals("still")) {
                	getLocation();
				}
            }
    	};
	}
	
	
	@Override
    public void onStart() {
        super.onStart();    
        mUpdatesRequested = true;
        if (servicesConnected()) {
			startPeriodicUpdates();
		}
        mLocationClient.connect();
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	
	@Override
    public void onStop() {
	    stopPeriodicUpdates();
        super.onStop();
        mLocationClient.disconnect();
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
    	teVi_conn.setText(getString(R.string.bullet_on));
    	mDetectionRequester.requestUpdates();
    }

    private void stopPeriodicUpdates() {  
    	teVi_conn.setText(getString(R.string.bullet_off));
        mDetectionRemover.removeUpdates(mDetectionRequester.getRequestPendingIntent());
        mDetectionRequester.getRequestPendingIntent().cancel();
    }


	private boolean servicesConnected() {
        Boolean temp = false;
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            temp =  true;
            
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                temp = false;
            }
            
        }
		return temp;
    }
	

	
	public static class ErrorDialogFragment extends DialogFragment {

        private Dialog mDialog;
        
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

        switch (requestCode) {

            case ActivityUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.d(ActivityUtils.APPTAG, "result ok");
                    break;

                    default:
                        Log.d(ActivityUtils.APPTAG, "no resolution");
                        
                    break;
                }

            default:
               Log.d(ActivityUtils.APPTAG, "unknown request code");

               break;
        }
    }
	
	
	public boolean savePreferences(String locChosed){
    	SharedPreferences save = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    	SharedPreferences.Editor edit = save.edit();
    	edit.putString(PREFERENCES_NAME, locChosed);
    	return edit.commit();
    }
    
    public String loadPreferences(){
    	SharedPreferences load = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    	String string = load.getString(PREFERENCES_NAME, "nessuna locaton");
    	return string;
    }

	
	public void getLocation(){
    	Location mCurrentLocation = mLocationClient.getLastLocation();
    	try {
    		getAddress(mCurrentLocation);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	
    }
    
    private void getAddress(Location loc){
    	Geocoder geo = new Geocoder(getBaseContext(), Locale.getDefault());
    	String city = null;
    	try {
    		List<Address> addresses = geo.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
			if (addresses.size() > 0) {
	    		city = addresses.get(0).getLocality();
	    		teVi_loc.setText("Sei arrivato a: "+city);
	    		if (city.equals(teVi_choosedLoc.getText())) {
	    			teVi_loc.setText("Sei arrivato a destinazione");
				}
	    		
	    		
	    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    //************ Menu Item ****************************
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
		MenuInflater inflaterMenu = getMenuInflater();
		inflaterMenu.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);    	
    }
    	
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()) {
		case R.id.action_gdg:
			Intent intent = new Intent(this, ActivityGDGL_Ab.class);
			startActivity(intent);
			overridePendingTransition(R.anim.anim_ingr, R.anim.anim_exit);
			return true;
			

		default:
			return super.onOptionsItemSelected(item);
		}
    	
    }
}