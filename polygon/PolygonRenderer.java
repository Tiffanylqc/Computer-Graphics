package polygon;

import Shading.FaceShader;
import Shading.LightCalculation;
import Shading.PixelShader;
import Shading.VertexShader;
import client.interpreter.SimpInterpreter.ShadeStyle;
import polygon.Shader;
import windowing.drawable.Drawable;

public interface PolygonRenderer {
	// assumes polygon is ccw.
	public void drawPolygon(Polygon polygon, Drawable drawable, ShadeStyle shadeStyle, LightCalculation lightCal);

//	default public void drawPolygon(Polygon polygon, Drawable panel) {
//		drawPolygon(polygon, panel,  c -> c);
//	};
}
