package client.interpreter;

import java.util.ArrayList;
import java.util.Stack;

import Shading.Lighting;
import client.interpreter.LineBasedReader;
import client.interpreter.SimpInterpreter.RenderStyle;
import geometry.Point3DH;
import geometry.Rectangle;
import geometry.Vertex3D;
import line.LineRenderer;
import client.Clipper;
import windowing.drawable.DepthCueingDrawable;
import windowing.drawable.DepthEffectDrawable;
import client.RendererTrio;
import geometry.Transformation;
import polygon.Polygon;
import polygon.PolygonRenderer;
import polygon.Shader;
import windowing.drawable.Drawable;
import windowing.graphics.Color;
import windowing.graphics.Dimensions;
import Shading.*;

public class SimpInterpreter {
	private static final int NUM_TOKENS_FOR_POINT = 3;
	private static final int NUM_TOKENS_FOR_COMMAND = 1;
	private static final int NUM_TOKENS_FOR_COLORED_VERTEX = 6;
	private static final int NUM_TOKENS_FOR_UNCOLORED_VERTEX = 3;
	private static final char COMMENT_CHAR = '#';
	private static RenderStyle renderStyle;
	private static ShadeStyle shadeStyle;
	
	public static double near,far;
	public static boolean DepthEffect=false;
	public static boolean Camera=false;
	
	public static Transformation CameraInverse;
	public static double xlow,ylow,xhigh,yhigh,hither,yon;
	public static double VIEWPLANE=-1.0;
	
	public static Transformation CTM;
	public Stack<Transformation> CTMStack;
	public static Transformation worldToScreen;
	public static Transformation cameraToScreen;
	
	private static double WORLD_LOW_X = -100.0;
	private static double WORLD_HIGH_X = 100.0;
	private static double WORLD_LOW_Y = -100.0;
	private static double WORLD_HIGH_Y = 100.0;
	
	private static double specularCoefficient;
	private static double specularExponent;
	
	private LineBasedReader reader;
	private Stack<LineBasedReader> readerStack;
	
	private static ArrayList<Lighting> lightSources;
	private static Color defaultColor = Color.WHITE;//surface color
	private static Color ambientLight = Color.BLACK;
	private static Color depthColor;
	
	public static Drawable drawable;
//	private DepthCueingDrawable depthCueingDrawable;
	private static DepthEffectDrawable depthEffectDrawable;
	
	private static LineRenderer lineRenderer;
	public static PolygonRenderer filledRenderer;
	public static PolygonRenderer wireframeRenderer;
	
	public static Clipper clipper;

	public enum RenderStyle {
		FILLED,
		WIREFRAME;
	}
	
	public enum ShadeStyle{
		PHONG,
		GOURAUD,
		FLAT;
	}
	
	public SimpInterpreter(String filename, 
			Drawable drawable,
			RendererTrio renderers) {
		this.drawable = drawable;
		this.lineRenderer = renderers.getLineRenderer();
		this.filledRenderer = renderers.getFilledRenderer();
		this.wireframeRenderer = renderers.getWireframeRenderer();
		makeWorldToScreenTransform(drawable.getDimensions());
		DepthEffect=false;
		Camera=false;
		reader = new LineBasedReader(filename);
		readerStack = new Stack<>();
		renderStyle = RenderStyle.FILLED;
		CTM = Transformation.identity();
		CTMStack=new Stack<>();
		clipper=new Clipper();
		near=-Double.MAX_VALUE;
		defaultColor=Color.WHITE;
		ambientLight=Color.BLACK;
		specularCoefficient=0.3;
		specularExponent=8.0;
		shadeStyle=ShadeStyle.PHONG;
		lightSources=new ArrayList<Lighting>();
	}

	private void makeWorldToScreenTransform(Dimensions dimensions) {
		int Height=dimensions.getHeight();
		int Width=dimensions.getWidth();
		double sx=650.0/(WORLD_HIGH_X-WORLD_LOW_X);
		double sy=650.0/(WORLD_HIGH_Y-WORLD_LOW_Y);
		
		Transformation Scale=Transformation.MakeScaleTransformation(sx, sy, 1.0);
		Transformation translate=Transformation.MakeTranslateTransformation(Width/2.0, Height/2.0, 0.0);
		worldToScreen=translate.doTransformation(Scale);
	}
	
