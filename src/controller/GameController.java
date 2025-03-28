package controller;

import view.Chessboard;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * 这个类主要完成由窗体上组件触发的动作。
 * 例如点击button等
 * ChessGameFrame中组件调用本类的对象，在本类中的方法里完成逻辑运算，将运算的结果传递至chessboard中绘制
 */
public class GameController {
    private Chessboard chessboard;

    public GameController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }


    public List<String> loadGameFromFile(String path) {
        try {
            List<String> chessData = Files.readAllLines(Path.of(path));
            chessboard.loadGame(chessData);
            return chessData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //开启作弊模式，由ChessGameFrame调用
    int[][]coordinateRef=new int[8][4];
    public void cheatingBegin() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                if (!chessboard.getChessComponents()[i][j].isReversal()) {
                    coordinateRef[i][j]=1;
                    chessboard.getChessComponents()[i][j].setReversal(true);
                    //chessboard.getChessComponents()[i][j].setSquareColor(Color.gray);
                    chessboard.getChessComponents()[i][j].setCheating(true);
                    chessboard.getChessComponents()[i][j].repaint();
                }
            }
        }
    }
    //结束作弊模式
    public void cheatingFinish() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                if(coordinateRef[i][j]==1){
                    chessboard.getChessComponents()[i][j].setReversal(false);
                    //chessboard.getChessComponents()[i][j].setSquareColor(new Color(250, 220, 190));
                    chessboard.getChessComponents()[i][j].setCheating(false);
                    coordinateRef[i][j]=0;
                    chessboard.getChessComponents()[i][j].repaint();
                }
            }
        }
    }

}
