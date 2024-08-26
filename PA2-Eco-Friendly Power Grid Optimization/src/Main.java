import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Main class
 */
// FREE CODE HERE
public class Main {
    public static void main(String[] args) throws IOException {

       /** MISSION POWER GRID OPTIMIZATION BELOW **/

        System.out.println("##MISSION POWER GRID OPTIMIZATION##");
        // TODO: Your code goes here
        // You are expected to read the file given as the first command-line argument to read 
        // the energy demands arriving per hour. Then, use this data to instantiate a 
        // PowerGridOptimization object. You need to call getOptimalPowerGridSolutionDP() method
        // of your PowerGridOptimization object to get the solution, and finally print it to STDOUT.

        ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour = new ArrayList<>();
        String splitBy = " ";

        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        String[] line = br.readLine().split(splitBy);
        br.close();

        for (String number : line) {
            amountOfEnergyDemandsArrivingPerHour.add(Integer.parseInt(number));
        }

        PowerGridOptimization powerGridOptimization = new PowerGridOptimization(amountOfEnergyDemandsArrivingPerHour);
        OptimalPowerGridSolution optimalPowerGridSolution = powerGridOptimization.getOptimalPowerGridSolutionDP();

        int sum = 0;
        for (int demand : amountOfEnergyDemandsArrivingPerHour) {
            sum += demand;
        }
        int satisfied = optimalPowerGridSolution.getmaxNumberOfSatisfiedDemands();
        int unsatisfied = sum - satisfied;

        System.out.println("The total number of demanded gigawatts: " + sum);
        System.out.println("Maximum number of satisfied gigawatts: " + satisfied);
        System.out.println("Hours at which the battery bank should be discharged: " + optimalPowerGridSolution.getHoursToDischargeBatteriesForMaxEfficiency().toString().replaceAll("\\[|\\]", ""));
        System.out.println("The number of unsatisfied gigawatts: " + unsatisfied);

        System.out.println("##MISSION POWER GRID OPTIMIZATION COMPLETED##");

        /** MISSION ECO-MAINTENANCE BELOW **/

        System.out.println("##MISSION ECO-MAINTENANCE##");
        // TODO: Your code goes here
        // You are expected to read the file given as the second command-line argument to read
        // the number of available ESVs, the capacity of each available ESV, and the energy requirements 
        // of the maintenance tasks. Then, use this data to instantiate an OptimalESVDeploymentGP object.
        // You need to call getMinNumESVsToDeploy(int maxNumberOfAvailableESVs, int maxESVCapacity) method
        // of your OptimalESVDeploymentGP object to get the solution, and finally print it to STDOUT.

        ArrayList<Integer> maintenanceTaskEnergyDemands = new ArrayList<>();

        BufferedReader br2 = new BufferedReader(new FileReader(args[1]));
        String[] line1 = br2.readLine().split(splitBy);
        String[] line2 = br2.readLine().split(splitBy);
        br2.close();

        int maxNumberOfAvailableESVs = Integer.parseInt(line1[0]);
        int maxESVCapacity = Integer.parseInt(line1[1]);

        for (String number : line2) {
            maintenanceTaskEnergyDemands.add(Integer.parseInt(number));
        }

        OptimalESVDeploymentGP optimalESVDeploymentGP = new OptimalESVDeploymentGP(maintenanceTaskEnergyDemands);
        int minNumESVsToDeploy = optimalESVDeploymentGP.getMinNumESVsToDeploy(maxNumberOfAvailableESVs, maxESVCapacity);

        if (minNumESVsToDeploy == -1) {
            System.out.println("Warning: Mission Eco-Maintenance Failed.");
        }
        else {
            System.out.println("The minimum number of ESVs to deploy: " + minNumESVsToDeploy);
            for (int i = 0; i < minNumESVsToDeploy; i++) {
                System.out.println("ESV " + (i + 1) + " tasks: " + optimalESVDeploymentGP.getMaintenanceTasksAssignedToESVs().get(i).toString());
            }
        }

        System.out.println("##MISSION ECO-MAINTENANCE COMPLETED##");
    }
}
