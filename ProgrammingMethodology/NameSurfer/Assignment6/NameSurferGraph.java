
/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import acm.util.RandomGenerator;

import java.awt.event.*;
import java.util.*;
import java.awt.*;

@SuppressWarnings("serial")
public class NameSurferGraph extends GCanvas implements NameSurferConstants, ComponentListener {

	private ArrayList<NameSurferEntry> list = new ArrayList<>();
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private Map<String, Color> map = new HashMap<>();
	private String name;
	private GLabel lab;

	/**
	 * Creates a new NameSurferGraph object that displays the data.
	 */
	public NameSurferGraph() {
		addComponentListener(this);

	}

	// this method adds two horizontal lines on the screen.
	private void topAndBottomLines() {
		double bottomLineX = getWidth();
		double bottomLineY = getHeight() - GRAPH_MARGIN_SIZE;
		GLine bottomLine = new GLine(0, bottomLineY, bottomLineX, bottomLineY);
		add(bottomLine);
		double topLineX = getWidth();
		double topLineY = GRAPH_MARGIN_SIZE;
		GLine topLine = new GLine(0, topLineY, topLineX, topLineY);
		add(topLine);

	}

	// this method adds vertical lines on the screen in order to make the difference
	// between decades clear.
	private void verticalLines() {
		int offset = getWidth() / (NDECADES);
		int lineX = 0;
		for (int i = 0; i < NDECADES; i++) {
			int decade = 1900 + i * 10;
			GLabel lab = new GLabel("" + decade);
			lineX = i * offset;
			add(lab, lineX, getHeight() - GRAPH_MARGIN_SIZE);
			GLine line = new GLine(lineX, 0, lineX, getHeight());
			add(line);
		}
	}

	/**
	 * Clears the list of name surfer entries stored inside this class.
	 */
	public void clear() {
		list.clear();
		map.clear();
	}

	/* Method: addEntry(entry) */
	/**
	 * Adds a new NameSurferEntry to the list of entries on the display. Note that
	 * this method does not actually draw the graph, but simply stores the entry;
	 * the graph is drawn by calling update.
	 */
	public void addEntry(NameSurferEntry entry) {
		if (!list.contains(entry) && entry != null) {
			list.add(entry);
		}
	}

	// this method is an actual implementation of graph of each entries .
	// it adds on the screen 10 distinct lines attached to each other and
	// writes on top of it the ranks of each decade.
	private void drawGraph(ArrayList<NameSurferEntry> entries) {
		int offset = getWidth() / (NDECADES);
		for (int j = 0; j < entries.size(); j++) {
			Color color = rgen.nextColor();
			color = checkColor(entries.get(j).getName(), color);
			for (int i = 0; i < NDECADES - 1; i++) {
				name = entries.get(j).getName();
				lab = new GLabel(name + entries.get(j).getRank(i));
				int x = i * offset;
				int y0 = GRAPH_MARGIN_SIZE + entries.get(j).getRank(i) * getHeight() / MAX_RANK;
				int y1 = GRAPH_MARGIN_SIZE + entries.get(j).getRank(i + 1) * getHeight() / MAX_RANK;
				if (entries.get(j).getRank(i) == 0) {
					y0 = getHeight() - GRAPH_MARGIN_SIZE;
					lab.setLabel(name + '*');
				}
				if (entries.get(j).getRank(i + 1) == 0) {
					y1 = getHeight() - GRAPH_MARGIN_SIZE;
				}
				GLine line = new GLine(x, y0, x + offset, y1);
				line.setColor(color);
				lab.setColor(color);
				add(line);
				add(lab, x, y0);
			}
			GLabel lab1 = new GLabel(name + entries.get(j).getRank(NDECADES - 1));
			lab1.setColor(color);
			int labY = GRAPH_MARGIN_SIZE + entries.get(j).getRank(NDECADES - 1) * getHeight() / MAX_RANK;
			if (entries.get(j).getRank(NDECADES - 1) == 0) {
				labY = getHeight() - GRAPH_MARGIN_SIZE;
				lab1.setLabel(name + '*');

			}
			add(lab1, getWidth() - offset, labY);
		}

	}

	// this method checks whether a color was used or not.
	private Color checkColor(String name, Color color) {
		if (!map.containsKey(name)) {
			map.put(name, color);

		} else {
			color = map.get(name);
		}
		return color;
	}

	/**
	 * Updates the display image by deleting all the graphical objects from the
	 * canvas and then reassembling the display according to the list of entries.
	 * Your application must call update after calling either clear or addEntry;
	 * update is also called whenever the size of the canvas changes.
	 */
	public void update() {
		topAndBottomLines();
		verticalLines();
		drawGraph(list);

	}

	// this method is used when the "Clear" button is pressed and clears the graph.
	public void screenRefresh() {
		removeAll();
		clear();
		update();

	}

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		removeAll();
		update();
	}

	public void componentShown(ComponentEvent e) {
	}
}
