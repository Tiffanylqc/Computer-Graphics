package windowing.drawable;

import java.util.ArrayList;

import geometry.Vertex3D;
import windowing.graphics.*;

public class DepthEffectDrawable extends DrawableDecorator{

	private double near;
	private double far;
	private Color depthColor;
	private Color ambientLight;
	
	private int Height=750;
	private int Width=750;
	private ArrayList<ArrayList<Double>> Z_buffer;
	
	public DepthEffectDrawable(Drawable delegate, double near,double far,Color depthColor, Color ambientLight) {
		super(delegate);
		// TODO Auto-generated constructor stub
		this.near=near;
		this.far=far;
		this.depthColor=depthColor;
		this.ambientLight=ambientLight;
		
		Z_buffer=new ArrayList<>();
		for(int i=0;i<=Height;i++){//create Z_buffer of size (Height+1)*(Width+1)
			ArrayList<Double> array=new ArrayList<Double>();
			for(int j=0;j<=Width;j++){
				array.add(-Double.MAX_VALUE);
			}
			Z_buffer.add(array);
		}
	}
	
	private Color getDepthEffectColor(double csz,Color color){
		Color nearColor=color;
		Color farColor=depthColor;
		double near_r=nearColor.getR();
		double near_g=nearColor.getG();
		double near_b=nearColor.getB();
		double far_r=farColor.getR();
		double far_g=farColor.getG();
		double far_b=farColor.getB();

		if(csz>=near){
			return nearColor;
		}
		else if(csz<=far){
			return farColor;
		}
		else{
			double percentage=(near-csz)/(near-far);
			double r=near_r*(1-percentage)+far_r*percentage;
			double g=near_g*(1-percentage)+far_g*percentage;
			double b=near_b*(1-percentage)+far_b*percentage;
			return new Color(r,g,b);
		}
	}

	
	public void setPixel(int x,int y,double z, int argbColor){
		Color color=Color.fromARGB(argbColor);
		Color depthEffect=getDepthEffectColor(z,color);
		if(x<0||x>750||y<0||y>750)
			return;
		if(Z_buffer.get(y).get(x)<z){
			Z_buffer.get(y).set(x, z);
//			System.out.println(z);
			delegate.setPixel(x, y, z, depthEffect.asARGB());
		}
	}
}
