package project;
import java.awt.geom.Point2D;
import java.util.Random;

import org.ejml.simple.SimpleMatrix;

public class MyAlgebra {	
	public static Point2D.Double solveLeastSquare( double [][] A, double []  deltaRho )
	{
		SimpleMatrix B = new SimpleMatrix(A);
		SimpleMatrix Y = new SimpleMatrix(deltaRho.length, 1, true, deltaRho);
		SimpleMatrix BTB = B.transpose().mult(B);
		SimpleMatrix inverseBTB = BTB.invert();
		SimpleMatrix pseudoInverseB = inverseBTB.mult(B.transpose());
		SimpleMatrix ms = pseudoInverseB.mult(Y);
		return new Point2D.Double(ms.get(0, 0), ms.get(1,0));
	}
	
	public static Point2D.Double  getRandomUnitVectorAound( Point2D.Double  p, double alpha, Random rnd )
	{
		double rx = rnd.nextDouble();
		double ry = Math.sqrt(1-rx*rx);
		
		rx = (rnd.nextInt(2)%2==0 ? rx : -1.0*rx);
		ry = (rnd.nextInt(2)%2==0 ? ry : -1.0*ry);
		
		double newx = p.x *(1.0-alpha)  + alpha * rx;
		double newy = p.y * (1.0-alpha)  + alpha * ry;
		double Delta = Math.sqrt((double)(newx*newx + newy*newy));

		return new Point2D.Double(newx/Delta, newy/Delta);
	}
	
	public static Point2D.Double  findNewtonRapson( double [] grad, double [][] hessian, Point2D.Double lastEstimate)
	{
		SimpleMatrix H = new SimpleMatrix(hessian);
		SimpleMatrix G = new SimpleMatrix(2, 1, true, grad);
		SimpleMatrix tmp = H.invert().mult(G);
		
		return new Point2D.Double( lastEstimate.x-tmp.get(0,0), lastEstimate.y-tmp.get(1,0));
	}
}
