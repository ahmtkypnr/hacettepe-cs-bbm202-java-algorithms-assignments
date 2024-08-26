import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HyperloopTrainNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageTrainSpeed;
    public final double averageWalkingSpeed = 1000 / 6.0;;
    public int numTrainLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<TrainLine> lines;

    /**
     * Method with a Regular Expression to extract integer numbers from the fileContent
     * @return the result as int
     */
    public int getIntVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        for (String line : fileContent.split("\n")) {
            Matcher m = p.matcher(line);
            if (m.find()) {
                return Integer.parseInt(m.group(1));
            }
        }
        return 0;
    }

    /**
     * Write the necessary Regular Expression to extract string constants from the fileContent
     * @return the result as String
     */
    public String getStringVar(String varName, String fileContent) {
        // TODO: Your code goes here
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\"([^\"]+)\"");
        for (String line : fileContent.split("\n")) {
            Matcher m = p.matcher(line);
            if (m.find()) {
                return m.group(1);
            }
        }
        return "";
    }

    /**
     * Write the necessary Regular Expression to extract floating point numbers from the fileContent
     * Your regular expression should support floating point numbers with an arbitrary number of
     * decimals or without any (e.g. 5, 5.2, 5.02, 5.0002, etc.).
     * @return the result as Double
     */
    public Double getDoubleVar(String varName, String fileContent) {
        // TODO: Your code goes here
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+(\\.[0-9]+)?)");
        for (String line : fileContent.split("\n")) {
            Matcher m = p.matcher(line);
            if (m.find()) {
                return Double.parseDouble(m.group(1));
            }
        }
        return 0.0;
    }

    /**
     * Write the necessary Regular Expression to extract a Point object from the fileContent
     * points are given as an x and y coordinate pair surrounded by parentheses and separated by a comma
     * @return the result as a Point object
     */
    public Point getPointVar(String varName, String fileContent) {
        // TODO: Your code goes here
        Pattern pattern = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\\([\\t ]*(\\d+)[\\t ]*,[\\t ]*(\\d+)[\\t ]*\\)");
        for (String line : fileContent.split("\n")) {
            Matcher m = pattern.matcher(line);
            if (m.find()) {
                int x = Integer.parseInt(m.group(1));
                int y = Integer.parseInt(m.group(2));
                return new Point(x, y);
            }
        }
        return null;
    } 

    /**
     * Function to extract the train lines from the fileContent by reading train line names and their 
     * respective stations.
     * @return List of TrainLine instances
     */
    public List<TrainLine> getTrainLines(String fileContent) {
        List<TrainLine> trainLines = new ArrayList<>();

        // TODO: Your code goes here

        String[] contentArray = fileContent.split("\n");
        boolean searchLine = false;
        String lineName = "";
        for (String line : contentArray) {
            if (!searchLine) {
                lineName = getStringVar("train_line_name", line);
                if (!lineName.isEmpty()) {
                    searchLine = true;
                }
            } else {
                Pattern pattern = Pattern.compile("[\\t ]*" + "train_line_stations" + "[\\t ]*=[\\t ]*(.*)");
                Matcher m = pattern.matcher(line);
                if (m.find()) {
                    searchLine = false;
                    List<Station> stations = new ArrayList<>();
                    int lineCounter = 0;
                    Pattern pattern2 = Pattern.compile("[\\t ]*\\([\\t ]*(\\d+)[\\t ]*,[\\t ]*(\\d+)[\\t ]*\\)");
                    Matcher m2 = pattern2.matcher(m.group(1));
                    while (m2.find()) {
                        int x = Integer.parseInt(m2.group(1));
                        int y = Integer.parseInt(m2.group(2));
                        lineCounter++;
                        stations.add(new Station(new Point(x, y), lineName + " Line Station " + lineCounter));
                    }
                    trainLines.add(new TrainLine(lineName, stations));
                }
            }
        }

        return trainLines;
    }

    /**
     * Function to populate the given instance variables of this class by calling the functions above.
     */
    public void readInput(String filename) {

        // TODO: Your code goes here
        try {
            Path filePath = Paths.get(filename);
            List<String> lines = Files.readAllLines(filePath);

            String content = String.join("\n", lines);
            this.averageTrainSpeed = getDoubleVar("average_train_speed", content) * 100/6.0;
            this.numTrainLines = getIntVar("num_train_lines", content);
            this.startPoint = new Station(getPointVar("starting_point", content), "Starting Point");
            this.destinationPoint = new Station(getPointVar("destination_point", content), "Final Destination");
            this.lines = getTrainLines(content);
        }
        catch (IOException ignored) {

        }
    }
}