	private static Transformation makeViewToScreenTransform(){
		double sx=650.0/(xhigh-xlow);
		double sy=650.0/(yhigh-ylow);
		double s=Math.min(sx, sy);
		double middlex,middley;
		middlex=(xhigh+xlow)/2.0*s;
		middley=(yhigh+ylow)/2.0*s;
		Transformation Scale=Transformation.MakeScaleTransformation(s, s,1.0);
		Transformation translate=Transformation.MakeTranslateTransformation(650.0/2.0-middlex, 650.0/2.0-middley, 0.0);
		Transformation viewToScreen=translate.doTransformation(Scale);
		return viewToScreen;
	}
	
	public void interpret() {
		while(reader.hasNext() ) {
			String line = reader.next().trim();
			interpretLine(line);
			while(!reader.hasNext()) {
				if(readerStack.isEmpty()) {
					return;
				}
				else {
					reader = readerStack.pop();
				}
			}
		}
	}
	public void interpretLine(String line) {
		if(!line.isEmpty() && line.charAt(0) != COMMENT_CHAR) {
			String[] tokens = line.split("[ \t,()]+");
			if(tokens.length != 0) {
				interpretCommand(tokens);
			}
		}
	}
	private void interpretCommand(String[] tokens) {
		switch(tokens[0]) {
		case "{" :      push();   break;
		case "}" :      pop();    break;
		case "wire" :   wire();   break;
		case "filled" : filled(); break;
		case "phong":	phong();  break;
		case "gouraud": gouraud();break;
		case "flat":	flat();	  break;
		
		case "file" :		interpretFile(tokens);		break;
		case "scale" :		interpretScale(tokens);		break;
		case "translate" :	interpretTranslate(tokens);	break;
		case "rotate" :		interpretRotate(tokens);	break;
		case "line" :		interpretLine(tokens);		break;
		case "polygon" :	interpretPolygon(tokens);	break;
		case "camera" :		interpretCamera(tokens);	break;
		case "surface" :	interpretSurface(tokens);	break;
		case "ambient" :	interpretAmbient(tokens);	break;
		case "depth" :		interpretDepth(tokens);		break;
		case "obj" :		interpretObj(tokens);		break;
		case "light":		interpretLight(tokens);		break;
		default :
			System.err.println("bad input line: " + tokens);
			break;
		}
	}

	private void push() {
		CTMStack.push(CTM);
	}
	private void pop() {
		// TODO: finish this method
		CTM=CTMStack.pop();
	}
	private void wire() {
		// TODO: finish this method
		renderStyle = RenderStyle.WIREFRAME;
	}
	private void filled() {
		// TODO: finish this method
		renderStyle = RenderStyle.FILLED;
	}
	private void phong(){
		shadeStyle=ShadeStyle.PHONG;
	}
	private void gouraud(){
		shadeStyle=ShadeStyle.GOURAUD;
	}
	private void flat(){
		shadeStyle=ShadeStyle.FLAT;
	}
	
	// this one is complete.
	private void interpretFile(String[] tokens) {
		String quotedFilename = tokens[1];
		int length = quotedFilename.length();
		assert quotedFilename.charAt(0) == '"' && quotedFilename.charAt(length-1) == '"'; 
		String filename = quotedFilename.substring(1, length-1);
		file(filename + ".simp");
	}
	private void file(String filename) {
		readerStack.push(reader);
		reader = new LineBasedReader(filename);
	}	
	
	private void interpretObj(String[] tokens){
		String quotedFilename=tokens[1];
		int length = quotedFilename.length();
		assert quotedFilename.charAt(0) == '"' && quotedFilename.charAt(length-1) == '"'; 
		String filename = quotedFilename.substring(1, length-1);
		objFile(filename+".obj");
	}
	
	private void objFile(String filename) {
		ObjReader objReader = new ObjReader(filename, defaultColor);
		objReader.read();
		objReader.render();
	}

