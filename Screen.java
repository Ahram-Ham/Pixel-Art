import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.util.Stack;
import java.util.ArrayList;
import java.awt.MouseInfo;

public class Screen extends JPanel implements ActionListener, MouseListener, MouseMotionListener 
{
	private Stack<Grid[][]> art;
	private Color currentColor;
	private JButton undoButton, clearButton;
	private Color[] palette;
	private Grid[][] top;

    public Screen() 
	{

        this.setLayout(null);
		palette = new Color[8];
		palette[0] = new Color(255,0,0);
		palette[1] = new Color(255,128,0);
		palette[2] = new Color(255,255,0);
		palette[3] = new Color(0,255,0);
		palette[4] = new Color(0,0,255);
		palette[5] = new Color(127,0,255);
		palette[6] = new Color(255,0,255);
		palette[7] = new Color(0,0,0);
		
        //current color
		currentColor = Color.red;
		
		art = new Stack<Grid[][]>();
		Grid[][] layer = new Grid[16][16];
		for (int r = 0; r < layer.length; r++) 
		{
			for (int c = 0; c<layer[r].length; c++) 
			{
				layer[r][c] = new Grid();
			}
		}

		art.push(layer);
		layer = new Grid[16][16];
		for (int r = 0; r<layer.length; r++) 
		{
			for (int c = 0; c<layer[r].length; c++) 
			{
				layer[r][c] = new Grid();
			}
		}

		art.push(layer);

        undoButton = new JButton("Undo");
        undoButton.setBounds(1000,50, 100, 30); //sets the location and size
        undoButton.addActionListener(this); //add the listener
        this.add(undoButton); //add to JPanel
		
		clearButton = new JButton("Clear");
        clearButton.setBounds(1000,10, 100, 30); //sets the location and size
        clearButton.addActionListener(this); //add the listener
        this.add(clearButton); //add to JPanel

        this.setFocusable(true);
		
		addMouseListener(this);

		addMouseMotionListener(this);
    }

    public Dimension getPreferredSize() 
	{
        //Sets the size of the panel
        return new Dimension(1200, 800);
    }

    public void paintComponent(Graphics g) 
	{
        //draw background
        g.setColor(Color.white);
        g.fillRect(0,0,1200,800);

        //set up font
        Font font = new Font("Arial", Font.PLAIN, 20);
        g.setFont(font);
        g.setColor(Color.black);
		
        //display current color
		g.drawString("Color pallete", 10, 100);
		g.drawString("Current color", 1000, 375);
		g.setColor(currentColor);
		
		g.fillRect(1000, 400, 32, 32);
		
		//Color selector buttons
		for (int i = 0; i<palette.length; i++) 
		{
			g.setColor(palette[i]);
			g.fillRect(44, 130+(i*48), 48, 48);
		}  
		//draw stack layer
		Grid[][] layer = art.peek();
		int x=200, y=20;

		for (int r = 0; r<layer.length; r++) 
		{
			x = 200;
			for (int c = 0; c<layer[r].length; c++) 
			{
				layer[r][c].draw(x, y, g);
				x += 48;
			}
			y += 48;
		}
    }



    public void actionPerformed(ActionEvent e)
	{
        if (e.getSource() == undoButton) 
		{
			if (art.size() > 1) 
			{
				art.pop();
			}
        } 
		
		else if (e.getSource() == clearButton) 
		{
			Grid[][] temp = new Grid[16][16];
			for (int r = 0; r<temp.length; r++) 
			{
				for (int c = 0; c<temp[r].length; c++) 
				{
					temp[r][c] = new Grid();
				}
			}
			art.push(temp);
		}
        repaint();
    }

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) 
	{ 
		//set up working layer
		if (e.getX() >= 200 && e.getX() <= 968 && e.getY() >= 20 && e.getY() <= 788)
		{
			Grid[][] top = art.peek();
			Grid[][] layer = new Grid[16][16];
			for (int r = 0; r<layer.length; r++) 
			{
				for (int c = 0; c<layer[r].length; c++) 
				{
					layer[r][c] = new Grid();
					layer[r][c].setColor(top[r][c].getColor());
				}
			}
			art.push(layer);
		}

		for (int i = 0; i<palette.length; i++) 
		{
			if (e.getX() >= 44 && e.getX() <= 92 && e.getY() >= (130 +(i* 48)) && e.getY() <= (130 +(i* 48) + 48))
			{
				currentColor = palette[i];
			}
		}
	}

	public void mouseReleased(MouseEvent e) 
	{
		//single Grid draw color
		int c = (e.getX() - 200)/48;
        int r = (e.getY() - 20)/48; 
		if (r >= 0 && r <16) 
		{
			if (c >= 0 && c <16) 
			{
				art.peek()[r][c].setColor(currentColor);
			}
		}
		repaint();
	}
	public void mouseDragged(MouseEvent e) 
	{
		int c = (e.getX() - 200)/48;
        int r = (e.getY() - 20)/48; 
		if (r >= 0 && r <16) 
		{
			if (c >= 0 && c <16) 
			{
				art.peek()[r][c].setColor(currentColor);
			}
		}
		repaint();
	}
	public void mouseMoved(MouseEvent e) {}


}