package client;

import line.LineRenderer;
import line.DDALineRenderer;
import polygon.PolygonRenderer;
import polygon.FilledPolygonRenderer;
import polygon.WireframePolygonRenderer;

public class RendererTrio {
	private LineRenderer lineRenderer;
	private FilledPolygonRenderer filledRenderer;
	private WireframePolygonRenderer wireRenderer;
	
	public RendererTrio(){
		lineRenderer=DDALineRenderer.make();
		filledRenderer=new FilledPolygonRenderer();
		wireRenderer=WireframePolygonRenderer.make(lineRenderer);
	}
	
	public LineRenderer getLineRenderer(){
		return this.lineRenderer;
	}
	
	public PolygonRenderer getFilledRenderer(){
		return this.filledRenderer;
	}
	
	public PolygonRenderer getWireframeRenderer(){
		return this.wireRenderer;
	}

}
