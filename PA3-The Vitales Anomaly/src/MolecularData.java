import java.util.*;

// Class representing molecular data
public class MolecularData {

    // Private fields
    private final List<Molecule> molecules; // List of molecules

    // Constructor
    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    // Getter for molecules
    public List<Molecule> getMolecules() {
        return molecules;
    }

    // Method to identify molecular structures
    // Return the list of different molecular structures identified from the input data

    public List<MolecularStructure> identifyMolecularStructures() {
        ArrayList<MolecularStructure> structures = new ArrayList<>();

        /* YOUR CODE HERE */

        HashMap<String, Molecule> moleculeHashMap = new HashMap<>();
        HashMap<Molecule, Boolean> visited = new HashMap<>();
        ArrayList<Molecule> molecules = new ArrayList<>();

        for (Molecule molecule : this.molecules) {
            moleculeHashMap.put(molecule.getId(), molecule);
            visited.put(molecule, false);
            molecules.add(molecule);
        }

        for (Molecule molecule : molecules) {
            for (String bond : molecule.getBonds()) {
                Molecule molecule1 = moleculeHashMap.get(bond);
                if (!molecule1.getBonds().contains(molecule.getId())) {
                    molecule1.getBonds().add(molecule.getId());
                }
            }
        }
        HashSet<Molecule> moleculeCopy = new HashSet<>(molecules);

        while (!moleculeCopy.isEmpty()) {
            Stack<Molecule> stack = new Stack<>();
            stack.push(moleculeCopy.iterator().next());
            MolecularStructure molecularStructure = new MolecularStructure();

            while (!stack.empty()) {
                Molecule vertex = stack.pop();
                if (!visited.get(vertex)) {
                    visited.put(vertex, true);
                    moleculeCopy.remove(vertex);
                    molecularStructure.addMolecule(vertex);

                    for (String adjVertex : vertex.getBonds()) {
                        if (!visited.get(moleculeHashMap.get(adjVertex))) {
                            stack.push(moleculeHashMap.get(adjVertex));
                        }
                    }
                }
            }
            structures.add(molecularStructure);
        }

        return structures;
    }

    // Method to print given molecular structures
    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {
        
        /* YOUR CODE HERE */
        int size = molecularStructures.size();
        switch (species) {
            case "typical humans":
                System.out.println(size + " molecular structures have been discovered in typical humans.");
                break;
            case "Vitales individuals":
                System.out.println(size + " molecular structures have been discovered in Vitales individuals.");
                break;
            default:
                break;
        }
        for (int i = 0; i < size; i++) {
            System.out.println("Molecules in Molecular Structure " + (i + 1) + ": " + molecularStructures.get(i).toString());
        }
    }

    // Method to identify anomalies given a source and target molecular structure
    // Returns a list of molecular structures unique to the targetStructure only
    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> sourceStructures, List<MolecularStructure> targeStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>();
        
        /* YOUR CODE HERE */
        for (MolecularStructure vitaleStructure : targeStructures) {
            boolean find = false;
            for (MolecularStructure humanStructure : sourceStructures) {
                if (vitaleStructure.toString().equals(humanStructure.toString())) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                anomalyList.add(vitaleStructure);
            }
        }

        return anomalyList;
    }

    // Method to print Vitales anomalies
    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {

        /* YOUR CODE HERE */

        System.out.println("Molecular structures unique to Vitales individuals:");
        for (MolecularStructure structure : molecularStructures) {
            System.out.println(structure.toString());
        }

    }
}
