package com.housy.mul_datavisualization;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Line {
	     // Our vertex buffer.
		 private FloatBuffer verticesBuffer = null;
		 
		 // Our index buffer.
		 private ByteBuffer indicesBuffer = null;
		 //private byte indices[] = {0,1};

		 // The number of indices.
		 private int numOfIndices = -1;

		 public void draw(GL10 gl) {
		     // Counter-clockwise winding.
		     gl.glFrontFace(GL10.GL_CCW);
		     // Enable face culling.
		     gl.glEnable(GL10.GL_CULL_FACE);
		     // What faces to remove with the face culling.
		     gl.glCullFace(GL10.GL_BACK);
		     // Enabled the vertices buffer for writing and to be used during rendering.
		     gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		     // Specifies the location and data format of an array of vertex coordinates to use when rendering.
	 		
			 gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
			 // Point out the where the color buffer is.
			 // Set flat color
			 gl.glColor4f(0, 0, 0, 0.5f);
			 gl.glDrawElements(GL10.GL_LINES, numOfIndices, 
					           GL10.GL_UNSIGNED_BYTE,indicesBuffer);
			 
			 
			 // Disable the vertices buffer.
			 gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			 // Disable face culling.
			 gl.glDisable(GL10.GL_CULL_FACE);
			 
		 }

		 protected void setVertices(float[] vertices) {
		 // a float is 4 bytes, therefore
		 //we multiply the number if
		 // vertices with 4.
		 ByteBuffer vbb
		 = ByteBuffer.allocateDirect(vertices.length * 4);
		 vbb.order(ByteOrder.nativeOrder());
		 verticesBuffer = vbb.asFloatBuffer();
		 verticesBuffer.put(vertices);
		 verticesBuffer.position(0);
		 }

		 protected void setIndices(byte[] indices) {
			 // short is 2 bytes
				 
			     ByteBuffer ibb
			     = ByteBuffer.allocateDirect(indices.length * 2);
			     ibb.order(ByteOrder.nativeOrder());
			     indicesBuffer = ibb;
		    	 indicesBuffer.put(indices);
			     indicesBuffer.position(0);
			     numOfIndices = indices.length;
			 }
}
	

