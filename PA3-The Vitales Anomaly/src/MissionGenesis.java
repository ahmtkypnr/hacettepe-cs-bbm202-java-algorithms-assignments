// Class representing the mission of Genesis
import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MissionGenesis {

    // Private fields
    private MolecularData molecularDataHuman; // Molecular data for humans
    private MolecularData molecularDataVitales; // Molecular data for Vitales

    // Getter for human molecular data
    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    // Getter for Vitales molecular data
    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    // Method to read XML data from the specified filename
    // This method should populate molecularDataHuman and molecularDataVitales fields once called
    public void readXML(String filename) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File(filename));

            document.getDocumentElement().normalize();

            Node nodeHuman = document.getElementsByTagName("HumanMolecularData").item(0);
            Element human = (Element) nodeHuman;
            NodeList humanMolecules = human.getElementsByTagName("Molecule");

            List<Molecule> moleculesInputHuman = new ArrayList<>();
            for (int i = 0; i < humanMolecules.getLength(); i++) {
                Node nodeMolecule = humanMolecules.item(i);
                Element molecule = (Element) nodeMolecule;

                String id = molecule.getElementsByTagName("ID").item(0).getTextContent();
                int bondStrength = Integer.parseInt(molecule.getElementsByTagName("BondStrength").item(0).getTextContent());

                Node nodeBonds = molecule.getElementsByTagName("Bonds").item(0);
                Element bonds = (Element) nodeBonds;
                NodeList bondList = bonds.getElementsByTagName("MoleculeID");

                List<String> bondsInput = new ArrayList<>();
                for (int j = 0; j < bondList.getLength(); j++) {
                    Node nodeBond = bondList.item(j);
                    Element bond = (Element) nodeBond;

                    bondsInput.add(bond.getTextContent());
                }
                moleculesInputHuman.add(new Molecule(id, bondStrength, bondsInput));
            }
            this.molecularDataHuman = new MolecularData(moleculesInputHuman);

            Node nodeVitales = document.getElementsByTagName("VitalesMolecularData").item(0);
            Element vitales = (Element) nodeVitales;
            NodeList vitalesMolecules = vitales.getElementsByTagName("Molecule");

            List<Molecule> moleculesInputVitales = new ArrayList<>();
            for (int i = 0; i < vitalesMolecules.getLength(); i++) {
                Node nodeMolecule = vitalesMolecules.item(i);
                Element molecule = (Element) nodeMolecule;

                String id = molecule.getElementsByTagName("ID").item(0).getTextContent();
                int bondStrength = Integer.parseInt(molecule.getElementsByTagName("BondStrength").item(0).getTextContent());

                Node nodeBonds = molecule.getElementsByTagName("Bonds").item(0);
                Element bonds = (Element) nodeBonds;
                NodeList bondList = bonds.getElementsByTagName("MoleculeID");

                List<String> bondsInput = new ArrayList<>();
                for (int j = 0; j < bondList.getLength(); j++) {
                    Node nodeBond = bondList.item(j);
                    Element bond = (Element) nodeBond;

                    bondsInput.add(bond.getTextContent());
                }
                moleculesInputVitales.add(new Molecule(id, bondStrength, bondsInput));
            }
            this.molecularDataVitales = new MolecularData(moleculesInputVitales);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
