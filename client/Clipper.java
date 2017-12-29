package client;

import geometry.Point3DH;
import geometry.Vertex3D;
import polygon.Polygon;
import windowing.graphics.Color;
import windowing.drawable.*;
import polygon.WireframePolygonRenderer;
import polygon.FilledPolygonRenderer;
import line.DDALineRenderer;

public class Clipper {
	
	//find the intersect point of plane z=zplane and line between vertex1 and vertex2
	public Vertex3D IntersectZPlane(double zplane,Vertex3D vertex1, Vertex3D vertex2){
		double[] vl=new double[3];//vector of line

		///////
//		double vlnx,vlny,vlnz;
//		vlnx=vertex1.getNormal().getX()-vertex2.getNormal().getX();
//		vlny=vertex1.getNormal().getY()-vertex2.getNormal().getY();
//		vlnz=vertex1.getNormal().getZ()-vertex2.getNormal().getZ();
		///////
		vl[0]=vertex1.getX()-vertex2.getX();
		vl[1]=vertex1.getY()-vertex2.getY();
		vl[2]=vertex1.getZ()-vertex2.getZ();
		
		double[] m=new double[3];//point m is on line
		m[0]=vertex1.getX();
		m[1]=vertex1.getY();
		m[2]=vertex1.getZ();
		
		double[] n=new double[3];//point n is on plane Z=zplane
		n[0]=0;
		n[1]=0;
		n[2]=zplane;
		
		double vp[]=new double[3];//plane normal vector
		vp[0]=0;
		vp[1]=0;
		vp[2]=1;
		
		double intersectPoint[]=new double[3];//x,y,z of intersection point of plane and line
		
		double t = ((n[0]-m[0])*vp[0]+(n[1]-m[1])*vp[1]+(n[2]-m[2])*vp[2])/(vp[0]*vl[0]+ vp[1]*vl[1]+ vp[2]*vl[2]);
		
		intersectPoint[0]=m[0]+vl[0]*t;
		intersectPoint[1]=m[1]+vl[1]*t;
		intersectPoint[2]=m[2]+vl[2]*t;
		//////
//		double nx,ny,nz;
//		nx=vertex1.getNormal().getX()+vlnx*t;
//		ny=vertex1.getNormal().getY()+vlny*t;
//		nz=vertex1.getNormal().getZ()+vlnz*t;
//		Point3DH normal=new Point3DH(nx,ny,nz);
		Vertex3D intersect=new Vertex3D(intersectPoint[0],intersectPoint[1],intersectPoint[2],vertex1.getColor());
//		intersect.addNormal(normal);
		///////
		return intersect;
	}
	//find the intersect point of plane x=xplane and line between vertex1 and vertex2
	public Vertex3D IntersectXPlane(double xplane,Vertex3D vertex1, Vertex3D vertex2){
		double[] vl=new double[3];//vector of line
		///////
//		double vlnx,vlny,vlnz;
//		vlnx=vertex1.getNormal().getX()-vertex2.getNormal().getX();
//		vlny=vertex1.getNormal().getY()-vertex2.getNormal().getY();
//		vlnz=vertex1.getNormal().getZ()-vertex2.getNormal().getZ();
		///////
		vl[0]=vertex1.getX()-vertex2.getX();
		vl[1]=vertex1.getY()-vertex2.getY();
		vl[2]=1/vertex1.getZ()-1/vertex2.getZ();
		
		double[] m=new double[3];//point m is on line
		m[0]=vertex1.getX();
		m[1]=vertex1.getY();
		m[2]=1/vertex1.getZ();
		
		double[] n=new double[3];//point n is on plane x=xplane
		n[0]=xplane;
		n[1]=0;
		n[2]=0;
		
		double vp[]=new double[3];//plane normal vector
		vp[0]=1;
		vp[1]=0;
		vp[2]=0;
		
		double intersectPoint[]=new double[3];//intersection point of plane and line
		
		double t = ((n[0]-m[0])*vp[0]+(n[1]-m[1])*vp[1]+(n[2]-m[2])*vp[2])/(vp[0]*vl[0]+ vp[1]*vl[1]+ vp[2]*vl[2]);
		
		intersectPoint[0]=m[0]+vl[0]*t;
		intersectPoint[1]=m[1]+vl[1]*t;
		intersectPoint[2]=1/(m[2]+vl[2]*t);
		//////
//		double nx,ny,nz;
//		nx=vertex1.getNormal().getX()+vlnx*t;
//		ny=vertex1.getNormal().getY()+vlny*t;
//		nz=vertex1.getNormal().getZ()+vlnz*t;
//		Point3DH normal=new Point3DH(nx,ny,nz);
		Vertex3D intersect=new Vertex3D(intersectPoint[0],intersectPoint[1],intersectPoint[2],vertex1.getColor());
//		intersect.addNormal(normal);
		///////
		return intersect;
	}
	//find the intersect point of plane y=yplane and line between vertex1 and vertex2
	public Vertex3D IntersectYPlane(double yplane,Vertex3D vertex1, Vertex3D vertex2){
		///////
//		double vlnx,vlny,vlnz;
//		vlnx=vertex1.getNormal().getX()-vertex2.getNormal().getX();
//		vlny=vertex1.getNormal().getY()-vertex2.getNormal().getY();
//		vlnz=vertex1.getNormal().getZ()-vertex2.getNormal().getZ();
		///////
		double[] vl=new double[3];//vector of line
		vl[0]=vertex1.getX()-vertex2.getX();
		vl[1]=vertex1.getY()-vertex2.getY();
		vl[2]=1/vertex1.getZ()-1/vertex2.getZ();
		
		double[] m=new double[3];//point m is on line
		m[0]=vertex1.getX();
		m[1]=vertex1.getY();
		m[2]=1/vertex1.getZ();
		
		double[] n=new double[3];//point n is on yplane 
		n[0]=0;
		n[1]=yplane;
		n[2]=0;
		
		double vp[]=new double[3];//plane normal vector
		vp[0]=0;
		vp[1]=1;
		vp[2]=0;
		
		double intersectPoint[]=new double[3];//intersection point of plane and line
		
		double t = ((n[0]-m[0])*vp[0]+(n[1]-m[1])*vp[1]+(n[2]-m[2])*vp[2])/(vp[0]*vl[0]+ vp[1]*vl[1]+ vp[2]*vl[2]);
		
		intersectPoint[0]=m[0]+vl[0]*t;
		intersectPoint[1]=m[1]+vl[1]*t;
		intersectPoint[2]=1/(m[2]+vl[2]*t);
		
		//////
//		double nx,ny,nz;
//		nx=vertex1.getNormal().getX()+vlnx*t;
//		ny=vertex1.getNormal().getY()+vlny*t;
//		nz=vertex1.getNormal().getZ()+vlnz*t;
//		Point3DH normal=new Point3DH(nx,ny,nz);
		Vertex3D intersect=new Vertex3D(intersectPoint[0],intersectPoint[1],intersectPoint[2],vertex1.getColor());
//		intersect.addNormal(normal);
		///////
		return intersect;
	}

