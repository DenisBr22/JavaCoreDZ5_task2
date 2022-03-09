import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Employee> list = parseXML("data.xml");
        String json = listToJson(list);
        writeString(json);
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> emplList = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));

            Node root = doc.getDocumentElement();
            NodeList nodeList = doc.getElementsByTagName("employee");
            NodeList emloyeeNode = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                emplList.add(getEmployesObj(nodeList.item(i)));
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        return emplList;
    }

    private static Employee getEmployesObj(Node node) {
        Employee employee = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            long id = Long.parseLong(getTagValue("id", element));
            String nameID = getTagValue("firstName", element);
            String lastNameID = getTagValue("lastName", element);
            String countryID = getTagValue("country", element);
            int ageID = Integer.parseInt(getTagValue("age", element));
            employee = new Employee(id, nameID, lastNameID, countryID, ageID);
        }
        return employee;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() { }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String textJsont) {
        try (FileWriter file = new FileWriter("new data.jason")) {
            file.write(textJsont);
            file.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}