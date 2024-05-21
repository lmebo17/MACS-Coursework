import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


public class SudokuFrame extends JFrame {

	public SudokuFrame() {
		super("Sudoku Solver");
		setLayout(new BorderLayout(4 ,4));
		JTextArea sourceTextArea = new JTextArea(15,20);
		JTextArea solutionTextArea = new JTextArea(15, 20);
		sourceTextArea.setBorder(new TitledBorder("Puzzle"));
		solutionTextArea.setBorder(new TitledBorder("Solution"));
		add(sourceTextArea, BorderLayout.WEST);
		add(solutionTextArea, BorderLayout.EAST);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JCheckBox checkBox = new JCheckBox("Auto Check");
		JButton check = new JButton("Check");
		panel.add(check);
		panel.add(checkBox);
		setVisible(true);
		add(panel, BorderLayout.SOUTH);

		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				trySolvingSudoku(sourceTextArea, solutionTextArea);
			}
		});
		sourceTextArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(checkBox.isSelected()) trySolvingSudoku(sourceTextArea, solutionTextArea);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if(checkBox.isSelected()) trySolvingSudoku(sourceTextArea, solutionTextArea);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if(checkBox.isSelected()) trySolvingSudoku(sourceTextArea, solutionTextArea);
			}
		});
		// Could do this:
		// setLocationByPlatform(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}


	private void trySolvingSudoku(JTextArea source, JTextArea solutionTextArea){
		try {
			Sudoku sudoku = new Sudoku(source.getText());
			int count = sudoku.solve();
			solutionTextArea.setText(sudoku.getSolutionText() + "\n" + "Solutions: " + count + "\n" + "elapsed: " + sudoku.getElapsed() + "\n");
			add(solutionTextArea, BorderLayout.EAST);
		} catch (Exception e) {
			solutionTextArea.setText("Parsing problem.\n");
		}
	}


	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }

		SudokuFrame frame = new SudokuFrame();
	}

}
