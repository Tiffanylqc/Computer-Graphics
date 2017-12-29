package geometry;

import geometry.Vertex3D;

public class Transformation {
	public double [][]matrix=new double [4][4];
	
	public Transformation(double[][] matrix){
		this.matrix=matrix;
	}
	
	public static Transformation identity(){
		double identity[][]={	{1.0,0.0,0.0,0.0},
								{0.0,1.0,0.0,0.0},
								{0.0,0.0,1.0,0.0},
								{0.0,0.0,0.0,1.0} };
		return new Transformation(identity);
	}

	public static Transformation MakeScaleTransformation(double sx,double sy,double sz){
		double Scale[][]={ 	{sx,0.0,0.0,0.0},
							{0.0,sy,0.0,0.0},
							{0.0,0.0,sz,0.0},
							{0.0,0.0,0.0,1.0} };
		
		return new Transformation(Scale);
	}
	
	public static Transformation MakeTranslateTransformation(double tx,double ty,double tz){
		double Translate[][]={ 	{1.0,0.0,0.0,tx},
								{0.0,1.0,0.0,ty},
								{0.0,0.0,1.0,tz},
								{0.0,0.0,0.0,1.0} };
		
		return new Transformation(Translate);
	}
	
	public static Transformation MakeRotateTransformation(String axis,double angleInDegree){
		double rotate[][]={	{0.0,0.0,0.0,0.0},
							{0.0,0.0,0.0,0.0},
							{0.0,0.0,0.0,0.0},
							{0.0,0.0,0.0,0.0} };
		double angleInRadian=(angleInDegree/180.0)*Math.PI;
		
		if(axis.equals("X")){
			rotate[0][0]=1.0;
			rotate[1][1]=Math.cos(angleInRadian);
			rotate[1][2]=-Math.sin(angleInRadian);
			rotate[2][1]=Math.sin(angleInRadian);
			rotate[2][2]=Math.cos(angleInRadian);
			rotate[3][3]=1.0;
		}
		else if(axis.equals("Y")){
			rotate[1][1]=1.0;
			rotate[0][0]=Math.cos(angleInRadian);
			rotate[2][0]=-Math.sin(angleInRadian);
			rotate[0][2]=Math.sin(angleInRadian);
			rotate[2][2]=Math.cos(angleInRadian);
			rotate[3][3]=1.0;
		}
		else if(axis.equals("Z")){
			rotate[2][2]=1.0;
			rotate[0][0]=Math.cos(angleInRadian);
			rotate[0][1]=-Math.sin(angleInRadian);
			rotate[1][0]=Math.sin(angleInRadian);
			rotate[1][1]=Math.cos(angleInRadian);
			rotate[3][3]=1.0;
		}
		return new Transformation(rotate);
	}
	
