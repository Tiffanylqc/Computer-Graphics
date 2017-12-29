package Shading;
import geometry.Point3DH;
import windowing.graphics.Color;

public class Lighting {
	private Point3DH cameraLoc;
	private Color intensity;
	private Color ambientLight;
	private double attenA;
	private double attenB;
	
	public Lighting(Point3DH cameraLoc, Color intensity, double attenA, double attenB,Color ambientLight){
		this.cameraLoc=cameraLoc;
		this.intensity=intensity;
		this.attenA=attenA;
		this.attenB=attenB;
		this.ambientLight=ambientLight;
	}
	public Point3DH getCameraLoc(){
		return cameraLoc;
	}
	public Color getIntensity(){
		return intensity;
	}
	public double getattenA(){
		return attenA;
	}
	public double getattenB(){
		return attenB;
	}
	public Color getAmbientLight(){
		return ambientLight;
	}
}
