import java.io.Serializable;
import java.util.*;

public class Project implements Serializable {
    static final long serialVersionUID = 33L;
    private final String name;
    private final List<Task> tasks;

    public Project(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    /**
     * @return the total duration of the project in days
     */
    public int getProjectDuration() {
        int projectDuration = 0;

        // TODO: YOUR CODE HERE

        int[] earliest = getEarliestSchedule();

        int[] topological = topologicalSort();

        int lastIndex = topological.length - 1;

        projectDuration = earliest[topological[lastIndex]] + tasks.get(topological[lastIndex]).getDuration();

        return projectDuration;
    }

    void topologicalSortUtil(int v, boolean[] visited, List<Integer> result) {
        visited[v] = true;

        for (int i : tasks.get(v).getDependencies()) {
            if (!visited[i]) {
                topologicalSortUtil(i, visited, result);
            }
        }

        result.add(v);
    }

    int[] topologicalSort() {
        int V = tasks.size();
        int[] result = new int[V];

        boolean[] visited = new boolean[V];

        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                topologicalSortUtil(i, visited, arrayList);
            }
        }

        for (int i = 0; i < V; i++) {
            result[i] = arrayList.get(i);
        }

        return result;
    }

    /**
     * Schedule all tasks within this project such that they will be completed as early as possible.
     *
     * @return An integer array consisting of the earliest start days for each task.
     */
    public int[] getEarliestSchedule() {

        // TODO: YOUR CODE HERE

        int size = this.tasks.size();

        int[] schedule = new int[size];

        int[] topological = topologicalSort();

        for (int i = 0; i < size; i++) {
            Task task = this.tasks.get(topological[i]);
            int earliest = 0;
            for (int dependency : task.getDependencies()) {
                Task dependentTask = this.tasks.get(dependency);
                int topologicalIndex = 0;
                for (int j = 0; j < i; j++) {
                    if (topological[j] == dependentTask.getTaskID()) {
                        topologicalIndex = j;
                        break;
                    }
                }
                earliest = Math.max(earliest, dependentTask.getDuration() + schedule[topologicalIndex]);
            }
            schedule[i] = earliest;
        }

        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[topological[i]] = schedule[i];
        }

        return result;
    }

    public static void printlnDash(int limit, char symbol) {
        for (int i = 0; i < limit; i++) System.out.print(symbol);
        System.out.println();
    }

    /**
     * Some free code here. YAAAY! 
     */
    public void printSchedule(int[] schedule) {
        int limit = 65;
        char symbol = '-';
        printlnDash(limit, symbol);
        System.out.println(String.format("Project name: %s", name));
        printlnDash(limit, symbol);

        // Print header
        System.out.println(String.format("%-10s%-45s%-7s%-5s","Task ID","Description","Start","End"));
        printlnDash(limit, symbol);
        for (int i = 0; i < schedule.length; i++) {
            Task t = tasks.get(i);
            System.out.println(String.format("%-10d%-45s%-7d%-5d", i, t.getDescription(), schedule[i], schedule[i]+t.getDuration()));
        }
        printlnDash(limit, symbol);
        System.out.println(String.format("Project will be completed in %d days.", tasks.get(schedule.length-1).getDuration() + schedule[schedule.length-1]));
        printlnDash(limit, symbol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;

        int equal = 0;

        for (Task otherTask : ((Project) o).tasks) {
            if (tasks.stream().anyMatch(t -> t.equals(otherTask))) {
                equal++;
            }
        }

        return name.equals(project.name) && equal == tasks.size();
    }

}
