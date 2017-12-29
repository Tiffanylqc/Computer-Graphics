package Shading;

import polygon.Polygon;
import geometry.Point3DH;

public class FlatFaceShader implements FaceShader{
	public Polygon shade(Polygon polygon){
		int length=polygon.length();
		boolean hasNormal=true;
		Point3DH normal;

		for(int i=0;i<length;i++){
			if(polygon.get(i).hasNormal()==false){
				hasNormal=false;
				break;
			}
		}

		if(hasNormal==true){
			normal=polygon.get(0).getNormal().add(polygon.get(1).getNormal());
			normal=normal.add(polygon.get(2).getNormal());
			polygon.addfaceNormal(normal.Normalize());
		}
		else{
			polygon.createfaceNormal();
		}
		return polygon;
	}
}
