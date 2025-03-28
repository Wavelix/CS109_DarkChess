package view;

import chessComponent.*;
import controller.ClickController;
import controller.GameController;
import javazoom.jl.player.Player;
import model.ChessColor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.Scanner;


/**
 * 这个类表示游戏窗体，窗体上包含：
 * 1 Chessboard: 棋盘
 * 2 JLabel:  标签
 * 3 JButton： 按钮
 */
public class ChessGameFrame extends JFrame implements ActionListener, MouseListener  {

    private final int WIDTH;
    private final int HEIGHT;

    public static boolean isReview=false;//load后禁止点击棋盘
    public int CHESSBOARD_SIZE;
    private GameController gameController;

    public static JTextArea textArea = new JTextArea("textArea", 10, 12);

    private static JLabel bGeneral, bAdvisor, bMinister, bChariot, bHorse, bSoldier, bCannon,
            rGeneral, rAdvisor, rMinister, rChariot, rHorse, rSoldier, rCannon;

    //    public static ImageIcon backgroundIcon = new ImageIcon("res/sunSet.png");
    public static ImageIcon backgroundIcon = Theme.sunSet.getBackgroundIcon();
    public static Image backgroundImage = backgroundIcon.getImage();
    JPanel backgroundPanel = new JPanel() {

        private static final long serialVersionUID = 1L;

        public void paint(Graphics g) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    };


    private static JLabel statusLabel;
    //增设得分标签成员12/01
    private static JLabel redPointsLabel;
    private static JLabel blackPointsLabel;

    public static boolean isRegret=false;
    StartFrame startFrame;
    //胜利对话框
    final static JDialog winDialog = new JDialog();
    //增添菜单相关成员变量11/30
    JMenuItem replayItem = new JMenuItem("Replay");
    JMenuItem loadItem = new JMenuItem("Load");
    JMenuItem quitItem = new JMenuItem("Quit");
    JMenuItem crazyItem=new JMenuItem("Crazy");
    JMenuItem sunSetItem = new JMenuItem("sunSet");
    JMenuItem waveItem = new JMenuItem("wave");
    //作弊模式按钮
    JButton cheatButton = new JButton("Cheating Mode");

    JButton showButton=new JButton("next");

    public static ArrayList<ChessGameFrame> chessGameFrames = new ArrayList<>();
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    //int second = 0;//时钟计数

    //JLabel countTimeLabel = new JLabel("TIMER: " + second + "s");
    public static JLabel countTimeLabel = new JLabel("TIMER: " + 35 + "s");



    public ChessGameFrame(int width, int height) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        setTitle("Dark Chess   V1.0"); //设置标题
        this.WIDTH = width;
        this.HEIGHT = height;
        this.CHESSBOARD_SIZE = 720 * 4 / 5;
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        getContentPane().setBackground(Color.WHITE);//设置背景色（考虑图片）
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);
        this.setResizable(false);
        addSaveButton();
        addJPanel();
        addChessboard();//添加棋盘
        addLabel();//添加“当前玩家颜色”
        addJMenuBar();//添加菜单栏
        addCheatButton();//添加作弊按钮
        addShowButton();
        addRegretButton();
        chessGameFrames.add(this);
        MusicButtonChangeListener musicListener = new MusicButtonChangeListener();
        //setName();
        addPointsControlButton();
        addChatButton();
        backgroundPanel.setOpaque(false);
        backgroundPanel.setSize(720, 720);
        backgroundPanel.setLocation(0, 0);
        add(backgroundPanel);
        JCheckBox musicBtn = new JCheckBox("Music");
        musicBtn.addChangeListener(musicListener);
        musicBtn.setSelected(true);
        musicBtn.setSize(50, 50);
        musicBtn.setLocation(540, 90);
        musicBtn.setContentAreaFilled(false);
        add(musicBtn);

    }

    public void setStartFrame(StartFrame startFrame) {
        this.startFrame = startFrame;
    }

    //-----------------------------------------------------------------------------------------文本域（聊天框）搭建
    private void addJPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.LIGHT_GRAY);
        textArea.setEditable(false);//设置不可编辑文字
        textArea.setLineWrap(true);//设置自动换行
        //textArea.setWrapStyleWord(true);//设置断行不断字
        textPanel.setForeground(Color.WHITE);
        textPanel.setFont(new Font("楷体", Font.BOLD, 10));
        textArea.setBackground(new Color(250, 220, 190));
        textArea.setText("  开始游戏\n");
        JScrollPane jsp = new JScrollPane(textArea);///
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);//
        Dimension size = textArea.getPreferredSize();
        textPanel.setBounds(560, 200, size.width + 10, size.height + 5);
        textPanel.add(jsp);
        //textPanel.add(textArea);
        add(textPanel);
        textPanel.setVisible(true);
        //textArea.append("1\n");
    }
