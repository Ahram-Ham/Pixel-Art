package com.company;

import javax.imageio.ImageIO;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import javax.swing.*;



public class Screen extends JPanel implements ActionListener, MouseListener, MouseMotionListener
{
    private Stack<Grid[][]> art;
    private Color currentColor;
    private JButton undoButton, clearButton, saveButton, customColor;
    private Color[] palette;
    private Grid[][] top;
    private ButtonGroup group = new ButtonGroup();;
    private JToggleButton draw, erase;
    private boolean drawState = true;
    private BufferedImage myImage;

    public Screen()
    {

        this.setLayout(null);
        palette = new Color[10];
        palette[0] = new Color(255,0,0);
        palette[1] = new Color(255,128,0);
        palette[2] = new Color(255,255,0);
        palette[3] = new Color(0,255,0);
        palette[4] = new Color(0,0,255);
        palette[5] = new Color(127,0,255);
        palette[6] = new Color(255,0,255);
        palette[7] = new Color(0,0,0);
        palette[8] = new Color(255, 255, 255);


        //current color
        currentColor = Color.red;

        art = new Stack<Grid[][]>();
        Grid[][] layer = new Grid[64][64];
        for (int r = 0; r < layer.length; r++)
        {
            for (int c = 0; c<layer[r].length; c++)
            {
                layer[r][c] = new Grid();
            }
        }

        art.push(layer);
        layer = new Grid[64][64];
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

        saveButton = new JButton("Save");
        saveButton.setBounds(1000,90, 100, 30); //sets the location and size
        saveButton.addActionListener(this); //add the listener
        this.add(saveButton); //add to JPanel

        draw = new JToggleButton("Paint", true);
        draw.setBounds(1000, 300, 75, 30);
        draw.addActionListener(this);
        group.add(draw);
        this.add(draw);

        erase = new JToggleButton("Eraser");
        erase.setBounds(1100, 300, 75, 30);
        erase.addActionListener(this);
        group.add(erase);
        this.add(erase);

        customColor = new JButton("Create Custom Color");
        customColor.setBounds(1000, 250, 175, 30);
        customColor.addActionListener(this);
        this.add(customColor);

        this.setFocusable(true);

        addMouseListener(this);

        addMouseMotionListener(this);
    }

    public Dimension getPreferredSize()
    {
        //Sets the size of the panel
        return new Dimension(1200, 900);
    }

    public void paintComponent(Graphics g)
    {
        if (myImage == null)
            myImage = (BufferedImage)(createImage(975, 800));

        Graphics gImage = myImage.createGraphics();

        //draw background
        g.setColor(Color.white);
        g.fillRect(0,0,1200,900);

        //set up font
        Font font = new Font("Arial", Font.PLAIN, 20);
        g.setFont(font);
        g.setColor(Color.black);

        //display current color
        g.drawString("Color pallete", 10, 100);
        g.drawString("Current color", 1000, 375);
        g.drawString("Custom color", 10, 580);
        g.setColor(currentColor);

        g.fillRect(1000, 400, 64, 64);
        g.setColor(Color.black);
        g.drawRect(1000, 400, 64, 64);

        //Color selector buttons
        for (int i = 0; i<palette.length-1; i++)
        {
            g.setColor(palette[i]);
            g.fillRect(44, 130+(i*48), 36, 36);
            g.setColor(Color.black);
            g.drawRect(44, 130+(i*48), 36,36);
        }
        g.setColor(palette[9]);
        g.fillRect(44, 598, 36, 36);
        g.setColor(Color.black);
        g.drawRect(44, 598, 36,36);


        //draw stack layer
        Grid[][] layer = art.peek();
        int x=200, y=20;

        for (int r = 0; r<layer.length; r++)
        {
            x = 200;
            for (int c = 0; c<layer[r].length; c++)
            {
                layer[r][c].draw(x,y,g);
                x += 12;
            }
            y += 12;
        }


        //For the saved drawing
        gImage.setColor(Color.white);
        gImage.fillRect(0,0,1200,800);

        //set up font

        gImage.setFont(font);
        gImage.setColor(Color.black);

        int a=200, b=20;

        for (int r = 0; r<layer.length; r++)
        {
            a = 100;
            for (int c = 0; c<layer[r].length; c++)
            {
                layer[r][c].draw(a, b, gImage);
                a += 12;
            }
            b += 12;
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
            Grid[][] temp = new Grid[64][64];
            for (int r = 0; r<temp.length; r++)
            {
                for (int c = 0; c<temp[r].length; c++)
                {
                    temp[r][c] = new Grid();
                }
            }
            art.push(temp);
        }

        else if(e.getSource() == saveButton)
        {
            if (myImage != null) {
                try {
                    File outputfile = new File("myimage.png");
                    ImageIO.write(myImage, "png", outputfile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        else if(e.getSource() == draw)
        {
            drawState = true;

        }

        else if(e.getSource() == erase)
        {
            drawState = false;
        }

        else if(e.getSource() == customColor)
        {
            String r = JOptionPane.showInputDialog(null,"Enter in the 'r' value");
            String g = JOptionPane.showInputDialog(null,"Enter in the 'g' value");
            String b = JOptionPane.showInputDialog(null,"Enter in the 'b' value");
            int red = Integer.parseInt(r);
            int green = Integer.parseInt(g);
            int blue = Integer.parseInt(b);
            palette[9] = new Color(red, green, blue);
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
            Grid[][] layer = new Grid[64][64];
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

        for (int i = 0; i<palette.length-1; i++)
        {
            if (e.getX() >= 44 && e.getX() <= 86 && e.getY() >= (130 +(i* 48)) && e.getY() <= (130 +(i* 48) + 48))
            {
                currentColor = palette[i];
            }
        }

        if (e.getX() >= 44 && e.getX() <= 86 && e.getY() >= 598 && e.getY() <= 634)
        {
            currentColor = palette[9];
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        int c = (e.getX() - 200)/12;
        int r = (e.getY() - 20)/12;

        if (r >= 0 && r <64 && !drawState)
        {
            if (c >= 0 && c <64)
            {
                art.peek()[r][c].setColor(Color.white);
            }
        }

        else if (r >= 0 && r <64)
        {
            if (c >= 0 && c <64)
            {
                art.peek()[r][c].setColor(currentColor);
            }
        }

        repaint();

        //single Grid draw color

    }
    public void mouseDragged(MouseEvent e)
    {

        int c = (e.getX() - 200)/12;
        int r = (e.getY() - 20)/12;

        if (r >= 0 && r <64 && !drawState)
        {
            if (c >= 0 && c <64)
            {
                art.peek()[r][c].setColor(Color.white);
            }
        }

        else if (r >= 0 && r <64)
        {
            if (c >= 0 && c <64)
            {
                art.peek()[r][c].setColor(currentColor);
            }
        }
        repaint();
    }
    public void mouseMoved(MouseEvent e) {}


}
