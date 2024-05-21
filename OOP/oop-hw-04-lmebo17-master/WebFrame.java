import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class WebFrame extends JFrame {

    private WebFrame currentFrame;
    public int completedCount;
    public int currentThreadCount;
    private String fileName = "links.txt";
    private String name;
    private DefaultTableModel TableModel;
    private JLabel RunningLabel;
    private JLabel CompletedLabel;
    private JLabel ElapsedLabel;

    private JButton SingleThreadFetch;
    private JButton ConcurentFetch;
    private JButton StopButton;

    private JTextField ThreadNumTextField;
    private JProgressBar ProgressBar;

    private ArrayList<String> urls;

    private Launcher ThreadLauncher;


    public WebFrame(String name){
        this.name = name;
        this.currentThreadCount = 0;
        this.completedCount = 0;
        this.currentFrame = this;
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        drawTable();
        drawOptions();
        SetupActionListeners();
    }


    public class Launcher extends Thread{

        private WebFrame frame;
        private int MAX_NUM_WORKERS;
        public Semaphore semaphore;
        private int WORKERS;

        public Launcher(WebFrame frame, int workers){
            this.MAX_NUM_WORKERS = workers;
            this.semaphore = new Semaphore(this.MAX_NUM_WORKERS);
            this.WORKERS = TableModel.getRowCount();
            this.frame = frame;
        }

        public synchronized void changeCurrentThreadCount(String str){
            if(str.equals("PLUS")) currentThreadCount++;
            if(str.equals("MINUS")) currentThreadCount--;
        }

        public synchronized void changeCompletedThreadCount(String str){
            if(str.equals("PLUS")) completedCount++;
            if(str.equals("MINUS")) completedCount--;
        }

        public void updateGUI(long passedTime){
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    RunningLabel.setText("Running:" + currentThreadCount);
                    CompletedLabel.setText("Completed:" + completedCount);
                    ElapsedLabel.setText("Elapsed:" + passedTime);
                    ProgressBar.setValue((int)(completedCount * ((double)ProgressBar.getMaximum() / TableModel.getRowCount())));
                }
            });
        }

        @Override
        public void run(){
            changeCurrentThreadCount("PLUS");
            long StartTime = System.currentTimeMillis();
            updateGUI(0);
            for(int i = 0; i < this.WORKERS; i++){
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    this.interrupt();
                }
                WebWorker worker = new WebWorker(this, this.frame, i, (String) TableModel.getValueAt(i,0));
                worker.start();
                if(isInterrupted()) break;
            }
            try {
                semaphore.acquire(this.MAX_NUM_WORKERS);
            } catch (InterruptedException e) {
                this.interrupt();
                    try {
                        semaphore.acquire(this.MAX_NUM_WORKERS);
                    } catch (InterruptedException ex) {
                    }
            }
            long EndTime = System.currentTimeMillis();
            long passedTime = EndTime - StartTime;
            changeCurrentThreadCount("MINUS");
            updateGUI(passedTime);
            if(currentThreadCount == 0){
                SingleThreadFetch.setEnabled(true);
                ConcurentFetch.setEnabled(true);
                StopButton.setEnabled(false);
            }
        }
    }

    private void SetupActionListeners() {
        SingleThreadFetch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SingleThreadFetch.setEnabled(false);
                ConcurentFetch.setEnabled(false);
                StopButton.setEnabled(true);
                currentThreadCount = 0;
                completedCount = 0;
                ThreadLauncher = new Launcher(currentFrame,1);
                ThreadLauncher.start();
            }
        });

        ConcurentFetch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SingleThreadFetch.setEnabled(false);
                ConcurentFetch.setEnabled(false);
                StopButton.setEnabled(true);
                completedCount = 0;
                currentThreadCount = 0;
                int WorkersNum = Integer.parseInt(ThreadNumTextField.getText());
                ThreadLauncher = new Launcher(currentFrame, WorkersNum);
                ThreadLauncher.start();
            }
        });

        StopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SingleThreadFetch.setEnabled(true);
                ConcurentFetch.setEnabled(true);
                StopButton.setEnabled(false);
                ThreadLauncher.interrupt();
                currentThreadCount = 0;
                ThreadLauncher.updateGUI(0);
            }
        });
    }

    public void updateStatus(String status, int index){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TableModel.setValueAt(status, index, 1);
            }
        });
    }

    private void drawTable(){
        TableModel = new DefaultTableModel(new String[] { "URL", "STATUS"}, 0);
        urls = new ArrayList<>();
        JTable table = new JTable(TableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(600,300));
        JPanel panel = new JPanel();
        panel.add(scrollpane);
        add(panel);
        readFile();
    }

    private void drawOptions(){
        SingleThreadFetch = new JButton("Single Thread Fetch");
        SingleThreadFetch.setMaximumSize(new Dimension(150, 40));
        ConcurentFetch = new JButton("Concurrent Fetch");
        ConcurentFetch.setMaximumSize(new Dimension(150, 40));
        ThreadNumTextField = new JTextField();
        ThreadNumTextField.setMaximumSize(new Dimension(100,40));
        add(SingleThreadFetch);
        add(ConcurentFetch);
        add(ThreadNumTextField);

        RunningLabel = new JLabel("Running:0");
        CompletedLabel = new JLabel("Completed:0");
        ElapsedLabel = new JLabel("Elapsed:0");

        add(RunningLabel);
        add(CompletedLabel);
        add(ElapsedLabel);

        ProgressBar = new JProgressBar();
        add(ProgressBar);

        StopButton = new JButton("Stop");
        StopButton.setEnabled(false);
        add(StopButton);

    }

    private void readFile(){
        try {
            BufferedReader rd = new BufferedReader(new FileReader(fileName));
            while(true){
                String line = rd.readLine();
                if(line == null) {
                    rd.close();
                    return;
                }
                TableModel.addRow(new String[]{line, ""});
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static public void main(String[] args){
        WebFrame frame = new WebFrame("WEB");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


}
