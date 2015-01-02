package com.housy.mul_datavisualization;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;


public class CubeRenderer extends ResultActivity implements Renderer {
	
	int cubenum;
	private Context context;

	public CubeRenderer(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);		
		gl.glEnable(GL10.GL_LINE_SMOOTH);

		for (cubenum = 0; cubenum < cubeList.size(); cubenum++){
			
			//Each time we want to draw a new cube, we must call glLoadIdentity() to reset the canvas 
			gl.glLoadIdentity();
			gl.glTranslatef((xLocation.get(cubenum)*DataConfig.scalelocation), 
					        (yLocation.get(cubenum)*DataConfig.scalelocation), 
					        -35+DataConfig.scalez);
			/*
			GLU.gluLookAt(gl, 
					DataConfig.dx, DataConfig.dy, 0, 
					0, 0, 0, 
					0, 1, 0);*/
			
			//计算轨迹时，全部以第一个cube为参照，通过acceList数组（应该是坐标系修正过的数组）中从0开始的数据，来计算新的cube与第一个cube的相对位置
			if (mode == 1){
				if (cubenum == 0)
					cubeList.get(0).draw(gl, GL10.GL_LINES);
				else{
			        cubeList.get(cubenum).draw(gl, GL10.GL_LINES);
			        gl.glLoadIdentity();
			        gl.glTranslatef(0,0,-35+DataConfig.scalez);//+DataConfig.scalez);
			        lineList.get(cubenum-1).draw(gl);
			       
				}
				
			}
			else if(mode == 2){
				if (cubenum == 0)
					cubeList.get(0).draw(gl, GL10.GL_TRIANGLES);
				else{
			        cubeList.get(cubenum).draw(gl, GL10.GL_TRIANGLES);
			        gl.glLoadIdentity();
			        gl.glTranslatef(0,0,-35+DataConfig.scalez);
			        lineList.get(cubenum-1).draw(gl);
				}
				
			}
				
		}
		
		gl.glDisable(GL10.GL_LINE_SMOOTH);
		

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		
		// 设置当前视口到新尺寸
		gl.glViewport(0, 0, width, height);
		// 选择投影矩阵
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// 重置投影矩阵
		gl.glLoadIdentity();
		// 计算窗口的外观比率
		GLU.gluPerspective(gl, 45.0f,
	                          (float) width / (float) height,
	                          0.1f, 100.0f);

		// 选择模式观察矩阵
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		// 重置模式观察矩阵
		gl.glLoadIdentity();

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.5f); 

		// 允许阴影平滑, 默认也可以，不是必须的
		gl.glShadeModel(GL10.GL_SMOOTH);

		// 深度缓冲设置
		gl.glClearDepthf(1.0f);

		// 允许深度检测
		gl.glEnable(GL10.GL_DEPTH_TEST);

		// 要做的深度检测类型
		gl.glDepthFunc(GL10.GL_LEQUAL);

		// 真是细微的透视计算
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		gl.glLineWidthx(5);
	}

}
