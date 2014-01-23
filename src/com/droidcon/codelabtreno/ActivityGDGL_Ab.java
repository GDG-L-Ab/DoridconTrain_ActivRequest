package com.droidcon.codelabtreno;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ActivityGDGL_Ab extends Activity{
	
	private ImageView plus;
	private ImageView face;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gdg);
		
		plus = (ImageView)findViewById(R.id.foll_gplus);
		face = (ImageView) findViewById(R.id.foll_face);
		
		
		face.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("https://www.facebook.com/groups/gugt.roma/"));
		        startActivity(intent);
			}
		});

		
		plus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("https://plus.google.com/u/0/communities/115263653461939871399"));
		        startActivity(intent);
			}
		});
	
		
		
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.anim_return_exit, R.anim.anim_return_ing );
	}

}
