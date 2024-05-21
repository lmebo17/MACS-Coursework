// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javafx.scene.paint.Stop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JCount extends JPanel {


	private JButton StartButton;
	private JButton StopButton;
	private JTextField limit;
	private JLabel counter;

	private int SLEEP_TIME = 100;
	private int FREQUENCY = 1000;


	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel panel = new JPanel();
		StartButton = new JButton("START");
		StopButton = new JButton("STOP");
		limit = new JTextField("100000");
		counter = new JLabel("0");
		add(limit);
		add(counter);
		add(StartButton);
		add(StopButton);
		add(Box.createRigidArea(new Dimension(0,40)));
		final Worker[] worker = {new Worker()};
		StartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(worker[0].isAlive()){
					counter.setText("0");
					worker[0].stop();
				}
				worker[0] = new Worker();
				worker[0].start();
			}
		});
		StopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(worker[0].isAlive()){
					counter.setText("0");
					worker[0].stop();
				}
			}
		});
	}

	private class Worker extends Thread{
		@Override
		public void run(){
			int integerLimit = Integer.parseInt(limit.getText());
			for(int i = 1; i <= integerLimit; i++){
				if(i % FREQUENCY == 0){
					try {
						sleep(SLEEP_TIME);
					} catch (InterruptedException e) {
						break;
					}
					int finalI = i;
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							counter.setText(String.valueOf(finalI));
						}
					});
				}
			}
		}

	}
	
	static public void main(String[] args)  {
		// Creates a frame with 4 JCounts in it.
		// (provided)
		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());
		frame.add(new JCount());

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

