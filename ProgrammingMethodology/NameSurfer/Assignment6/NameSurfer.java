
/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */


import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class NameSurfer extends Program implements NameSurferConstants {

	private NameSurferDataBase data;
	private NameSurferEntry entry;
	private JButton graph;
	private JButton clear;
	private JLabel lab;
	private JTextField txt;
	private NameSurferGraph graphic;

	public void run() {
		add(graphic); 
	}

	public void init() {
		data = new NameSurferDataBase(NAMES_DATA_FILE);
		graph = new JButton("Graph");
		add(graph, SOUTH);
		clear = new JButton("Clear");
		add(clear, SOUTH);
		txt = new JTextField(10);
		txt.addActionListener(this);
		lab = new JLabel("Name");
		add(lab, SOUTH);
		add(txt, SOUTH);
		addActionListeners();
		graphic = new NameSurferGraph();

	}

	/* Method: actionPerformed(e) */
	/**
	 * This class is responsible for detecting when the buttons are clicked, so you
	 * will have to define a method to respond to button actions.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == txt || e.getActionCommand().equals("Graph")) {
			String name = txt.getText().toUpperCase();
			if (name != null) {
				entry = data.findEntry(name);
				graphic.addEntry(entry);
				graphic.update();
			}
			txt.setText("");
		} else if (e.getActionCommand().equals("Clear")) {
			graphic.screenRefresh();
		}

	}
}
