package windowing.drawable;

import java.util.ArrayList;

import windowing.drawable.Drawable;
import windowing.graphics.Color;

public class DepthCueingDrawable extends DrawableDecorator{
	private Color FrontColor;
	private Color BackColor=Color.BLACK;
	private int FrontZ;
	private int BackZ;
	
	private int Height=650;
	private int Width=650;
	private ArrayList<ArrayList<Double>> Z_buffer=new ArrayList<>();
	
	public DepthCueingDrawable(Drawable delegate,int FrontZ, int BackZ, Color FrontColor) {
		super(delegate);
		this.FrontColor=FrontColor;
		this.FrontZ=FrontZ;
		this.BackZ=BackZ;
		
		for(int i=0;i<=Height;i++){//create Z_buffer of size (Height+1)*(Width+1)
			ArrayList<Double> array=new ArrayList<Double>();
			for(int j=0;j<=Width;j++){
				array.add(-Double.MAX_VALUE);
			}
			Z_buffer.add(array);
		}
	}
	
	public void setPixel(int x,int y,double z, int argbColor){
//		if(x<0||x>650||y<0||y>650||z<-200||z>0){//pixel clipping
//			return;
//		}
		double front_r=FrontColor.getR();
		double front_g=FrontColor.getG();
		double front_b=FrontColor.getB();
		double back_r=BackColor.getR();
		double back_g=BackColor.getG();
		double back_b=BackColor.getB();
		
		double slope=(z-BackZ)/((FrontZ-BackZ)*1.0);
		
		double r=front_r*slope+back_r*(1-slope);
		double g=front_g*slope+back_g*(1-slope);
		double b=front_b*slope+back_b*(1-slope);
		Color color=new Color(r,g,b);
		
		if(Z_buffer.get(y).get(x)<z){
			Z_buffer.get(y).set(x, z);
			delegate.setPixel(x, y, z, color.asARGB());
		}
	}
	
}
