import java.util.ArrayList;

/**
 * This class accomplishes Mission POWER GRID OPTIMIZATION
 */
public class PowerGridOptimization {
    private ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour;

    public PowerGridOptimization(ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour){
        this.amountOfEnergyDemandsArrivingPerHour = amountOfEnergyDemandsArrivingPerHour;
    }

    public ArrayList<Integer> getAmountOfEnergyDemandsArrivingPerHour() {
        return amountOfEnergyDemandsArrivingPerHour;
    }
    /**
     *     Function to implement the given dynamic programming algorithm
     *     SOL(0) <- 0
     *     HOURS(0) <- [ ]
     *     For{j <- 1...N}
     *         SOL(j) <- max_{0<=i<j} [ (SOL(i) + min[ E(j), P(j − i) ] ]
     *         HOURS(j) <- [HOURS(i), j]
     *     EndFor
     *
     * @return OptimalPowerGridSolution
     */

    private int calculateSol(int[] array, int j, int i) {
        return array[i] + Math.min(this.amountOfEnergyDemandsArrivingPerHour.get(j - 1), (j - i) * (j - i));
    }

    private int findMaxIndex(int[] array, int j) {
        int max = 0;
        for (int i = 1; i < j; i++) {
            if (calculateSol(array, j, max) < calculateSol(array, j, i)) {
                max = i;
            }
        }
        return max;
    }

    public OptimalPowerGridSolution getOptimalPowerGridSolutionDP(){
        // TODO: YOUR CODE HERE

        int size = this.amountOfEnergyDemandsArrivingPerHour.size();

        ArrayList<Integer>[] hoursDpArray = new ArrayList[size + 1];
        int[] solDpArray = new int[size + 1];

        solDpArray[0] = 0;
        hoursDpArray[0] = new ArrayList<Integer>();

        for (int j = 1; j <= size; j++) {
            int i = findMaxIndex(solDpArray, j);
            solDpArray[j] = calculateSol(solDpArray, j, i);
            hoursDpArray[j] = new ArrayList<Integer>(hoursDpArray[i]);
            hoursDpArray[j].add(j);
        }

        OptimalPowerGridSolution powerGridSolution = new OptimalPowerGridSolution(solDpArray[size], hoursDpArray[size]);

        return powerGridSolution;
    }
}
