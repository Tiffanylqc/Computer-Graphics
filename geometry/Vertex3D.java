package geometry;

import windowing.graphics.Color;

public class Vertex3D implements Vertex {
	protected Point3DH point;//screen space point
	protected Color color;
	protected Point3DH cameraPoint;//camera space point
	protected Point3DH normal;
	protected boolean HasNormal;
	
	public Vertex3D(Point3DH point, Color color) {
		super();
		this.point = point;
		this.color = color;
		this.HasNormal=false;
	}
	public Vertex3D(double x, double y, double z, Color color) {
		this(new Point3DH(x, y, z), color);
		this.HasNormal=false;
	}
	///////////////
	public void addCameraPoint(double x,double y,double z){
		cameraPoint=new Point3DH(x,y,z);
	}
	public Point3DH getCameraPoint(){
		if(this.cameraPoint!=null)
			return this.cameraPoint;
		else{ 
//			this.cameraPoint=new Point3DH(-this.getX()*this.getZ(),-this.getY()*this.getZ(),this.getZ());
//			return this.cameraPoint;
			System.out.println("no camera point");
			return null;
		}
	}
	public void addNormal(Point3DH normal){
		this.normal=new Point3DH(normal.getX(),normal.getY(),normal.getZ(),normal.getW());
		this.normal=this.normal.Normalize();
		HasNormal=true;
	}
	public Point3DH getNormal(){
		return normal;
	}
	public boolean hasNormal(){
		return HasNormal;
	}
	///////////////
	public Vertex3D() {
	}
	public double getX() {
		return point.getX();
	}
	public double getY() {
		return point.getY();
	}
	public double getZ() {
		return point.getZ();
	}
	public double getCameraSpaceZ() {
		return getZ();
	}
	public Point getPoint() {
		return point;
	}
	public Point3DH getPoint3D() {
		return point;
	}
	
	public int getIntX() {
		return (int) Math.round(getX());
	}
	public int getIntY() {
		return (int) Math.round(getY());
	}
	public int getIntZ() {
		return (int) Math.round(getZ());
	}
	
	public Color getColor() {
		return color;
	}
	
	public Vertex3D rounded() {
		return new Vertex3D(point.round(), color);
	}
	public Vertex3D add(Vertex other) {
		Vertex3D other3D = (Vertex3D)other;
		return new Vertex3D(point.add(other3D.getPoint()),
				            color.add(other3D.getColor()));
	}
	public Vertex3D subtract(Vertex other) {
		Vertex3D other3D = (Vertex3D)other;
		return new Vertex3D(point.subtract(other3D.getPoint()),
				            color.subtract(other3D.getColor()));
	}
	public Vertex3D scale(double scalar) {
		return new Vertex3D(point.scale(scalar),
				            color.scale(scalar));
	}
	public Vertex3D replacePoint(Point3DH newPoint) {
		return new Vertex3D(newPoint, color);
	}
	public Vertex3D replaceColor(Color newColor) {
		return new Vertex3D(point, newColor);
	}
	public Vertex3D euclidean() {
		Point3DH euclidean = getPoint3D().euclidean();
		return replacePoint(euclidean);
	}
	
	public String toString() {
		return "(" + getX() + ", " + getY() + ", " + getZ() + ", " + getColor().toIntString() + ")";
	}
	public String toIntString() {
		return "(" + getIntX() + ", " + getIntY() + getIntZ() + ", " + ", " + getColor().toIntString() + ")";
	}

}
