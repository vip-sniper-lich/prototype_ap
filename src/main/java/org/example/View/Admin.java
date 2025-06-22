package org.example.View;

import org.example.Controller.Admin_func;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import static org.example.Controller.Admin_func.all;
import static org.example.Controller.Admin_func.equalsBD;
import static org.example.Controller.JsonReader.all_users;
import static org.example.Controller.JsonWriter.save_bd;

public class Admin extends JFrame
{
    private JPanel admin_form;
    private JButton настройкиButton;
    private JButton помощьButton;
    private JButton справкаButton;
    private JButton Button;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JPanel work_zone;

    public Admin ()
    {
        Button.setBorderPainted(false);
        Button.addActionListener(e ->
        {
            // Создаем комбо-бокс для статуса
            JComboBox<String> status = new JComboBox<>();
            status.addItem("new");
            status.addItem("active");
            status.addItem("blocked");

            // Создаем таблицу
            save_bd(all());
            DefaultTableModel date = all_users();
            JTable table = new JTable(date);
            table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(status));

            // Создаем скролл-панель для таблицы
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setVisible(true);

            // Создаем кнопку "Обновить"
            JButton refreshButton = new JButton("Обновить");
            refreshButton.addActionListener(e1 ->
            {
                Admin_func test = new Admin_func();
                test.compareAndUpdate(table);
                equalsBD();
            });

            // Создаем панель для размещения таблицы и кнопки
            JPanel containerPanel = new JPanel(new BorderLayout());
            containerPanel.add(scrollPane, BorderLayout.CENTER);
            containerPanel.add(refreshButton, BorderLayout.SOUTH);

            // Обновляем work_zone
            work_zone.removeAll();
            work_zone.setLayout(new BorderLayout());
            work_zone.add(containerPanel, BorderLayout.CENTER);
            work_zone.revalidate();
            work_zone.repaint();
        });

        setBounds(400,100, 800, 600);
        add(admin_form);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}