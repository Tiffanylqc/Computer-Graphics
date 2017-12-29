package Shading;

import geometry.Vertex3D;
import polygon.Polygon;
import windowing.graphics.Color;
import Shading.*;

public class FlatPixelShader implements PixelShader{
	private LightCalculation lightCal;
	public FlatPixelShader(LightCalculation lightCal){
		this.lightCal=lightCal;
	}
	public Color shade(Polygon polygon, Vertex3D current){
		Vertex3D center=new Vertex3D(polygon.getCenter(),current.getColor());
		return this.lightCal.lightAtVertexForFlat(polygon, center);
	}
}
