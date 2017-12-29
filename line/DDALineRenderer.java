package line;

import geometry.Vertex3D;
import windowing.drawable.Drawable;

import windowing.graphics.Color;//not provided in teacher's code

public class DDALineRenderer implements LineRenderer{
	
	public static LineRenderer make(){//static factory pattern --like a constructor 
		return new AnyOctantLineRenderer(new DDALineRenderer());
	}

	public void drawLine(Vertex3D p1, Vertex3D p2, Drawable panel){//from p1 to p2 draw a line on panel 
//		System.out.println("DDALINE");
//		System.out.println(p1.toString());
//		System.out.println(p2.toString());
		
		double z=p1.getZ();
		double y=p1.getY();
		double r=p1.getColor().getR();
		double g=p1.getColor().getG();
		double b=p1.getColor().getB();
		
		double deltaX = p2.getX() - p1.getX();
		double deltaY = p2.getY() - p1.getY();
		double deltaZ = p2.getZ() - p1.getZ();
		double deltaR=p2.getColor().getR()-p1.getColor().getR();
		double deltaG=p2.getColor().getG()-p1.getColor().getG();
		double deltaB=p2.getColor().getB()-p1.getColor().getB();
		
		double slope = deltaY / deltaX;
		double slopeZ= deltaZ/deltaX;
		double slopeR=deltaR/deltaX;
		double slopeG=deltaG/deltaX;
		double slopeB=deltaB/deltaX;
		
		for(int x = p1.getIntX(); x <= p2.getIntX(); x++,y+=slope,z+=slopeZ,g+=slopeG,b+=slopeB,r+=slopeR) {
			Color argbColor=new Color(r, g, b);
			panel.setPixel(x, (int)Math.round(y), z, argbColor.asARGB());
		}

	}
}
