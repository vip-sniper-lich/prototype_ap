package org.example.View;

import javax.swing.*;

public class Stockman extends JFrame
{
    private JPanel admin_form;

    public Stockman()
    {
        setBounds(400,100, 800, 600);
        add(admin_form);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
