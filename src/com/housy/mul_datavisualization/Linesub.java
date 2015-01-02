package com.housy.mul_datavisualization;

import java.util.ArrayList;

public class Linesub extends Line{
	
	public Linesub(ArrayList <Float> verticesList, int index){
		
		Float Fvertices[] = verticesList.toArray(new Float[0]);
		//int size = verticesList.size();
		float vertices[] =new float[6];
		for(int i=0; i<6; i++)
			vertices[i] = Fvertices[(index-1)*3+i];
		
		byte indices[] = new byte[] {0,1};
		/*
		indices[0]=0;
		indices[2*(size/3-1)-1]=(byte) (size/3-1);
		byte k=1;
		for(int j=1; j<2*(size/3-1)-1; j+=2){
			
			indices[j]=k;
			indices[j+1]=k;
			k++;
			
		}*/
		
		setIndices(indices);
		setVertices(vertices);
		
	}

}
