package com.housy.mul_datavisualization;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.opengl.Matrix;


public class ResultActivity extends Activity{
	
	public CubeSurfaceView myGLSurfaceView;
	
	public static ArrayList<Cube> cubeList = new ArrayList<Cube>();
	public static ArrayList<Linesub> lineList = new ArrayList<Linesub>();
	public static Linesub trace;
	
	public static ArrayList<Double> cali_time  =  new ArrayList<Double>();
	public static ArrayList<Float> cali_avalues = new ArrayList<Float>();
	public static ArrayList<Float> average_a = new ArrayList<Float>();
	
	public static ArrayList<Float> cali_axvalues = new ArrayList<Float>();
	public static ArrayList<Float> cali_ayvalues = new ArrayList<Float>();
	public static ArrayList<Float> cali_azvalues = new ArrayList<Float>();
	
	public static ArrayList<Float> xVelocity = new ArrayList<Float>();
	public static ArrayList<Float> yVelocity = new ArrayList<Float>();
	
	public static ArrayList<Float> xDistance = new ArrayList<Float>();
	public static ArrayList<Float> yDistance = new ArrayList<Float>();
	
	public static ArrayList<Float> xLocation = new ArrayList<Float>();
	public static ArrayList<Float> yLocation = new ArrayList<Float>();
	
	public static ArrayList<Float> lineVertices = new ArrayList<Float>();




	
	int asize;
	int gsize;
	static int mode;
	
	
	static int cali_tsize = cali_time.size();
	static int cali_asize = cali_avalues.size();
	static int cali_axsize = cali_axvalues.size();
	static int cali_aysize = cali_ayvalues.size();
	static int cali_azsize = cali_azvalues.size();
	static int xvsize = xVelocity.size();
	static int yvsize = yVelocity.size();
	static int xdsize = xDistance.size();
	static int ydsize = yDistance.size();
	static int xlsize = xLocation.size();
	static int ylsize = yLocation.size();
	
	
	//从main函数传递过来的传感器以及时间戳数据
	public static float  avalues[] = new float[]{};
	public static float  gvalues[] = new float[]{};
	public static float  angles [] = new float[]{};
	public static float  matrix [] = new float[]{};
	public static long   tvalues[] = new long []{};
	public static long rawtime[] = new long[]{};
	public static float acce[] = new float[]{};
	public static float gravity[] = new float[]{};
	
	public static double cali_tvalues[] = new double[cali_tsize];
	
	//经过坐标系转换后的各轴加速度数据
	public static float cal_axvalues[] = new float[cali_axsize];
	public static float cal_ayvalues[] = new float[cali_aysize];
	public static float cal_azvalues[] = new float[cali_azsize];

	//各轴向的速度数据
	public static float xvelocity[] = new float[xvsize];
	public static float yvelocity[] = new float[yvsize];
	//各轴向的位移数据（每两个时间节点之间的位移）
	public static float xdistance[] = new float[xdsize];
	public static float ydistance[] = new float[ydsize];
	
	//每个采样出来的绘图点之间的位置
	public static float xlocation[] = new float[xlsize];
	public static float ylocation[] = new float[ylsize];
	
