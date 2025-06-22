package org.example.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.swing.table.DefaultTableModel;
import java.io.FileReader;
import java.util.Vector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.File;
import java.io.IOException;

public class JsonReader
{
    public static ArrayNode read_bd()
    {
        String path = "src/main/java/org/example/jsonFile/data.json";
        ObjectMapper mapper = new ObjectMapper();

        try
        {
            // Чтение JSON файла и преобразование в ArrayNode
            ArrayNode jsonArray = (ArrayNode) mapper.readTree(new File(path));
            return jsonArray;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return mapper.createArrayNode(); // Возвращаем пустой ArrayNode в случае ошибки
        }
    }
    public static DefaultTableModel all_users()
    {
        try
        {
            // Указываем путь к файлу относительно корня проекта
            String filePath = "src/main/java/org/example/jsonFile/data.json";
            JSONParser parser = new JSONParser();
            JSONArray users = (JSONArray) parser.parse(new FileReader(filePath));

            if (users.isEmpty())
            {
                return new DefaultTableModel(); // Пустая таблица, если нет данных
            }

            // Получаем названия столбцов из первого объекта
            JSONObject firstUser = (JSONObject) users.get(0);
            Vector<String> columnNames = new Vector<>(firstUser.keySet());

            // Заполняем данные
            Vector<Vector<Object>> data = new Vector<>();
            for (Object userObj : users)
            {
                JSONObject user = (JSONObject) userObj;
                Vector<Object> row = new Vector<>();
                for (String column : columnNames)
                {
                    row.add(user.get(column));
                }
                data.add(row);
            }

            // Создаём модель таблицы
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            return model;

        }
        catch (Exception e)
        {
            System.err.println("Ошибка при чтении JSON: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public static ArrayNode compareArrayNodes(ArrayNode array1, ArrayNode array2) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray = mapper.createArrayNode();

        if (array1 == null || array2 == null) {
            System.out.println("Один из ArrayNode равен null");
            return null;
        }

        int minSize = Math.min(array1.size(), array2.size());
        int maxSize = Math.max(array1.size(), array2.size());

        // Сравниваем элементы по индексам
        for (int i = 0; i < minSize; i++) {
            JsonNode node1 = array1.get(i);
            JsonNode node2 = array2.get(i);

            if (!node1.equals(node2)) {
                System.out.printf("Различие в элементе [%d]:\n", i);
                //System.out.printf("  В первом ArrayNode: %s\n", node1);
                System.out.printf("  Во втором ArrayNode: %s\n", node2);
                jsonArray.add(node2);

            }
        }


        // Если размеры массивов разные, выводим дополнительные элементы
        if (array1.size() != array2.size()) {
            System.out.printf("Разные размеры массивов: первый = %d, второй = %d\n", array1.size(), array2.size());
            if (array1.size() > array2.size()) {
                for (int i = minSize; i < maxSize; i++) {
                    System.out.printf("Элемент [%d] есть только в первом ArrayNode: %s\n", i, array1.get(i));
                }
            } else {
                for (int i = minSize; i < maxSize; i++) {
                    System.out.printf("Элемент [%d] есть только во втором ArrayNode: %s\n", i, array2.get(i));
                }
            }
        } else {
            System.out.println("Массивы одинакового размера, различия только в содержимом (если есть выше)");
        }
        return jsonArray;
    }
}