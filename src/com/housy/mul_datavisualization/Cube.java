package com.housy.mul_datavisualization;

import java.util.ArrayList;

public class Cube extends Mesh{
	
	
	public Cube(ArrayList<Float> side, float angle[], int num, float dis, int mode){ //num参数对应的是时间节点 所以要进行乘3的操作
	
		float width  = side.get( 3*num )*1.5f;
		float height = side.get(3*num+1)*1.5f; 
		float depth  = side.get(3*num+2)*1.5f;
		rx = angle[ 3*num ];
		ry = angle[3*num+1];
		rz = angle[3*num+2];
		
		width /=2; 
		height /=2; 
		depth /=2; 
		
 		
		float vertices[]={-width, -height, -depth, //0
				           width, -height, -depth, //1
				           width,  height, -depth, //2
				          -width,  height, -depth, //3
				          -width, -height,  depth, //4
				           width, -height,  depth, //5
				           width,  height,  depth, //6
				          -width,  height,  depth, //7
				           width,  Math.abs(height+dis), -depth,//平行线顶点1（顶点8）
				          -width,  Math.abs(height+dis), -depth //平行线顶点2（顶点9）
				          };
		/*
		short indices[]={0, 4, 5,
				         0, 5, 1,
				         1, 5, 6,
                         1, 6, 2,
                         2, 6, 7,
                         2, 7, 3,
                         3, 7, 4,
                         3, 4, 0,
                         4, 7, 6,
                         4, 6, 5,
                         3, 0, 1,
                         3, 1, 2, 
                         };*/
		
		short indices_r[] = {3, 0, 1,
				             3, 1, 2,
				             4, 7, 6,
				             4, 6, 5
				             };
		short indices_g[] = {1, 5, 6,
				             1, 6, 2,
				             3, 7, 4,
				             3, 4, 0
				             };
		short indices_b[] = {0, 4, 5,
				             0, 5, 1,
				             2, 6, 7,
				             2, 7, 3,
				             };
		byte paralineIndices[] = {8, 9};
		
		byte lineIndicesx[]={0,1,   //x axis, red color
				             2,3,
				             4,5,
				             6,7
		                     };
		byte lineIndicesy[]={0,4,   //y axis, green color
	                         2,6,
	                         1,5,
	                         3,7
                             };
		byte lineIndicesz[]={0,3,   //x axis, blue color
	                         1,2,
	                         5,6,
	                         4,7
	                         };
		
		setVertices(vertices);
		
		//setIndices(indices);
		setIndicesR(indices_r);
		setIndicesG(indices_g);
		setIndicesB(indices_b);
		
		setParalineIndices(paralineIndices);
		
		setLineIndex_X(lineIndicesx);
		setLineIndex_Y(lineIndicesy);
		setLineIndex_Z(lineIndicesz);

	}

}
