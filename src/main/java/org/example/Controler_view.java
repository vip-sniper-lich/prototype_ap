package org.example;  //принадлежность к пакету controler


public class Controler_view //название класса
{
    //метод определения роли
    public static boolean role (String role)
    {
        Admin admin;

        //проверка на роль админа
        if ("admin".equals(role))
        {
            //вывод в консоль сообщения
            System.out.println("автаризация пройдена");
            //создание объекта интерфейса админа
            admin = get_Admin();
            System.out.println("автаризация пройдена");
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
    //вывод формы официанта

    public static Authorization start ()
    {
        Authorization authorization = new Authorization();
        return authorization;
    }
}
