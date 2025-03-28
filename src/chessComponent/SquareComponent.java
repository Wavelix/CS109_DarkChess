package chessComponent;

import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;
import view.ChessGameFrame;
import view.MemoryList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * 这个类是一个抽象类，主要表示8*4棋盘上每个格子的棋子情况。
 * 有两个子类：
 * 1. EmptySlotComponent: 空棋子
 * 2. ChessComponent: 表示非空棋子
 */
public abstract class SquareComponent extends JComponent {

    public int level;
    public static boolean isHint = false;
    private Color squareColor = new Color(250, 220, 190);
    protected static int spacingLength;
    protected static final Font CHESS_FONT = new Font("sans serif", Font.BOLD, 36);

    /**
     * chessboardPoint: 表示8*4棋盘中，当前棋子在棋格对应的位置，如(0, 0), (1, 0)等等
     * chessColor: 表示这个棋子的颜色，有红色，黑色，无色三种
     * isReversal: 表示是否翻转
     * selected: 表示这个棋子是否被选中
     */
    protected ChessboardPoint chessboardPoint;
public ChessColor chessColor;
    protected boolean isReversal;
    private boolean selected;

    //11111
    public boolean isCheating;


    /**
     * handle click event
     */
    private final ClickController clickController;

    protected SquareComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        setLocation(location);
        setSize(size, size);
        this.chessboardPoint = chessboardPoint;
        this.chessColor = chessColor;
        this.selected = false;
        this.clickController = clickController;
        this.isReversal = false;

