package se.pablokarlsson.drawer.scare;

import se.pablokarlsson.drawer.scare.R;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ScareImageActivity extends Activity{
    private int sScream;
	private SoundPool sounds;

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scare_image);
		this.sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
		this.sScream = this.sounds.load(this, R.raw.screaming, 1);
		android.os.SystemClock.sleep(800);
		ImageView img = (ImageView) findViewById(R.id.imageView1);
		img.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ScareImageActivity.this, PostScareActivity.class);
				startActivity(i);
			}
		});
    }
	
	@Override
	public void onStart(){
		super.onStart();
		sounds.play(sScream, 1.0f, 1.0f, 1, 0, 1);
	}

    @Override
    protected void onDestroy() {
		sounds.release();
    	super.onDestroy();
    }
}
