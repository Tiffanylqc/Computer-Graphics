package Shading;

import geometry.Point3DH;
import geometry.Vertex3D;
import polygon.Polygon;

public class GouraudVertexShader implements VertexShader {

	@Override
	public Vertex3D shade(Polygon polygon, Vertex3D vertex) {
		Point3DH normal;

		if(vertex.hasNormal()==false){
			polygon.createfaceNormal();
			normal=polygon.getFaceNormal();
			vertex.addNormal(normal);
		}
		return vertex;	
	}
}
