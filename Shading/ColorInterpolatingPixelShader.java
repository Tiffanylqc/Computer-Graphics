package Shading;

import geometry.Vertex3D;
import polygon.Chain;
import polygon.Polygon;
import windowing.graphics.Color;
import Shading.LightCalculation;

public class ColorInterpolatingPixelShader implements PixelShader {
	private LightCalculation lightCal;
	public ColorInterpolatingPixelShader(LightCalculation lightCal){
		this.lightCal=lightCal;
	}
	@Override
	public Color shade(Polygon polygon, Vertex3D current) {
		Color color0=lightCal.lightAtVertexForGou(polygon, polygon.get(0));
		Color color1=lightCal.lightAtVertexForGou(polygon, polygon.get(1));
		Color color2=lightCal.lightAtVertexForGou(polygon, polygon.get(2));
		
		double x1=polygon.get(0).getX();
		double y1=polygon.get(0).getY();
		double z1=1/polygon.get(0).getZ();
		double r1=color0.getR();
		double g1=color0.getG();
		double b1=color0.getB();
		
		double x2=polygon.get(1).getX();
		double y2=polygon.get(1).getY();
		double z2=1/polygon.get(1).getZ();
		double r2=color1.getR();
		double g2=color1.getG();
		double b2=color1.getB();
		
		double x3=polygon.get(2).getX();
		double y3=polygon.get(2).getY();
		double z3=1/polygon.get(2).getZ();
		double r3=color2.getR();
		double g3=color2.getG();
		double b3=color2.getB();
		
		double x=current.getX();
		double y=current.getY();

		double ra=(y2 - y1)*(r3 - r1) - (r2 -r1)*(y3 - y1);
		double rb = (x3 - x1)*(r2 - r1) - (x2 - x1)*(r3 - r1);
		double rc = (x2 - x1)*(y3 - y1) - (x3 - x1)*(y2 - y1);
		double r=(ra*(x-x1)+rb*(y-y1)-rc*r1)/(-rc);
		
		double ga=(y2 - y1)*(g3 - g1) - (g2 -g1)*(y3 - y1);
		double gb = (x3 - x1)*(g2 - g1) - (x2 - x1)*(g3 - g1);
		double gc = (x2 - x1)*(y3 - y1) - (x3 - x1)*(y2 - y1);
		double g=(ga*(x-x1)+gb*(y-y1)-gc*g1)/(-gc);
		
		double ba=(y2 - y1)*(b3 - b1) - (b2 -b1)*(y3 - y1);
		double bb = (x3 - x1)*(b2 - b1) - (x2 - x1)*(b3 - b1);
		double bc = (x2 - x1)*(y3 - y1) - (x3 - x1)*(y2 - y1);
		double b=(ba*(x-x1)+bb*(y-y1)-bc*b1)/(-bc);
		
		return new Color(r,g,b);
	}

}
