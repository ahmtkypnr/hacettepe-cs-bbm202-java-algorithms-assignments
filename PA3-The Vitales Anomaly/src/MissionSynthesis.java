import java.util.*;

// Class representing the Mission Synthesis
public class MissionSynthesis {

    // Private fields
    private final List<MolecularStructure> humanStructures; // Molecular structures for humans
    private final ArrayList<MolecularStructure> diffStructures; // Anomalies in Vitales structures compared to humans

    // Constructor
    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
    }

    // Method to synthesize bonds for the serum
    public List<Bond> synthesizeSerum() {
        List<Bond> serum = new ArrayList<>();
        class UnionFind<T> {
            private Map<T, T> parent = new HashMap<>();
            private Map<T, Integer> rank = new HashMap<>();

            /**
             * Adds an element as a new set.
             */
            public void add(T x) {
                if (!parent.containsKey(x)) {
                    parent.put(x, x);
                    rank.put(x, 0);
                }
            }

            /**
             * Finds the representative of the set containing 'x'.
             */
            public T find(T x) {
                if (!parent.containsKey(x)) {
                    parent.put(x, x);
                    rank.put(x, 0);
                }
                if (!x.equals(parent.get(x))) {
                    parent.put(x, find(parent.get(x)));  // Path compression
                }
                return parent.get(x);
            }

            /**
             * Unites the sets containing 'x' and 'y'.
             */
            public void union(T x, T y) {
                T rootX = find(x);
                T rootY = find(y);

                if (!rootX.equals(rootY)) {
                    int rankX = rank.get(rootX);
                    int rankY = rank.get(rootY);
                    if (rankX < rankY) {
                        parent.put(rootX, rootY);
                    } else if (rankX > rankY) {
                        parent.put(rootY, rootX);
                    } else {
                        parent.put(rootY, rootX);
                        rank.put(rootX, rank.get(rootX) + 1);  // Increment the rank
                    }
                }
            }

            /**
             * Returns true if 'x' and 'y' are in the same set.
             */
            public boolean connected(T x, T y) {
                return find(x).equals(find(y));
            }
        }

        /* YOUR CODE HERE */
        ArrayList<Bond> edges = new ArrayList<>();
        ArrayList<Molecule> molecules = new ArrayList<>();

        for (MolecularStructure structure : humanStructures) {
            int minValue = Integer.MAX_VALUE;
            Molecule minMolecule = null;
            for (Molecule molecule : structure.getMolecules()) {
                if (molecule.getBondStrength() < minValue) {
                    minValue = molecule.getBondStrength();
                    minMolecule = molecule;
                }
            }
            molecules.add(minMolecule);
        }

        for (MolecularStructure structure : diffStructures) {
            int minValue = Integer.MAX_VALUE;
            Molecule minMolecule = null;
            for (Molecule molecule : structure.getMolecules()) {
                if (molecule.getBondStrength() < minValue) {
                    minValue = molecule.getBondStrength();
                    minMolecule = molecule;
                }
            }
            molecules.add(minMolecule);
        }

        for (int i = 0; i < molecules.size(); i++) {
            for (int j = i + 1; j < molecules.size(); j++) {
                Molecule molecule1 = molecules.get(i);
                Molecule molecule2 = molecules.get(j);
                double bondStrength = ((double) molecule1.getBondStrength() + (double) molecule2.getBondStrength()) / 2.0;
                edges.add(new Bond(molecule1, molecule2, bondStrength));
            }
        }

        Comparator comparator = Comparator.comparingDouble(Bond::getWeight);
        edges.sort(comparator);

        UnionFind<Molecule> unionFind = new UnionFind<>();

        for (Molecule molecule : molecules) {
            unionFind.add(molecule);
        }

        int size = molecules.size() - 1;
        for (Bond edge : edges) {
            if (serum.size() >= size) {
                break;
            }

            Molecule u = edge.getFrom();
            Molecule v = edge.getTo();

            if (!unionFind.connected(u, v)) {
                serum.add(edge);
                unionFind.union(u, v);
            }
        }

        return serum;
    }

    // Method to print the synthesized bonds
    public void printSynthesis(List<Bond> serum) {

        /* YOUR CODE HERE */
        MolecularStructure humanStructure = new MolecularStructure();
        MolecularStructure vitalesStructure = new MolecularStructure();

        for (MolecularStructure structure : humanStructures) {
            int minValue = Integer.MAX_VALUE;
            Molecule minMolecule = null;
            for (Molecule molecule : structure.getMolecules()) {
                if (molecule.getBondStrength() < minValue) {
                    minValue = molecule.getBondStrength();
                    minMolecule = molecule;
                }
            }
            humanStructure.addMolecule(minMolecule);
        }

        for (MolecularStructure structure : diffStructures) {
            int minValue = Integer.MAX_VALUE;
            Molecule minMolecule = null;
            for (Molecule molecule : structure.getMolecules()) {
                if (molecule.getBondStrength() < minValue) {
                    minValue = molecule.getBondStrength();
                    minMolecule = molecule;
                }
            }
            vitalesStructure.addMolecule(minMolecule);
        }

        System.out.println("Typical human molecules selected for synthesis: " + humanStructure.toString());
        System.out.println("Vitales molecules selected for synthesis: " + vitalesStructure.toString());
        System.out.println("Synthesizing the serum...");
        double sum = 0;
        for (Bond bond : serum) {
            double strength = bond.getWeight();
            System.out.println("Forming a bond between " + bond.getFrom() + " - " + bond.getTo() + " with strength " + String.format(Locale.US, "%.2f", strength));
            sum += strength;
        }
        System.out.println("The total serum bond strength is " + String.format(Locale.US, "%.2f", sum));
    }
}
