package polygon;

import java.util.ArrayList;
import java.util.List;
import geometry.Point3DH;
import geometry.Vertex;
import geometry.Vertex3D;
 
public class Polygon extends Chain {
	private static final int INDEX_STEP_FOR_CLOCKWISE = -1;
	private static final int INDEX_STEP_FOR_COUNTERCLOCKWISE = 1;
	private double specularCoefficient;
	private double specularExponent;
	private Point3DH faceNormal;
	private boolean hasfaceNormal;
	public void addspecCoeExp(double specularCoefficient, double specularExponent){
		this.specularCoefficient=specularCoefficient;
		this.specularExponent=specularExponent;
	}
	public void addfaceNormal(Point3DH normal){
		this.faceNormal=normal;
		hasfaceNormal=true;
	}
	
	public void createfaceNormal(){
		hasfaceNormal=true;
		double x1=this.get(0).getCameraPoint().getX();
		double y1=this.get(0).getCameraPoint().getY();
		double z1=this.get(0).getCameraPoint().getZ();
		
		double x2=this.get(1).getCameraPoint().getX();
		double y2=this.get(1).getCameraPoint().getY();
		double z2=this.get(1).getCameraPoint().getZ();
		
		double x3=this.get(2).getCameraPoint().getX();
		double y3=this.get(2).getCameraPoint().getY();
		double z3=this.get(2).getCameraPoint().getZ();

		double x,y,z;
		x=(y2-y1)*(z3-z1)-(z2-z1)*(y3-y1);
		y=(z2-z1)*(x3-x1)-(x2-x1)*(z3-z1);
		z=(x2-x1)*(y3-y1)-(y2-y1)*(x3-x1);
		this.faceNormal=new Point3DH(x,y,z);
//		System.out.println(this.faceNormal.Normalize());
		this.faceNormal=this.faceNormal.Normalize();
	}
	
	public Point3DH getFaceNormal(){
		return faceNormal;
	}
	
	public Point3DH getCenter(){
		int length=this.length();
		double x=0.0,y=0.0,z=0.0;
		for(int i=0;i<length;i++){
			x+=this.get(i).getCameraPoint().getX();
			y+=this.get(i).getCameraPoint().getY();
			z+=this.get(i).getCameraPoint().getZ();
		}
		x=x/(length*1.0);
		y=y/(length*1.0);
		z=z/(length*1.0);
		return new Point3DH(x,y,z);
	}
	
	private Polygon(Vertex3D... initialVertices) {
		super(initialVertices);
		if(length() < 3) {
			throw new IllegalArgumentException("Not enough vertices to construct a polygon");
		}
	}
	
	// the EmptyMarker is to distinguish this constructor from the one above (when there are no initial vertices).
	private enum EmptyMarker { MARKER; };
	private Polygon(EmptyMarker ignored) {
		super();
	}
	
	public static Polygon makeEmpty() {
		return new Polygon(EmptyMarker.MARKER);
	}

	public static Polygon make(Vertex3D... initialVertices) {
		return new Polygon(initialVertices);
	}
	public static Polygon makeEnsuringClockwise(Vertex3D... initialVertices) {
		if(isClockwise(initialVertices[0], initialVertices[1], initialVertices[2])) {
			return new Polygon(reverseArray(initialVertices));
		}
		return new Polygon(initialVertices);
	}


	public static <V extends Vertex> boolean isClockwise(Vertex3D a, Vertex3D b, Vertex3D c) {
		Vertex3D vector1 = b.subtract(a);
		Vertex3D vector2 = c.subtract(a);
		
		double term1 = vector1.getX() * vector2.getY();
		double term2 = vector2.getX() * vector1.getY();
		double cross = term1 - term2;
		
		return cross < 0;
	}
	
	private static <V extends Vertex> V[] reverseArray(V[] initialVertices) {
		int length = initialVertices.length;
		List<V> newVertices = new ArrayList<V>();
		
		for(int index = 0; index < length; index++) {
			newVertices.add(initialVertices[index]);
		}
		for(int index = 0; index < length; index++) {
			initialVertices[index] = newVertices.get(length - 1 - index);
		}
		return initialVertices;
	}
	
	/** 
	 * The Polygon is a circular Chain and
	 *  the index given will be taken modulo the number
	 *  of vertices in the Chain. 
	 *  
	 * @param index
	 * @return
	 */
	public Vertex3D get(int index) {
		int realIndex = wrapIndex(index);
		return vertices.get(realIndex);
	}
	/**
	 *  Wrap the indices for the list vertices.
	 *  
	 *  @param index any integer
	 *  @return the number n such that n is equivalent 
	 *  to the given index modulo the number of vertices.
	 */
	private int wrapIndex(int index) {
		return ((index % numVertices) + numVertices) % numVertices;
	}

	
	/////////////////////////////////////////////////////////////////////////////////
	//
	// methods for dividing a y-monotone polygon into a left chain and a right chain.

	/**
	 * returns the left-hand chain of the polygon, ordered from top to bottom.
	 */
	public Chain leftChain() {
		return sideChain(INDEX_STEP_FOR_COUNTERCLOCKWISE);
	}
	/**
	 * returns the right-hand chain of the polygon, ordered from top to bottom.
	 */
	public Chain rightChain() {
		return sideChain(INDEX_STEP_FOR_CLOCKWISE);
	}
	private Chain sideChain(int indexStep) {
		int topIndex = topIndex();
		int bottomIndex = bottomIndex();
		
		Chain chain = new Chain();
		for(int index = topIndex; wrapIndex(index) != bottomIndex; index += indexStep) {
			chain.add(get(index));
		}
		chain.add(get(bottomIndex));
		
		return chain;
	}
	
	private int topIndex() {
		int maxIndex = 0;
		double maxY = get(0).getY();
		
		for(int index = 1; index < vertices.size(); index++) {
			if(get(index).getY() > maxY) {
				maxY = get(index).getY();
				maxIndex = index;
			}
		}
		return maxIndex;
	}
	private int bottomIndex() {
		int minIndex = 0;
		double minY = get(0).getY();
		
		for(int index = 1; index < vertices.size(); index++) {
			if(get(index).getY() < minY) {
				minY = get(index).getY();
				minIndex = index;
			}
		}
		return minIndex;
	}
	
	public String toString() {
		return "Polygon[" + super.toString() + "]";
	}
}
