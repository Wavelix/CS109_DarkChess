package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;

public class StartFrame extends JFrame{
   public static ChessGameFrame gameFrame;
    static  ImageIcon welcomeIcon = new ImageIcon("res/sunSet.png");
    static final Image welcome = welcomeIcon.getImage();
    JPanel welcomeBackground = new JPanel(){
        @Serial
        private static final long serialVersionUID = 1L;
        public void paint(Graphics g){
            g.drawImage(welcome,0,0,this.getWidth(),this.getHeight(),null);
        }
    };
    public StartFrame(){
        this.setTitle("Let's Play Dark Chess!");
        this.setLayout(null);
        this.setSize(500, 430);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JButton StartGameButton=new JButton("开始游戏");
        StartGameButton.setFocusPainted(false);
        StartGameButton.setBackground(new Color(146, 196, 184));
        StartGameButton.setFont(new Font("黑体",0,22));
        StartGameButton.setSize(140,45);
        StartGameButton.setLocation(160,290);
        StartGameButton.addActionListener(e -> runMain());
        add(StartGameButton,BorderLayout.CENTER);
        welcomeBackground.setSize(500,400);
        welcomeBackground.setLocation(0,0);
        add(welcomeBackground);
    }
    public void runMain(){
        SwingUtilities.invokeLater(() -> {
            ChessGameFrame mainFrame = new ChessGameFrame(720,720);
            gameFrame=mainFrame;
            mainFrame.setStartFrame(this);
            mainFrame.setVisible(true);
            this.setVisible(false);
        });
    }
}
