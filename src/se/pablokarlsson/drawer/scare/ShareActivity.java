package se.pablokarlsson.drawer.scare;

import se.pablokarlsson.drawer.scare.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShareActivity extends Activity {

    private Button shareButton;
    private Button skipButton;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        OnClickListener shareListener = new OnClickListener() {
            public void onClick(View v) {
            	ShareActivity.this.shareScare(ShareActivity.this.getIntent().getExtras().getString(Intent.EXTRA_TEXT));
            }
        };
        OnClickListener skipListner = new OnClickListener() {
            public void onClick(View v) {
        		Intent i = new Intent(ShareActivity.this, MainActivity.class);
        		startActivity(i);	
            }
        };
        setContentView(R.layout.activity_share);
        shareButton = (Button) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(shareListener);
        skipButton = (Button) findViewById(R.id.skipButton);
        skipButton.setOnClickListener(skipListner);
    }

    protected void shareScare(String string) {
		Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setType("text/plain");
	    intent.putExtra(Intent.EXTRA_TEXT, string);
	    startActivity(Intent.createChooser(intent, "How do you like to share this?"));
	}

	@Override
    public void onBackPressed() {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
    }

}
