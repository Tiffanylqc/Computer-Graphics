package line;

import geometry.Vertex3D;
import windowing.drawable.Drawable;

public class AntialiasingLineRenderer implements LineRenderer{

	private static final double RADIUS=Math.sqrt(2.0);
	private static final double LINEWIDTH=1.0;
	public static LineRenderer make(){
		return new AnyOctantLineRenderer(new AntialiasingLineRenderer());
	}
	
	@Override
	public void drawLine(Vertex3D p1, Vertex3D p2, Drawable panel) {
		double deltaX = p2.getIntX() - p1.getIntX();
		double deltaY = p2.getIntY() - p1.getIntY();	
		double slope = deltaY / deltaX;
		double intercept = p2.getIntY() - slope * p2.getIntX();	
		double distance;
		double coverage;
		double theta;
		int Height=panel.getHeight();
		int argbColor = p1.getColor().asARGB();
		int y;		
		for(int x = p1.getIntX(); x <= p2.getIntX(); x++) {
			for(y=-Height;y<Height;y++){
				distance=Math.abs(((slope*x-y+intercept)/Math.sqrt(slope*slope+1)));
				if(distance<RADIUS+LINEWIDTH/2){
					distance-=LINEWIDTH/2;
					theta=Math.acos(distance/RADIUS);
					coverage=1-(((1-theta/Math.PI)*Math.PI*RADIUS*RADIUS+distance*Math.sqrt(RADIUS*RADIUS-distance*distance))/(Math.PI*RADIUS*RADIUS));
					panel.setPixelWithCoverage(x, y, 0.0, argbColor, coverage);
				}
			}
		}	
	}

}
