package se.pablokarlsson.drawer.scare;

import se.pablokarlsson.drawer.scare.R;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class ScareService extends Service {

	private final class ServiceHandler extends Handler implements SensorEventListener{
		private SensorManager mSensor;
		private Sensor mAccelerometer;
		private int processId;
		private Boolean processRunning;
		private SoundPool soundHandler;
		private int streamId;
		private float initialSensorValue;
		private boolean sensorInitiated = false;

		public ServiceHandler(Looper looper) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			this.processId = msg.arg1;
			this.processRunning = true;
			int timeUntilScare = msg.arg2;
			
			// Wait the initial time!
			android.os.SystemClock.sleep(timeUntilScare*1000 + 2000);
			
			// Start registering sensor events that will kill the process
			this.mSensor = (SensorManager)getSystemService(SENSOR_SERVICE);
			this.mAccelerometer = mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mSensor.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
			
			// Play sounds at regular intervals until we stop the service			
			while (processRunning){
				int randomKnock = (Math.random() > 0.5)? sKnock : sKnock2;
				this.streamId = sounds.play(randomKnock, 1.0f, 1.0f, 1, 0, 1);
				android.os.SystemClock.sleep((long) (5000 + Math.random()*10000));
			}
			stopSelf(this.processId);
		}
		
		public void onSensorChanged(SensorEvent event) {
			
			// Set the sensor stabilized value
			if (!sensorInitiated){
				initialSensorValue = event.values[1]+10;
				sensorInitiated = !sensorInitiated;
				return;
			}
			
			// Stop the service if the sensor indicates a 5% difference in sensor activity
			System.out.println(initialSensorValue);
			System.out.println(event.values[1] + 10);
			System.out.println(Math.abs(initialSensorValue - event.values[1] - 10));
			System.out.println(10*(initialSensorValue)/100);

			if (Math.abs(initialSensorValue - event.values[1] - 10) > 3*(initialSensorValue)/100){
				this.processRunning = false;
				sounds.stop(streamId);
				sounds.release();
				mSensor.unregisterListener(this);
				stopSelf(this.processId);
			}
		}
		// Unimplemented knowingly
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	}

	private SoundPool sounds;
	private int sKnock;
	private int sKnock2;
	int loaded = 0;
	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;

	@Override
	public void onCreate() {
		this.sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
		this.sKnock = this.sounds.load(this, R.raw.knock, 1);
		this.sKnock2 = this.sounds.load(this, R.raw.knock2, 1);

		HandlerThread thread = new HandlerThread("ServiceStartArguments", 1);
		thread.start();
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int timeUntilScare = intent.getIntExtra("timeUntilScare", 0);
		Toast.makeText(this, "Starting scare in " + timeUntilScare + " seconds!", Toast.LENGTH_SHORT).show();
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		msg.arg2 = timeUntilScare;
		mServiceHandler.sendMessage(msg);

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void onDestroy() {
		Intent i = new Intent(this, ScareImageActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}
}
