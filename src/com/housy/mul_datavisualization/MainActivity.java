package com.housy.mul_datavisualization;

import java.util.ArrayList;

import android.opengl.Matrix;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Button bstart;
	private Button bend;
	private Button bcheck;
	
	private CheckBox linecb;
	private CheckBox entitycb;
	
	private TextView startText;
	private TextView endText;
	
	private SensorManager mSensorManager;
	private Sensor gyro;
	//private Sensor acce;
	private Sensor magn;
	private Sensor lina;
	private Sensor grav;
	private Sensor rota;
	
	float RotationVector[] = new float[3];
	
	ArrayList<Float> gyroList  = new ArrayList<Float>();
	ArrayList<Float> acceList  = new ArrayList<Float>();
	ArrayList<Float> linaList  = new ArrayList<Float>();
	ArrayList<Float> gravList  = new ArrayList<Float>();
	ArrayList<Float> angleList = new ArrayList<Float>();
	ArrayList<Float> matrixList= new ArrayList<Float>();
	ArrayList<Float> magnList  = new ArrayList<Float>();
	
	ArrayList<Long> timeList    = new ArrayList<Long>();
	ArrayList<Long> rawtimeList = new ArrayList<Long>();
	
	ArrayList<Integer> dupindex = new ArrayList<Integer>();
	
	float[] avalues_o = new float[3];//为计算方位角而获取加速度以及磁感应强度的数据
	float[] mvalues_o = new float[3];
	
	int accesize;
	int linasize;
	int gravsize;
	int gyrosize;
	int magnsize;
	int anglesize;
	int matrixsize;
	int timesize;
	int rawtimesize;
	
	public static int drawmode;
	
	float[] Rm    =new float[16];
	float[] Rrot =new float[9];
	float[] RInv =new float[16];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		setContentView(R.layout.activity_main);
		
		TextView startText = (TextView)findViewById(R.id.sText);
		TextView endText = (TextView)findViewById(R.id.eText);

		CheckBox linecb = (CheckBox)findViewById(R.id.line);
		CheckBox entitycb = (CheckBox)findViewById(R.id.entity);
		
        linecb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                // TODO Auto-generated method stub
            	if (ischecked){
            		drawmode=1;
            	    Toast.makeText(MainActivity.this, "line", Toast.LENGTH_LONG).show();
            	}
                
            }

		
        });
        
        entitycb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                // TODO Auto-generated method stub
            	if(ischecked){
            		drawmode=2;
            	    Toast.makeText(MainActivity.this, "entity", Toast.LENGTH_LONG).show();
            	    }

                
            }

		
        });
        
        
		startText.setVisibility(View.INVISIBLE);
		endText.setVisibility(View.INVISIBLE);
		
		Button bcheck = (Button)findViewById(R.id.check);
		Button bstart = (Button)findViewById(R.id.start);
		Button bend = (Button)findViewById(R.id.end);
		
		bstart.setOnClickListener(new StartClassListener());
		bend.setOnClickListener(new EndClassListener());
		
		
		bcheck.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 在end按钮按下之后 数组列表就不再变化 此时再把数组列表放进check按钮下进行操作
				//String result = "This is the visualization result.\n";
				
				//删除ArrayList中的重复数据，以时间戳为判别标准

				
				//新建一个数组用来装载不重复的值，同时记录下重复值的序号
				//发现删除完重复的时间数据之后，时间节点与对应的加速度值正好对应起来，没有必要再去删除加速度和陀螺仪的数据了
				for (int dup = 0; dup<rawtimeList.size(); dup++){
					if(!timeList.contains(rawtimeList.get(dup))){
						timeList.add(rawtimeList.get(dup));
					}
					else{
						dupindex.add(dup);
					}
				}
				
				accesize = acceList.size();
				linasize = linaList.size();
				gravsize = gravList.size();
				gyrosize = gyroList.size();
				magnsize = magnList.size();
				timesize = timeList.size();
				rawtimesize = rawtimeList.size();
				
				//得到时间戳、加速度、磁传感器数据：对应计算各个位置的方位角度
				//单独拿出来计算是为了排除传感器监听器在监听时，由于多线程运行造成的大量无用数据的影响
				/*
				for(int timeindex=0; 
						3*timeindex<accesize&&3*timeindex<gyrosize&&3*timeindex<magnsize; 
						timeindex++){
					avalues_o[0]=acceList.get(3*timeindex);
					avalues_o[1]=acceList.get(3*timeindex+1);
					avalues_o[2]=acceList.get(3*timeindex+2);
					mvalues_o[0]=magnList.get(3*timeindex);
					mvalues_o[1]=magnList.get(3*timeindex+1);
					mvalues_o[2]=magnList.get(3*timeindex+2);
					
					CalculateOrientation();	
					
					
				}*/
				matrixsize=matrixList.size();
				anglesize = angleList.size();
				
				//因为bundle只能传递浮点的数组而不能传送数组列表 所以要先进行类型转换
				
				Float [] Faccevalues = acceList.toArray(new Float[0]);//类型转换成功，注意toArray()中的new语句
				Float [] Flinavalues = linaList.toArray(new Float[0]);
				Float [] Fgravvalues = gravList.toArray(new Float[0]);
				Float [] Fgyrovalues = gyroList.toArray(new Float[0]);
				Float [] Fmagnvalues = magnList.toArray(new Float[0]);
				Float [] Fanglevalues = angleList.toArray(new Float[0]);
				Float [] Fmatrixvalues = matrixList.toArray(new Float[0]);
				Long  [] Ltimevalues = timeList.toArray(new Long [0]);
				Long  [] Lrawtimevalues = rawtimeList.toArray(new Long[0]);
				
				
				//确定好了数组的长度之后 将Float型数组复制给float数组
				float accevalues[]  = new float[accesize];
				float linavalues[]  = new float[linasize];
				float gravvalues[]  = new float[gravsize];
				float gyrovalues[]  = new float[gyrosize];
				float magnvalues[]  = new float[magnsize];
				float anglevalues[] = new float[anglesize];
				float matrixvalues[]= new float[matrixsize];
				long  timevalues[]  = new long [timesize];
				long  rawtimevalues[] = new long [rawtimesize];
				
				for (int i=0; i<Lrawtimevalues.length; i++){
					rawtimevalues[i] = Lrawtimevalues[i].longValue();
				}
				
				for(int i=0; i<Faccevalues.length; i++){
					accevalues[i] = Faccevalues[i].floatValue();
				}
				for(int i=0; i<Flinavalues.length; i++){
					linavalues[i] = Flinavalues[i].floatValue();
				}
				for(int i=0; i<Fgravvalues.length; i++){
					gravvalues[i] = Fgravvalues[i].floatValue();
				}
				for(int j=0; j<Fgyrovalues.length; j++){
					gyrovalues[j] = Fgyrovalues[j].floatValue();
				}
				for(int m=0; m<Fmagnvalues.length; m++){
					magnvalues[m] = Fmagnvalues[m].floatValue();
				}
				for(int l=0; l<Fanglevalues.length; l++){
					anglevalues[l] = Fanglevalues[l].floatValue();
				}
				for(int l=0; l<Fmatrixvalues.length; l++){
					matrixvalues[l] = Fmatrixvalues[l].floatValue();
				}
				
				for(int k=0; k<Ltimevalues.length; k++){
					timevalues[k] = Ltimevalues[k].longValue();
				}
				
				
				
				
				
				
				Intent intent = new Intent(MainActivity.this, ResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putFloatArray("aList", accevalues);
				bundle.putFloatArray("linear",linavalues);
				bundle.putFloatArray("gravity", gravvalues);
				bundle.putFloatArray("gList", gyrovalues);
				bundle.putFloatArray("angle", anglevalues);
				bundle.putFloatArray("matrix", matrixvalues);
				bundle.putInt("asize", linasize);
				bundle.putInt("gsize", gyrosize);
				bundle.putInt("mode", drawmode);
				bundle.putLongArray("time", timevalues);
				bundle.putLongArray("rawtime", rawtimevalues);
				intent.putExtras(bundle);
				
				startActivity(intent);
			}
		});
		
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		gyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		lina = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);		
		//acce = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magn = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		grav = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		rota = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);		
		
	}
	
	protected void onPause(){
		super.onPause();
		
	}
	
	protected void onStop(){
		super.onStop();
		
		gyroList.clear();
		acceList.clear(); 
		linaList.clear();  
		angleList.clear();
		matrixList.clear();
		magnList.clear();
		
		timeList.clear();   
		rawtimeList.clear();
		
		dupindex.clear();
		
	}
	
	protected void onStart(){
		super.onStart();
	}
	
	protected void onResume(){
		super.onResume();
	}
	
	private SensorEventListener sel = new SensorEventListener(){

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			float values[] = event.values;
			switch(event.sensor.getType()){
			
			case Sensor.TYPE_GYROSCOPE:
			{
				gyroList.add(values[0]);
				gyroList.add(values[1]);
				gyroList.add(values[2]);
			}
			break;
			/*
			case Sensor.TYPE_GRAVITY:
			{
				gravList.add(values[0]);
				gravList.add(values[1]);
				gravList.add(values[2]);
			}
			*/
			case Sensor.TYPE_LINEAR_ACCELERATION:
			{
				linaList.add(values[0]);
				linaList.add(values[1]);
				linaList.add(values[2]);
				
				CalculateOrientation();

			}
			/*
			case Sensor.TYPE_ACCELEROMETER:
			{
				acceList.add(values[0]);
				acceList.add(values[1]);
				acceList.add(values[2]);
				
			}
			break;*/
			/*
			case Sensor.TYPE_MAGNETIC_FIELD:
			{
				magnList.add(values[0]);
				magnList.add(values[1]);
				magnList.add(values[2]);
			}
			break;*/
			case Sensor.TYPE_ROTATION_VECTOR:
			{
				RotationVector=event.values;
			}
			
			}
		
			
			rawtimeList.add(event.timestamp);//nanoseconds
			
		}
		
	};
	
	
	public void CalculateOrientation(){
		
		
		//获取到加速度以及磁传感器的数据后，调用getRotationMatrix静态方法就可以获取R旋转矩阵
		//SensorManager.getRotationMatrix(Rm, null, avalues_o, mvalues_o);
		
		SensorManager.getRotationMatrixFromVector(Rm, RotationVector);
		Matrix.transposeM(RInv, 0, Rm, 0);
		//获得R旋转矩阵之后就可以利用getOrientation静态方法获取各个方位角（以弧度为单位）
		
		for(int i=0; i<16; i++)
			matrixList.add(RInv[i]);

	}
	
	public class StartClassListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//创建了新类，注意要重新定义TextView变量 否则会有空指针错误出现 因为原来的TextView是在onCreate方法中定义的
			TextView startText = (TextView)findViewById(R.id.sText);
			TextView endText = (TextView)findViewById(R.id.eText);
			startText.setVisibility(View.VISIBLE);
			endText.setVisibility(View.INVISIBLE);
			
			//mSensorManager.registerListener(sel, acce, SensorManager.SENSOR_DELAY_FASTEST);
			mSensorManager.registerListener(sel, lina, SensorManager.SENSOR_DELAY_FASTEST);
			mSensorManager.registerListener(sel, gyro, SensorManager.SENSOR_DELAY_FASTEST);
			//mSensorManager.registerListener(sel, magn, SensorManager.SENSOR_DELAY_FASTEST);
			//mSensorManager.registerListener(sel, grav, SensorManager.SENSOR_DELAY_FASTEST);
			mSensorManager.registerListener(sel, rota, SensorManager.SENSOR_DELAY_FASTEST);

		}
		
	}
	
	public class EndClassListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TextView startText = (TextView)findViewById(R.id.sText);
			TextView endText = (TextView)findViewById(R.id.eText);
			startText.setVisibility(View.INVISIBLE);
			endText.setVisibility(View.VISIBLE);
			
			//mSensorManager.unregisterListener(sel, acce);
			mSensorManager.unregisterListener(sel, lina);
			mSensorManager.unregisterListener(sel, gyro);
			//mSensorManager.unregisterListener(sel, magn);
			//mSensorManager.unregisterListener(sel, grav);
			mSensorManager.unregisterListener(sel, rota);

		
		}
		
	}

}