	//Clip polygon with plane z<zplane
	public Polygon ClipSmallerZPlane(double zplane,Polygon polygon){
		int length=polygon.length();
		Vertex3D[] vertices=new Vertex3D[length];
		for(int i=0;i<length;i++)
			vertices[i]=polygon.get(i);
		Polygon ClippedPolygon=Polygon.makeEmpty();
		
		int k=-1;
		for(int i=0;i<length;i++){
			if(vertices[i%length].getZ()>zplane&&vertices[(i+1)%length].getZ()<=zplane){//i is outside and i+1 is inside
				k=i;
			}
		}
		
		if(k==-1&&vertices[0].getZ()>zplane){//entirely outside
			return null;
		}
		else if(k==-1&&vertices[0].getZ()<=zplane){//entirely inside
//			System.out.println("yes");
			return polygon;
		}
		else{
			Vertex3D intersect1=IntersectZPlane(zplane,vertices[k%length],vertices[(k+1)%length]);
			ClippedPolygon.add(intersect1);
			for(int i=1;i<=length;i++){
				if(vertices[(k+i)%length].getZ()<=zplane){//vertices[i] is inside
					ClippedPolygon.add(vertices[(k+i)%length]);
				}
				else{//vertices[i] is outside
					Vertex3D intersect2=IntersectZPlane(zplane,vertices[(k+i-1)%length],vertices[(k+i)%length]);
					ClippedPolygon.add(intersect2);
					return ClippedPolygon;
				}
			}
			return ClippedPolygon;
		}
	}

