package se.pablokarlsson.drawer.scare;

import se.pablokarlsson.drawer.scare.R;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends Activity {

	OnClickListener listener = null;
    Button scareStartButton;
	private WakeLock mWakeLock;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.startScare();
            }
        };
        setContentView(R.layout.activity_main);
        scareStartButton = (Button) findViewById(R.id.startScareButton);
        scareStartButton.setOnClickListener(listener);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,"");
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (am.getStreamVolume(AudioManager.STREAM_MUSIC) < 10){
    		Toast.makeText(this, "Don't forget to crank up the media sound!", Toast.LENGTH_LONG).show();
        }
        mWakeLock.acquire();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
    }
    
    @Override
    public void onBackPressed() {
    	 this.finish();
    }
    
    @Override
    public void finish() {
        super.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

	private void startScare() {
		SeekBar timeUntilScare = (SeekBar) findViewById(R.id.timeUntilScare);
		Intent scareSomeOne = new Intent(this, ScareService.class);
		scareSomeOne.putExtra("timeUntilScare", timeUntilScare.getProgress());
		startService(scareSomeOne);
		
		// TODO here we could switch to a black screen
	}
	
	@Override
	protected void onDestroy() {
		mWakeLock.release();
		super.onDestroy();
	}
}
