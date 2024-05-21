import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MetropolisFrame extends JFrame {

    MetropolisTable metropolisTable;
    JButton addButton;
    JButton searchButton;
    JTextField metropolisTextField;
    JTextField continentTextField;
    JTextField populationTextField;
    JComboBox populationOption;
    JComboBox matchOption;


    public MetropolisFrame() throws SQLException, ClassNotFoundException {
        super("Metropolis Frame");
        setLayout(new BorderLayout(4,4));
        setPreferredSize(new Dimension(1000, 600));

        addTextFields();
        addButtons();
        addJTable();
        actionListeners();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void actionListeners(){

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String city = metropolisTextField.getText();
                String continent = continentTextField.getText();
                String population = populationTextField.getText();
                try {
                    metropolisTable.addMetropolis(city, continent, population);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String city = metropolisTextField.getText();
                String continent = continentTextField.getText();
                String population = populationTextField.getText();
                int populationArgs = populationOption.getSelectedIndex();
                int matchOptionArgs = matchOption.getSelectedIndex();
                try {
                    metropolisTable.search(city, continent, population, populationArgs, matchOptionArgs);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void addButtons(){
        JPanel eastPanel = new JPanel();
        eastPanel.setPreferredSize(new Dimension(200, 600));
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.PAGE_AXIS));
        addButton = new JButton("add:");
        addButton.setPreferredSize(new Dimension(100, 30));
        searchButton = new JButton("search:");
        searchButton.setPreferredSize(new Dimension(10, 30));
        eastPanel.add(addButton);
        eastPanel.add(searchButton);
        add(eastPanel, BorderLayout.EAST);
        JPanel southPanel = new JPanel();
        southPanel.setBorder(new TitledBorder("Search Options:"));




        String[] populationArgs = {"Population Larger Than", "Population Smaller Than"};
        populationOption = new JComboBox(populationArgs);
        populationOption.setPreferredSize(new Dimension(180, 30));
        southPanel.add(populationOption);
        String[] matchArgs = {"Exact Match", "Partial Match"};;
        matchOption = new JComboBox(matchArgs);
        matchOption.setPreferredSize(new Dimension(180, 30));
        southPanel.add(matchOption);

        eastPanel.add(southPanel, BorderLayout.SOUTH);
    }

    private void addTextFields(){
        JPanel panel = new JPanel();
        metropolisTextField = new JTextField(15);
        continentTextField = new JTextField(15);
        populationTextField = new JTextField(15);
        panel.add(new JLabel("Metropolis: "));
        panel.add(metropolisTextField);
        panel.add(new JLabel("Continent: "));
        panel.add(continentTextField);
        panel.add(new JLabel("Population: "));
        panel.add(populationTextField);
        add(panel, BorderLayout.NORTH);

    }

    private void addJTable() throws SQLException, ClassNotFoundException {
        metropolisTable = new MetropolisTable("metropolises");
        JTable table = new JTable(metropolisTable);
        JScrollPane panel = new JScrollPane(table);
        add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MetropolisFrame metroFrame = new MetropolisFrame();
    }



}
