package polygon;

import windowing.drawable.Drawable;
import windowing.graphics.Color;
import polygon.Chain;
import geometry.*;
import line.*;
import client.interpreter.*;
import client.interpreter.SimpInterpreter.ShadeStyle;
import Shading.*;

public class FilledPolygonRenderer implements PolygonRenderer{

	public static PolygonRenderer make(){
		return new FilledPolygonRenderer();
	}
	public void drawPolygon(Polygon polygon, Drawable drawable, ShadeStyle shadeStyle, LightCalculation lightCal) {
		FaceShader faceShader;
		VertexShader vertexShader;
		PixelShader pixelShader;
		
		if(shadeStyle==ShadeStyle.FLAT){
			faceShader=new FlatFaceShader();
			vertexShader=new NullVertexShader();
			pixelShader=new FlatPixelShader(lightCal);
		}
		else if(shadeStyle==ShadeStyle.GOURAUD)
		{
			faceShader= new NullFaceShader();
			vertexShader=new GouraudVertexShader();
			pixelShader=new ColorInterpolatingPixelShader(lightCal);
		}
		else{
			faceShader=new NullFaceShader();
			vertexShader=new GouraudVertexShader();
			pixelShader=new phongPixelShader(lightCal);
		}
		
		polygon=faceShader.shade(polygon);
		int length=polygon.length();
		for(int i=0;i<length;i++)
			vertexShader.shade(polygon, polygon.get(i));
		
		double x1=polygon.get(0).getX();
		double y1=polygon.get(0).getY();
		double z1=1/polygon.get(0).getZ();
		double x2=polygon.get(1).getX();
		double y2=polygon.get(1).getY();
		double z2=1/polygon.get(1).getZ();
		double x3=polygon.get(2).getX();
		double y3=polygon.get(2).getY();
		double z3=1/polygon.get(2).getZ();
		double A=(y2 - y1)*(z3 - z1) - (z2 -z1)*(y3 - y1);
		double B = (x3 - x1)*(z2 - z1) - (x2 - x1)*(z3 - z1);
		double C = (x2 - x1)*(y3 - y1) - (x3 - x1)*(y2 - y1);
		
//		Color c=vertexShader.shade(Color.random());
		
		Chain left=polygon.leftChain();
		Chain right=polygon.rightChain();
				
		int leftLength=left.length();
		int rightLength=right.length();
		
		double mLeft,mRight,xLeft,xRight,x,mRLeft,mGLeft,mBLeft,rLeft,rRight,gLeft,gRight,bLeft,bRight;
		double mRRight,mGRight,mBRight;
		double horimR,horimG,horimB,pointR,pointG,pointB;
		Vertex3D Top=left.get(0);
		Vertex3D Bottom=left.get(leftLength-1);
		int yTop=Top.getIntY();	
		int yBottom=Bottom.getIntY();
		int y;
		double z;
		
		if(leftLength==1||rightLength==1)
			return;

		if(leftLength>2&&left.get(1).getIntY()==left.get(0).getIntY()){
			mRLeft=(left.get(2).getColor().getR()-left.get(1).getColor().getR())/(left.get(2).getY()-left.get(1).getY());
			mGLeft=(left.get(2).getColor().getG()-left.get(1).getColor().getG())/(left.get(2).getY()-left.get(1).getY());
			mBLeft=(left.get(2).getColor().getB()-left.get(1).getColor().getB())/(left.get(2).getY()-left.get(1).getY());
			mRRight=(right.get(1).getColor().getR()-right.get(0).getColor().getR())/(right.get(1).getY()-right.get(0).getY());
			mGRight=(right.get(1).getColor().getG()-right.get(0).getColor().getG())/(right.get(1).getY()-right.get(0).getY());
			mBRight=(right.get(1).getColor().getB()-right.get(0).getColor().getB())/(right.get(1).getY()-right.get(0).getY());
			rLeft=left.get(1).getColor().getR();
			rRight=right.get(0).getColor().getR();
			gLeft=left.get(1).getColor().getG();
			gRight=right.get(0).getColor().getG();
			bLeft=left.get(1).getColor().getB();
			bRight=right.get(0).getColor().getB();
			
			mLeft=(left.get(2).getX()-left.get(1).getX())/(left.get(2).getY()-left.get(1).getY());
			mRight=(right.get(1).getX()-right.get(0).getX())/(right.get(1).getY()-right.get(0).getY());			
			xLeft=left.get(1).getX();
			xRight=right.get(0).getX();
			for(y=yTop;y>yBottom;y--){
				if(xRight!=xLeft){
					horimR=(rRight-rLeft)/(xRight-xLeft);
					horimG=(gRight-gLeft)/(xRight-xLeft);
					horimB=(bRight-bLeft)/(xRight-xLeft);
				}
				else{
					horimR=horimG=horimB=0;
				}
				
				pointR=rLeft;
				pointG=gLeft;
				pointB=bLeft;
				for(x=Math.round(xLeft+1);Math.round(x)<Math.round(xRight+1);x+=1.0,pointR+=horimR,pointG+=horimG,pointB+=horimB){
					Color argbColor=new Color(pointR,pointG,pointB);
					z=(A*(x-x1)+B*(y-y1)-C*z1)/(-C);
//					argbColor=pixelShader.shade(polygon, new Vertex3D(x,y*1.0,1/z,argbColor));//////////////
					argbColor=pixelShader.shade(polygon, new Vertex3D(x,y,1.0/z,argbColor));
					drawable.setPixel((int)Math.round(x), y, 1.0/z, argbColor.asARGB());
				}
				xLeft-=mLeft;
				xRight-=mRight;
				rLeft-=mRLeft;
				rRight-=mRRight;
				gLeft-=mGLeft;
				gRight-=mGRight;
				bLeft-=mBLeft;
				bRight-=mBRight;
			}		
			return;
		}
		
		if(rightLength>2&&right.get(1).getIntY()==right.get(0).getIntY()){
			mRLeft=(left.get(1).getColor().getR()-left.get(0).getColor().getR())/(left.get(1).getY()-left.get(0).getY());
			mGLeft=(left.get(1).getColor().getG()-left.get(0).getColor().getG())/(left.get(1).getY()-left.get(0).getY());
			mBLeft=(left.get(1).getColor().getB()-left.get(0).getColor().getB())/(left.get(1).getY()-left.get(0).getY());
			mRRight=(right.get(2).getColor().getR()-right.get(1).getColor().getR())/(right.get(2).getY()-right.get(1).getY());
			mGRight=(right.get(2).getColor().getG()-right.get(1).getColor().getG())/(right.get(2).getY()-right.get(1).getY());
			mBRight=(right.get(2).getColor().getB()-right.get(1).getColor().getB())/(right.get(2).getY()-right.get(1).getY());
			
			rLeft=left.get(0).getColor().getR();
			rRight=right.get(1).getColor().getR();
			gLeft=left.get(0).getColor().getG();
			gRight=right.get(1).getColor().getG();
			bLeft=left.get(0).getColor().getB();
			bRight=right.get(1).getColor().getB();
			
			mRight=(right.get(2).getX()-right.get(1).getX())/(right.get(2).getY()-right.get(1).getY());
			mLeft=(left.get(1).getX()-left.get(0).getX())/(left.get(1).getY()-left.get(0).getY());
			
			xLeft=left.get(0).getX();
			xRight=right.get(1).getX();
			for(y=yTop;y>yBottom;y--){
				if(xRight!=xLeft){
					horimR=(rRight-rLeft)/(xRight-xLeft);
					horimG=(gRight-gLeft)/(xRight-xLeft);
					horimB=(bRight-bLeft)/(xRight-xLeft);
				}
				else{
					horimR=horimG=horimB=0;
				}
				pointR=rLeft;
				pointG=gLeft;
				pointB=bLeft;
				
				for(x=Math.round(xLeft+1);Math.round(x)<Math.round(xRight+1);x+=1.0,pointR+=horimR,pointG+=horimG,pointB+=horimB){
					Color argbColor=new Color(pointR,pointG,pointB);
					z=(A*(x-x1)+B*(y-y1)-C*z1)/(-C);
//					argbColor=pixelShader.shade(polygon, new Vertex3D(x,y*1.0,1/z,argbColor));//////////////
					argbColor=pixelShader.shade(polygon, new Vertex3D(x,y,1.0/z,argbColor));
					drawable.setPixel((int)Math.round(x), y, 1.0/z, argbColor.asARGB());
				}
				xLeft-=mLeft;
				xRight-=mRight;
				rLeft-=mRLeft;
				rRight-=mRRight;
				gLeft-=mGLeft;
				gRight-=mGRight;
				bLeft-=mBLeft;
				bRight-=mBRight;
			}		
			return;
		}
		//normal case 
		mLeft=(left.get(1).getX()-left.get(0).getX())/(left.get(1).getY()-left.get(0).getY());
		mRight=(right.get(1).getX()-right.get(0).getX())/(right.get(1).getY()-right.get(0).getY());		
		mRLeft=(left.get(1).getColor().getR()-left.get(0).getColor().getR())/(left.get(1).getY()-left.get(0).getY());
		mGLeft=(left.get(1).getColor().getG()-left.get(0).getColor().getG())/(left.get(1).getY()-left.get(0).getY());
		mBLeft=(left.get(1).getColor().getB()-left.get(0).getColor().getB())/(left.get(1).getY()-left.get(0).getY());
		mRRight=(right.get(1).getColor().getR()-right.get(0).getColor().getR())/(right.get(1).getY()-right.get(0).getY());
		mGRight=(right.get(1).getColor().getG()-right.get(0).getColor().getG())/(right.get(1).getY()-right.get(0).getY());
		mBRight=(right.get(1).getColor().getB()-right.get(0).getColor().getB())/(right.get(1).getY()-right.get(0).getY());
		rLeft=left.get(0).getColor().getR();
		rRight=right.get(0).getColor().getR();
		gLeft=left.get(0).getColor().getG();
		gRight=right.get(0).getColor().getG();
		bLeft=left.get(0).getColor().getB();
		bRight=right.get(0).getColor().getB();
		int yturn;
		if(leftLength>rightLength){
//			System.out.println("yes");
			yturn=left.get(1).getIntY();
			xRight=xLeft=left.get(0).getX();
			for(y=yTop;y>yturn;y--){
				horimR=(rRight-rLeft)/(xRight-xLeft);
				horimG=(gRight-gLeft)/(xRight-xLeft);
				horimB=(bRight-bLeft)/(xRight-xLeft);
				pointR=rLeft;
				pointG=gLeft;
				pointB=bLeft;
//				System.out.println(xLeft);
//				System.out.println(xRight);
				for(x=Math.round(xLeft+1);Math.round(x)<Math.round(xRight+1);x+=1.0,pointR+=horimR,pointG+=horimG,pointB+=horimB){
					Color argbColor=new Color(pointR,pointG,pointB);
					z=(A*(x-x1)+B*(y-y1)-C*z1)/(-C);
//					argbColor=pixelShader.shade(polygon, new Vertex3D(x,y*1.0,1/z,argbColor));//////////////
					argbColor=pixelShader.shade(polygon, new Vertex3D(x,y,1.0/z,argbColor));
//					System.out.println(argbColor);
					drawable.setPixel((int)Math.round(x), y, 1.0/z, argbColor.asARGB());
				}
				xLeft-=mLeft;
				xRight-=mRight;
				rLeft-=mRLeft;
				rRight-=mRRight;
				gLeft-=mGLeft;
				gRight-=mGRight;
				bLeft-=mBLeft;
				bRight-=mBRight;	
			}
			mLeft=(left.get(2).getX()-left.get(1).getX())/(left.get(2).getY()-left.get(1).getY());
			xLeft=left.get(1).getX();
			mRLeft=(left.get(2).getColor().getR()-left.get(1).getColor().getR())/(left.get(2).getY()-left.get(1).getY());
			mGLeft=(left.get(2).getColor().getG()-left.get(1).getColor().getG())/(left.get(2).getY()-left.get(1).getY());
			mBLeft=(left.get(2).getColor().getB()-left.get(1).getColor().getB())/(left.get(2).getY()-left.get(1).getY());
			rLeft=left.get(1).getColor().getR();
			gLeft=left.get(1).getColor().getG();
			bLeft=left.get(1).getColor().getB();
			for(y=yturn;y>yBottom;y--){
				horimR=(rRight-rLeft)/(xRight-xLeft);
				horimG=(gRight-gLeft)/(xRight-xLeft);
				horimB=(bRight-bLeft)/(xRight-xLeft);
				pointR=rLeft;
				pointG=gLeft;
				pointB=bLeft;
				
				for(x=Math.round(xLeft+1);Math.round(x)<Math.round(xRight+1);x+=1.0,pointR+=horimR,pointG+=horimG,pointB+=horimB){
					Color argbColor=new Color(pointR,pointG,pointB);
					z=(A*(x-x1)+B*(y-y1)-C*z1)/(-C);
//					argbColor=pixelShader.shade(polygon, new Vertex3D(x,y*1.0,1/z,argbColor));//////////////
					argbColor=pixelShader.shade(polygon, new Vertex3D(x,y,1.0/z,argbColor));
					drawable.setPixel((int)Math.round(x), y, 1.0/z, argbColor.asARGB());
				}
				xLeft-=mLeft;
				xRight-=mRight;
				rLeft-=mRLeft;
				rRight-=mRRight;
				gLeft-=mGLeft;
				gRight-=mGRight;
				bLeft-=mBLeft;
				bRight-=mBRight;
			}
			return;
		}
		
		if(leftLength<rightLength){
//			System.out.println(polygon);
			yturn=right.get(1).getIntY();
			xRight=xLeft=right.get(0).getX();
//			System.out.println(yTop);
//			System.out.println(yturn);
			for(y=yTop;y>yturn;y--){
				horimR=(rRight-rLeft)/(xRight-xLeft);
				horimG=(gRight-gLeft)/(xRight-xLeft);
				horimB=(bRight-bLeft)/(xRight-xLeft);
				pointR=rLeft;
				pointG=gLeft;
				pointB=bLeft;
//				System.out.println(xLeft);
//				System.out.println(xRight);
				for(x=Math.round(xLeft+1);Math.round(x)<Math.round(xRight+1);x+=1.0,pointR+=horimR,pointG+=horimG,pointB+=horimB){
					Color argbColor=new Color(pointR,pointG,pointB);
					z=(A*(x-x1)+B*(y-y1)-C*z1)/(-C);
//					argbColor=pixelShader.shade(polygon, new Vertex3D(x,y*1.0,1/z,argbColor));//////////////
					argbColor=pixelShader.shade(polygon, new Vertex3D(x,y,1.0/z,argbColor));
//					System.out.println(argbColor);
					drawable.setPixel((int)Math.round(x), y, 1.0/z, argbColor.asARGB());
				}
				xLeft-=mLeft;
				xRight-=mRight;
				rLeft-=mRLeft;
				rRight-=mRRight;
				gLeft-=mGLeft;
				gRight-=mGRight;
				bLeft-=mBLeft;
				bRight-=mBRight;
			}
			mRight=(right.get(2).getX()-right.get(1).getX())/(right.get(2).getY()-right.get(1).getY());
			xRight=right.get(1).getX();
			mRRight=(right.get(2).getColor().getR()-right.get(1).getColor().getR())/(right.get(2).getY()-right.get(1).getY());
			mGRight=(right.get(2).getColor().getG()-right.get(1).getColor().getG())/(right.get(2).getY()-right.get(1).getY());
			mBRight=(right.get(2).getColor().getB()-right.get(1).getColor().getB())/(right.get(2).getY()-right.get(1).getY());
			for(y=yturn;y>yBottom;y--){
				horimR=(rRight-rLeft)/(xRight-xLeft);
				horimG=(gRight-gLeft)/(xRight-xLeft);
				horimB=(bRight-bLeft)/(xRight-xLeft);
				pointR=rLeft;
				pointG=gLeft;
				pointB=bLeft;
				for(x=Math.round(xLeft+1);Math.round(x)<Math.round(xRight+1);x+=1.0,pointR+=horimR,pointG+=horimG,pointB+=horimB){
					Color argbColor=new Color(pointR,pointG,pointB);
					z=(A*(x-x1)+B*(y-y1)-C*z1)/(-C);
//					argbColor=pixelShader.shade(polygon, new Vertex3D(x,y*1.0,1/z,argbColor));//////////////
					argbColor=pixelShader.shade(polygon, new Vertex3D(x,y,1.0/z,argbColor));
					drawable.setPixel((int)Math.round(x), y, 1.0/z, argbColor.asARGB());
				}
				xLeft-=mLeft;
				xRight-=mRight;
				rLeft-=mRLeft;
				rRight-=mRRight;
				gLeft-=mGLeft;
				gRight-=mGRight;
				bLeft-=mBLeft;
				bRight-=mBRight;
			}
			return;
		}			
	}
}

	
	
	
	