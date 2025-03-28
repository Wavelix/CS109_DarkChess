package view;


import chessComponent.*;
import model.*;
import controller.ClickController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 这个类表示棋盘组建，其包含：
 * SquareComponent[][]: 4*8个方块格子组件
 */
public class Chessboard extends JComponent {


    private static final int ROW_SIZE = 8;
    private static final int COL_SIZE = 4;

    public static final SquareComponent[][] squareComponents = new SquareComponent[ROW_SIZE][COL_SIZE];
    //todo: you can change the initial player
    private ChessColor currentColor = ChessColor.BLACK;

    //all chessComponents in this chessboard are shared only one model controller
    public final ClickController clickController = new ClickController(this);
    private final int CHESS_SIZE;
    public static boolean toStop=false;

    int second=35;
    ActionListener timeListener = e -> {

        if(second==0){
            ClickController.first=null;
            clickController.swapPlayer();
            ChessGameFrame.textArea.append("  超时，回合交换\n");
            ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
            second=35;
            ChessGameFrame.countTimeLabel.setForeground(Color.BLACK);
            ChessGameFrame.countTimeLabel.setText("TIMER: "+second+"s");
        }
        if(!toStop){
            second--;
        }else {
            second=35;
        }
        if(ChessGameFrame.isReplay){
            second=35;
            ChessGameFrame.isReplay=false;
        }
        ChessGameFrame.countTimeLabel.setText("TIMER: " + second + "s");
        if(second==5){
            ChessGameFrame.countTimeLabel.setForeground(Color.RED);
        }

    };
    public Timer timer ;


