package controller;

import view.Music;

import chessComponent.CannonChessComponent;
import chessComponent.SquareComponent;
import chessComponent.EmptySlotComponent;
import model.ChessColor;
import view.ChessGameFrame;
import view.Chessboard;
import view.Step;
import view.MemoryList;
import java.awt.*;

public class ClickController {

    private final Chessboard chessboard;
    public static SquareComponent first;

    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }

    public void onClick(SquareComponent squareComponent) {

        if (first == null) {
            if (handleFirst(squareComponent)) {
                squareComponent.setSelected(true);
                first = squareComponent;
                //***
//                Step step=new Step();
//                step.x=first.getChessboardPoint().getX();
//                step.y=first.getChessboardPoint().getY();
//                list.reviewList.add(step);
//                Step step=new Step();
//                step.x=first.getChessboardPoint().getX();
//                step.y=first.getChessboardPoint().getY();
//                list.huiqiList.add(step);
//              System.out.println(list.huiqiList);
//              System.out.println("frist");
                first.repaint();
                //以下内容：当选定棋子后会提示可以走的格子
                SquareComponent.setHint(true);
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (first.canMoveTo(chessboard.getChessComponents(), chessboard.getChessComponents()[i][j].getChessboardPoint())
                                && chessboard.getChessComponents()[i][j].getChessColor() != chessboard.getCurrentColor()&&chessboard.getChessComponents()[i][j].isReversal()) {
                            chessboard.getChessComponents()[i][j].setSquareColor(new Color(222, 176, 131));
                            chessboard.getChessComponents()[i][j].repaint();
                        }else if(first instanceof CannonChessComponent
                                &&first.canMoveTo(chessboard.getChessComponents(), chessboard.getChessComponents()[i][j].getChessboardPoint())){
                            chessboard.getChessComponents()[i][j].setSquareColor(new Color(222, 176, 131));
                            chessboard.getChessComponents()[i][j].repaint();
                        }
                    }
                }
                SquareComponent.setHint(false);
                //结束操作
            }
            Music.Click();

        } else {
            if (first == squareComponent) { // 再次点击取消选取
                squareComponent.setSelected(false);

                SquareComponent recordFirst = first;
                //***
//                Step step=new Step();
//                step.x=first.getChessboardPoint().getX();
//                step.y=first.getChessboardPoint().getY();
//                list.reviewList.add(step);
//                Step step=new Step();
//                step.x=first.getChessboardPoint().getX();
//                step.y=first.getChessboardPoint().getY();
//                list.huiqiList.add(step);
//                System.out.println(list.huiqiList);
                first = null;
                recordFirst.repaint();
                //解除提示色
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 4; j++) {
                        chessboard.getChessComponents()[i][j].setSquareColor(new Color(250, 220, 190));
                        chessboard.getChessComponents()[i][j].repaint();
                    }
                }
                //完成
                Music.Click();
            }//！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
            //下面这个：如果现在点的是跑 且符合跑的移动规则，可以点击，进行移动
            else if (first instanceof CannonChessComponent
                    && first.canMoveTo(chessboard.getChessComponents(), squareComponent.getChessboardPoint())) {
                Step step=new Step();
                step.x=first.getChessboardPoint().getX();
                step.y=first.getChessboardPoint().getY();
                MemoryList.reviewList.add(step);
                Step step1=new Step();
                step1.x=squareComponent.getChessboardPoint().getX();
                step1.y =squareComponent.getChessboardPoint().getY();
                MemoryList.reviewList.add(step1);
                System.out.println(MemoryList.reviewList);
                chessboard.swapChessComponents(first, squareComponent);
                chessboard.clickController.swapPlayer();
                if(!ChessGameFrame.isRegret){
                    chessboard.setSecond(35);
                }
                first.setSelected(false);
                first = null;

                //解除提示色
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 4; j++) {
                        chessboard.getChessComponents()[i][j].setSquareColor(new Color(250, 220, 190));
                        chessboard.getChessComponents()[i][j].repaint();
                    }
                }
                //完成
                Music.Click();
            } else if (handleSecond(squareComponent)) {
                //repaint in swap chess method.
                Step step=new Step();
                step.x=first.getChessboardPoint().getX();
                step.y=first.getChessboardPoint().getY();
                MemoryList.reviewList.add(step);
                Step step3=new Step();
                step3.x=squareComponent.getChessboardPoint().getX();
                step3.y =squareComponent.getChessboardPoint().getY();
                System.out.println(step3.x);
                System.out.println(step3.y);
                MemoryList.reviewList.add(step3);
               System.out.println(MemoryList.reviewList);
                chessboard.swapChessComponents(first, squareComponent);
                chessboard.clickController.swapPlayer();
                if(!ChessGameFrame.isRegret){
                    chessboard.setSecond(35);
                }
                first.setSelected(false);
                first = null;
                //解除提示色
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 4; j++) {
                        chessboard.getChessComponents()[i][j].setSquareColor(new Color(250, 220, 190));
                        chessboard.getChessComponents()[i][j].repaint();
                    }
                }
                //完成
                Music.Click();
            }
        }
    }


    /**
     * @param squareComponent 目标选取的棋子
     * @return 目标选取的棋子是否与棋盘记录的当前行棋方颜色相同
     */

    public static int playerCount = 0;//当大于0时表示本回合已经翻开了第一枚棋子

    private boolean handleFirst(SquareComponent squareComponent) {
        if (!(squareComponent instanceof EmptySlotComponent)) {
            if (!squareComponent.isReversal()) {
                Step step=new Step();
                step.x=squareComponent.getChessboardPoint().getX();
                step.y=squareComponent.getChessboardPoint().getY();
                MemoryList.reviewList.add(step);
                System.out.println(MemoryList.reviewList);
                System.out.println("--------------------------------------------------------------------------------------");
                if (playerCount == 0) {
                    chessboard.setCurrentColor(squareComponent.getChessColor());//表示翻开棋子的颜色即是先手所代表的颜色
                }
                playerCount++;
                squareComponent.setReversal(true);
                //if(squareComponent.getChessColor().getColor()==Color.BLACK)
                System.out.printf("onClick to reverse a chess [%d,%d]\n", squareComponent.getChessboardPoint().getX(), squareComponent.getChessboardPoint().getY());
                squareComponent.repaint();
                chessboard.clickController.swapPlayer();
                if(!ChessGameFrame.isRegret){
                    chessboard.setSecond(35);
                }
                return false;
            }else {
                if(squareComponent.getChessColor() != chessboard.getCurrentColor()){
                    if(squareComponent.getChessColor().getColor()==Color.BLACK){
                        ChessGameFrame.textArea.append("  红方回合！\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    } else if (squareComponent.getChessColor().getColor() == Color.RED) {
                        ChessGameFrame.textArea.append("  黑方回合！\n");
                        ChessGameFrame.textArea.setCaretPosition(ChessGameFrame.textArea.getText().length());
                    }
                }
            }
        }
        return squareComponent.getChessColor() == chessboard.getCurrentColor();
    }

    /**
     * @param squareComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */

    private boolean handleSecond(SquareComponent squareComponent) {
        //没翻开或空棋子，进入if
        if (!squareComponent.isReversal()) {
            //没翻开且非空棋子不能走
            if (!(squareComponent instanceof EmptySlotComponent)) {
                return false;
            }
        }
        return squareComponent.getChessColor() != chessboard.getCurrentColor() &&
                first.canMoveTo(chessboard.getChessComponents(), squareComponent.getChessboardPoint());
    }

    public void swapPlayer() {
        chessboard.setCurrentColor(chessboard.getCurrentColor() == ChessColor.BLACK ? ChessColor.RED : ChessColor.BLACK);
        ChessGameFrame.getStatusLabel().setText(String.format("%s's TURN", chessboard.getCurrentColor().getName()));
        //凸显当前回合的颜色
        if(chessboard.getCurrentColor()==ChessColor.BLACK){
            ChessGameFrame.getStatusLabel().setForeground(Color.BLACK);
        }else {
            ChessGameFrame.getStatusLabel().setForeground(Color.RED);
        }
    }

}
