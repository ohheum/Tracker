package project;
import java.awt.geom.Point2D;

public class TrackerNewton extends Tracker {

	public TrackerNewton(Target t, int n, Point2D.Double  [] anc)
	{
		super(t, n, anc);
	}

	public void calculate()
	{
		double []  range = new double [nbrAnchors];
		double [] grad = new double [2];
		double [][] hessian = new double[2][2];

		for ( int i=0; i<nbrAnchors; i++)
			range[i] = target.measureDist(anchors[i]);

		double delta = Double.MAX_VALUE;
		double Eps = 1.0;
		do {
			for ( int i=0; i<nbrAnchors; i++ ) {
				double norm = Math.sqrt(sq(curEstimate.x-anchors[i].x) + sq(curEstimate.y-anchors[i].y));
				grad[0] += 2.0 * (norm - range[i]) * ( curEstimate.x-anchors[i].x )/norm;
				grad[1] += 2.0 * (norm - range[i]) * ( curEstimate.y-anchors[i].y )/norm;

				hessian[0][0] += (2.0 - 2.0*range[i]/norm + 2.0*range[i] * sq(curEstimate.x-anchors[i].x )/(norm*norm*norm));
				hessian[0][1] += (2.0*range[i] * (curEstimate.x-anchors[i].x )*( curEstimate.y-anchors[i].y ) /(norm*norm*norm));
				hessian[1][0] += (2.0*range[i] * (curEstimate.x-anchors[i].x )*( curEstimate.y-anchors[i].y ) /(norm*norm*norm));
				hessian[1][1] += (2.0 - 2.0*range[i]/norm + 2.0*range[i] * sq(curEstimate.y-anchors[i].y )/(norm*norm*norm));
			}	
			Point2D.Double newEstimate = MyAlgebra.findNewtonRapson( grad, hessian, curEstimate);
			delta = newEstimate.distance(curEstimate);
			curEstimate = newEstimate;
		} while(delta > Eps);
		myEstimateRoute.add(curEstimate);
	}
	
	private double sq(double a)
	{
		return a*a;
	}
}

