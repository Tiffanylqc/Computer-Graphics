package windowing.drawable;

public class ColoredDrawable extends DrawableDecorator{
	private int argbColor;
	
	public ColoredDrawable(Drawable delegate, int argbColor){
		super(delegate);
		this.argbColor=argbColor;
	}

	public void clear(){
		fill(argbColor,Double.MAX_VALUE);
	}
}

