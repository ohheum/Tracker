package project;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;


public abstract class Tracker {
	protected Target target;
	protected Point2D.Double  [] anchors;
	protected int nbrAnchors;
	protected Point2D.Double curEstimate;	
	protected List<Point2D.Double> myEstimateRoute = new LinkedList<Point2D.Double>();
	public abstract void calculate();
	
	public Tracker(Target t, int n, Point2D.Double  [] anc)
	{
		target = t;
		if (anc != null )  {
			anchors = anc;
			nbrAnchors = n;
		}
		double sumx = .0, sumy = .0;
		for ( int i=0; i<nbrAnchors; i++) {
			sumx += anchors[i].x;
			sumy += anchors[i].y;
		}
		curEstimate = new Point2D.Double( sumx/(double)nbrAnchors, sumy/(double)nbrAnchors ); 
		myEstimateRoute.add(curEstimate);
	}
	
	private Color mBallColor = Color.green; 			// Color of the ball
	private int cmPerPixel = 5;
	private int mRadius = 5; 									// Radius of the ball
	public void drawItself(Graphics g)
	{
		g.setColor(mBallColor);
		Point2D.Double prev = null;
		for ( Point2D.Double p : myEstimateRoute ) {
			if (prev != null) {
				g.drawLine((int)prev.x/cmPerPixel, (int)prev.y/cmPerPixel, (int)p.x/cmPerPixel, (int)p.y/cmPerPixel);
			}
			prev = p;
		}
		g.fillOval( (int)curEstimate.x/cmPerPixel - mRadius, (int)curEstimate.y/cmPerPixel - mRadius, 2 * mRadius, 2 * mRadius);
	}
}