        this.isCheating = false;
    }

    public static void setHint(boolean hint) {
        isHint = hint;
    }

    public boolean isReversal() {
        return isReversal;
    }

    public void setReversal(boolean reversal) {
        isReversal = reversal;
    }

    public boolean isCheating() {
        return isCheating;
    }

    public void setCheating(boolean cheating) {
        isCheating = cheating;
    }

    public static void setSpacingLength(int spacingLength) {
        SquareComponent.spacingLength = spacingLength;
    }

    public ChessboardPoint getChessboardPoint() {
        return chessboardPoint;
    }

    public void setChessboardPoint(ChessboardPoint chessboardPoint) {
        this.chessboardPoint = chessboardPoint;
    }

    public ChessColor getChessColor() {
        return chessColor;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setSquareColor(Color squareColor) {
        this.squareColor = squareColor;
    }
    public Color getSquareColor(){
        return squareColor;
    }

    /**
     * @param another 主要用于和另外一个棋子交换位置
     *                <br>
     *                调用时机是在移动棋子的时候，将操控的棋子和对应的空位置棋子(EmptySlotComponent)做交换
     */
    public void swapLocation(SquareComponent another) {
        ChessboardPoint chessboardPoint1 = getChessboardPoint(), chessboardPoint2 = another.getChessboardPoint();
        Point point1 = getLocation(), point2 = another.getLocation();
        setChessboardPoint(chessboardPoint2);
        setLocation(point2);
        another.setChessboardPoint(chessboardPoint1);
        another.setLocation(point1);
    }

    /**
     * @param e 响应鼠标监听事件
     *          <br>
     *          当接收到鼠标动作的时候，这个方法就会自动被调用，调用监听者的onClick方法，处理棋子的选中，移动等等行为。
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if(!ChessGameFrame.isReview){
            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                System.out.printf("Click [%d,%d]\n", chessboardPoint.getX(), chessboardPoint.getY());
                clickController.onClick(this);
            }
        }
    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    /**
     * @param chessboard  棋盘
     * @param destination 目标位置，如(0, 0), (0, 1)等等
     * @return this棋子对象的移动规则和当前位置(chessboardPoint)能否到达目标位置
     * <br>
     * 这个方法主要是检查移动的合法性，如果合法就返回true，反之是false。
     */
    //todo: Override this method for Cannon
    public boolean canMoveTo(SquareComponent[][] chessboard, ChessboardPoint destination) {

        SquareComponent destinationChess = chessboard[destination.getX()][destination.getY()];
        //以下是判断当下是什么棋子并且附上等级
        if (this instanceof EmptySlotComponent) {
            this.level = -2;
        }//空
        if (this instanceof AdvisorChessComponent) {
            this.level = 4;
        }//士
        if (this instanceof MinisterChessComponent) {
            this.level = 3;
        }//象
        if (this instanceof HorseChessComponent) {
            this.level = 1;
        }//马
        if (this instanceof ChariotChessComponent) {
            this.level = 2;
        }//车
        if (this instanceof SoldierChessComponent) {
            this.level = 0;
        }//象
        if (this instanceof GeneralChessComponent) {
            this.level = 5;
        }//帅
        //------------------------------------------------------------------------------------------------------------
        //以下是判断目标位置是什么棋子，并且附上等级
        if (destinationChess instanceof GeneralChessComponent) {
            destinationChess.level = 5;
        }//帅
        if (destinationChess instanceof EmptySlotComponent) {
            destinationChess.level = -2;
        }//空
        if (destinationChess instanceof AdvisorChessComponent) {
            destinationChess.level = 4;
        }//车
        if (destinationChess instanceof MinisterChessComponent) {
            destinationChess.level = 3;
        }
        if (destinationChess instanceof HorseChessComponent) {
            destinationChess.level = 1;
        }//马
        if (destinationChess instanceof ChariotChessComponent) {
            destinationChess.level = 2;
        }//车
        if (destinationChess instanceof SoldierChessComponent) {
            destinationChess.level = 0;
        }//兵
        if (destinationChess instanceof CannonChessComponent) {
            destinationChess.level = 1;
        }

//------------------------------------------------------------------------------------------------------
        if (this instanceof CannonChessComponent) {//单独写炮的走法
            //如果目标位置不是空
            //---------------------------------------------------------------------------------------------------------


            if (this.chessboardPoint.getY() == (destinationChess.chessboardPoint.getY())) {//判断竖着走
                int start = this.chessboardPoint.getX();
                int end = destinationChess.chessboardPoint.getX();
                //--------------------------------------------------------------------------------------------------------------------
                if (start < end) {//从上向下走
                    int counter = 0;
                    for (int i = start + 1; i < end; i++) {
                        SquareComponent road = chessboard[i][chessboardPoint.getY()];
                        if (!(road instanceof EmptySlotComponent)) {
                            if (!isHint) {
                                System.out.println("!!!!!!!!!!!!!!!!!1");
                                System.out.println(road);
                            }
                            counter++;
                        }
                    }
                    if (counter == 1) {
                        if (!destinationChess.isReversal) {//如果目标没翻，直接吃！！！！！！！！！！！！！！！！！！1
                            return true;
                        } else if (!(destinationChess.chessColor.getColor().equals(this.chessColor.getColor()))
                                && !(destinationChess instanceof EmptySlotComponent)) {
                            return true;
                        }//如果目标翻了，只能吃同色的！！！！！！！！！！！！！！！！！！！！！1
                    }
                }
                //--------------------------------------------------------------------------------------------------------------------
                if (start > end) {//从下向上走
                    int counter = 0;
                    for (int i = start - 1; i > end; i--) {
                        SquareComponent road = chessboard[i][chessboardPoint.getY()];
                        if (!(road instanceof EmptySlotComponent)) {
                            if (!isHint) {
                                System.out.println("!!!!!!!!!!!!!!!!!2");
                                System.out.println(road);
                            }
                            counter++;
                        }
                    }
                    if (counter == 1) {
                        if (!destinationChess.isReversal) {
                            return true;
                        } else if (!(destinationChess.chessColor.getColor().equals(this.chessColor.getColor()))
                                && !(destinationChess instanceof EmptySlotComponent)) {
                            return true;
                        }
                    }
                }
            }
//----------------------------------------------------------------------------------------------------------------------
            if (this.chessboardPoint.getX() == (destinationChess.chessboardPoint.getX())) {//判断横着走
                int start = this.chessboardPoint.getY();
                int end = destinationChess.chessboardPoint.getY();
                //---------------------------------------------------------------------------------------------------------------------
                if (start < end) {//从左向右走
                    int counter = 0;
                    for (int i = start + 1; i < end; i++) {
                        SquareComponent road = chessboard[chessboardPoint.getX()][i];
                        if (!(road instanceof EmptySlotComponent)) {
                            if (!isHint) {
                                System.out.println("!!!!!!!!!!!!!!!!!3");
                            }
                            counter++;
                        }
                    }
                    if (counter == 1) {
                        if (!destinationChess.isReversal) {

                            return true;
                        } else if (!(destinationChess.chessColor.getColor().equals(this.chessColor.getColor()))
                                && !(destinationChess instanceof EmptySlotComponent)) {
                            return true;
                        }
                    }
                }
                //--------------------------------------------------------------------------------------------------------------------
                if (start > end) {
                    int counter = 0;
                    for (int i = start - 1; i > end; i--) {
                        SquareComponent road = chessboard[chessboardPoint.getX()][i];
                        if (!(road instanceof EmptySlotComponent)) {
                            if (!isHint) {
                                System.out.println("!!!!!!!!!!!!!!!!!4");
                            }
                            counter++;
                        }
                    }
                    if (counter == 1) {
                        if (!destinationChess.isReversal) {
                            return true;
                        } else if (!(destinationChess.chessColor.getColor().equals(this.chessColor.getColor()))
                                && !(destinationChess instanceof EmptySlotComponent)) {
                            return true;
                        }
                    }
                }//从右往左走
            }
        }
//--------------------------------------------------------------------------------------------------------------------
        //下面这个if来判断一次性只能走一格
        else if (this instanceof GeneralChessComponent && destinationChess instanceof SoldierChessComponent) {
            if (!isHint) {
                JOptionPane.showMessageDialog(null, "你会被他吃掉的，快逃！");
            }
            return false;
        } else if (this.chessboardPoint.getX() == destinationChess.chessboardPoint.getX()
                && Math.abs(this.chessboardPoint.getY() - destinationChess.chessboardPoint.getY()) == 1
                || this.chessboardPoint.getY() == destinationChess.chessboardPoint.getY()
                && Math.abs(this.chessboardPoint.getX() - destinationChess.chessboardPoint.getX()) == 1) {
            //下面这个if来实现兵吃将
            if (this instanceof SoldierChessComponent && destinationChess instanceof GeneralChessComponent) {
                if (!isHint) {
                    System.out.println(MemoryList.reviewList);
                    //JOptionPane.showMessageDialog(null, "喜提30分，乐");
                }
                return destinationChess.isReversal;
            }
            //下面这个if来实现用等级吃子
            else if (this.level >= destinationChess.level) {
                if (!isHint) {
                    System.out.println("吃");
                }
                return true;
            } else {
                if (!isHint) {
                    if(!ChessGameFrame.isRegret){
                        JOptionPane.showMessageDialog(null, "你会被他吃掉的，快逃！");
                    }
                }
            }
        } else {
            if (!isHint) {
                if(!ChessGameFrame.isRegret){
                    JOptionPane.showMessageDialog(null, "脚踏实地，不要“眼高手低”");
                }
            }
        }
        return false;
        //todo: complete this method
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        System.out.printf("repaint chess [%d,%d]\n", chessboardPoint.getX(), chessboardPoint.getY());
        g.setColor(squareColor);
        g.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
    }

}
