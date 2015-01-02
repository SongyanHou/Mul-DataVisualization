package com.housy.mul_datavisualization;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class CubeSurfaceView extends GLSurfaceView{
	
	private CubeRenderer cubeRenderer;
	public double lastFingerDis;
	
	public float dx;
	public float dy;
	private float lastXMove=-1;
	private float lastYMove=-1;
	
	
	public CubeSurfaceView(Context context){
		super(context);
		
		cubeRenderer = new CubeRenderer(context);
		setRenderer(cubeRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}
	
	public boolean onTouchEvent(MotionEvent event){
		int count = event.getPointerCount();
		
		switch (event.getActionMasked()){
		case MotionEvent.ACTION_POINTER_DOWN:
			
			if(count == 2){
				lastFingerDis = distanceBetweenFingers(event);
			}
			break;
		
		case MotionEvent.ACTION_MOVE:
			if(count == 1){
				float xMove = event.getX();
				float yMove = event.getY();
				
				if(lastXMove == -1 && lastYMove ==-1){
					lastXMove = xMove;
					lastYMove = yMove;
				}
				DataConfig.dx = (xMove - lastXMove)*10;
				DataConfig.dy = (yMove - lastYMove)*10;
				
				lastXMove = xMove;
				lastYMove = yMove;
			}
			
			if(count == 2){
				double fingerDis = distanceBetweenFingers(event);
				
				if (fingerDis > lastFingerDis){
					//zoom out
					DataConfig.scalez+=0.5;
				}
				
				else{
					//zoom in
					DataConfig.scalez-=0.5;
				}
			}
			break;
		
		case MotionEvent.ACTION_POINTER_UP:
			if(count==2){
			lastXMove = -1;
			lastYMove = -1;
			}
			break;
			
		case MotionEvent.ACTION_UP:
			
			lastXMove = -1;
			lastYMove = -1;
			
			break;
		
		default:
			break;
		}
		
		return true;
	}
	
	public double distanceBetweenFingers(MotionEvent event){
		float disX = Math.abs(event.getX(0)-event.getX(1));
		float disY = Math.abs(event.getY(0)-event.getY(1));
		
		return Math.sqrt(disX*disX+disY*disY);
	}

}
