package Shading;

import java.util.ArrayList;

import geometry.Vertex3D;
import polygon.Polygon;
import geometry.Point3DH;
import windowing.graphics.Color;

public class LightCalculation {
	private ArrayList<Lighting> lightSources;
	private Color IaLambda;
	private Color KdLambda;
	private double Ks;
	private double p;
	
	public LightCalculation(ArrayList<Lighting> lightSources, Color IaLambda, Color KdLambda,double Ks,double p){
		this.lightSources=lightSources;
		this.IaLambda=IaLambda;
		this.KdLambda=KdLambda;
		this.Ks=Ks;
		this.p=p;
	}
	
	private Point3DH calculateLVec(Lighting light,Vertex3D point){
		double x=light.getCameraLoc().getX()-point.getX();
		double y=light.getCameraLoc().getY()-point.getY();
		double z=light.getCameraLoc().getZ()-point.getZ();
		Point3DH L=new Point3DH(x,y,z);
		L=L.Normalize();
		return L;
	}
	
	private Point3DH calculateVVec(Vertex3D point){
		Point3DH V=new Point3DH(-point.getX(),-point.getY(),-point.getZ());
		V=V.Normalize();
		return V;
	}
	
	private Point3DH calculateRVec(Point3DH N,Point3DH L){
		double scalar=2*(N.dotProduct(L));
		Point3DH R=N.scale(scalar);
		R=R.subtract(L);
		R=R.Normalize();
		return R;
	}

	private double lightDistance(Lighting light,Vertex3D vertex){
		double deltax=light.getCameraLoc().getX()-vertex.getX();
		double deltay=light.getCameraLoc().getY()-vertex.getY();
		double deltaz=light.getCameraLoc().getZ()-vertex.getZ();
		return Math.sqrt(deltax*deltax+deltay*deltay+deltaz*deltaz);
	}
	public Color lightAtVertexForFlat(Polygon polygon, Vertex3D vertex){//vertex is already in camera space
		double red,green,blue,distance,fatt;
		Point3DH N,V,L,R;
		
		if(vertex.hasNormal()==false)
			N=polygon.getFaceNormal().Normalize();
		else{
			N=vertex.getNormal().Normalize();
		}

		V=calculateVVec(vertex);
		
		int lightnum=this.lightSources.size();
		Lighting light;
		red=IaLambda.getR()*KdLambda.getR();

		for(int i=0;i<lightnum;i++){
			light=lightSources.get(i);
			distance=lightDistance(light,vertex);
			
			fatt=1.0/(light.getattenA()+light.getattenB()*distance);
			L=calculateLVec(light,vertex);
			R=calculateRVec(N,L);
			if(N.dotProduct(L)>=0.0){
				red+=light.getIntensity().getR()*fatt*(this.KdLambda.getR()*(N.dotProduct(L)));
				if(V.dotProduct(R)>=0.0)
					red+=light.getIntensity().getR()*fatt*(this.Ks*Math.pow(V.dotProduct(R),this.p));
			}
		}

		green=IaLambda.getG()*KdLambda.getG();
		for(int i=0;i<lightnum;i++){
			light=lightSources.get(i);
			distance=lightDistance(light,vertex);
			fatt=1.0/(light.getattenA()+light.getattenB()*distance);
			L=calculateLVec(light,vertex);
			R=calculateRVec(N,L);
			if(N.dotProduct(L)>=0.0){
				green+=light.getIntensity().getG()*fatt*(this.KdLambda.getG()*(N.dotProduct(L)));
				if(V.dotProduct(R)>=0.0)
					green+=light.getIntensity().getG()*fatt*(this.Ks*Math.pow(V.dotProduct(R),this.p));
			}
		}

		blue=IaLambda.getB()*KdLambda.getB();
		for(int i=0;i<lightnum;i++){
			light=lightSources.get(i);
			distance=lightDistance(light,vertex);
			fatt=1.0/(light.getattenA()+light.getattenB()*distance);
			L=calculateLVec(light,vertex);
			R=calculateRVec(N,L);
			if(N.dotProduct(L)>=0.0){
				blue+=light.getIntensity().getB()*fatt*(this.KdLambda.getB()*(N.dotProduct(L)));
				if(V.dotProduct(R)>=0.0)
					blue+=light.getIntensity().getB()*fatt*(this.Ks*Math.pow(V.dotProduct(R),this.p));
			}
		}
//		System.out.println(new Color(red,green,blue));
		return new Color(red,green,blue);
	}
	
	
	public Color lightAtVertexForGou(Polygon polygon, Vertex3D vertex){//vertex is not in camera space
		double red,green,blue,distance,fatt;
		Point3DH N,V,L,R;
		
		if(vertex.hasNormal()==false)
			N=polygon.getFaceNormal().Normalize();
		else{
			N=vertex.getNormal().Normalize();
		}
		
		vertex=new Vertex3D(vertex.getCameraPoint().getX(),vertex.getCameraPoint().getY(),vertex.getCameraPoint().getZ(),vertex.getColor());
		
		V=calculateVVec(vertex);
		
		int lightnum=this.lightSources.size();
		Lighting light;
		
		red=IaLambda.getR()*KdLambda.getR();
		for(int i=0;i<lightnum;i++){
			light=lightSources.get(i);
			distance=lightDistance(light,vertex);
			fatt=1.0/(light.getattenA()+light.getattenB()*distance);
			L=calculateLVec(light,vertex);
			R=calculateRVec(N,L);
			if(N.dotProduct(L)>=0.0){
				red+=light.getIntensity().getR()*fatt*(this.KdLambda.getR()*(N.dotProduct(L)));
				if(V.dotProduct(R)>=0.0)
					red+=light.getIntensity().getR()*fatt*(this.Ks*Math.pow(V.dotProduct(R),this.p));
			}
		}
		
		green=IaLambda.getG()*KdLambda.getG();
		for(int i=0;i<lightnum;i++){
			light=lightSources.get(i);
			distance=lightDistance(light,vertex);
			fatt=1.0/(light.getattenA()+light.getattenB()*distance);
			L=calculateLVec(light,vertex);
			R=calculateRVec(N,L);
			if(N.dotProduct(L)>=0.0){
				green+=light.getIntensity().getG()*fatt*(this.KdLambda.getG()*(N.dotProduct(L)));
				if(V.dotProduct(R)>=0.0)
					green+=light.getIntensity().getG()*fatt*(this.Ks*Math.pow(V.dotProduct(R),this.p));
			}
		}
		
		blue=IaLambda.getB()*KdLambda.getB();
		for(int i=0;i<lightnum;i++){
			light=lightSources.get(i);
			distance=lightDistance(light,vertex);
			fatt=1.0/(light.getattenA()+light.getattenB()*distance);
			L=calculateLVec(light,vertex);
			R=calculateRVec(N,L);
			if(N.dotProduct(L)>=0.0){
				blue+=light.getIntensity().getB()*fatt*(this.KdLambda.getB()*(N.dotProduct(L)));
				if(V.dotProduct(R)>=0.0)
					blue+=light.getIntensity().getB()*fatt*(this.Ks*Math.pow(V.dotProduct(R),this.p));
			}
		}
		
		return new Color(red,green,blue);
	}
}
