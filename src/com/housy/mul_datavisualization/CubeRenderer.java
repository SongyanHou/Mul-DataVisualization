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
			
			//����켣ʱ��ȫ���Ե�һ��cubeΪ���գ�ͨ��acceList���飨Ӧ��������ϵ�����������飩�д�0��ʼ�����ݣ��������µ�cube���һ��cube�����λ��
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
		
		// ���õ�ǰ�ӿڵ��³ߴ�
		gl.glViewport(0, 0, width, height);
		// ѡ��ͶӰ����
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// ����ͶӰ����
		gl.glLoadIdentity();
		// ���㴰�ڵ���۱���
		GLU.gluPerspective(gl, 45.0f,
	                          (float) width / (float) height,
	                          0.1f, 100.0f);

		// ѡ��ģʽ�۲����
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		// ����ģʽ�۲����
		gl.glLoadIdentity();

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.5f); 

		// ������Ӱƽ��, Ĭ��Ҳ���ԣ����Ǳ����
		gl.glShadeModel(GL10.GL_SMOOTH);

		// ��Ȼ�������
		gl.glClearDepthf(1.0f);

		// ������ȼ��
		gl.glEnable(GL10.GL_DEPTH_TEST);

		// Ҫ������ȼ������
		gl.glDepthFunc(GL10.GL_LEQUAL);

		// ����ϸ΢��͸�Ӽ���
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		gl.glLineWidthx(5);
	}

}