	private void interpretScale(String[] tokens) {
		double sx = cleanNumber(tokens[1]);
		double sy = cleanNumber(tokens[2]);
		double sz = cleanNumber(tokens[3]);
		
		// TODO: finish this method
		Transformation scale=Transformation.MakeScaleTransformation(sx, sy, sz);
		CTM=CTM.doTransformation(scale);
	}
	private void interpretTranslate(String[] tokens) {
		double tx = cleanNumber(tokens[1]);
		double ty = cleanNumber(tokens[2]);
		double tz = cleanNumber(tokens[3]);
		
		// TODO: finish this method
		Transformation translate=Transformation.MakeTranslateTransformation(tx, ty, tz);
		CTM=CTM.doTransformation(translate);
	}
	private void interpretRotate(String[] tokens) {
		String axisString = tokens[1];
		double angleInDegrees = cleanNumber(tokens[2]);

		// TODO: finish this method
		Transformation rotate=Transformation.MakeRotateTransformation(axisString, angleInDegrees);
		CTM=CTM.doTransformation(rotate);
	}
	
	private static double cleanNumber(String string) {
		return Double.parseDouble(string);
	}
	
	private enum VertexColors {
		COLORED(NUM_TOKENS_FOR_COLORED_VERTEX),
		UNCOLORED(NUM_TOKENS_FOR_UNCOLORED_VERTEX);
		
		private int numTokensPerVertex;
		
		private VertexColors(int numTokensPerVertex) {
			this.numTokensPerVertex = numTokensPerVertex;
		}
		public int numTokensPerVertex() {
			return numTokensPerVertex;
		}
	}
	
	private void interpretLine(String[] tokens) {			
		Vertex3D[] vertices = interpretVertices(tokens, 2, 1);
		Vertex3D[] transformedVertices=new Vertex3D[2];
		// TODO: finish this method
		Transformation objectToCamera=CameraInverse.doTransformation(CTM);
		transformedVertices[0]=objectToCamera.transformVertex(vertices[0]);
		transformedVertices[1]=objectToCamera.transformVertex(vertices[1]);
		
		lineRenderer.drawLine(transformedVertices[0], transformedVertices[1], depthEffectDrawable);
	}	
	
	public void interpretPolygon(String[] tokens) {	
		Vertex3D[] vertices = interpretVertices(tokens, 3, 1);
//		if(renderStyle==RenderStyle.FILLED&&Polygon.isClockwise(vertices[0],vertices[1],vertices[2]))
//			return;
		
		Vertex3D[] transformedVertices=new Vertex3D[3];
		// TODO: finish this method
		Transformation objectToCamera;
		if(Camera==true)
			objectToCamera=CameraInverse.doTransformation(CTM);
		else
			objectToCamera=CTM;
//		System.out.println(objectToCamera);
		transformedVertices[0]=objectToCamera.transformVertex(vertices[0]);
		transformedVertices[1]=objectToCamera.transformVertex(vertices[1]);
		transformedVertices[2]=objectToCamera.transformVertex(vertices[2]);
		
		//clip between hither and yon
		Polygon originalPolygon=Polygon.make(transformedVertices);
		
		Polygon ZclippedPolygon=clipper.ClipZPlane(originalPolygon, hither, yon);
		
		if(ZclippedPolygon==null)
			return;
		
		Polygon cameraPolygon=Polygon.makeEmpty();
		int length=ZclippedPolygon.length();
		for(int i=0;i<length;i++){
			cameraPolygon.add(transformToCamera(ZclippedPolygon.get(i)));
		}
		//clip cameraPolygon in X and Y on view plane
		
		Polygon cameraClippedPolygon=clipper.ClipXYPlane(cameraPolygon, xlow, xhigh, ylow, yhigh);
		if(cameraClippedPolygon==null)
			return;
		
		//transform to screen space 
		length=cameraClippedPolygon.length();
		Polygon screenPolygon=Polygon.makeEmpty();
		Transformation viewToScreen=makeViewToScreenTransform();
		for(int i=0;i<length;i++){
			Vertex3D vertex=viewToScreen.transformVertex(cameraClippedPolygon.get(i));
			double x=cameraClippedPolygon.get(i).getX();
			double y=cameraClippedPolygon.get(i).getY();
			double z=cameraClippedPolygon.get(i).getZ();
			
			vertex.addCameraPoint(x*(-z), y*(-z), z);
			screenPolygon.add(vertex);			
//			screenPolygon.add(viewToScreen.transformVertex(cameraClippedPolygon.get(i)));
		}
		//do light calculation
//		screenPolygon=LightCalculation(screenPolygon);

		//use depth effect to draw
		if(DepthEffect==true){
			drawable=depthEffectDrawable;
		}
		
		LightCalculation lightCal=new LightCalculation(lightSources, ambientLight, defaultColor, specularCoefficient, specularExponent);
		screenPolygon.addspecCoeExp(specularCoefficient, specularExponent);

		if(length==3){
			if(renderStyle==RenderStyle.FILLED){
				filledRenderer.drawPolygon(screenPolygon, drawable,shadeStyle,lightCal);
			}
			else{
//				wireframeRenderer.drawPolygon(screenPolygon, drawable);
			}
		}
		else if(length>3){
			Polygon[] screenPolygons=breakPolygon(screenPolygon);
			if(renderStyle==RenderStyle.FILLED){
				for(int i=0;i<length-2;i++)
					filledRenderer.drawPolygon(screenPolygons[i], drawable,shadeStyle,lightCal);
			}
			else{
//				wireframeRenderer.drawPolygon(screenPolygon, drawable);
			}			
		}
	}
	