    public Chessboard(int width, int height) {
        setLayout(null); // Use absolute layout.
//        setSize(width + 2, height);
        setSize(288 + 2, 288*2);
//        CHESS_SIZE = (height - 6) / 8;
        CHESS_SIZE = (288*2 - 6) / 8;
        SquareComponent.setSpacingLength(CHESS_SIZE / 12);
//        System.out.printf("chessboard [%d * %d], chess size = %d\n", width, height, CHESS_SIZE);
        System.out.printf("chessboard [%d * %d], chess size = %d\n", 288, 288*2, CHESS_SIZE);
        initAllChessOnBoard();
        if(TotalPoint.theFirstGame){
            timer= new Timer(1000, timeListener);
            timer.start();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        this.setVisible(true);
        //！！！！！！！！！！！！！！！！！！！！！！！！！！
    }

    public void setSecond(int second){
        this.second=second;
    }

    public SquareComponent[][] getChessComponents() {
        return squareComponents;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(ChessColor currentColor) {
        this.currentColor = currentColor;
    }

    /**
     * 将SquareComponent 放置在 ChessBoard上。里面包含移除原有的component及放置新的component
     *
     * @param squareComponent
     */
    public void putChessOnBoard(SquareComponent squareComponent) {
        int row = squareComponent.getChessboardPoint().getX(), col = squareComponent.getChessboardPoint().getY();
        if (squareComponents[row][col] != null) {
            remove(squareComponents[row][col]);
        }
        add(squareComponents[row][col] = squareComponent);
    }


    /**
     * 交换chess1 chess2的位置
     *
     * @param chess1
     * @param chess2
     */
    public void swapChessComponents(SquareComponent chess1, SquareComponent chess2) {
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        if (!(chess2 instanceof EmptySlotComponent)) {
            remove(chess2);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            if (chess2 instanceof GeneralChessComponent) {
                if (chess2.getChessColor().equals(ChessColor.BLACK)) {
                    TotalPoint.redTotalPoint += 30;
                    TotalPoint.blackGeneral += 1;
                    if(!ChessGameFrame.isRegret){
                        ChessGameFrame.textArea.append("  吃帥，红方+30\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                } else {
                    TotalPoint.blackTotalPoint += 30;
                    TotalPoint.redGeneral += 1;
                    if(!ChessGameFrame.isRegret){
                        ChessGameFrame.textArea.append("  吃將，黑方+30\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                }
            }//吃的是帅
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
            if (chess2 instanceof AdvisorChessComponent) {
                if (chess2.getChessColor().equals(ChessColor.BLACK)) {
                    TotalPoint.redTotalPoint += 10;
                    TotalPoint.blackAdvisor += 1;
                    if(!ChessGameFrame.isRegret){
                        ChessGameFrame.textArea.append("  吃士，红方+10\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                } else {
                    TotalPoint.blackTotalPoint += 10;
                    TotalPoint.redAdvisor += 1;
                    if(!ChessGameFrame.isRegret){
                        ChessGameFrame.textArea.append("  吃仕，黑方+10\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                }
            }//吃的是士
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
            if (chess2 instanceof MinisterChessComponent) {
                if (chess2.getChessColor().equals(ChessColor.BLACK)) {
                    TotalPoint.redTotalPoint += 5;
                    TotalPoint.blackMinister += 1;
                    if(!ChessGameFrame.isRegret){
                        ChessGameFrame.textArea.append("  吃象，红方+5\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                } else {
                    TotalPoint.blackTotalPoint += 5;
                    TotalPoint.redMinister += 1;
                    if(!ChessGameFrame.isRegret){
                        ChessGameFrame.textArea.append("  吃相，黑方+5\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                }
            }//吃的是象
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
            if (chess2 instanceof HorseChessComponent) {
                if (chess2.getChessColor().equals(ChessColor.BLACK)) {
                    TotalPoint.redTotalPoint += 5;
                    TotalPoint.blackHorse += 1;
                    if(!ChessGameFrame.isRegret){
                        ChessGameFrame.textArea.append("  吃馬，红方+5\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                } else {
                    TotalPoint.blackTotalPoint += 5;
                    TotalPoint.redHorse += 1;
                    if(!ChessGameFrame.isRegret){
                        ChessGameFrame.textArea.append("  吃傌，黑方+5\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                }
            }//吃的是马
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
            if (chess2 instanceof ChariotChessComponent) {
                if (chess2.getChessColor().equals(ChessColor.BLACK)) {
                    TotalPoint.redTotalPoint += 5;
                    TotalPoint.blackChariot += 1;
                    if(!ChessGameFrame.isRegret){
                        ChessGameFrame.textArea.append("  吃車，红方+5\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                } else {
                    TotalPoint.blackTotalPoint += 5;
                    TotalPoint.redChariot += 1;
                    if(!ChessGameFrame.isRegret){
                        ChessGameFrame.textArea.append("  吃俥，黑方+5\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                }
            }//吃的是车
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
            if (chess2 instanceof CannonChessComponent) {
                if (chess2.getChessColor().equals(ChessColor.BLACK)) {
                    TotalPoint.redTotalPoint += 5;
                    TotalPoint.blackCannon += 1;
                    if(!ChessGameFrame.isRegret){
                        ChessGameFrame.textArea.append("  吃砲，红方+5\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                } else {
                    TotalPoint.blackTotalPoint += 5;
                    TotalPoint.redCannon += 1;
                    if (!ChessGameFrame.isRegret){
                        ChessGameFrame.textArea.append("  吃炮，黑方+5\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                }
            }//吃的是炮
            if (chess2 instanceof SoldierChessComponent) {
                if (chess2.getChessColor().equals(ChessColor.BLACK)) {
                    TotalPoint.redTotalPoint += 1;
                    TotalPoint.blackSoldier += 1;
                    if(!ChessGameFrame.isRegret){
                        ChessGameFrame.textArea.append("  吃卒，红方+1\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                } else {
                    TotalPoint.blackTotalPoint += 1;
                    TotalPoint.redSoldier += 1;
                    if(!ChessGameFrame.isRegret){
                        ChessGameFrame.textArea.append("  吃兵，黑方+1\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                }
            }//吃的是兵
            System.out.printf("红方总分%d\n", TotalPoint.redTotalPoint);
            System.out.printf("黑方总分%d\n", TotalPoint.blackTotalPoint);

            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE));
        }
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        squareComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        squareComponents[row2][col2] = chess2;
        //只重新绘制chess1 chess2，其他不变
        chess1.repaint();
        chess2.repaint();
        ChessGameFrame.setPointsLabel();
        ChessGameFrame.setCalLabel();
        if (TotalPoint.redTotalPoint >= 60 || TotalPoint.blackTotalPoint >= 60) {
            ChessGameFrame.win();
        }
    }

    /*
    以下内容为摆设棋子。
     */
    private final ArrayList<String> theChessOnBoardList = new ArrayList<>();
    public static String theChessOnBoardStr;

    //红1黑2
    private void initAllChessOnBoard() {

        Random random = new Random();
        //打乱数组
        int[] refArr = {1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 6, 6, 6, 7, 7};
        for (int i = 0; i < refArr.length; i++) {
            int first = (int) (Math.random() * refArr.length);
            int second = refArr[first];
            refArr[first] = refArr[i];
            refArr[i] = second;
        }
        for (int i = 0; i < refArr.length; i++) {
            int first = (int) (Math.random() * refArr.length);
            int second = refArr[first];
            refArr[first] = refArr[i];
            refArr[i] = second;
        }
        //1將2士3相4车5馬6卒7炮，方法在此类末尾
        int RedCount = 0, BlackCount = 0;
        for (int i = 0; i < squareComponents.length; i++) {
            for (int j = 0; j < squareComponents[i].length; j++) {
                SquareComponent squareComponent;
                int colorRef = random.nextInt(2);
                if ((colorRef == 0 && RedCount < 16) || (colorRef == 1 && BlackCount >= 16)) {
                    ChessColor color = ChessColor.RED;
                    squareComponent = Check(refArr[RedCount], i, j, color);
                    theChessOnBoardList.add("1" + refArr[RedCount]);
                    RedCount++;
                } else {
                    ChessColor color = ChessColor.BLACK;
                    squareComponent = Check(refArr[BlackCount], i, j, color);
                    theChessOnBoardList.add("2" + refArr[BlackCount]);
                    BlackCount++;
                }
                squareComponent.setVisible(true);
                putChessOnBoard(squareComponent);
            }
        }
        String listToString = theChessOnBoardList.toString();
        theChessOnBoardStr = listToString.replace(" ", "").replaceAll(",", " ").substring(1, 96);

        System.out.println(theChessOnBoardStr);
    }

    //棋子摆设完成

    //load 后重新摆放棋子
    public ChessColor beginColor;
    public void rePutChessOnBoard(String s) {
        ClickController.first=null;
        String[] indexArr = s.split(" ");
        int counter = 0;
        ChessColor color;
//清空本局开始时的数据
        theChessOnBoardList.clear();//***
        for (int i = 0; i < 32; i++) {
            theChessOnBoardList.add(indexArr[i]);
        }
        theChessOnBoardStr = theChessOnBoardList.toString().replace(" ", "").replaceAll(",", " ").substring(1, 96);
//清空结束
        if(String.valueOf(indexArr[0].charAt(0)).equals("1")){
            beginColor=ChessColor.RED;
        }else {
            beginColor=ChessColor.BLACK;
        }
        for (int i = 0; i < squareComponents.length; i++) {
            for (int j = 0; j < squareComponents[i].length; j++) {
                String str = indexArr[counter];
                SquareComponent squareComponent;
                int checkChessName = Integer.parseInt(String.valueOf(str.charAt(1)));
                if (String.valueOf(str.charAt(0)).equals("1")) {
                    color = ChessColor.RED;
                } else {
                    color = ChessColor.BLACK;
                }
                squareComponent = Check(checkChessName, i, j, color);



                putChessOnBoard(squareComponent);
                squareComponent.setVisible(true);
                squareComponent.repaint();
                counter++;
            }
        }
    }
    //重新摆放棋子完成

    //展示保存的步骤
//    public void showSteps(String s)  {
//        String[] indexArr = s.split(" ");
//        for (int k = 32; k + 1 < indexArr.length; k += 2) {
//            try {
//                clickController.onClick(squareComponents[Integer.parseInt(indexArr[k])][Integer.parseInt(indexArr[k + 1])]);
//                squareComponents[Integer.parseInt(indexArr[k])][Integer.parseInt(indexArr[k + 1])].repaint();
//                this.repaint();
//                sleep(1000L);
//            } catch (Exception e) {
//                System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            }
//        }
//    }
    public void showSteps(int i,int j){
        clickController.onClick(squareComponents[i][j]);
    }
    //展示结束


/*

    //load后重新摆放棋子
    public void rePutChessOnBoard(String s){
        String[]indexArr=s.split(" ");
        int newCounter=17;
        ChessColor color;
        for (int i = 0; i < squareComponents.length; i++) {
            for (int j = 0; j < squareComponents[i].length; j++) {
                String referenceStr=indexArr[newCounter];
                SquareComponent squareComponent;
                if(referenceStr.equals("0")){

                } else if (referenceStr.length() == 3) {
                    if(String.valueOf(referenceStr.charAt(0)).equals("1")){
                        color=ChessColor.RED;
                    }else {
                        color=ChessColor.BLACK;
                    }
                    squareComponent=Check(Integer.parseInt(String.valueOf(referenceStr.charAt(1))),i,j,color);
                    squareComponent.setVisible(true);
                    putChessOnBoard(squareComponent);
                    squareComponent.repaint();
                } else if (referenceStr.length() == 2) {
                    if(String.valueOf(referenceStr.charAt(0)).equals("1")){
                        color=ChessColor.RED;
                    }else {
                        color=ChessColor.BLACK;
                    }
                    squareComponent=Check(Integer.parseInt(String.valueOf(referenceStr.charAt(1))),i,j,color);
                    squareComponent.setVisible(true);
                    putChessOnBoard(squareComponent);
                    squareComponent.setReversal(true);
                    squareComponent.repaint();
                }
                newCounter++;
            }
        }
    }
*/

    //清空棋盘
    public void cleanAllChessOnBoard() {
        for (int i = 0; i < squareComponents.length; i++) {
            for (int j = 0; j < squareComponents[i].length; j++) {
                //squareComponents[i][j].setSelected(false);
                remove(squareComponents[i][j]);
                add(squareComponents[i][j] = new EmptySlotComponent(squareComponents[i][j].getChessboardPoint(), squareComponents[i][j].getLocation(), clickController, CHESS_SIZE));
                squareComponents[i][j].repaint();
            }
        }
    }

    public void changeAllChessOnBoard() {
        for (int i = 0; i < squareComponents.length; i++) {
            for (int j = 0; j < squareComponents[i].length; j++) {
                if (!(squareComponents[i][j] instanceof GeneralChessComponent) && squareComponents[i][j].chessColor.equals(ChessColor.RED))
                    remove(squareComponents[i][j]);
                add(squareComponents[i][j] = new GeneralChessComponent(squareComponents[i][j].getChessboardPoint(), squareComponents[i][j].getLocation(), ChessColor.RED, clickController, CHESS_SIZE));
                squareComponents[i][j].repaint();
            }
        }
    }

    /**
     * 绘制棋盘格子
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    /**
     * 将棋盘上行列坐标映射成Swing组件的Point
     *
     * @param row 棋盘上的行
     * @param col 棋盘上的列
     * @return
     */
    private Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE + 3, row * CHESS_SIZE + 3);
    }

    /**
     * 通过GameController调用该方法
     *
     * @param chessData
     */
    public void loadGame(List<String> chessData) {
        chessData.forEach(System.out::println);
    }

    //方法：使序号与棋名对应。
    //在本类摆设棋子时使用。
    public SquareComponent Check(int ref, int i, int j, ChessColor color) {
        return switch (ref) {
            case 1 ->
                    new GeneralChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
            case 2 ->
                    new AdvisorChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
            case 3 ->
                    new MinisterChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
            case 4 ->
                    new ChariotChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
            case 5 ->
                    new HorseChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
            case 6 ->
                    new SoldierChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
            case 7 ->
                    new CannonChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
            default -> throw new IllegalStateException("Unexpected value: " + ref);
        };

    }


}
