package org.example;

import java.awt.*;
import java.sql.*;

import static org.example.Controler_view.role;
import static org.example.Controler_view.start;

//класс контролера
public class Controler
{
    public Controler()
    {
        start();
    }
    //Проверка существования пользователя
    public static String get_accept_in_bd (String login)
    {
        try
        {
            //запрос
            String query = "SELECT id_user FROM public.\"user\" WHERE login = '" + login + "';";
            //метод, который выполняет запрос
            T<ResultSet> resultSetT = query("postgres", "1111", query);
            ResultSet resultSet = resultSetT.getItem();

            System.out.println("Вывод сохранён 1 запрос");
            //сохраняет вывод запроса
            //если в запросе есть данные (вроде как)
            if (resultSet != null)
            {
                if (resultSet.next())
                {
                    return resultSet.getString(1);
                }
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            System.out.println("Ошибка подключения: " + e.getMessage());
            return null;    //чтоб java не ругалась
        }
        return null;
    }
    //Проверка существования пользователя
    //Проверка на пустые поля
    //я это даже разбирать не буду
    public static boolean cheak_accept(String login, String password)
    {
        boolean cheak = false;

        if (!"".equals(login))
        {
            if (!"".equals(password))
            {
                cheak = true;
            }
        }
        return cheak;
    }
    //Проверка на пустые поля
    public static boolean get_accept (String login, String password)
    {
        try {
            String id = get_accept_in_bd(login);
            if (id == null)
            {
                throw new Exception("Пользователя с данным логином не существует!!");
            }
            else
            {
                String query1 = "SELECT id_user FROM \"user\" WHERE login = '" + login + "' and " +
                        "password = '" + password + "';";
                T<ResultSet> res = query("postgres", "1111", query1);
                if(res.getItem().next())
                {
                    ResultSet result = res.getItem();
                    if(result.getString(1).equals(id))
                    {
                        String query2 = "SELECT id_role FROM \"user\" WHERE id_user = " + id + ";";
                        res = query("postgres", "1111", query2);
                        if(res.getItem().next()) {
                            result = res.getItem();
                            query2 = "SELECT role FROM role WHERE id_role = '" + Integer.parseInt(result.getString(1)) + "';";
                            res = query("postgres", "1111", query2);
                            if(res.getItem().next()) {
                                result = res.getItem();
                                if (result.getString(1).equals("admin")) {
                                    role(result.getString(1));
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        return false;
    }
    //проверка роли
    //запросы к бд
    //самое интересное
    private static int type_query (String query)
    {
        //сохранения запроса по словесно?
        String[] words = query.trim().split("\\s+");
        //сохранение первого слова
        String query_type = words.length > 0 ? words[0] : "";

        //вывод запроса для проверки
        System.out.println("Запрос:" + query);
        //вывод 1 слова для проверки
        System.out.println("Первое слова запроса:" + query_type);
        //тип запроса
        int type = 0;

        try
        {
            //распределение
            switch (query_type) {
                case "INSERT" -> type = 1;
                case "SELECT" -> type = 2;
                case "CREATE" -> type = 3;
                case "UPDATE" -> type = 4;
            }
        }
        catch (Exception e)
        {
            e.getMessage();
        }

        //тип запроса для проверки
        System.out.println("Тип запроса определён:" + type);
        return type;
    }
    public static T query(String login, String password, String query)
    {
        //ссылка на бд

        String url = "jdbc:postgresql://localhost:5433/cafe";
        try
        {
            switch (type_query(query))
            {
                //insert
                case (1) ->
                {
                    Class.forName("org.postgresql.Driver");
                    Connection connection = DriverManager.getConnection(url, login, password);
                    System.out.println("Запрос: " + query);


                    T<Integer> item = new T();
                    item.setItem(Insert(connection, query));
                    connection.close();
                    return item;
                }
                //SELECT
                case (2) ->
                {
                    Class.forName("org.postgresql.Driver");
                    //определения какая именно бд

                    // Устанавливаем соединение
                    Connection connection = DriverManager.getConnection(url, login, password);

                    T<ResultSet> item = new T();
                    item.setItem(Select(connection, query));

                    // Закрываем соединение
                    connection.close();
                    return item;
                }
                //CREATE
                case (3) ->
                {
                    Class.forName("org.postgresql.Driver");
                    Connection connection = DriverManager.getConnection(url, login, password);

                    T<Boolean> item = new T();
                    item.setItem(Create(connection, query));

                    connection.close();
                    return item;
                }
                case (4) ->
                {
                    Class.forName("org.postgresql.Driver");
                    Connection connection = DriverManager.getConnection(url, login, password);

                    T<Integer> t = new T();
                    t.setItem(Update(connection, query));

                    connection.close();
                    return t;

                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
    private static ResultSet Select(Connection conn, String query)
    {
        ResultSet result;
        try
        {
            //для проверки
            System.out.println("Подключение к базе данных успешно! 2P");
            //чтобы можно было туда-сюда вертеть ячейки
            Statement resultUsers = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //ты до сих пор не запомнил что делает этот кусок кода?
            result = resultUsers.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return result;
    }
    private static int Insert(Connection conn, String query)
    {
        try
        {
            Statement resultUsers = conn.createStatement();
            return resultUsers.executeUpdate(query);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    private static boolean Create(Connection conn, String query)
    {
        try
        {
            Statement resultUsers = conn.createStatement();
            return resultUsers.execute(query);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    private static int Update (Connection conn, String query)
    {
        try
        {
            Statement resultUsers = conn.createStatement();
            return resultUsers.executeUpdate(query);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    //запросы к бд
}