	//Clip polygon with plane z>zplane
	public Polygon ClipBiggerZPlane(double zplane,Polygon polygon){
		int length=polygon.length();
		Vertex3D[] vertices=new Vertex3D[length];
		for(int i=0;i<length;i++)
			vertices[i]=polygon.get(i);
		Polygon ClippedPolygon=Polygon.makeEmpty();
		
		int k=-1;
		for(int i=0;i<length;i++){
			if(vertices[i%length].getZ()<zplane&&vertices[(i+1)%length].getZ()>=zplane){//i is outside and i+1 is inside
				k=i;
			}
		}
		if(k==-1&&vertices[0].getZ()<zplane){//entirely outside
			return null;
		}
		else if(k==-1&&vertices[0].getZ()>=zplane){//entirely inside
			return polygon;
		}
		else{
			Vertex3D intersect1=IntersectZPlane(zplane,vertices[k%length],vertices[(k+1)%length]);
			ClippedPolygon.add(intersect1);
			for(int i=1;i<=length;i++){
				if(vertices[(k+i)%length].getZ()>=zplane){//vertices[i] is inside
					ClippedPolygon.add(vertices[(k+i)%length]);
				}
				else{//vertices[i] is outside
					Vertex3D intersect2=IntersectZPlane(zplane,vertices[(k+i-1)%length],vertices[(k+i)%length]);
					ClippedPolygon.add(intersect2);
					return ClippedPolygon;
				}
			}
			return ClippedPolygon;
		}
	}

	//Clip polygon with plane x>xplane
	public Polygon ClipBiggerXPlane(double xplane,Polygon polygon){
		int length=polygon.length();
		Vertex3D[] vertices=new Vertex3D[length];
		for(int i=0;i<length;i++)
			vertices[i]=polygon.get(i);
		Polygon ClippedPolygon=Polygon.makeEmpty();
		
		int k=-1;
		for(int i=0;i<length;i++){
			if(vertices[i%length].getX()<xplane&&vertices[(i+1)%length].getX()>=xplane){//i is outside and i+1 is inside
				k=i;
			}
		}
				
		if(k==-1&&vertices[0].getX()<xplane){//entirely outside
			return null;
		}
		else if(k==-1&&vertices[0].getX()>=xplane){//entirely inside
			return polygon;
		}
		else{
			Vertex3D intersect1=IntersectXPlane(xplane,vertices[k%length],vertices[(k+1)%length]);
			ClippedPolygon.add(intersect1);
			for(int i=1;i<=length;i++){
				if(vertices[(k+i)%length].getX()>=xplane){//vertices[i] is inside
					ClippedPolygon.add(vertices[(k+i)%length]);
				}
				else{//vertices[i] is outside
					Vertex3D intersect2=IntersectXPlane(xplane,vertices[(k+i-1)%length],vertices[(k+i)%length]);
					ClippedPolygon.add(intersect2);
					return ClippedPolygon;
				}
			}
			return ClippedPolygon;
		}
	}
	
	//Clip polygon with plane y>yplane
	public Polygon ClipBiggerYPlane(double yplane,Polygon polygon){
		int length=polygon.length();
		Vertex3D[] vertices=new Vertex3D[length];
		for(int i=0;i<length;i++)
			vertices[i]=polygon.get(i);
		Polygon ClippedPolygon=Polygon.makeEmpty();
		
		int k=-1;
		for(int i=0;i<length;i++){
			if(vertices[i%length].getY()<yplane&&vertices[(i+1)%length].getY()>=yplane){//i is outside and i+1 is inside
				k=i;
			}
		}
				
		if(k==-1&&vertices[0].getY()<yplane){//entirely outside
			return null;
		}
		else if(k==-1&&vertices[0].getY()>=yplane){//entirely inside
			return polygon;
		}
		else{
			Vertex3D intersect1=IntersectYPlane(yplane,vertices[k%length],vertices[(k+1)%length]);
			ClippedPolygon.add(intersect1);
			
			for(int i=1;i<=length;i++){
				if(vertices[(k+i)%length].getY()>=yplane){//vertices[i] is inside
					ClippedPolygon.add(vertices[(k+i)%length]);
				}
				else{//vertices[i] is outside
					Vertex3D intersect2=IntersectYPlane(yplane,vertices[(k+i-1)%length],vertices[(k+i)%length]);
					ClippedPolygon.add(intersect2);
					return ClippedPolygon;
				}
			}
			return ClippedPolygon;
		}
	}
	
	//Clip polygon with plane x<xplane
	public Polygon ClipSmallerXPlane(double xplane,Polygon polygon){
		int length=polygon.length();
		Vertex3D[] vertices=new Vertex3D[length];
		for(int i=0;i<length;i++)
			vertices[i]=polygon.get(i);
		Polygon ClippedPolygon=Polygon.makeEmpty();
		
		int k=-1;
		for(int i=0;i<length;i++){
			if(vertices[i%length].getX()>xplane&&vertices[(i+1)%length].getX()<=xplane){//i is outside and i+1 is inside
				k=i;
			}
		}
				
		if(k==-1&&vertices[0].getX()>xplane){//entirely outside
			return null;
		}
		else if(k==-1&&vertices[0].getX()<=xplane){//entirely inside
			return polygon;
		}
		else{
			Vertex3D intersect1=IntersectXPlane(xplane,vertices[k%length],vertices[(k+1)%length]);
			ClippedPolygon.add(intersect1);
			
			for(int i=1;i<=length;i++){
				if(vertices[(k+i)%length].getX()<=xplane){//vertices[i] is inside
					ClippedPolygon.add(vertices[(k+i)%length]);
				}
				else{//vertices[i] is outside
					Vertex3D intersect2=IntersectXPlane(xplane,vertices[(k+i-1)%length],vertices[(k+i)%length]);
					ClippedPolygon.add(intersect2);
					return ClippedPolygon;
				}
			}
			return ClippedPolygon;
		}
	}

