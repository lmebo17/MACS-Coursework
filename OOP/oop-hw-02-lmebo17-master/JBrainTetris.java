import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


public class JBrainTetris extends JTetris{

    private JSlider adversary;
    private JPanel AdversaryPanel;
    private int PieceCounter;
    private int rotationMaxCount;
    private Brain.Move move;
    private JCheckBox brainMode;
    private DefaultBrain brain;
    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    JBrainTetris(int pixels) {
        super(pixels);
        brain = new DefaultBrain();
        move = new Brain.Move();
        rotationMaxCount = 4;
        PieceCounter = 0;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JBrainTetris tetris = new JBrainTetris(16);
        JFrame frame = JBrainTetris.createFrame(tetris);
        frame.setVisible(true);
    }


    @Override
    public Piece pickNextPiece(){
        int rand = random.nextInt(99) + 1;
        if(rand >= adversary.getValue()){
            return super.pickNextPiece();
        }
        return Adversary();
    }

    private Piece Adversary(){
        Piece worstPiece = null;
        double maxScore = 0;
        for (Piece piece : pieces) {
            brain.bestMove(super.board, piece, HEIGHT - TOP_SPACE, move);
            if (maxScore < move.score) {
                worstPiece = piece;
                maxScore = move.score;
            }
        }
        return worstPiece;
    }

    @Override
    public JComponent createControlPanel() {
        JPanel panel = (JPanel) super.createControlPanel();
        JPanel AdversaryPanel = new JPanel();
        AdversaryPanel.add(new JLabel("Adversary"));
        adversary = new JSlider(0, 100, 0);
        adversary.setPreferredSize(new Dimension(100, 15));
        AdversaryPanel.add(adversary);
        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        panel.add(brainMode);
        panel.add(AdversaryPanel);
        return panel;
    }

    @Override
    public void tick(int verb) {
       if(verb == DOWN && brainMode.isSelected()){
           if(PieceCounter < super.count){
               PieceCounter = super.count;
               rotationMaxCount = 4;
               super.board.undo();
               brain.bestMove(super.board, super.currentPiece, HEIGHT - TOP_SPACE, move);
           }
            if(move.x < super.currentX){
                super.tick(LEFT);
            } else if(move.x > super.currentX){
                super.tick(RIGHT);
            } else if(!currentPiece.equals(move.piece) && rotationMaxCount > 0){
                super.tick(ROTATE);
                rotationMaxCount--;
            }
       }
       super.tick(verb);
    }
    @Override
    public void startGame(){
        PieceCounter = 0;
        super.startGame();
    }



}
