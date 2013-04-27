package project;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MyApplication extends JFrame  {
	private static final long serialVersionUID = 1L;
	private JPanel thePanel;
	private Timer mTimer; 					// Ball moves every time
	private int mInterval = 500; 			// Milliseconds between updates.

	private double alpha = 0.3;  			// randomness in target's walk
	private double sigma = 200.0;  		// ranging error - std. dev,
	private double speed = 50.0;  		// target's moving speed, cm/sec
	private double width = 2000.0, height = 2000.0;
	private int screenWidth = 400, screenHeight = 400;
	
	private Target theTarget;
	private Tracker theTracker;	
	private Point2D.Double  [] anchors = { 	new Point2D.Double (0.0, 0.0), 
			new Point2D.Double (width, 0.0), 
			new Point2D.Double (0.0, height), 
			new Point2D.Double (width, height) };
	
	public MyApplication(String title)
	{
		super(title);
		theTarget = new Target(new Point2D.Double(width, height), speed, sigma, alpha);
//		theTracker = new TrackerLS(theTarget, anchors.length, anchors );
		theTracker = new TrackerNewton(theTarget, anchors.length, anchors );
		makeGUI();
		mTimer = new Timer(mInterval, new TimerListener()); 			// Calls actionPerformed every 500 ms.
	}

	class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			theTarget.move();
			theTracker.calculate();
			thePanel.repaint();
		}
	}
	
	@SuppressWarnings("serial")
	private void makeGUI()
	{
		thePanel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g); 
				theTarget.paintItself(g);
				theTracker.drawItself(g);
			}
		};
		thePanel.setPreferredSize(new Dimension(screenWidth,screenHeight));
		
		JButton startButton = new JButton("Start");
		JButton pauseButton = new JButton("Pause");
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(startButton);
		buttonPanel.add(pauseButton);

		JPanel wholePanel = new JPanel();
		wholePanel.setLayout(new BoxLayout(wholePanel, BoxLayout.Y_AXIS));
		wholePanel.add(buttonPanel);
		wholePanel.add(thePanel);

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mTimer.start(); 	
			}
		});

		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mTimer.stop(); 		
			}
		});
		setContentPane(wholePanel);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MyApplication frame = new MyApplication("Tracking");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setResizable(false);
				frame.setVisible(true);
			}
		});
	}
}