	//创建新的活动时，记得要在manifest文件中对新activity进行补充说明
	//<activity android name: = "新活动名"> </activity>
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); //设置全屏
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                         WindowManager.LayoutParams.FLAG_FULLSCREEN);


		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		avalues = bundle.getFloatArray("linear");
		gvalues = bundle.getFloatArray("gList");
		matrix  = bundle.getFloatArray("matrix");
		angles  = bundle.getFloatArray("angle");
		acce = bundle.getFloatArray("aList");
		gravity = bundle.getFloatArray("gravity");
		tvalues = bundle.getLongArray("time");
		rawtime = bundle.getLongArray("rawtime");
		
		for(int t=0; t<tvalues.length; t++)
			cali_time.add(tvalues[t]/10e9);
		for(int t=0; t<cali_time.size()-1; t++){
			cali_time.set(t+1, cali_time.get(t+1)-cali_time.get(0));
			
		}
		cali_time.set(0, 0d);
		Double Dcalitime[] = cali_time.toArray(new Double[0]); 
		for(int t=0; t<cali_tsize; t++)
			cali_tvalues[t] = Dcalitime[t].doubleValue();


		
		asize = bundle.getInt("asize");
		gsize = bundle.getInt("gsize");
		mode =  bundle.getInt("mode");
	
		
		
		int Index; //ArrayList中元素标号
		
			//先获取各个时间节点对应的实际加速度数值（世界坐标系下的）
			for (Index = 0; (Index<tvalues.length)&&(3*Index < asize)&&(3*Index < gsize); Index++){
				float rinv[] = new float[16];
				for(int i=0; i<16; i++)
				    rinv[i] = matrix[Index*16+i];
				 
				float Calculated_Acce [] = CalculateAcce(rinv, 
						//acce[3*Index], acce[3*Index+1], acce[3*Index+2]);
						avalues[3*Index],avalues[3*Index+1],avalues[3*Index+2]);
				
				cali_axvalues.add(Calculated_Acce[0]);
				cali_ayvalues.add(Calculated_Acce[1]);
				cali_azvalues.add(Calculated_Acce[2]);

				
				cali_avalues.add(Calculated_Acce[0]);
				cali_avalues.add(Calculated_Acce[1]);
				cali_avalues.add(Calculated_Acce[2]);

			}
			
			
			//计算加速的的平均值作为新添加的维度
			for (int i=0; i<cali_avalues.size(); i+=3){
				float average = (Math.abs(cali_avalues.get(i))+
								 Math.abs(cali_avalues.get(i+1))+
								 Math.abs(cali_avalues.get(i+2)))/3;
				average_a.add(average);
			}

			
			//计算各个时间节点对应的速度值
			//计算加速度到速度的积分时，简单采用梯形法进行面积计算
			for(int tIndex=1; (tIndex<tvalues.length)&&(3*tIndex < asize)&&(3*tIndex < gsize); tIndex++){
				if(xVelocity.size()==0 && yVelocity.size()==0){
				xVelocity.add(0f);
				yVelocity.add(0f);
				}
				
				xVelocity.add((float) (xVelocity.get(tIndex-1)+
						          (cali_axvalues.get(tIndex)+cali_axvalues.get(tIndex-1))*(cali_time.get(tIndex)-cali_time.get(tIndex-1))/2));
				yVelocity.add((float) (yVelocity.get(tIndex-1)+
						          (cali_ayvalues.get(tIndex)+cali_ayvalues.get(tIndex-1))*(cali_time.get(tIndex)-cali_time.get(tIndex-1))/2));
				
			}
			
			Float Fxvelocity[] = xVelocity.toArray(new Float[0]);
			for(int i=0; i<xvsize; i++){
				xvelocity[i] = Fxvelocity[i].floatValue();
			}
			
			Float Fyvelocity[] = yVelocity.toArray(new Float[0]);
			for(int i=0; i<yvsize; i++){
				yvelocity[i] = Fyvelocity[i].floatValue();
			}
			
			//计算各个时间节点（dIndex-1与dIndex）之间的位移值
			for(int dIndex=1; (dIndex<tvalues.length)&&(3*dIndex < asize)&&(3*dIndex < gsize); dIndex++){
				if(xDistance.size()==0 && yDistance.size()==0){
				xDistance.add(0f);
				yDistance.add(0f);}
				
				xDistance.add(CalculateDistance(xVelocity.get(dIndex-1), 
						                              cali_axvalues.get(dIndex-1), cali_axvalues.get(dIndex),
						                              cali_time.get(dIndex-1), cali_time.get(dIndex) ));
				
				yDistance.add(CalculateDistance(yVelocity.get(dIndex-1), 
                                                      cali_ayvalues.get(dIndex-1), cali_ayvalues.get(dIndex),
                                                      cali_time.get(dIndex-1), cali_time.get(dIndex) ));				
			}
			Float Fxdistance[] = xDistance.toArray(new Float[0]);
			for(int i=0; i<xdsize; i++){
				xdistance[i] = Fxdistance[i].floatValue();
			}
			
			Float Fydistance[] = yDistance.toArray(new Float[0]);
			for(int i=0; i<ydsize; i++){
				ydistance[i] = Fydistance[i].floatValue();
			}
			
			for(int cIndex=0; (cIndex<tvalues.length)&&(3*cIndex < asize)&&(3*cIndex < gsize); cIndex++){
				

				if(cIndex%6==0){
					float totaldisx=0f, totaldisy=0f;
					
					cubeList.add(new Cube(cali_avalues, gvalues, cIndex,average_a.get(cIndex),mode));
					
					for(int seg=0;seg<=cIndex;seg++){
						
								totaldisx+=xDistance.get(seg);
								totaldisy+=yDistance.get(seg);
					}
					xLocation.add(totaldisx);
					yLocation.add(totaldisy);

					
				}
			}
			Float Fxlocation[] = xLocation.toArray(new Float[0]);
			for(int i=0; i<xlsize; i++){
				xlocation[i] = Fxlocation[i].floatValue();
			}
			
			Float Fylocation[] = yLocation.toArray(new Float[0]);
			for(int i=0; i<ylsize; i++){
				ylocation[i] = Fylocation[i].floatValue();
			}
			
			for (int i=0; i<xLocation.size(); i++){
				lineVertices.add(xLocation.get(i)*DataConfig.scalelocation);
				lineVertices.add(yLocation.get(i)*DataConfig.scalelocation);
				lineVertices.add(0f);
			}
			
			for (int k=0; k<lineVertices.size()/3-1; k++){
				
				lineList.add(new Linesub(lineVertices, k+1));
				
			}
			
			
			
			Integer num = cubeList.size();
			String s=num.toString();
			Toast.makeText(ResultActivity.this,s,Toast.LENGTH_LONG).show();
			
			
		
		myGLSurfaceView = new CubeSurfaceView(this);
		setContentView(myGLSurfaceView);
		myGLSurfaceView.requestFocus();
		myGLSurfaceView.setFocusableInTouchMode(true);
		
		
	}

    /*根据Orientation方法得到的角度，其对应的坐标系是：
    *x轴与地面相切，大致指向正西方，是y轴与z轴的向量乘积；
    *y轴与地面相切，指向地磁北极；
    *z轴与地面垂直，指向地心
    *所有的角度数值都是以弧度为单位，并且以逆时针旋转为正*/
	public float[] CalculateAcce(float rinv[],float rax, float ray, float raz){
		float resultVec[] = new float[4];
		float ra[] = new float[4];
		
		ra[0] = rax;
		ra[1] = ray;
		ra[2] = raz;
		ra[3] = 0;
		/*
		resultVec[0]=rinv[0]*ra[0]+rinv[1]*ra[1]+rinv[2]*ra[2];
		resultVec[1]=rinv[4]*ra[0]+rinv[5]*ra[1]+rinv[6]+ra[2];
		resultVec[2]=rinv[8]*ra[0]+rinv[9]*ra[1]+rinv[10]+ra[2];
		*/
		Matrix.multiplyMV(resultVec, 0, rinv, 0, ra, 0);
		return resultVec;
		
	}
	

	public float CalculateDistance (float vstart, float astart, float aend, double tstart, double tend){
		float dist;
		double slope;

		slope = (aend-astart)/(tend-tstart);
		
	    dist = (float) 
	    	   (1/6*slope*(Math.pow(tend, 3)-Math.pow(tstart, 3))+
	    	   1/2*(astart-slope*tstart)*(Math.pow(tend, 2)-Math.pow(tstart, 2))+
	    	   (vstart-(0.5*slope*tstart*tstart)-(aend-slope*tend)*tstart)*(tend-tstart));
		
		return dist;
	}
	
	@Override
	protected void onPause(){
		
		super.onPause();
		myGLSurfaceView.onPause();
	}
	
	@Override
	protected void onResume(){
		
		super.onResume();
		myGLSurfaceView.onResume();
	}
	
	protected void onStop(){
		
		super.onStop();
		super.finish();

	}
	

	
}