//------------------------------------------------------------------------------------------搭建完成


    //--------------------------------------------------------------------------------------初始化菜单栏
    private void addJMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();

        JMenu functionJMenu = new JMenu("Function");
        JMenu themeJMenu = new JMenu("Theme");

        functionJMenu.add(replayItem);
        functionJMenu.add(loadItem);
        functionJMenu.add(crazyItem);
        functionJMenu.add(quitItem);
        themeJMenu.add(sunSetItem);
        themeJMenu.add(waveItem);
        jMenuBar.add(functionJMenu);
        jMenuBar.add(themeJMenu);
        //绑定事件
        replayItem.addActionListener(this);
        loadItem.addActionListener(this);
        quitItem.addActionListener(this);
        crazyItem.addMouseListener(this);
        sunSetItem.addActionListener(e -> {
            backgroundImage = Theme.sunSet.getBackgroundIcon().getImage();
            backgroundPanel.repaint();
        });
        waveItem.addActionListener(e -> {
            backgroundImage = Theme.wave.getBackgroundIcon().getImage();
            backgroundPanel.repaint();
        });
        this.setJMenuBar(jMenuBar);
    }

    /**
     * 在游戏窗体中添加棋盘
     */

    Chessboard chessboard = new Chessboard(CHESSBOARD_SIZE / 2, CHESSBOARD_SIZE);
    private void addChessboard() {
        gameController = new GameController(chessboard);
        chessboard.setLocation(HEIGHT / 6, HEIGHT / 11);

        add(chessboard);
        chessboard.setVisible(true);
        chessboard.repaint();
    }

    /**
     * 在游戏窗体中添加标签
     */
    private void addLabel() {
        countTimeLabel.setForeground(Color.BLACK);
        countTimeLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        countTimeLabel.setSize(120, 60);
        countTimeLabel.setLocation(540, 80);

        add(countTimeLabel);

        //状态标签
        statusLabel = new JLabel("Game Begin!");
        statusLabel.setLocation(WIDTH * 24 / 100, HEIGHT / 70);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("Rockwell", Font.BOLD, 25));
        add(statusLabel);

        //新增得分标签-----------------------------------------------------------
        //红方得分标签
        redPointsLabel = new JLabel("R  00 points");
        redPointsLabel.setForeground(Color.RED);
        redPointsLabel.setLocation(540, HEIGHT / 70);
        redPointsLabel.setSize(150, 40);
        redPointsLabel.setFont(new Font("sans series", Font.BOLD, 15));
        add(redPointsLabel);
        //黑方得分标签
        blackPointsLabel = new JLabel("B  00 points");
        blackPointsLabel.setForeground(Color.BLACK);
        blackPointsLabel.setLocation(540, HEIGHT / 20);
        blackPointsLabel.setSize(150, 40);
        blackPointsLabel.setFont(new Font("sans series", Font.BOLD, 15));
        add(blackPointsLabel);

        //两侧死棋----------------------------------------------------------------------
        final JLabel blackChessLabel = new JLabel();
        final Icon icon1 = new ImageIcon("res/blackChess.png");
        blackChessLabel.setIcon(icon1);
        blackChessLabel.setSize(getPreferredSize());
        blackChessLabel.setBounds(10, 100, 60, 435);
        add(blackChessLabel);
        blackChessLabel.setVisible(true);

        final JLabel redChessLabel = new JLabel();
        final Icon icon2 = new ImageIcon("res/redChess.png");
        redChessLabel.setIcon(icon2);
        redChessLabel.setSize(getPreferredSize());
        redChessLabel.setBounds(460, 100, 60, 435);
        add(redChessLabel);
        redChessLabel.setVisible(true);
        //死棋的统计-----------------------------------------------------------------------
        bGeneral = new JLabel("x0");
        bAdvisor = new JLabel("x0");
        bMinister = new JLabel("x0");
        bChariot = new JLabel("x0");
        bHorse = new JLabel("x0");
        bSoldier = new JLabel("x0");
        bCannon = new JLabel("x0");
        rGeneral = new JLabel("x0");
        rAdvisor = new JLabel("x0");
        rMinister = new JLabel("x0");
        rChariot = new JLabel("x0");
        rHorse = new JLabel("x0");
        rSoldier = new JLabel("x0");
        rCannon = new JLabel("x0");
        bGeneral.setLocation(71, 140);
        bAdvisor.setLocation(71, 200);
        bMinister.setLocation(71, 260);
        bChariot.setLocation(71, 320);
        bHorse.setLocation(71, 380);
        bSoldier.setLocation(71, 440);
        bCannon.setLocation(71, 500);
        rGeneral.setLocation(521, 140);
        rAdvisor.setLocation(521, 200);
        rMinister.setLocation(521, 260);
        rChariot.setLocation(521, 320);
        rHorse.setLocation(521, 380);
        rSoldier.setLocation(521, 440);
        rCannon.setLocation(521, 500);
        JLabel[] calLabels = {bGeneral, bAdvisor, bMinister, bChariot, bHorse, bSoldier, bCannon,
                rGeneral, rAdvisor, rMinister, rChariot, rHorse, rSoldier, rCannon};
        for (int i = 0; i < calLabels.length; i++) {
            calLabels[i].setSize(40, 40);
            calLabels[i].setFont(new Font("sans series", Font.BOLD, 30));
            add(calLabels[i]);
        }
    }

    //-------------------------------------------------------------------------死子统计的setter方法开始
    public static void setCalLabel() {
        bGeneral.setText("x" + TotalPoint.blackGeneral);
        bAdvisor.setText("x" + TotalPoint.blackAdvisor);
        bMinister.setText("x" + TotalPoint.blackMinister);
        bChariot.setText("x" + TotalPoint.blackChariot);
        bHorse.setText("x" + TotalPoint.blackHorse);
        bSoldier.setText("x" + TotalPoint.blackSoldier);
        bCannon.setText("x" + TotalPoint.blackCannon);
        rGeneral.setText("x" + TotalPoint.redGeneral);
        rAdvisor.setText("x" + TotalPoint.redAdvisor);
        rMinister.setText("x" + TotalPoint.redMinister);
        rChariot.setText("x" + TotalPoint.redChariot);
        rHorse.setText("x" + TotalPoint.redHorse);
        rSoldier.setText("x" + TotalPoint.redSoldier);
        rCannon.setText("x" + TotalPoint.redCannon);
    }

    //-------------------------------------------------------------------------死子统计的setter方法结束

    JButton cheatPointButton = new JButton("PointsCenter");


    private void addPointsControlButton() {
        cheatPointButton.setLocation(560, 460);
        cheatPointButton.setSize(130, 40);
        cheatPointButton.setFont(new Font("sans series", Font.BOLD, 16));
        cheatPointButton.setMargin(new Insets(0, 0, 0, 0));
        cheatPointButton.setBackground(new Color(146, 196, 184));
        add(cheatPointButton);
        cheatPointButton.addMouseListener(this);
    }

    ///！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！悔棋功能

    JButton regretButton = new JButton("Regret");
    private void addRegretButton() {
        regretButton.setLocation(560, 520);
      regretButton.setSize(130, 40);
      regretButton.setFont(new Font("sans series", Font.BOLD, 20));
      regretButton.setMargin(new Insets(0, 0, 0, 0));
       regretButton.setBackground(new Color(146, 196, 184));
        add(regretButton);
    regretButton.addMouseListener(this);
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
    JButton chatButton = new JButton("Chat");
    private void addChatButton() {
        chatButton.setLocation(560, 400);
        chatButton.setSize(130, 40);
        chatButton.setFont(new Font("sans series", Font.BOLD, 20));
        chatButton.setMargin(new Insets(0, 0, 0, 0));
        chatButton.setBackground(new Color(146, 196, 184));
        add(chatButton);
        chatButton.addMouseListener(this);
    }

    //！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
    public static void setPointsLabel() {
        redPointsLabel.setText("R  " + TotalPoint.redTotalPoint + " points");
        blackPointsLabel.setText("B  " + TotalPoint.blackTotalPoint + " points");
    }

    public static JLabel getStatusLabel() {
        return statusLabel;
    }

    //-----------------------------------------------------------------------------------------------------------------设置分数
    public void setName() {
        Boolean skip=false;
        String offensiveName;
        String defensiveName;

        offensiveName = JOptionPane.showInputDialog("Enter player's name on the offensive:\n(input 0 to skip)");
        if(offensiveName.equals("0")){
            skip=true;
        }
        if(!skip){
            defensiveName = JOptionPane.showInputDialog("Enter player's name on the defensive:");
//        if (offensiveName != null && defensiveName != null) {
//            while (offensiveName.equals(defensiveName) && !offensiveName.equals("")) {
//                defensiveName = JOptionPane.showInputDialog("Repeated name!Please rewrite 2st player's name:");
//            }
//        }
            if (offensiveName == null || offensiveName.length() == 0)
                offensiveName = "offensive";
            if (defensiveName == null || defensiveName.length() == 0)
                defensiveName = "defensive";
        }
    }

    public void setPoint() {
        String str1, str2;
        str1 = JOptionPane.showInputDialog("Enter BLACK player's point:\n(warning:the sum cannot be more than 60)");
        str2 = JOptionPane.showInputDialog("Enter Red player's point:\n(warning:the sum cannot be more than 60)");
        boolean flag = true;
        try {
            int check1 = Integer.parseInt(str1);
            int check2 = Integer.parseInt(str2);
            if (TotalPoint.redTotalPoint+check1>=60|| TotalPoint.blackTotalPoint+check2>=60) {
                JOptionPane.showMessageDialog(null, "Wrong point");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Wrong point");
            flag = false;
        }
        if (flag&&(TotalPoint.redTotalPoint+Integer.parseInt(str1)<=60&& TotalPoint.blackTotalPoint+Integer.parseInt(str2)<=60)) {
            int a = Integer.parseInt(str1);
            int b = Integer.parseInt(str2);
            TotalPoint.blackTotalPoint += a;
            TotalPoint.redTotalPoint += b;
            ChessGameFrame.setPointsLabel();
        }
    }

    //------------------------------------------------------------------------------------------------------------------聊天功能
    public void chat() {
        String content;
        content = JOptionPane.showInputDialog("输入你想向对方说的话:");

        JOptionPane.showMessageDialog(null, "对面的在大声对你说" + content);
    }
    //------------------------------------------------------------------------------------------------游戏胜利

    public static JDialog getWinDialog() {
        return winDialog;
    }

    public static void win() {
        if (TotalPoint.redTotalPoint >= 60) {
            System.out.println("红方胜利");
        } else {
            System.out.println("黑方胜利");
        }
        winDialog.setSize(220, 150);
        winDialog.setLocationRelativeTo(null);
        winDialog.setLayout(null);
        JButton winReplayButton = new JButton("再来一局");
        JButton winQuitButton = new JButton("退出游戏");
        JLabel label = new JLabel("红方胜利，游戏结束。");
        if (TotalPoint.blackTotalPoint >= 60) {
            label.setText("黑方胜利，游戏结束。");
        }
        label.setFont(new Font("sans series", Font.BOLD, 11));
        label.setBounds(13, 20, 200, 20);
        winReplayButton.setBounds(25, 65, 60, 30);
        winReplayButton.setMargin(new Insets(0, 0, 0, 0));
        winReplayButton.setFocusPainted(false);
        winQuitButton.setBounds(120, 65, 60, 30);
        winQuitButton.setMargin(new Insets(0, 0, 0, 0));
        winQuitButton.setFocusPainted(false);
        winDialog.add(label);
        winDialog.add(winReplayButton);
        winDialog.add(winQuitButton);
        winDialog.setModal(true);

        winQuitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("退出游戏");
                System.exit(0);//关闭虚拟机
            }
        });
        winReplayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("重新游戏");
                ClickController.first=null;
                ClickController.playerCount=0;
                TotalPoint.blackTotalPoint = 0;
                TotalPoint.redTotalPoint = 0;
                TotalPoint.blackGeneral = 0;
                TotalPoint.blackAdvisor = 0;
                TotalPoint.blackMinister = 0;
                TotalPoint.blackChariot = 0;
                TotalPoint.blackHorse = 0;
                TotalPoint.blackSoldier = 0;
                TotalPoint.blackCannon = 0;
                TotalPoint.redGeneral = 0;
                TotalPoint.redAdvisor = 0;
                TotalPoint.redMinister = 0;
                TotalPoint.redChariot = 0;
                TotalPoint.redHorse = 0;
                TotalPoint.redSoldier = 0;
                TotalPoint.redCannon = 0;
                getWinDialog().dispose();