	//Clip polygon with plane y<yplane
	public Polygon ClipSmallerYPlane(double yplane,Polygon polygon){
		int length=polygon.length();
		Vertex3D[] vertices=new Vertex3D[length];
		for(int i=0;i<length;i++)
			vertices[i]=polygon.get(i);
		Polygon ClippedPolygon=Polygon.makeEmpty();
		
		int k=-1;
		for(int i=0;i<length;i++){
			if(vertices[i%length].getY()>yplane&&vertices[(i+1)%length].getY()<=yplane){//i is outside and i+1 is inside
				k=i;
			}
		}
				
		if(k==-1&&vertices[0].getY()>yplane){//entirely outside
			return null;
		}
		else if(k==-1&&vertices[0].getY()<=yplane){//entirely inside
			return polygon;
		}
		else{
//			System.out.println(vertices[k%length]);
//			System.out.println(vertices[(k+1)%length]);
			
			Vertex3D intersect1=IntersectYPlane(yplane,vertices[k%length],vertices[(k+1)%length]);
			ClippedPolygon.add(intersect1);
			
//			System.out.println("clipp small y intersect1");
//			System.out.println(intersect1);
			
			for(int i=1;i<=length;i++){
				if(vertices[(k+i)%length].getY()<=yplane){//vertices[i] is inside
					ClippedPolygon.add(vertices[(k+i)%length]);
				}
				else{//vertices[i] is outside
					Vertex3D intersect2=IntersectYPlane(yplane,vertices[(k+i-1)%length],vertices[(k+i)%length]);
					
//					System.out.println("clipp small y intersect2");
//					System.out.println(intersect2);
					
					ClippedPolygon.add(intersect2);
					return ClippedPolygon;
				}
			}
			return ClippedPolygon;
		}
	}
	
	//Clip polygon with z=hither and z=yon
	public Polygon ClipZPlane(Polygon polygon, double hither, double yon){
		Vertex3D[] vertices=new Vertex3D[3];
		vertices[0]=polygon.get(0);
		vertices[1]=polygon.get(1);
		vertices[2]=polygon.get(2);
		
		Polygon ClippedPolygon=Polygon.makeEmpty();
		
//		System.out.println(polygon.toString());
		
		ClippedPolygon=ClipSmallerZPlane(hither,polygon);
		if(ClippedPolygon==null)
			return null;
//		System.out.println("clip smaller z plane");
//		System.out.println(ClippedPolygon.toString());

		ClippedPolygon=ClipBiggerZPlane(yon,ClippedPolygon);
		if(ClippedPolygon==null)
			return null;
		return ClippedPolygon;
	}
	
	public Polygon ClipXYPlane(Polygon polygon,double xlow,double xhigh,double ylow,double yhigh){
		Vertex3D[] vertices=new Vertex3D[3];
		vertices[0]=polygon.get(0);
		vertices[1]=polygon.get(1);
		vertices[2]=polygon.get(2);
		
		Polygon ClippedPolygon=Polygon.makeEmpty();
		
//		System.out.println(polygon.toString());
		
//		System.out.println(ClippedPolygon.toString());
		ClippedPolygon=ClipSmallerYPlane(yhigh,polygon);
		if(ClippedPolygon==null)
			return null;
		
//		System.out.println(ClippedPolygon.toString());		
		ClippedPolygon=ClipBiggerYPlane(ylow,ClippedPolygon);
		if(ClippedPolygon==null)
			return null;
//		System.out.println(ClippedPolygon.toString());
		ClippedPolygon=ClipBiggerXPlane(xlow,ClippedPolygon);
		if(ClippedPolygon==null)
			return null;
//		System.out.println(ClippedPolygon.toString());
		ClippedPolygon=ClipSmallerXPlane(xhigh,ClippedPolygon);
		if(ClippedPolygon==null)
			return null;
//		System.out.println(ClippedPolygon.toString());
		return ClippedPolygon;
	}
	
}