	public static void interpretPolygon(Polygon polygon) {
		Vertex3D[] vertices = new Vertex3D[3];
		boolean hasNormals=false;
		vertices[0]=polygon.get(0);
		vertices[1]=polygon.get(1);
		vertices[2]=polygon.get(2);
		if(vertices[0].hasNormal()&&vertices[1].hasNormal()&&vertices[2].hasNormal())
			hasNormals=true;
//		if(renderStyle!=RenderStyle.FILLED&&Polygon.isClockwise(vertices[0],vertices[1],vertices[2]))
//			return;
		
		Vertex3D[] transformedVertices=new Vertex3D[3];
		// TODO: finish this method
		Transformation objectToCamera;
		if(Camera==true)
			objectToCamera=CameraInverse.doTransformation(CTM);
		else
			objectToCamera=CTM;
		transformedVertices[0]=objectToCamera.transformVertex(vertices[0]);
		transformedVertices[1]=objectToCamera.transformVertex(vertices[1]);
		transformedVertices[2]=objectToCamera.transformVertex(vertices[2]);
		
		if(hasNormals==true){
//			System.out.println("yes");
			Transformation invobjectToCamera=objectToCamera.Inverse();
			Point3DH normal0=invobjectToCamera.transformNormal(vertices[0].getNormal());
			transformedVertices[0].addNormal(normal0.Normalize());
			
			Point3DH normal1=invobjectToCamera.transformNormal(vertices[1].getNormal());
			transformedVertices[1].addNormal(normal1.Normalize());
			
			Point3DH normal2=invobjectToCamera.transformNormal(vertices[2].getNormal());
			transformedVertices[2].addNormal(normal2.Normalize());
		}
		
		//clip between hither and yon
		Polygon originalPolygon=Polygon.make(transformedVertices);
		
		Polygon ZclippedPolygon=clipper.ClipZPlane(originalPolygon, hither, yon);
		
		if(ZclippedPolygon==null)
			return;

		Polygon cameraPolygon=Polygon.makeEmpty();
		int length=ZclippedPolygon.length();
		for(int i=0;i<length;i++){
			cameraPolygon.add(transformToCamera(ZclippedPolygon.get(i)));
			if(ZclippedPolygon.get(i).getNormal()!=null)
				cameraPolygon.get(i).addNormal(ZclippedPolygon.get(i).getNormal());
		}
		//clip cameraPolygon in X and Y on view plane

		Polygon cameraClippedPolygon=clipper.ClipXYPlane(cameraPolygon, xlow, xhigh, ylow, yhigh);
		if(cameraClippedPolygon==null)
			return;
		
		//transform to screen space 
		length=cameraClippedPolygon.length();
		Polygon screenPolygon=Polygon.makeEmpty();
		Transformation viewToScreen=makeViewToScreenTransform();
		for(int i=0;i<length;i++){
			Vertex3D vertex=viewToScreen.transformVertex(cameraClippedPolygon.get(i));
			double x=cameraClippedPolygon.get(i).getX();
			double y=cameraClippedPolygon.get(i).getY();
			double z=cameraClippedPolygon.get(i).getZ();
			
			vertex.addCameraPoint(x*(-z), y*(-z), z);
			if(cameraClippedPolygon.get(i).getNormal()!=null)
				vertex.addNormal(cameraClippedPolygon.get(i).getNormal());
			screenPolygon.add(vertex);			
//			screenPolygon.add(viewToScreen.transformVertex(cameraClippedPolygon.get(i)));
		}

		//do light calculation
//		if(renderStyle==RenderStyle.FILLED)
//			screenPolygon=LightCalculation(screenPolygon);
//		System.out.println(screenPolygon);
		
		//use depth effect to draw
		if(DepthEffect==true){
			drawable=depthEffectDrawable;
		}
		
		LightCalculation lightCal=new LightCalculation(lightSources, ambientLight, defaultColor, specularCoefficient, specularExponent);
		screenPolygon.addspecCoeExp(specularCoefficient, specularExponent);

		if(length==3){
			if(renderStyle==RenderStyle.FILLED){
				filledRenderer.drawPolygon(screenPolygon, drawable,shadeStyle,lightCal);
			}
			else{
//				wireframeRenderer.drawPolygon(screenPolygon, drawable);
			}
		}
		else if(length>3){
			Polygon[] screenPolygons=breakPolygon(screenPolygon);
			if(renderStyle==RenderStyle.FILLED){
				for(int i=0;i<length-2;i++)
					filledRenderer.drawPolygon(screenPolygons[i], drawable,shadeStyle,lightCal);
			}
			else{
//				wireframeRenderer.drawPolygon(screenPolygon, drawable);
			}			
		}
	}
	