	public Transformation doTransformation(Transformation x){
		double result[][]=new double[4][4];//this*x
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				double sum=0.0;
				for(int k=0;k<4;k++){
					sum+=this.matrix[i][k]*x.matrix[k][j];
				}
				result[i][j]=sum;
			}
		}
		return new Transformation(result);
	}
	
	public Vertex3D transformVertex(Vertex3D vertex){
		double[] vector=new double[4];
		double[] result=new double[4];
		vector[0]=vertex.getPoint3D().getX();
		vector[1]=vertex.getPoint3D().getY();
		vector[2]=vertex.getPoint3D().getZ();
		vector[3]=vertex.getPoint3D().getW();
		
		for(int i=0;i<4;i++){
			double sum=0;
			for(int j=0;j<4;j++){
				sum+=matrix[i][j]*vector[j];
			}
			result[i]=sum;
		}
		return new Vertex3D(result[0]/result[3],result[1]/result[3],result[2]/result[3],vertex.getColor());
	}
	
	public Point3DH transformVertex(Point3DH vertex){
		double[] vector=new double[4];
		double[] result=new double[4];
		vector[0]=vertex.getX();
		vector[1]=vertex.getY();
		vector[2]=vertex.getZ();
		vector[3]=vertex.getW();
		
		for(int i=0;i<4;i++){
			double sum=0;
			for(int j=0;j<4;j++){
				sum+=matrix[i][j]*vector[j];
			}
			result[i]=sum;
		}
		return new Point3DH(result[0]/result[3],result[1]/result[3],result[2]/result[3]);
	}
	
	public Transformation Inverse(){
		double det=matrix[0][0]*matrix[1][1]*matrix[2][2]*matrix[3][3]+matrix[0][0]*matrix[1][2]*matrix[2][3]*matrix[3][1]
				  +matrix[0][0]*matrix[1][3]*matrix[2][1]*matrix[3][2]+matrix[0][1]*matrix[1][0]*matrix[2][3]*matrix[3][2]
				  +matrix[0][1]*matrix[1][2]*matrix[2][0]*matrix[3][3]+matrix[0][1]*matrix[1][3]*matrix[2][2]*matrix[3][0]
				  +matrix[0][2]*matrix[1][0]*matrix[2][1]*matrix[3][3]+matrix[0][2]*matrix[1][1]*matrix[2][3]*matrix[3][0]
				+matrix[0][2]*matrix[1][3]*matrix[2][0]*matrix[3][1]+matrix[0][3]*matrix[1][0]*matrix[2][2]*matrix[3][1]
						+matrix[0][3]*matrix[1][1]*matrix[2][0]*matrix[3][2]+matrix[0][3]*matrix[1][2]*matrix[2][1]*matrix[3][0]
						-matrix[0][0]*matrix[1][1]*matrix[2][3]*matrix[3][2]-matrix[0][0]*matrix[1][2]*matrix[2][1]*matrix[3][3]
						-matrix[0][0]*matrix[1][3]*matrix[2][2]*matrix[3][1]-matrix[0][1]*matrix[1][0]*matrix[2][2]*matrix[3][3]
						-matrix[0][1]*matrix[1][2]*matrix[2][3]*matrix[3][0]-matrix[0][1]*matrix[1][3]*matrix[2][0]*matrix[3][2]
						-matrix[0][2]*matrix[1][0]*matrix[2][3]*matrix[3][1]-matrix[0][2]*matrix[1][1]*matrix[2][0]*matrix[3][3]
						-matrix[0][2]*matrix[1][3]*matrix[2][1]*matrix[3][0]-matrix[0][3]*matrix[1][0]*matrix[2][1]*matrix[3][2]
						-matrix[0][3]*matrix[1][1]*matrix[2][2]*matrix[3][0]-matrix[0][3]*matrix[1][2]*matrix[2][0]*matrix[3][1];
		
		double[][] inverse=new double[4][4];
		inverse[0][0]=matrix[1][1]*matrix[2][2]*matrix[3][3]+matrix[1][2]*matrix[2][3]*matrix[3][1]+matrix[1][3]*matrix[2][1]*matrix[3][2]
					-matrix[1][1]*matrix[2][3]*matrix[3][2]-matrix[1][2]*matrix[2][1]*matrix[3][3]-matrix[1][3]*matrix[2][2]*matrix[3][1];
		
		inverse[0][1]=matrix[0][1]*matrix[2][3]*matrix[3][2]+matrix[0][2]*matrix[2][1]*matrix[3][3]+matrix[0][3]*matrix[2][2]*matrix[3][1]
				-matrix[0][1]*matrix[2][2]*matrix[3][3]-matrix[0][2]*matrix[2][3]*matrix[3][1]-matrix[0][3]*matrix[2][1]*matrix[3][2];
		
		inverse[0][2]=matrix[0][1]*matrix[1][2]*matrix[3][3]+matrix[0][2]*matrix[1][3]*matrix[3][1]+matrix[0][3]*matrix[1][1]*matrix[3][2]
				-matrix[0][1]*matrix[1][3]*matrix[3][2]-matrix[0][2]*matrix[1][1]*matrix[3][3]-matrix[0][3]*matrix[1][2]*matrix[3][1];
		
		inverse[0][3]=matrix[0][1]*matrix[1][3]*matrix[2][2]+matrix[0][2]*matrix[1][1]*matrix[2][3]+matrix[0][3]*matrix[1][2]*matrix[2][1]
				-matrix[0][1]*matrix[1][2]*matrix[2][3]-matrix[0][2]*matrix[1][3]*matrix[2][1]-matrix[0][3]*matrix[1][1]*matrix[2][2];
		
		inverse[1][0]=matrix[1][0]*matrix[2][3]*matrix[3][2]+matrix[1][2]*matrix[2][0]*matrix[3][3]+matrix[1][3]*matrix[2][2]*matrix[3][0]
				-matrix[1][0]*matrix[2][2]*matrix[3][3]-matrix[1][2]*matrix[2][3]*matrix[3][0]-matrix[1][3]*matrix[2][0]*matrix[3][2];
		
		inverse[1][1]=matrix[0][0]*matrix[2][2]*matrix[3][3]+matrix[0][2]*matrix[2][3]*matrix[3][0]+matrix[0][3]*matrix[2][0]*matrix[3][2]
				-matrix[0][0]*matrix[2][3]*matrix[3][2]-matrix[0][2]*matrix[2][0]*matrix[3][3]-matrix[0][3]*matrix[2][2]*matrix[3][0];
		
		inverse[1][2]=matrix[0][0]*matrix[1][3]*matrix[3][2]+matrix[0][2]*matrix[1][0]*matrix[3][3]+matrix[0][3]*matrix[1][2]*matrix[3][0]
				-matrix[0][0]*matrix[1][2]*matrix[3][3]-matrix[0][2]*matrix[1][3]*matrix[3][0]-matrix[0][3]*matrix[1][0]*matrix[3][2];
		
		inverse[1][3]=matrix[0][0]*matrix[1][2]*matrix[2][3]+matrix[0][2]*matrix[1][3]*matrix[2][0]+matrix[0][3]*matrix[1][0]*matrix[2][2]
				-matrix[0][0]*matrix[1][3]*matrix[2][2]-matrix[0][2]*matrix[1][0]*matrix[2][3]-matrix[0][3]*matrix[1][2]*matrix[2][0];
		
		inverse[2][0]=matrix[1][0]*matrix[2][1]*matrix[3][3]+matrix[1][1]*matrix[2][3]*matrix[3][0]+matrix[1][3]*matrix[2][0]*matrix[3][1]
				-matrix[1][0]*matrix[2][3]*matrix[3][1]-matrix[1][1]*matrix[2][0]*matrix[3][3]-matrix[1][3]*matrix[2][1]*matrix[3][0];
		
		inverse[2][1]=matrix[0][0]*matrix[2][3]*matrix[3][1]+matrix[0][1]*matrix[2][0]*matrix[3][3]+matrix[0][3]*matrix[2][1]*matrix[3][0]
				-matrix[0][0]*matrix[2][1]*matrix[3][3]-matrix[0][1]*matrix[2][3]*matrix[3][0]-matrix[0][3]*matrix[2][0]*matrix[3][1];
		
		inverse[2][2]=matrix[0][0]*matrix[1][1]*matrix[3][3]+matrix[0][1]*matrix[1][3]*matrix[3][0]+matrix[0][3]*matrix[1][0]*matrix[3][1]
				-matrix[0][0]*matrix[1][3]*matrix[3][1]-matrix[0][1]*matrix[1][0]*matrix[3][3]-matrix[0][3]*matrix[1][1]*matrix[3][0];
		
		inverse[2][3]=matrix[0][0]*matrix[1][3]*matrix[2][1]+matrix[0][1]*matrix[1][0]*matrix[2][3]+matrix[0][3]*matrix[1][1]*matrix[2][0]
				-matrix[0][0]*matrix[1][1]*matrix[2][3]-matrix[0][1]*matrix[1][3]*matrix[2][0]-matrix[0][3]*matrix[1][0]*matrix[2][1];
		
		inverse[3][0]=matrix[1][0]*matrix[2][2]*matrix[3][1]+matrix[1][1]*matrix[2][0]*matrix[3][2]+matrix[1][2]*matrix[2][1]*matrix[3][0]
				-matrix[1][0]*matrix[2][1]*matrix[3][2]-matrix[1][1]*matrix[2][2]*matrix[3][0]-matrix[1][2]*matrix[2][0]*matrix[3][1];
		
		inverse[3][1]=matrix[0][0]*matrix[2][1]*matrix[3][2]+matrix[0][1]*matrix[2][2]*matrix[3][0]+matrix[0][2]*matrix[2][0]*matrix[3][1]
				-matrix[0][0]*matrix[2][2]*matrix[3][1]-matrix[0][1]*matrix[2][0]*matrix[3][2]-matrix[0][2]*matrix[2][1]*matrix[3][0];
		
		inverse[3][2]=matrix[0][0]*matrix[1][2]*matrix[3][1]+matrix[0][1]*matrix[1][0]*matrix[3][2]+matrix[0][2]*matrix[1][1]*matrix[3][0]
				-matrix[0][0]*matrix[1][1]*matrix[3][2]-matrix[0][1]*matrix[1][2]*matrix[3][0]-matrix[0][2]*matrix[1][0]*matrix[3][1];
		
		inverse[3][3]=matrix[0][0]*matrix[1][1]*matrix[2][2]+matrix[0][1]*matrix[1][2]*matrix[2][0]+matrix[0][2]*matrix[1][0]*matrix[2][1]
				-matrix[0][0]*matrix[1][2]*matrix[2][1]-matrix[0][1]*matrix[1][0]*matrix[2][2]-matrix[0][2]*matrix[1][1]*matrix[2][0];
		
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++)
				inverse[i][j]/=det;
		
		return new Transformation(inverse);
	}
	
	public Point3DH transformNormal(Point3DH normal){//l=l*M right-multiplied
		double[] vector=new double[4];
		double[] result=new double[4];
		vector[0]=normal.getX();
		vector[1]=normal.getY();
		vector[2]=normal.getZ();
		vector[3]=normal.getW();
		
		for(int i=0;i<4;i++){
			double sum=0;
			for(int j=0;j<4;j++)
				sum+=matrix[j][i]*vector[j];
			result[i]=sum;
		}
		return new Point3DH(result[0]/result[3],result[1]/result[3],result[2]/result[3]);
	}
	
	public String toString(){
		return "[ "+"["+matrix[0][0]+","+matrix[0][1]+","+matrix[0][2]+","+matrix[0][3]+"]"+"\n"
					+"["+matrix[1][0]+","+matrix[1][1]+","+matrix[1][2]+","+matrix[1][3]+"]"+"\n"
					+"["+matrix[2][0]+","+matrix[2][1]+","+matrix[2][2]+","+matrix[2][3]+"]"+"\n"
					+"["+matrix[3][0]+","+matrix[3][1]+","+matrix[3][2]+","+matrix[3][3]+"]"+" ]"+"\n";
	}
	
}
