package client;

import geometry.Point2D;
import line.AlternatingLineRenderer;
import line.LineRenderer;
import windowing.drawable.ColoredDrawable;
import windowing.drawable.ZbufferingDrawable;
import client.RendererTrio;
import line.AntialiasingLineRenderer;
import line.BresenhamLineRenderer;
import line.DDALineRenderer;
import polygon.FilledPolygonRenderer;
import polygon.WireframePolygonRenderer;
import polygon.PolygonRenderer;
import windowing.PageTurner;
import windowing.drawable.Drawable;
import windowing.drawable.GhostWritingDrawable;
import windowing.drawable.InvertedYDrawable;
import windowing.drawable.TranslatingDrawable;
import windowing.drawable.DepthCueingDrawable;
import windowing.graphics.Color;
import windowing.graphics.Dimensions;
import client.interpreter.*;

public class Client implements PageTurner {
	private static final int ARGB_WHITE = 0xff_ff_ff_ff;
	private static final int ARGB_GREEN = 0xff_00_ff_40;
	
	private static final int NUM_PAGES = 16;

	private final Drawable drawable;
	private int pageNumber = 0;
	
	private Drawable image;
	private Drawable fullPanel;
	
	
	private PolygonRenderer polygonRenderer;
	private LineRenderer lineRenderer;
	private WireframePolygonRenderer wireframeRenderer;
	private RendererTrio renderers;
//	private DepthCueingDrawable depthCueingDrawable;
	private ZbufferingDrawable zbufferDrawable;
	
	private SimpInterpreter interpreter;
	
	public Client(Drawable drawable) {
		this.drawable = drawable;	
		createDrawables();
		createRenderers();
	}

	public void createDrawables() {
		image = new InvertedYDrawable(drawable);
		image = new TranslatingDrawable(image, point(0, 0), dimensions(750, 750));
		image = new ColoredDrawable(image, ARGB_WHITE);

		fullPanel = new TranslatingDrawable(image, point(  50, 50),  dimensions(650, 650));
		zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
	}

	private Point2D point(int x, int y) {
		return new Point2D(x, y);
	}	
	private Dimensions dimensions(int x, int y) {
		return new Dimensions(x, y);
	}
	private void createRenderers() {
		lineRenderer=DDALineRenderer.make();
		wireframeRenderer=WireframePolygonRenderer.make(lineRenderer);
		polygonRenderer = FilledPolygonRenderer.make();
		renderers=new RendererTrio();
	}
	
	@Override
	public void nextPage() {
		if(Main.hasArgument) {
			argumentNextPage();
		}
		else {
			noArgumentNextPage();
		}
	}

	private void argumentNextPage() {
		image.clear();
		fullPanel.clear();
		
		interpreter = new SimpInterpreter(Main.filename + ".simp", zbufferDrawable, renderers);
		interpreter.interpret();
	}
	
	public void noArgumentNextPage() {
		System.out.println("PageNumber " + (pageNumber + 1));
		pageNumber = (pageNumber + 1) % NUM_PAGES;
		image.clear();
		fullPanel.clear();
		String filename;
//		pageNumber=0;
		switch(pageNumber) {
		case 1:  filename = "page-a1";
				 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
				 break;
		case 2:  filename = "page-a2";	 
				 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
				 break;
		case 3:	 filename = "page-a3";	 
		     	 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
		     	 break;
		case 4:  filename = "page-b1";	 
				 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
				 break;
		case 5:  filename = "page-b2";	 
				 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
				 break;
		case 6:  filename = "page-b3";	 
				 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
				 break;
		case 7:  filename = "page-c1";	 
				 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
				 break;
		case 8:  filename = "page-c2";	 
				 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
				 break;
		case 9:  filename = "page-c3";	 
				 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
				 break;
		case 10: filename = "page-d";	 
		 		 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
		 		 break;
		case 11: filename = "page-e";	 
		 		 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
		 		 break; 
		case 12: filename = "page-f1";	 
		 		 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
		 		 break;
		case 13: filename = "page-f2";	 
		 		 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
		 		 break;
		case 14: filename = "page-g";	 
		 		 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
		 		 break;
		case 15:  filename = "page-h";	 
		 		 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
		 		 break;
		case 0:  filename = "page-i";	 
				 zbufferDrawable=new ZbufferingDrawable(fullPanel, 750,750);
				 break;

		default: defaultPage();
				 return;
		}
		interpreter = new SimpInterpreter(filename + ".simp", zbufferDrawable, renderers);
		interpreter.interpret();
	}

	@FunctionalInterface
	private interface TestPerformer {
		public void perform(Drawable drawable, LineRenderer renderer);
	}

	private void defaultPage() {
		image.clear();
		fullPanel.fill(ARGB_GREEN, Double.MAX_VALUE);
	}
}
