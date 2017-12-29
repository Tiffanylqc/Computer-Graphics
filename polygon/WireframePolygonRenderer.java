package polygon;

import windowing.drawable.Drawable;
import windowing.graphics.Color;
import line.LineRenderer;
import line.DDALineRenderer;
import Shading.*;
import client.interpreter.SimpInterpreter.ShadeStyle;

public class WireframePolygonRenderer implements PolygonRenderer{
//public class WireframePolygonRenderer{
	private LineRenderer lineRenderer;
	
	private WireframePolygonRenderer(LineRenderer lineRenderer){
		this.lineRenderer=lineRenderer;
	}
	public static WireframePolygonRenderer make(LineRenderer lineRenderer){
		return new WireframePolygonRenderer(lineRenderer);
	}

	@Override
	public void drawPolygon(Polygon polygon, Drawable drawable, ShadeStyle shadeStyle, LightCalculation lightCal) {
		int length=polygon.length();
		for(int i=0;i<length-1;i++)
			lineRenderer.drawLine(polygon.get(i),polygon.get(i+1),drawable);
		
		lineRenderer.drawLine(polygon.get(length-1),polygon.get(0),drawable);
	}
}
