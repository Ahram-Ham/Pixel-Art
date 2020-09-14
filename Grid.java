package com.company;

import java.awt.Graphics;
import java.awt.Color;

public class Grid
{
    private Color color;
    public Grid()
    {
        color = Color.white;
    }

    public void draw(int x, int y, Graphics g)
    {
        g.setColor(color);
        g.fillRect(x, y, 12, 12);
        g.setColor(Color.black);
        g.drawRect(x, y, 12, 12);
    }

    public void setColor(int r, int g, int b)
    {
        color = new Color(r, g, b);
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public Color getColor()
    {
        return color;
    }

    public String toString()
    {
        return color.toString();
    }
}
