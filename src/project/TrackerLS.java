package project;
import java.awt.geom.Point2D;

public class TrackerLS  extends Tracker {
	private double []  lastRange = new double [4];
	public TrackerLS(Target t, int n, Point2D.Double  [] anc)
	{
		super(t, n, anc);
		for ( int i=0; i<nbrAnchors; i++)
			lastRange[i] = target.measureDist(anchors[i]);	
	}
	
	public void calculate()
	{
		double [][] A = new double [nbrAnchors][2];
		double []  range = new double [nbrAnchors];
		
		for ( int i=0; i<nbrAnchors; i++)
			range[i] = target.measureDist(anchors[i]);
		
		for ( int i=0; i<nbrAnchors; i++ ) {
				A[i][0] = (curEstimate.x - anchors[i].x)/Math.sqrt(
						(curEstimate.x - anchors[i].x)*(curEstimate.x - anchors[i].x)
						+ (curEstimate.y - anchors[i].y)*(curEstimate.y - anchors[i].y));
				A[i][1] = (curEstimate.y - anchors[i].y)/Math.sqrt(
						(curEstimate.x - anchors[i].x)*(curEstimate.x - anchors[i].x)
						+ (curEstimate.y - anchors[i].y)*(curEstimate.y - anchors[i].y));			
		}
		
		double [] deltaRho = new double [nbrAnchors];
		for ( int i=0; i<nbrAnchors; i++ )
			deltaRho[i] = range[i] - lastRange[i];
		
		Point2D.Double deltaPos = MyAlgebra.solveLeastSquare( A, deltaRho );
		curEstimate = new Point2D.Double(curEstimate.x + deltaPos.x, curEstimate.y + deltaPos.y);
		myEstimateRoute.add(curEstimate);
		lastRange = range;
	}
}
