package chessComponent;

import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;
import view.ChessGameFrame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

/**
 * 表示棋盘上非空棋子的格子，是所有非空棋子的父类
 */
public class ChessComponent extends SquareComponent {
    protected String name;// 棋子名字：例如 兵，卒，士等

    protected ChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
    }
    //***

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //绘制棋子填充色
        g.setColor(new Color(250, 220, 190));
        g.fillOval(spacingLength, spacingLength, this.getWidth() - 2 * spacingLength, this.getHeight() - 2 * spacingLength);
       //绘制棋子边框
        g.setColor(Color.DARK_GRAY);
        g.drawOval(spacingLength, spacingLength, getWidth() - 2 * spacingLength, getHeight() - 2 * spacingLength);


        if (isReversal) {
            //绘制棋子文字
            g.setColor(this.getChessColor().getColor());
            g.setFont(CHESS_FONT);
            g.drawString(this.name, this.getWidth() / 4, this.getHeight() * 2 / 3);
            g.drawOval(spacingLength+3, spacingLength+3, getWidth() - 2*  spacingLength-6, getHeight() - 2* spacingLength-6);
            //绘制棋子被选中时状态
            if (isSelected()) {
                if(this.getChessColor().getColor()==Color.BLACK){
                    g.setColor(new Color(72, 71, 71));
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setStroke(new BasicStroke(4f));
                    g2.drawOval(spacingLength+3, spacingLength+3, getWidth() - 2 * spacingLength-6, getHeight() - 2 * spacingLength-6);
                }else {
                    g.setColor(new Color(227, 94, 94));
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setStroke(new BasicStroke(4f));
                    g2.drawOval(spacingLength+3, spacingLength+3, getWidth() - 2 * spacingLength-6, getHeight() - 2 * spacingLength-6);
                }
            }
        }
        if (isCheating){
            //g.setColor(Color.LIGHT_GRAY);
            if(this.getChessColor().getColor()==Color.RED){
                g.setColor(new Color(250, 220, 190));
            } else if (this.getChessColor().getColor() == Color.BLACK) {
                g.setColor(new Color(250, 220, 190));
            }
            g.fillOval(spacingLength, spacingLength, this.getWidth() - 2 * spacingLength, this.getHeight() - 2 * spacingLength);

            //g.setColor(this.getChessColor().getColor());
            if(this.getChessColor().getColor()==Color.RED){
                g.setColor(new Color(222, 156, 156));
            } else if (this.getChessColor().getColor() == Color.BLACK) {
                g.setColor(new Color(150, 143, 138));
            }
            g.setFont(CHESS_FONT);
            g.drawString(this.name, this.getWidth() / 4, this.getHeight() * 2 / 3);
        }
    }
//    protected void processMouseEvent(MouseEvent e) {
//        super.processMouseEvent(e);
//        if(!ChessGameFrame.isReview){
//            if (!isHint) {
//                if(e.getID() == MouseEvent.MOUSE_ENTERED) {
//                    this.setSquareColor(new Color(238, 199, 160));
//                    this.repaint();
//                }
//                if(e.getID()==MouseEvent.MOUSE_EXITED){
//                    this.setSquareColor(new Color(250, 220, 190));
//                    this.repaint();
//                }
//            }
//        }
//    }

}
