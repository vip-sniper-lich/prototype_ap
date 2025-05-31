package org.example;

import javax.swing.*;

public class Admin extends JFrame
{
    private JPanel admin_form;

    public Admin ()
    {
        setBounds(400,100, 800, 600);
        add(admin_form);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
