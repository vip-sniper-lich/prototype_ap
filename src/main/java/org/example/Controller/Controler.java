package org.example.Controller;

import org.example.T;
import org.example.model.ModelUser;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.example.Controller.Controler_view.role;
import static org.example.Controller.Controler_view.start;

//класс контролера
public class Controler
{
    public Controler()
    {
        start();
    }
    //Проверка существования пользователя

    public static ModelUser get_accept_in_bd(String login) {
        ModelUser model = null;
        try {
            // Основной запрос для получения данных пользователя
            String query = "SELECT " +
                    "    u.id_user, " +
                    "    r.role, " +
                    "    u.status, " +
                    "    COALESCE(s.name, ''), " +
                    "    COALESCE(s.last_name, ''), " +
                    "    COALESCE(s.patronymic, ''), " +
                    "    COALESCE(s.post, '') " +
                    "FROM " +
                    "    \"user\" u " +
                    "JOIN " +
                    "    role r ON u.id_role = r.id_role " +
                    "LEFT JOIN " +
                    "    staff s ON u.id_user = s.id_user " +
                    "WHERE " +
                    "    u.login = '" + login + "' ;";


            T<ResultSet> resultSetT = query("postgres", "1111", query);
            ResultSet resultSet = resultSetT.getItem();

            if (resultSet != null && resultSet.next()) {
                model = new ModelUser(resultSet.getString(1));
                model.setRoleUser(resultSet.getString(2));
                model.setStatus(resultSet.getString(3));
                model.setFirstnameUser(resultSet.getString(4));
                model.setNameUser(resultSet.getString(5));
                model.setPatronymic(resultSet.getString(6));
                model.setPost(resultSet.getString(7));           
            }

            if (model != null) {
                model.getData(); // если этот метод выводит информацию о пользователе
            }

            return model;
        } catch (Exception e) {
            System.out.println("Ошибка подключения: " + e.getMessage());
            return null;
        }
    }

    //Проверка на пустые поля
    public static boolean cheak_accept(String login, String password)
    {
        return !"".equals(login) && !"".equals(password);
    }

    public static boolean cheak_accept(String data)
    {
        return !"".equals(data);
    }

    //Проверка авторизации с расширенной логикой
    public static boolean get_accept(String login, String password)
    {
        try
        {
            ModelUser user = get_accept_in_bd(login);

            if (user == null)
            {
                throw new Exception("Пользователя с данным логином не существует!!1");
            }
            else
            {
                String query = "SELECT status, date_open FROM \"user\" WHERE id_user = '" + user.getIdUser() + "';";
                T<ResultSet> res = query("postgres", "1111", query);

                if(res.getItem().next())
                {
                    ResultSet result = res.getItem();
                    if(!result.getString(1).equals("blocked"))
                    {
                        LocalDate currentTime = LocalDate.now();
                        if (ChronoUnit.DAYS.between(currentTime, LocalDate.parse(result.getString(2))) > 31)
                        {
                            if (!result.getString(1).equals("blocked"))
                            {
                                query("postgres", "1111", "UPDATE \"user\" SET status = 'blocked' WHERE id_user ='" + user.getIdUser() + "';");
                                throw new Exception("Вы заблокированы из-за длительного бездействия!\nСообщите Администратору");
                            }
                        }
                        else
                        {
                            if (result.getString(1).equals("new"))
                            {
                                String query1 = "SELECT true FROM \"user\" WHERE id_user = '" + user.getIdUser() + "' AND password = '" + password + "';";
                                T<ResultSet> t1 = query("postgres", "1111", query1);
                                if (t1.getItem().next() && t1.getItem().getBoolean(1))
                                {
                                    // Вызов метода для нового пользователя
                                    return role(user.getRoleUser(), user);
                                }
                                else
                                {
                                    user.add_err();
                                    if (user.getErr_auth() < 3)
                                    {
                                        throw new Exception("Вы совершили ошибку при вводе пароля!!!\nВы будете заблокированы после " + (3 - user.getErr_auth()) + " попыток.");
                                    }
                                    else
                                    {
                                        query("postgres", "1111", "UPDATE \"user\" SET status = 'blocked' WHERE id_user ='" + user.getIdUser() + "';");
                                        throw new Exception("Вы были заблокированы из-за превышения лимита попыток!\nОбратитесь Администратору");
                                    }
                                }
                            }
                            else
                            {
                                String query1 = "SELECT true FROM \"user\" WHERE id_user = '" + user.getIdUser() + "' AND password = '" + password + "';";
                                T<ResultSet> t1 = query("postgres", "1111", query1);
                                if (t1.getItem().next() && t1.getItem().getBoolean(1))
                                {
                                    user.reset_err();
                                    System.out.println("Сброс неудачных попыток");
                                    return role(user.getRoleUser(), user);
                                }
                                else
                                {
                                    user.add_err();
                                    if (user.getErr_auth() < 3)
                                    {
                                        throw new Exception("Вы совершили ошибку при вводе пароля!!!\nВы будете заблокированы после " + (3 - user.getErr_auth()) + " попыток.");
                                    }
                                    else
                                    {
                                        query("postgres", "1111", "UPDATE \"user\" SET status = 'blocked' WHERE id_user ='" + user.getIdUser() + "';");
                                        throw new Exception("Вы были заблокированы из-за превышения лимита попыток!\nОбратитесь Администратору");
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        throw new Exception("Вы заблокированы!\nСообщите Администратору для выяснения причины");
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Ошибка: " + e.getMessage());
            return false;
        }
        return false;
    }

    // Метод для смены пароля
    public static boolean new_password_user(ModelUser user, String password)
    {
        try
        {
            String query = "UPDATE \"user\" SET password = '" + password + "' WHERE id_user ='" + user.getIdUser() + "';";
            T<ResultSet> res = query("postgres", "1111", query);
            if (res.getItem().next())
            {
                System.out.println("Изменено строк: " + res.getItem().getString(1));
                return true;
            }
            else
            {
                throw new Exception("Ошибка обратитесь Администратору");
            }
        }
        catch (Exception e)
        {
            System.out.println("Ошибка: " + e.getMessage());
            return false;
        }
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