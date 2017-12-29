package windowing.drawable;
import java.util.*;

public class ZbufferingDrawable extends DrawableDecorator{
	private int Height=650;
	private int Width=650;
	private ArrayList<ArrayList<Double>> Z_buffer=new ArrayList<>();
	
	public ZbufferingDrawable(Drawable delegate, int Height, int Width) {
		super(delegate);
		this.Height=Height;
		this.Width=Width;
		
		for(int i=0;i<=Height;i++){//create Z_buffer of size (Height+1)*(Width+1)
			ArrayList<Double> array=new ArrayList<Double>();
			for(int j=0;j<=Width;j++){
				array.add(-Double.MAX_VALUE);
			}
			Z_buffer.add(array);
		}
	}

	public void setPixel(int x, int y,double z,int argbColor){
		if(Z_buffer.get(y).get(x)<z){
			delegate.setPixel(x, y, z, argbColor);
			Z_buffer.get(y).set(x, z);
		}	
	}
}
