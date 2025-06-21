package org.example.Controller;  //принадлежность к пакету controler

import org.example.View.Admin;
import org.example.View.Authorization;
import org.example.View.Stockman;
import org.example.View.Teller;
import org.example.model.ModelUser;

public class Controler_view //название класса
{
    //метод определения роли
    public static boolean role (String role, ModelUser user) {
    Admin admin;
    Teller teller;
    Stockman stockman;
    //проверка на роль админа
    if ("admin".equals(role)) {
        //вывод в консоль сообщения
        System.out.println("автаризация пройдена");
        //создание объекта интерфейса админа
        admin = get_Admin();
        return true;
    }
    //проверка на роль официанта
    else if ("stockman".equals(role)) {
        //вывод в консоль сообщения
        System.out.println("автаризация пройдена");
        //создание объекта интерфейса официанта
        stockman = get_Stockman();
        return true;
    }
    //проверка на роль повара
    else if ("teller".equals(role)) {
        //вывод в консоль сообщения
        System.out.println("автаризация пройдена");
        //создание объекта интерфейса официанта
        teller = get_Teller();
        return true;
    }
    return false;
    }

    //вывод формы админа
    private static Admin get_Admin()
    {
        //создание объекта формы админа
        Admin admin = new Admin();
        //вывод админа
        return admin;
    }
    private static Stockman get_Stockman()
    {
        //создание объекта формы админа
        Stockman stockman = new Stockman();
        //вывод админа
        return stockman;
    }
    private static Teller get_Teller()
    {
        //создание объекта формы админа
        Teller teller = new Teller();
        //вывод админа
        return teller;
    }
    //вывод формы официанта

    public static Authorization start ()
    {
        Authorization authorization = new Authorization();
        return authorization;
    }
}
