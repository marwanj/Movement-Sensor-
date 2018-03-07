package selcouth.bluetooth;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends Activity implements SensorEventListener {
    double last_x, last_y, last_z;
    double lastUpdate, lastspeed;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    boolean moving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lastUpdate = 0;
        last_x = 0;
        last_y = 0;
        last_z = 0;
        lastspeed = 0;
        moving = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onSensorChanged(SensorEvent sensorevent) {

        TextView motion_dat = (TextView) findViewById(R.id.motion_text);
        TextView accelerometer_dat = (TextView) findViewById(R.id.accel_reading);
        Sensor mySensor = sensorevent.sensor;
        double x = sensorevent.values[0];
        double y = sensorevent.values[1];
        double z = sensorevent.values[2];
        double curTime = System.currentTimeMillis();
        accelerometer_dat.setText(getString(R.string.gyro_string, x, y, z));
        if ((curTime - lastUpdate) > 100) {
            double diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;
            double speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;  //algorithim

            if (speed > 40 && lastspeed > 40) {
                motion_dat.setText("Moving");
                moving = true;
            } else {
                motion_dat.setText("Not Moving");
                moving = false;
            }


            last_x = x;
            last_y = y;
            last_z = z;
            lastspeed = speed;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }


    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

}