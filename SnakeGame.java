import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
public class SnakeGame  extends JPanel implements ActionListener,KeyListener
{
    private class Tile
    {
        int x;
        int y;
        Tile(int x,int y)
        {
            this.x=x;
            this.y=y;
        }
    }
    int BoardWidth;
    int BoardHeight;
    int TileSize=25;
    //Snake
    Tile snakeHead;
    ArrayList<Tile>snakeBody;
    //Food
    Tile food;
    Random random;
    //game Logic
    Timer gameloop;
    int velocityX;
    int velocityY;
    boolean gameOver=false;
    SnakeGame(int BoardWidth,int BoardHeight)
    {
        this.BoardHeight=BoardHeight;
        this.BoardWidth=BoardWidth;
        setPreferredSize(new Dimension(this.BoardWidth,this.BoardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        snakeHead=new Tile(5,5);
        snakeBody=new ArrayList<Tile>();
        food =new Tile(10,10);
        random=new Random();
        PlaceFood();
        velocityX=0;
        velocityY=0; 
        gameloop=new Timer(100, this);
        gameloop.start();    
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g)
    {
        //grid Lines
        for(int i=0;i<BoardWidth/TileSize;i++)
        {
            g.drawLine(i*TileSize, 0, i*TileSize, BoardHeight);//verticle line
            g.drawLine(0, i*TileSize, BoardWidth, i*TileSize);//horizontal line
        }
        //food
        g.setColor(Color.red);
        g.fill3DRect(food.x*TileSize,food.y*TileSize,TileSize,TileSize,true);
        
        //snake
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x*TileSize, snakeHead.y*TileSize, TileSize, TileSize,true);

        //snake body
        for(int i=0;i<snakeBody.size();i++)
        {
            Tile snakePart=snakeBody.get(i);
            g.fill3DRect(snakePart.x*TileSize,snakePart.y*TileSize,TileSize,TileSize,true);
        }

        //score
        g.setFont(new Font("Arial",Font.PLAIN,16));
        if(gameOver)
        {
            g.setColor(Color.red);
            g.drawString("Game Over: "+String.valueOf(snakeBody.size()),TileSize-16,TileSize);
        }
        else
        {
            g.drawString("Score: "+String.valueOf(snakeBody.size()),TileSize-16,TileSize);
        }
    }
    public void PlaceFood()
    {
        food.x=random.nextInt(BoardWidth/TileSize);
        food.y=random.nextInt(BoardHeight/TileSize);
    }
    public boolean collision(Tile tile1,Tile tile2)
    {
        return tile1.x==tile2.x && tile1.y==tile2.y;
    }
    public void move()
    {
        //eat food
        if(collision(snakeHead, food))
        {
            snakeBody.add(new Tile(food.x,food.y));
            PlaceFood();
        }
        //snake body
        for(int i=snakeBody.size()-1;i>=0;i--)
        {
            Tile snakePart=snakeBody.get(i);
            if(i==0)
            {
                snakePart.x=snakeHead.x;
                snakePart.y=snakeHead.y;
            }
            else
            {
                Tile prevSnakePart=snakeBody.get(i-1);
                snakePart.x=prevSnakePart.x;
                snakePart.y=prevSnakePart.y;
            }
        }
        //snake head
        snakeHead.x+=velocityX;
        snakeHead.y+=velocityY;
        //game over
        //collision with body part
        for(int i=0;i<snakeBody.size();i++)
        {
            Tile snakePart=snakeBody.get(i);
            if(collision(snakeHead, snakePart))
            {
                gameOver=true;
            }
        }
        //collision with wall part
        if(snakeHead.x*TileSize<0 || snakeHead.x*TileSize>BoardWidth ||
            snakeHead.y*TileSize<0 || snakeHead.y*TileSize>BoardHeight)
            {
                gameOver=true;
            }

    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        move();
        repaint();
        if(gameOver)
        {
            gameloop.stop();
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode()==KeyEvent.VK_UP && velocityY!=1)
        {
            velocityX=0;
            velocityY=-1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN && velocityY!=-1)
        {
            velocityX=0;
            velocityY=1;
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT && velocityX!=1)
        {
            velocityX=-1;
            velocityY=0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT && velocityX!=-1)
        {
            velocityX=1;
            velocityY=0;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }

}
