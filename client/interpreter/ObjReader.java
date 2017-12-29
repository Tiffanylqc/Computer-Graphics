package client.interpreter;

import java.util.ArrayList;
import java.util.List;

import client.interpreter.SimpInterpreter.RenderStyle;
import geometry.Point3DH;
import geometry.Vertex3D;
import polygon.Polygon;
import windowing.graphics.Color;
import geometry.Transformation;

class ObjReader {
	private static final char COMMENT_CHAR = '#';
	
	private class ObjVertex {
		// TODO: fill this class in.  Store indices for a vertex, a texture, and a normal.  Have getters for them.
		//store the real index starting from 0, store -1 if not existed
		int vertexIndex;
		int textureIndex;
		int normalIndex;

		ObjVertex(int vertexIndex, int textureIndex, int normalIndex){
			this.vertexIndex=vertexIndex;
			this.textureIndex=textureIndex;
			this.normalIndex=normalIndex;
		}
		public int getVertexIndex(){
			return vertexIndex;
		}
		public int getTextureIndex(){
			return textureIndex;
		}
		public int getNormalIndex(){
			return normalIndex;
		}
	}
	private class ObjFace extends ArrayList<ObjVertex> {
		private static final long serialVersionUID = -4130668677651098160L;
	}	
	private LineBasedReader reader;
	
	private List<Vertex3D> objVertices;
	private List<Point3DH> objNormals;
	private List<ObjFace> objFaces;

	private Color defaultColor;
	
	ObjReader(String filename, Color defaultColor) {
		// TODO: Initialize an instance of this class.
		this.defaultColor=defaultColor;

		reader=new LineBasedReader(filename);
		objVertices=new ArrayList<Vertex3D>();
		objNormals=new ArrayList<Point3DH>();
		objFaces=new ArrayList<ObjFace>();
	}

	public void render() {
		// TODO: Implement.  All of the vertices, normals, and faces have been defined.
		// First, transform all of the vertices.		
		// Then, go through each face, break into triangles if necessary, and send each triangle to the renderer.
		// You may need to add arguments to this function, and/or change the visibility of functions in SimpInterpreter.

		int length=objFaces.size();
		for(int i=0;i<length;i++){
			Polygon polygon=polygonForFace(objFaces.get(i));
			Polygon[] polygons=breakPolygon(polygon);
			
			for(int j=0;j<polygons.length;j++){
				SimpInterpreter.interpretPolygon(polygons[j]);
			}
		}
	}
	
	private Polygon[] breakPolygon(Polygon polygon){
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
	
	private Polygon polygonForFace(ObjFace face) {
		// TODO: This function might be used in render() above.  Implement it if you find it handy.
		int length=face.size();
		int vertexIndex,normalIndex;
		Polygon polygon=Polygon.makeEmpty();
		Vertex3D vertex;
		Point3DH normal;
		for(int i=0;i<length;i++){
			vertexIndex=face.get(i).getVertexIndex();
			normalIndex=face.get(i).getNormalIndex();

			vertex=objVertices.get(vertexIndex);
			if(normalIndex!=-1){
				normal=objNormals.get(normalIndex);
				vertex.addNormal(normal);
			}
			polygon.add(vertex);
		}
		return polygon;
	}

	public void read() {
		while(reader.hasNext() ) {
			String line = reader.next().trim();
			interpretObjLine(line);
		}
	}
	private void interpretObjLine(String line) {
		if(!line.isEmpty() && line.charAt(0) != COMMENT_CHAR) {
			String[] tokens = line.split("[ \t,()]+");
			if(tokens.length != 0) {
				interpretObjCommand(tokens);
			}
		}
	}

	private void interpretObjCommand(String[] tokens) {
		switch(tokens[0]) {
		case "v" :
		case "V" :
			interpretObjVertex(tokens);
			break;
		case "vn":
		case "VN":
			interpretObjNormal(tokens);
			break;
		case "f":
		case "F":
			interpretObjFace(tokens);
			break;
		default:	// do nothing
			break;
		}
	}
	private void interpretObjFace(String[] tokens) {
		ObjFace face = new ObjFace();
		
		for(int i = 1; i<tokens.length; i++) {
			String token = tokens[i];
			String[] subtokens = token.split("/");
			
			int vertexIndex  = objIndex(subtokens, 0, objVertices.size());
			int textureIndex = objIndex(subtokens, 1, 0);
			int normalIndex  = objIndex(subtokens, 2, objNormals.size());

			// TODO: fill in action to take here.
			ObjVertex objVertex=new ObjVertex(vertexIndex,textureIndex,normalIndex);
			face.add(objVertex);
		}
		// TODO: fill in action to take here.
		objFaces.add(face);
	}

	private int objIndex(String[] subtokens, int tokenIndex, int baseForNegativeIndices) {
		// TODO: write this.  subtokens[tokenIndex], if it exists, holds a string for an index.
		// use Integer.parseInt() to get the integer value of the index.
		// Be sure to handle both positive and negative indices.
		if(tokenIndex==1)
			return -1;
		if(tokenIndex==2&&subtokens.length<3)
			return -1;
			
		int ObjIndex=Integer.parseInt(subtokens[tokenIndex]);
		if(ObjIndex<0){
			if(baseForNegativeIndices+ObjIndex<0)
				System.out.println("In objReader objIndex, index out of bound");
			return baseForNegativeIndices+ObjIndex;
		}
		else{
			if(ObjIndex-1<0||ObjIndex>baseForNegativeIndices)
				System.out.println("In objReader objIndex, index out of bound");
			return ObjIndex-1;
		}
	}

	private void interpretObjNormal(String[] tokens) {
		int numArgs = tokens.length - 1;
		if(numArgs != 3) {
			throw new BadObjFileException("vertex normal with wrong number of arguments : " + numArgs + ": " + tokens);				
		}
		Point3DH normal = SimpInterpreter.interpretPoint(tokens, 1);
		// TODO: fill in action to take here.
		objNormals.add(normal);
	}
	private void interpretObjVertex(String[] tokens) {
		int numArgs = tokens.length - 1;
		Point3DH point = objVertexPoint(tokens, numArgs);
		Color color = objVertexColor(tokens, numArgs);
		
		// TODO: fill in action to take here.
		objVertices.add(new Vertex3D(point,color));
	}

	private Color objVertexColor(String[] tokens, int numArgs) {
		if(numArgs == 6) {
			return SimpInterpreter.interpretColor(tokens, 4);
		}
		if(numArgs == 7) {
			return SimpInterpreter.interpretColor(tokens, 5);
		}
		return defaultColor;
	}

	private Point3DH objVertexPoint(String[] tokens, int numArgs) {
		if(numArgs == 3 || numArgs == 6) {
			return SimpInterpreter.interpretPoint(tokens, 1);
		}
		else if(numArgs == 4 || numArgs == 7) {
			return SimpInterpreter.interpretPointWithW(tokens, 1);
		}
		throw new BadObjFileException("vertex with wrong number of arguments : " + numArgs + ": " + tokens);
	}
}