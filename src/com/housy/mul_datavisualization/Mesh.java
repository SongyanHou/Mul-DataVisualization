package com.housy.mul_datavisualization;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Mesh {
	 // Our vertex buffer.
	 private FloatBuffer verticesBuffer = null;
	 // Our index buffer.
	 private ShortBuffer indicesBuffer_R = null;
	 private ShortBuffer indicesBuffer_G = null;
	 private ShortBuffer indicesBuffer_B = null;
	 //Line mode index
	 private ByteBuffer lineindicesBufferX = null;
	 private ByteBuffer lineindicesBufferY = null;
	 private ByteBuffer lineindicesBufferZ = null;
	 
	 private ByteBuffer paralineIndicesBuffer = null;

	 // The number of indices.
	 private int numOfIndices = -1;
	 private int numOfIndices_R = -1;
	 private int numOfIndices_G = -1;
	 private int numOfIndices_B = -1;

	 // Flat Color
	 private float[] rgba
	 = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };

	 // Smooth Colors
	 private FloatBuffer colorBuffer = null;

	 // Translate parameters.
	 public float x = 0;
	 public float y = 0;
	 public float z = 0;

	 // Rotate parameters.
	 public float rx = 0;
	 public float ry = 0;
	 public float rz = 0;

	 public void draw(GL10 gl, int mode) {
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
	 
	 if(mode == GL10.GL_TRIANGLES){

	 // Set flat color
	 gl.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
	

	 gl.glTranslatef(x, y, z);
	 gl.glRotatef(rx, 1, 0, 0);
	 gl.glRotatef(ry, 0, 1, 0);
	 gl.glRotatef(rz, 0, 0, 1);

	 // Point out the where the color buffer is.
	 gl.glColor4f(1.0f, 0.0f, 0.0f, 0.5f);
	 gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices_R,
	 GL10.GL_UNSIGNED_SHORT, indicesBuffer_R);
	 
	 gl.glColor4f(0.0f, 1.0f, 0.0f, 0.5f);
	 gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices_G,
	 GL10.GL_UNSIGNED_SHORT, indicesBuffer_G);
	 
	 gl.glColor4f(0.0f, 0.0f, 1.0f, 0.5f);
	 gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices_B,
	 GL10.GL_UNSIGNED_SHORT, indicesBuffer_B);
	 
	 gl.glColor4f(0, 0, 0, 0.5f);
	 gl.glDrawElements(GL10.GL_LINES, 2,
			 GL10.GL_UNSIGNED_BYTE, paralineIndicesBuffer);
	 // Disable the vertices buffer.
	 gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	 // Disable face culling.
	 gl.glDisable(GL10.GL_CULL_FACE);
	 }
	 
	 else if(mode == GL10.GL_LINES){

		 gl.glTranslatef(x, y, z);
		 gl.glRotatef(rx, 1, 0, 0);
		 gl.glRotatef(ry, 0, 1, 0);
		 gl.glRotatef(rz, 0, 0, 1);

		 // Point out the where the color buffer is.
		 // Set flat color
		 gl.glColor4f(1, 0, 0, 0.5f);
		 gl.glDrawElements(GL10.GL_LINES, numOfIndices,
		         GL10.GL_UNSIGNED_BYTE, lineindicesBufferX);
		 
		 gl.glColor4f(0, 1, 0, 0.5f);
		 gl.glDrawElements(GL10.GL_LINES, numOfIndices,
				 GL10.GL_UNSIGNED_BYTE, lineindicesBufferY);
		 
		 gl.glColor4f(0, 0, 1, 0.5f);
		 gl.glDrawElements(GL10.GL_LINES, numOfIndices,
				 GL10.GL_UNSIGNED_BYTE, lineindicesBufferZ);
		 
		 gl.glColor4f(0, 0, 0, 0.5f);
		 gl.glDrawElements(GL10.GL_LINES, 2,
				 GL10.GL_UNSIGNED_BYTE, paralineIndicesBuffer);
		 
		 
		 // Disable the vertices buffer.
		 gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		 // Disable face culling.
		 gl.glDisable(GL10.GL_CULL_FACE);
		 
	     }
	 }

	 protected void setVertices(float[] vertices) {
	 // a float is 4 bytes
	
	     ByteBuffer vbb
	     = ByteBuffer.allocateDirect(vertices.length * 4);
	     vbb.order(ByteOrder.nativeOrder());
	     verticesBuffer = vbb.asFloatBuffer();
	     verticesBuffer.put(vertices);
	     verticesBuffer.position(0);
	 }

	 protected void setIndicesR(short[] indices) {
	 // short is 2 bytes
		 
	     ByteBuffer ibb
	     = ByteBuffer.allocateDirect(indices.length * 2);
	     ibb.order(ByteOrder.nativeOrder());
	     indicesBuffer_R = ibb.asShortBuffer();
    	 indicesBuffer_R.put(indices);
	     indicesBuffer_R.position(0);
	     numOfIndices_R = indices.length;
	 }
	 
	 protected void setIndicesG(short[] indices) {
	 // short is 2 bytes
		 
	     ByteBuffer ibb
	     = ByteBuffer.allocateDirect(indices.length * 2);
	     ibb.order(ByteOrder.nativeOrder());
	     indicesBuffer_G = ibb.asShortBuffer();
    	 indicesBuffer_G.put(indices);
	     indicesBuffer_G.position(0);
	     numOfIndices_G = indices.length;
	 }
	 
	 protected void setIndicesB(short[] indices) {
	 // short is 2 bytes
		 
	     ByteBuffer ibb
	     = ByteBuffer.allocateDirect(indices.length * 2);
	     ibb.order(ByteOrder.nativeOrder());
	     indicesBuffer_B = ibb.asShortBuffer();
    	 indicesBuffer_B.put(indices);
	     indicesBuffer_B.position(0);
	     numOfIndices_B = indices.length;
	 }
	 
	 protected void setParalineIndices(byte[] indices) {
	 // short is 2 bytes, therefore we multiply
	 //the number if
	 // vertices with 2.
	 ByteBuffer ibb
	 = ByteBuffer.allocateDirect(indices.length);
	 ibb.order(ByteOrder.nativeOrder());
	 paralineIndicesBuffer = ibb;
	 paralineIndicesBuffer.put(indices);
	 paralineIndicesBuffer.position(0);
	 numOfIndices = indices.length;
	 }
	 
	 protected void setLineIndex_X(byte[] lineIndex){
	 ByteBuffer ibb
     = ByteBuffer.allocateDirect(lineIndex.length * 2);
	 ibb.order(ByteOrder.nativeOrder());
	 lineindicesBufferX = ibb;
	 lineindicesBufferX.put(lineIndex);
	 lineindicesBufferX.position(0);
	 numOfIndices = lineIndex.length;
		 
	 }
	 protected void setLineIndex_Y(byte[] lineIndex){
	 ByteBuffer ibb
     = ByteBuffer.allocateDirect(lineIndex.length * 2);
	 ibb.order(ByteOrder.nativeOrder());
	 lineindicesBufferY = ibb;
	 lineindicesBufferY.put(lineIndex);
	 lineindicesBufferY.position(0);
	 numOfIndices = lineIndex.length;
		 
	 }
	 protected void setLineIndex_Z(byte[] lineIndex){
	 ByteBuffer ibb
     = ByteBuffer.allocateDirect(lineIndex.length * 2);
	 ibb.order(ByteOrder.nativeOrder());
	 lineindicesBufferZ = ibb;
	 lineindicesBufferZ.put(lineIndex);
	 lineindicesBufferZ.position(0);
	 numOfIndices = lineIndex.length;
		 
	 }

	 protected void setColor(float red, float green,
	 float blue, float alpha) {
	 // Setting the flat color.
	 rgba[0] = red;
	 rgba[1] = green;
	 rgba[2] = blue;
	 rgba[3] = alpha;
	 }


	 protected void setColors(float[] colors) {
	 // float has 4 bytes.
	 ByteBuffer cbb
	 = ByteBuffer.allocateDirect(colors.length * 4);
	 cbb.order(ByteOrder.nativeOrder());
	 colorBuffer = cbb.asFloatBuffer();
	 colorBuffer.put(colors);
	 colorBuffer.position(0);
	 }
}