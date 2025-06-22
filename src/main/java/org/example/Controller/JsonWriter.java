package org.example.Controller;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonWriter
{
    public static void save_bd(ResultSet resultSet)
    {
        String path = "src/main/java/org/example/jsonFile/data.json";
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();

        try
        {
            while (resultSet.next())
            {
                ObjectNode jsonObject = mapper.createObjectNode();
                // Добавляем поля в нужном порядке
                jsonObject.put("id_user", resultSet.getInt("id_user"));
                jsonObject.put("id_role", resultSet.getString("id_role"));
                jsonObject.put("login", resultSet.getString("login"));
                jsonObject.put("password", resultSet.getString("password"));
                jsonObject.put("date_open", resultSet.getString("date_open"));
                jsonObject.put("status", resultSet.getString("status"));
                jsonArray.add(jsonObject);
            }
            // Запись в файл
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), jsonArray);
            System.out.println("JSON файл успешно создан: " + path);
        }
        catch (SQLException | IOException e)
        {
            e.printStackTrace();
        }
    }
}