package se.pablokarlsson.drawer.scare;

import se.pablokarlsson.drawer.scare.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PostScareActivity extends Activity{
    private Button iGotScaredButton;
    private Button iDidNotGetScaredButton;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        OnClickListener gotScaredListner = new OnClickListener() {
            public void onClick(View v) {
            	PostScareActivity.this.postScaredToWall();
            }
        };
        OnClickListener notScaredListner = new OnClickListener() {
            public void onClick(View v) {
            	PostScareActivity.this.postNotScaredToWall();
            }
        };
        setContentView(R.layout.activity_post_scare);
        iGotScaredButton = (Button) findViewById(R.id.iGotScaredButton);
        iGotScaredButton.setOnClickListener(gotScaredListner);
        iGotScaredButton = (Button) findViewById(R.id.iDidNotGetScaredButton);
        iGotScaredButton.setOnClickListener(notScaredListner);
    }

    protected void postNotScaredToWall() {
		Intent i = new Intent(this, ShareActivity.class);
	    i.setType("text/plain");
	    i.putExtra(Intent.EXTRA_TEXT, "I failed to scared my friends with Drawer Scare!");
		startActivity(i);	
		
	}

	protected void postScaredToWall() {
		Intent i = new Intent(this, ShareActivity.class);
	    i.setType("text/plain");
	    i.putExtra(Intent.EXTRA_TEXT, "I scared my friends with Drawer Scare!");
		startActivity(i);	
	}

	@Override
    public void onBackPressed() {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
    }
}