	private static Polygon[] breakPolygon(Polygon polygon){
		int length=polygon.length();
		Polygon[] polygons=new Polygon[length-2];
		for(int i=1;i<=length-2;i++){
			polygons[i-1]=Polygon.makeEmpty();
			polygons[i-1].add(polygon.get(0));
			polygons[i-1].add(polygon.get(i));
			polygons[i-1].add(polygon.get(i+1));
		}
		return polygons;
	}
	
	
	public static Vertex3D[] interpretVertices(String[] tokens, int numVertices, int startingIndex) {
		VertexColors vertexColors = verticesAreColored(tokens, numVertices);	
		Vertex3D vertices[] = new Vertex3D[numVertices];
		
		for(int index = 0; index < numVertices; index++) {
			vertices[index] = interpretVertex(tokens, startingIndex + index * vertexColors.numTokensPerVertex(), vertexColors);
		}
		return vertices;
	}
	
	public static VertexColors verticesAreColored(String[] tokens, int numVertices) {
		return hasColoredVertices(tokens, numVertices) ? VertexColors.COLORED :
														 VertexColors.UNCOLORED;
	}
	public static boolean hasColoredVertices(String[] tokens, int numVertices) {
		return tokens.length == numTokensForCommandWithNVertices(numVertices);
	}
	public static int numTokensForCommandWithNVertices(int numVertices) {
		return NUM_TOKENS_FOR_COMMAND + numVertices*(NUM_TOKENS_FOR_COLORED_VERTEX);
	}

	
	private static Vertex3D interpretVertex(String[] tokens, int startingIndex, VertexColors colored) {
		Point3DH point = interpretPoint(tokens, startingIndex);
		
		Color color = defaultColor;
		if(colored == VertexColors.COLORED) {
			color = interpretColor(tokens, startingIndex + NUM_TOKENS_FOR_POINT);
		}

		// TODO: finish this method
		return new Vertex3D(point,color);
	}
	
	public static Point3DH interpretPoint(String[] tokens, int startingIndex) {
		double x = cleanNumber(tokens[startingIndex]);
		double y = cleanNumber(tokens[startingIndex + 1]);
		double z = cleanNumber(tokens[startingIndex + 2]);

		// TODO: finish this method
		return new Point3DH(x,y,z);
	}
	
	public static Point3DH interpretPointWithW(String[] tokens, int startingIndex) {
		double x = cleanNumber(tokens[startingIndex]);
		double y = cleanNumber(tokens[startingIndex + 1]);
		double z = cleanNumber(tokens[startingIndex + 2]);
		double w = cleanNumber(tokens[startingIndex + 3]);
		Point3DH point = new Point3DH(x, y, z, w);
		return point;
	}
	
