package org.example.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.T;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.example.Controller.Controler.query;
import static org.example.Controller.JsonReader.read_bd;
import static org.example.Controller.JsonReader.compareArrayNodes;

public class Admin_func
{
    public static ResultSet all ()
    {
        String query = "SELECT * FROM \"user\"";
        T<ResultSet> resultSetT = query("postgres", "1111", query);
        ResultSet resultSet = resultSetT.getItem();
        return resultSet;
    }
    // Метод для сравнения и обновления данных
    public void compareAndUpdate(JTable tableModel)
    {
        String jsonFilePath = "src/main/java/org/example/jsonFile/data.json";
        try
        {
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            JSONArray jsonArray = new JSONArray(jsonContent);
            List<User> tableUsers = getUsersFromTable(tableModel);
            boolean changesDetected = false;
            JSONArray updatedJsonArray = new JSONArray();

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonUser = jsonArray.getJSONObject(i);
                User jsonUserObj = new User(
                        jsonUser.optString("password", ""),
                        jsonUser.isNull("date_open") ? null : jsonUser.getString("date_open"),
                        jsonUser.optString("id_role", ""),
                        jsonUser.optInt("id_user", -1),
                        jsonUser.optString("login", ""),
                        jsonUser.optString("status", "")
                );

                User tableUser = findUserById(tableUsers, jsonUserObj.id_user);

                if (tableUser != null && !tableUser.equals(jsonUserObj))
                {
                    if (tableUser.password != null) jsonUser.put("password", tableUser.password);
                    if (tableUser.date_open != null) jsonUser.put("date_open", tableUser.date_open);
                    if (tableUser.id_role != null) jsonUser.put("id_role", tableUser.id_role);
                    if (tableUser.login != null) jsonUser.put("login", tableUser.login);
                    if (tableUser.status != null) jsonUser.put("status", tableUser.status);
                    changesDetected = true;
                }
                updatedJsonArray.put(jsonUser);
            }
            if (changesDetected)
            {
                try (FileWriter file = new FileWriter(jsonFilePath))
                {
                    file.write(updatedJsonArray.toString(4));
                    System.out.println("JSON файл успешно обновлен");
                }
            }
            else
            {
                System.out.println("Изменений не обнаружено");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Получение списка пользователей из таблицы
    private List<User> getUsersFromTable(JTable tableModel) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object idUserObj = tableModel.getValueAt(i, 3);
            int idUser = idUserObj instanceof Long ? ((Long) idUserObj).intValue() :
                    (idUserObj instanceof Integer ? (Integer) idUserObj : -1);

            Object dateOpenObj = tableModel.getValueAt(i, 1);
            String dateOpen = dateOpenObj != null ? dateOpenObj.toString() : null;

            User user = new User(
                    tableModel.getValueAt(i, 0) != null ? tableModel.getValueAt(i, 0).toString() : null,
                    dateOpen,
                    tableModel.getValueAt(i, 2) != null ? tableModel.getValueAt(i, 2).toString() : null,
                    idUser,
                    tableModel.getValueAt(i, 4) != null ? tableModel.getValueAt(i, 4).toString() : null,
                    tableModel.getValueAt(i, 5) != null ? tableModel.getValueAt(i, 5).toString() : null
            );
            users.add(user);
        }
        return users;
    }

    // Поиск пользователя по ID
    private User findUserById(List<User> users, int id)
    {
        for (User user : users)
        {
            if (user.id_user == id)
            {
                return user;
            }
        }
        return null;
    }

    // Внутренний класс для представления пользователя
    private static class User
    {
        String password;
        String date_open;
        String id_role;
        int id_user;
        String login;
        String status;

        public User(String password, String date_open, String id_role,
                    int id_user, String login, String status)
        {
            this.password = password;
            this.date_open = date_open;
            this.id_role = id_role;
            this.id_user = id_user;
            this.login = login;
            this.status = status;
        }
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            User user = (User) obj;
            return id_user == user.id_user &&
                    (password != null ? password.equals(user.password) : user.password == null) &&
                    (date_open != null ? date_open.equals(user.date_open) : user.date_open == null) &&
                    (id_role != null ? id_role.equals(user.id_role) : user.id_role == null) &&
                    (login != null ? login.equals(user.login) : user.login == null) &&
                    (status != null ? status.equals(user.status) : user.status == null);
        }
    }
    public static void equalsBD()
    {
        ResultSet bd = all();
        ArrayNode json = read_bd();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();

        try
        {
            while (bd.next())
            {
                ObjectNode jsonObject = mapper.createObjectNode();
                // Добавляем поля в нужном порядке
                jsonObject.put("id_user", bd.getInt("id_user"));
                jsonObject.put("id_role", bd.getString("id_role"));
                jsonObject.put("login", bd.getString("login"));
                jsonObject.put("password", bd.getString("password"));
                jsonObject.put("date_open", bd.getString("date_open"));
                jsonObject.put("status", bd.getString("status"));
                jsonArray.add(jsonObject);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        ArrayNode jsonArray1 = compareArrayNodes(jsonArray, json);

        for(int i = 0; i < jsonArray1.size(); i++) {
            JsonNode jsonNode = jsonArray1.get(i);

            if (jsonNode.isObject()) {
                ObjectNode jsonObject = (ObjectNode) jsonNode;

                // Получаем id_user для условия WHERE
                int id = jsonObject.get("id_user").asInt();

                // Перебираем все поля в JSON объекте
                java.util.Iterator<String> fieldNames = jsonObject.fieldNames();

                while (fieldNames.hasNext()) {
                    String key = fieldNames.next();

                    // Пропускаем id_user, так как он используется в WHERE
                    if (key.equals("id_user")) {
                        continue;
                    }

                    // Получаем значение
                    JsonNode valueNode = jsonObject.get(key);
                    String value = valueNode.isTextual() ? valueNode.asText() : valueNode.toString();

                    // Формируем SQL запрос
                    String sql = "UPDATE public.\"user\" " +
                            "SET " + key + " = '" + value + "' " +
                            "WHERE id_user = '" + id + "';";

                    System.out.println(sql);
                    System.out.println(query("postgres", "1111", sql));
                }
            }
        }
    }
}