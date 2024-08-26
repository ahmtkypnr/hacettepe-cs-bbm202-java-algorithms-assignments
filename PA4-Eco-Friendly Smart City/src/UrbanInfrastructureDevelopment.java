import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.*;

public class UrbanInfrastructureDevelopment implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        // TODO: YOUR CODE HERE

        for (Project project : projectList) {
            project.printSchedule(project.getEarliestSchedule());
        }
    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();
        // TODO: YOUR CODE HEREi
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File(filename));

            document.getDocumentElement().normalize();

            NodeList projectNodes = document.getElementsByTagName("Project");
            for (int i = 0; i < projectNodes.getLength(); i++) {
                Node projectNode = projectNodes.item(i);
                Element project = (Element) projectNode;

                String name = project.getElementsByTagName("Name").item(0).getTextContent();

                Node tasks = project.getElementsByTagName("Tasks").item(0);
                NodeList taskNodes = ((Element) tasks).getElementsByTagName("Task");
                ArrayList<Task> taskArrayList = new ArrayList<>();
                for (int j = 0; j < taskNodes.getLength(); j++) {
                    Node taskNode = taskNodes.item(j);
                    Element task = (Element) taskNode;

                    String taskID = task.getElementsByTagName("TaskID").item(0).getTextContent();
                    String description = task.getElementsByTagName("Description").item(0).getTextContent();
                    String duration = task.getElementsByTagName("Duration").item(0).getTextContent();

                    Node dependencies = task.getElementsByTagName("Dependencies").item(0);
                    NodeList dependencyNodes = ((Element) dependencies).getElementsByTagName("DependsOnTaskID");
                    ArrayList<Integer> dependencyArrayList = new ArrayList<>();
                    for (int k = 0; k < dependencyNodes.getLength(); k++) {
                        Node dependencyNode = dependencyNodes.item(k);
                        Element dependency = (Element) dependencyNode;

                        dependencyArrayList.add(Integer.parseInt(dependency.getTextContent()));
                    }
                    taskArrayList.add(new Task(Integer.parseInt(taskID), description, Integer.parseInt(duration), dependencyArrayList));
                }
                projectList.add(new Project(name, taskArrayList));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return projectList;
    }
}