	public static Color interpretColor(String[] tokens, int startingIndex) {
		double r = cleanNumber(tokens[startingIndex]);
		double g = cleanNumber(tokens[startingIndex + 1]);
		double b = cleanNumber(tokens[startingIndex + 2]);

		// TODO: finish this method
		return new Color(r,g,b);
	}
	
	private void interpretCamera(String[] tokens){
		xlow=cleanNumber(tokens[1]);
		ylow=cleanNumber(tokens[2]);
		xhigh=cleanNumber(tokens[3]);
		yhigh=cleanNumber(tokens[4]);
		hither=cleanNumber(tokens[5]);
		yon=cleanNumber(tokens[6]);
		CameraInverse=CTM.Inverse();
		Camera=true;
	}
	
	private void interpretAmbient(String[] tokens){
		ambientLight=interpretColor(tokens,1);
	}
	
	private void interpretSurface(String[] tokens){
		defaultColor=interpretColor(tokens,1);
		specularCoefficient=cleanNumber(tokens[4]);
		specularExponent=cleanNumber(tokens[5]);
	}
	
	private void interpretDepth(String[] tokens){
		near=cleanNumber(tokens[1]);
		far=cleanNumber(tokens[2]);
		depthColor=interpretColor(tokens,3);
		DepthEffect=true;
		depthEffectDrawable=new DepthEffectDrawable(drawable, near, far, depthColor, ambientLight);
	}
	
	private void interpretLight(String[] tokens){
		double red=cleanNumber(tokens[1]);
		double green=cleanNumber(tokens[2]);
		double blue=cleanNumber(tokens[3]);
		Color intensity=new Color(red,green,blue);
		double A=cleanNumber(tokens[4]);
		double B=cleanNumber(tokens[5]);
		//change light to camera space 
		Point3DH cameraLoc=new Point3DH(0,0,0);
		Transformation objectToCamera=CameraInverse.doTransformation(CTM);
		cameraLoc=objectToCamera.transformVertex(cameraLoc);
		Lighting lightSource=new Lighting(cameraLoc, intensity, A, B, ambientLight);
		lightSources.add(lightSource);
	}
	
//	private Color getDepthEffectColor(Vertex3D vertex){
//		double csz=vertex.getZ();
//		Color nearColor=getLightCalculateColor(vertex);
//		Color farColor=depthColor;
//		double near_r=nearColor.getR();
//		double near_g=nearColor.getG();
//		double near_b=nearColor.getB();
//		double far_r=farColor.getR();
//		double far_g=farColor.getG();
//		double far_b=farColor.getB();
//		System.out.println(vertex.toString());
//
//		if(csz>=near){
//			return nearColor;
//		}
//		else if(csz<=far){
//			return farColor;
//		}
//		else{
//			double percentage=(near-csz)/(near-far);
//			double r=near_r*(1-percentage)+far_r*percentage;
//			double g=near_g*(1-percentage)+far_g*percentage;
//			double b=near_b*(1-percentage)+far_b*percentage;
//			return new Color(r,g,b);
//		}
//	}
//	
//	private Color getLightCalculateColor(Vertex3D vertex){
//		Color color=vertex.getColor();
//		double r=ambientLight.getR()*color.getR();
//		double g=ambientLight.getG()*color.getG();
//		double b=ambientLight.getB()*color.getB();
//		Color lightCalculate=new Color(r,g,b);
//		return lightCalculate;
//	}
//	private void line(Vertex3D p1, Vertex3D p2) {
//		Vertex3D screenP1 = transformToCamera(p1);
//		Vertex3D screenP2 = transformToCamera(p2);
//		// TODO: finish this method
//	}
//	private void polygon(Vertex3D p1, Vertex3D p2, Vertex3D p3) {
//		Vertex3D screenP1 = transformToCamera(p1);
//		Vertex3D screenP2 = transformToCamera(p2);
//		Vertex3D screenP3 = transformToCamera(p3);
//		// TODO: finish this method
//	}

	private static Vertex3D transformToCamera(Vertex3D vertex) {
		// TODO: finish this method
		//leave z unchanged
		double x=vertex.getX();
		double y=vertex.getY();
		double z=vertex.getZ();
		x=(-x)/z;
		y=(-y)/z;
		Vertex3D v=new Vertex3D(x,y,z,vertex.getColor());
		return v;
	}

}
