package project;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Target {
	private Point2D.Double  curPos;							// current position
	private Point2D.Double  curDir;								// current moving direction
	private List<Point2D.Double > trajectory = new LinkedList<Point2D.Double >();
    private double speed;  								
    private Random rnd = new Random();
    private double sigma;   							
    private double alpha;
    
	/**
	 * @param p the size of the playground
	 * @param speed the moving speed
	 * @param sigma standard deviation of range measurement error
	 * @param alpha the amount of randomness in moving path
	 */
	public Target(Point2D.Double  p, double speed, double sigma, double alpha)
	{
		curPos = new Point2D.Double( rnd.nextDouble()*p.x, rnd.nextDouble()*p.y);		// random point within playground
		curDir =  new Point2D.Double (1.0, 0.0);															// east
		trajectory.add(curPos);																					// keep track of the trajectory
		this.speed = speed;
		this.sigma = sigma;
		this.alpha = alpha;
	}

	public void move( )
	{
		curDir = MyAlgebra.getRandomUnitVectorAound(curDir, alpha, rnd);					// select a direction randomly
		Point2D.Double nextPos = new Point2D.Double(curPos.x + curDir.x*speed, curPos.y+curDir.y*speed);		// compute next position
		if (nextPos.x < 0 || nextPos.x > 2000.0) {															// if hit east or west boundary, then bounce.
			curDir = new Point2D.Double(-curDir.x, curDir.y);
			nextPos = new Point2D.Double(curPos.x + curDir.x*speed, curPos.y+curDir.y*speed);
		}
		else if  (nextPos.y < 0 || nextPos.y > 2000.0) {													// if hit upper or lower boundary, then bounce.
			curDir = new Point2D.Double(curDir.x, -curDir.y);
			nextPos = new Point2D.Double(curPos.x + curDir.x*speed, curPos.y+curDir.y*speed);
		}
		curPos = nextPos;
		trajectory.add(curPos);
	}

	public double measureDist(Point2D.Double  anchor)
	{
		double dist = anchor.distance(curPos);				// true distance from anchor 
		return dist + rnd.nextGaussian()*sigma;			// add random error
	}

	private int mRadius = 5; 								// Radius of the ball
	private Color mBallColor = Color.red; 			// Color of the ball
    private int cmPerPixel = 5;							// cm per a single pixel
	public void paintItself(Graphics g)
	{
		g.setColor(mBallColor);
		Point2D.Double prev = null;
		for ( Point2D.Double p : trajectory ) {
			if (prev != null) {
				g.drawLine((int)prev.x/cmPerPixel, (int)prev.y/cmPerPixel, (int)p.x/cmPerPixel, (int)p.y/cmPerPixel);
			}
			prev = p;
		}
		g.fillOval( (int)curPos.x/cmPerPixel - mRadius, (int)curPos.y/cmPerPixel - mRadius, 2 * mRadius, 2 * mRadius);
	}
}
