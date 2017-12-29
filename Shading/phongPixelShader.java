package Shading;
import Shading.LightCalculation;
import geometry.Vertex3D;
import geometry.Point3DH;
import polygon.Polygon;
import windowing.graphics.Color;

public class phongPixelShader implements PixelShader {
	LightCalculation lightCal;
	public phongPixelShader(LightCalculation lightCal){
		this.lightCal=lightCal;
	}
	@Override
	public Color shade(Polygon polygon, Vertex3D current) {
		Point3DH normal0=polygon.get(0).getNormal().Normalize();
		double normal1x=normal0.getX();
		double normal1y=normal0.getY();
		double normal1z=normal0.getZ();
		double camera1x=polygon.get(0).getCameraPoint().getX();
		double camera1y=polygon.get(0).getCameraPoint().getY();
		double camera1z=polygon.get(0).getCameraPoint().getZ();
		
		Point3DH normal1=polygon.get(1).getNormal().Normalize();
		double normal2x=normal1.getX();
		double normal2y=normal1.getY();
		double normal2z=normal1.getZ();
		double camera2x=polygon.get(1).getCameraPoint().getX();
		double camera2y=polygon.get(1).getCameraPoint().getY();
		double camera2z=polygon.get(1).getCameraPoint().getZ();
		
		Point3DH normal2=polygon.get(2).getNormal().Normalize();
		double normal3x=normal2.getX();
		double normal3y=normal2.getY();
		double normal3z=normal2.getZ();
		double camera3x=polygon.get(2).getCameraPoint().getX();
		double camera3y=polygon.get(2).getCameraPoint().getY();
		double camera3z=polygon.get(2).getCameraPoint().getZ();
		
//		Vertex3D cameraVertex=new Vertex3D(current.getX()*(-current.getZ()),current.getY()*(-current.getZ()),current.getZ(),current.getColor());
		double x=current.getX();
		double y=current.getY();
		
		double x1=polygon.get(0).getX();
		double y1=polygon.get(0).getY();
		
		double x2=polygon.get(1).getX();
		double y2=polygon.get(1).getY();
		
		double x3=polygon.get(2).getX();
		double y3=polygon.get(2).getY();
		
		double nxa=(y2 - y1)*(normal3x - normal1x) - (normal2x -normal1x)*(y3 - y1);
		double nxb = (x3 - x1)*(normal2x - normal1x) - (x2 - x1)*(normal3x - normal1x);
		double nxc = (x2 - x1)*(y3 - y1) - (x3 - x1)*(y2 - y1);
		double nx=(nxa*(x-x1)+nxb*(y-y1)-nxc*normal1x)/(-nxc);
		
		double nya=(y2 - y1)*(normal3y - normal1y) - (normal2y -normal1y)*(y3 - y1);
		double nyb = (x3 - x1)*(normal2y - normal1y) - (x2 - x1)*(normal3y - normal1y);
		double nyc = (x2 - x1)*(y3 - y1) - (x3 - x1)*(y2 - y1);
		double ny=(nya*(x-x1)+nyb*(y-y1)-nyc*normal1y)/(-nyc);
		
		double nza=(y2 - y1)*(normal3z - normal1z) - (normal2z -normal1z)*(y3 - y1);
		double nzb = (x3 - x1)*(normal2z - normal1z) - (x2 - x1)*(normal3z - normal1z);
		double nzc = (x2 - x1)*(y3 - y1) - (x3 - x1)*(y2 - y1);
		double nz=(nza*(x-x1)+nzb*(y-y1)-nzc*normal1z)/(-nzc);
		
		double cxa=(y2 - y1)*(camera3x - camera1x) - (camera2x -camera1x)*(y3 - y1);
		double cxb = (x3 - x1)*(camera2x - camera1x) - (x2 - x1)*(camera3x - camera1x);
		double cxc = (x2 - x1)*(y3 - y1) - (x3 - x1)*(y2 - y1);
		double cx=(cxa*(x-x1)+cxb*(y-y1)-cxc*camera1x)/(-cxc);
		
		double cya=(y2 - y1)*(camera3y - camera1y) - (camera2y -camera1y)*(y3 - y1);
		double cyb = (x3 - x1)*(camera2y - camera1y) - (x2 - x1)*(camera3y - camera1y);
		double cyc = (x2 - x1)*(y3 - y1) - (x3 - x1)*(y2 - y1);
		double cy=(cya*(x-x1)+cyb*(y-y1)-cyc*camera1y)/(-cyc);
		
		double cza=(y2 - y1)*(camera3z - camera1z) - (camera2z -camera1z)*(y3 - y1);
		double czb = (x3 - x1)*(camera2z - camera1z) - (x2 - x1)*(camera3z - camera1z);
		double czc = (x2 - x1)*(y3 - y1) - (x3 - x1)*(y2 - y1);
		double cz=(cza*(x-x1)+czb*(y-y1)-czc*camera1z)/(-czc);
		
		Point3DH normal=new Point3DH(nx,ny,nz).Normalize();
		Vertex3D cameraVertex=new Vertex3D(cx,cy,cz,current.getColor());
		cameraVertex.addNormal(normal);
//		System.out.println(cameraVertex);
		return lightCal.lightAtVertexForFlat(polygon, cameraVertex);
	}
}