//                view.startFrame.getGameFrame().dispose();
                chessGameFrames.get(chessGameFrames.size() - 1).dispose();
                SwingUtilities.invokeLater(() -> {
                    ChessGameFrame mainFrame = new ChessGameFrame(720, 720);
                    mainFrame.setVisible(true);
                });
            }
        });
        winDialog.setVisible(true);
    }


    //-----------------------------------------------------------------------------------------------作弊模式按钮
    //-----------------------------------------------------------------------------------------------作弊模式按钮(已修改)
    private void addCheatButton() {

        cheatButton.setLocation(540, 600);
        cheatButton.setSize(150, 40);
        cheatButton.setFont(new Font("Rockwell", Font.BOLD, 15));
        cheatButton.setMargin(new Insets(0, 0, 0, 0));
        cheatButton.setBackground(new Color(240, 117, 92));
        add(cheatButton);
        cheatButton.addMouseListener(this);
    }
    private void addShowButton(){
        showButton.setLocation(450,600);
        showButton.setSize(60,40);
        showButton.setBackground(Color.green);
        showButton.setFont(new Font("sans series", Font.PLAIN, 15));
        showButton.setMargin(new Insets(0, 0, 0, 0));
        showButton.setVisible(false);
        showButton.addActionListener(this);
        add(showButton);
    }


    //-------------------------------------------------------------------------------------------------实现菜单功能
    public static String s;
    public static boolean isReplay=false;
    @Override
    public void actionPerformed(ActionEvent e) {
        //重新游戏-->需要多次调整代码(当前玩家，得分清零，记录清零等)
        if (e.getSource() == replayItem) {
            System.out.println("click on replayItem");
            final JDialog replayDialog = new JDialog(this, "重新游戏提醒");//对话框设计
            replayDialog.setSize(220, 150);
            replayDialog.setLocationRelativeTo(null);
            replayDialog.setLayout(null);
            JButton OKButton = new JButton("确定");
            JButton cancelButton = new JButton("再玩会儿");
            final JLabel label = new JLabel("游戏尚未结束，您确定重新开始吗？");
            label.setFont(new Font("sans series", Font.BOLD, 11));
            label.setBounds(13, 20, 200, 20);
            OKButton.setBounds(25, 65, 40, 30);
            OKButton.setMargin(new Insets(0, 0, 0, 0));
            OKButton.setFocusPainted(false);
            cancelButton.setBounds(120, 65, 60, 30);
            cancelButton.setMargin(new Insets(0, 0, 0, 0));
            cancelButton.setFocusPainted(false);
            replayDialog.add(label);
            replayDialog.add(OKButton);
            replayDialog.add(cancelButton);
            replayDialog.setModal(true);
            OKButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("重新游戏");
                    TotalPoint.theFirstGame=false;
                    ClickController.first=null;
                    ClickController.playerCount=0;
                    TotalPoint.blackTotalPoint = 0;
                    TotalPoint.redTotalPoint = 0;
                    TotalPoint.blackGeneral = 0;
                    TotalPoint.blackAdvisor = 0;
                    TotalPoint.blackMinister = 0;
                    TotalPoint.blackChariot = 0;
                    TotalPoint.blackHorse = 0;
                    TotalPoint.blackSoldier = 0;
                    TotalPoint.blackCannon = 0;
                    TotalPoint.redGeneral = 0;
                    TotalPoint.redAdvisor = 0;
                    TotalPoint.redMinister = 0;
                    TotalPoint.redChariot = 0;
                    TotalPoint.redHorse = 0;
                    TotalPoint.redSoldier = 0;
                    TotalPoint.redCannon = 0;
                    ChessGameFrame.super.dispose();
                    MemoryList.reviewList.clear();
                    showCount=32;
                    isReview=false;
                    SwingUtilities.invokeLater(() -> {
                        ChessGameFrame mainFrame = new ChessGameFrame(720, 720);
                        mainFrame.setVisible(true);
                    });
                    isReplay=true;
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    replayDialog.dispose();
                }
            });
            replayDialog.setVisible(true);
            //下载
        } else if (e.getSource() == loadItem) {
            //-----------------------------------------------------------------------------------------load功能开始
            if(!isReview){
                System.out.println("click on loadItem");
                ChessGameFrame.textArea.append("  click loadItem\n");
                ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                //String s = null;
                Boolean isSuccess = false;

                JFileChooser jfc = new JFileChooser(new File("date"));
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.showDialog(new JLabel(), "选择");
                File file = jfc.getSelectedFile();
                BufferedReader reader = null;
                try {
                    if (file != null) {
                        if (!file.getName().endsWith(".txt")) {
                            JOptionPane.showMessageDialog(null, "Wrong:101 Invalid File Format");
                        } else {
                            Scanner in = new Scanner(new FileReader(file));
                            s = in.nextLine();
                            System.out.println(s);
                            isSuccess = true;
                            in.close();
                            String[] index = s.split(" ");
                            int a=0;
                            if(index.length<33){
                                JOptionPane.showMessageDialog(null, "Wrong:105 Invalid game");
                                isSuccess=false;
                            }
                            else if (Integer.parseInt(index[31]) < 10 || Integer.parseInt(index[32]) > 10) {
                                JOptionPane.showMessageDialog(null, "Wrong:102 Invalid Chessboard");
                                isSuccess = false;
                            } else {
                                for (int i = 0; i < 32; i++) {
                                    if (Integer.parseInt(index[i]) > 29) {
                                        JOptionPane.showMessageDialog(null, "Wrong:103 Invalid color chess");
                                        isSuccess = false;
                                        a=1;
                                        break;
                                    }
                                }
                                if(a==0){
                                    int totlol = 0;
                                    for (int i = 0; i < 32; i++) {
                                        totlol += Integer.parseInt(index[i]);
                                        System.out.println(totlol);
                                    }
                                    if (totlol != 626) {
                                        JOptionPane.showMessageDialog(null, "Wrong:103 Invalid number of chess");
                                        isSuccess = false;
                                    } else {
                                        for (int i = 32; i < index.length; i++) {
                                            if (Integer.parseInt(index[i])>8) {
                                                JOptionPane.showMessageDialog(null, "Wrong:105 Invalid play");
                                                isSuccess=false;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Wrong:106 Unexpected Input File");
                    ex.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (isSuccess) {
                    ClickController.first=null;
                    ChessGameFrame.textArea.append("  导入成功\n");
                    ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    //清空棋盘
                    MemoryList.reviewList.clear();
                    chessboard.cleanAllChessOnBoard();
                    //分解字符串
                    String[] index = s.split(" ");
                    //状态栏设置
                    ChessGameFrame.getStatusLabel().setText("GAME REVIEW");
                    ChessGameFrame.getStatusLabel().setForeground(new Color(16, 80, 39));
                    //黑红双方总分
                    TotalPoint.blackTotalPoint=0;
                    TotalPoint.redTotalPoint=0;
                    ChessGameFrame.setPointsLabel();
                    //双方吃子个数统计
                    TotalPoint.blackGeneral =0;
                    TotalPoint.blackAdvisor =0;
                    TotalPoint.blackMinister =0;
                    TotalPoint.blackChariot =0;
                    TotalPoint.blackHorse =0;
                    TotalPoint.blackSoldier =0;
                    TotalPoint.blackCannon =0;
                    TotalPoint.redGeneral =0;
                    TotalPoint.redAdvisor =0;
                    TotalPoint.redMinister =0;
                    TotalPoint.redChariot =0;
                    TotalPoint.redHorse =0;
                    TotalPoint.redSoldier =0;
                    TotalPoint.redCannon =0;
                    ChessGameFrame.setCalLabel();
                    //回归原始棋盘

                    chessboard.rePutChessOnBoard(s);
                    //chessboard.setCurrentColor(chessboard.beginColor);
                    chessboard.setCurrentColor(ChessColor.NONE);
                    ClickController.playerCount=0;
                    System.out.println(chessboard.getCurrentColor());
                    isReview=true;
                    Chessboard.toStop=true;
                    showButton.setVisible(true);

                    showCount=32;
            }else {
                    ChessGameFrame.textArea.append("  导入失败\n");
                    ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                }

            }else {
                ChessGameFrame.textArea.append("  load功能已被禁\n");
                ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
            }
            //退出游戏
        } else if (e.getSource() == quitItem) {
            System.out.println("click on quitItem");
            final JDialog quitDialog = new JDialog(this, "游戏退出提醒");//对话框设计
            quitDialog.setSize(220, 150);
            quitDialog.setLocationRelativeTo(null);
            quitDialog.setLayout(null);
            JButton OKButton = new JButton("确定");
            JButton cancelButton = new JButton("再玩会儿");
            final JLabel label = new JLabel("游戏尚未结束，您确定要退出吗？");
            label.setFont(new Font("sans series", Font.BOLD, 11));
            label.setBounds(13, 20, 200, 20);
            OKButton.setBounds(25, 65, 40, 30);
            OKButton.setMargin(new Insets(0, 0, 0, 0));
            OKButton.setFocusPainted(false);
            cancelButton.setBounds(120, 65, 60, 30);
            cancelButton.setMargin(new Insets(0, 0, 0, 0));
            cancelButton.setFocusPainted(false);
            quitDialog.add(label);
            quitDialog.add(OKButton);
            quitDialog.add(cancelButton);
            quitDialog.setModal(true);
            OKButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("退出游戏");
                    System.exit(0);//关闭虚拟机
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    quitDialog.dispose();
                }
            });
            quitDialog.setVisible(true);
        } else if (e.getSource() == showButton) {

            String[] indexArr=s.split(" ");
            if(showCount+1<indexArr.length){
                System.out.println("here");
//                if(showCount==32){
//                    ClickController.first=null;
//                }
                chessboard.showSteps(Integer.parseInt(indexArr[showCount]), Integer.parseInt(indexArr[showCount+1]));
                showCount+=2;
            }
            if(showCount+1>=indexArr.length){
                showButton.setVisible(false);
                showButton.repaint();
                isReview=false;
                Chessboard.toStop=false;
                ChessGameFrame.textArea.append("  回放结束，请继续游戏\n");
                ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
            }
        }
    }
private int showCount=32;
    //实现MouseListener的方法
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == cheatButton) {
            gameController.cheatingBegin();
        }
        if (e.getSource() == cheatPointButton) {
            setPoint();
        }
        if (e.getSource() == crazyItem) {
            if(!isReview){
                chessboard.changeAllChessOnBoard();
            }else {
                ChessGameFrame.textArea.append("  crazy 键已被禁\n");
                ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
            }
        }
        if (e.getSource() == chatButton) {
            chat();
        }
    }
//____________________________________________________________________________________________________________-
    private LinkedList<Step>reviewList1;
    public void regretChess() {
        ChessGameFrame.isRegret=true;
        chessboard.cleanAllChessOnBoard();
        ChessGameFrame.getStatusLabel().setText("GAME REVIEW");
        ChessGameFrame.getStatusLabel().setForeground(new Color(16, 80, 39));
        TotalPoint.blackTotalPoint = 0;
        TotalPoint.redTotalPoint = 0;
        ChessGameFrame.setPointsLabel();
        TotalPoint.blackGeneral = 0;
        TotalPoint.blackAdvisor = 0;
        TotalPoint.blackMinister = 0;
        TotalPoint.blackChariot = 0;
        TotalPoint.blackHorse = 0;
        TotalPoint.blackSoldier = 0;
        TotalPoint.blackCannon = 0;
        TotalPoint.redGeneral = 0;
        TotalPoint.redAdvisor = 0;
        TotalPoint.redMinister = 0;
        TotalPoint.redChariot = 0;
        TotalPoint.redHorse = 0;
        TotalPoint.redSoldier = 0;
        TotalPoint.redCannon = 0;
        ChessGameFrame.setCalLabel();
        chessboard.rePutChessOnBoard(Chessboard.theChessOnBoardStr);
        chessboard.setCurrentColor(ChessColor.NONE);
        ClickController.playerCount = 0;
        Color nowColor=chessboard.getCurrentColor().getColor();
        LinkedList<Step> reviewList1 = new LinkedList<>(MemoryList.reviewList);

        if (reviewList1.size() == 0) {
            JOptionPane.showMessageDialog(null, "你已经退无可退了");
        } else {
                reviewList1.pollLast();
                //System.out.println(reviewList1.size());
                MemoryList.reviewList.clear();
                for (int i = 0; i < reviewList1.size(); i++) {
                    chessboard.showSteps(reviewList1.get(i).x, reviewList1.get(i).y);
                    repaint();
                }

            ChessGameFrame.isRegret=false;
            ChessGameFrame.textArea.append(" 悔棋成功，请继续游戏\n");
            ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
        }
    }
    //————————————————————————————————————————————————————————————————————————————————————————————————————————————————--
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == cheatButton) {
            gameController.cheatingFinish();
        }
        if (e.getSource() == cheatPointButton) {
            System.out.println("结束了");
        }
        if (e.getSource() == crazyItem) {
            System.out.println("爽到没");
        }
        if(e.getSource()==regretButton){
            regretChess();
        }
    }


    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    //------------------------------------------------------------------------------------------------------背景音乐

    MusicButtonChangeListener musicListener = new MusicButtonChangeListener();

    class MusicButtonChangeListener implements ChangeListener {
        public static PlayThread playThread = null;

        public static void setPlayThread(PlayThread playThread) {
            MusicButtonChangeListener.playThread = playThread;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            checkBox.setText("音乐");
            checkBox.setFont(new Font("sans series", Font.PLAIN, 15));
            checkBox.setSize(80, 80);
            checkBox.setMargin(new Insets(0, 0, 0, 0));
            if (checkBox.isSelected()) {
                if (playThread == null) {
                    System.out.println("played");
                    playThread = new PlayThread();
                    playThread.start();
                }
            } else {
                if (playThread != null) {
                    System.out.println("stopped");
                    playThread.stop();
                    playThread = null;
                }
            }
        }
    }

    class PlayThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    File file = new File("res/music.mp3");
                    //            System.out.println(file.getAbsolutePath());
                    Player player = new Player(new FileInputStream(file));
                    player.play();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


    public void addSaveButton() {
        JButton saveGameBtn = new JButton("Save");
        saveGameBtn.setSize(100, 40);
        saveGameBtn.setFocusPainted(false);
        saveGameBtn.setBackground(new Color(250, 220, 190));
        saveGameBtn.setLocation(10, 10);
        add(saveGameBtn);
        saveGameBtn.addActionListener(e -> {
            System.out.println("clicked Save Btn");
            Calendar cal = Calendar.getInstance();
            String nowDate;
            String nowTime;
            int nowYear = cal.get(Calendar.YEAR);
            int nowMonth = cal.get(Calendar.MONTH) + 1;
            int nowDay = cal.get(Calendar.DAY_OF_MONTH);
            int nowHour = cal.get(Calendar.HOUR_OF_DAY);
            int nowMinute = cal.get(Calendar.MINUTE);
            nowDate = nowYear + "." + nowMonth + "." + nowDay + "_";
            nowTime = "" + nowHour + nowMinute;
            JFileChooser jfc = new JFileChooser(new File("date"));
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setSelectedFile(new File(nowDate + nowTime + ".txt"));
            jfc.showDialog(new JLabel(), "选择");
            File file = jfc.getSelectedFile();
            if (!file.getName().endsWith(".txt"))
                file = new File(file.getAbsolutePath() + ".txt");
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(Chessboard.theChessOnBoardStr);
                for (int i = 0; i < MemoryList.reviewList.size(); i++) {
                    writer.write(String.format(" %d %d", MemoryList.reviewList.get(i).x, MemoryList.reviewList.get(i).y));
                    writer.flush();
                }
                writer.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close(); //打开的Reader或Writer必须关闭
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}

