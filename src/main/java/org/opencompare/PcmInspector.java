package org.opencompare;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

/**
 * Program to calculate the absolute frequencies from a PCM object. It
 * calculates the frequencies by cells, features, products and types.
 *
 * @author Group #3 PDL
 * @version 1.0
 * @since 2017-10-04
 */
public class PcmInspector {

    // Create Map of Frequencies
    public Map<String, HashMap<String, Integer>> mapOfFrequencies = new HashMap<>();
    // HashMap to store the processed information from the PCM file
    public HashMap<String, Integer> frequenciesCells = new HashMap<>();
    public HashMap<String, Integer> frequenciesFeatures = new HashMap<>();
    public HashMap<String, Integer> frequenciesProducts = new HashMap<>();
    public HashMap<String, Integer> frequenciesTypes = new HashMap<>();
    public HashMap<String, String> matrixSize = new HashMap<>();
    public Integer cellCounter, cellFeatures, cellProducts = 0;

    public static final String FREQUENCY_CELLS = "Cells";
    public static final String FREQUENCY_PRODUCTS = "Products";
    public static final String FREQUENCY_FEATURES = "Features";
    public static final String FREQUENCY_TYPES = "Types";
    public static final String MATRIX_SIZE = "SizeMatrix";
    public static final String PCM_OBJECT_NAME = "org.opencompare.api.java.impl.value.";
    public static final Integer OUTPUT_LENGTH = 80;


    /**
     * This is the main method which is used to explore a PCM and call the
     * procedures to storage the frequencies by cell, features products and type.
     *
     * @throws IOException
     */

    public void intialiceMaps() throws IOException {
        mapOfFrequencies.put("frequenciesCells", frequenciesCells);
        mapOfFrequencies.put("frequenciesFeatures", frequenciesFeatures);
        mapOfFrequencies.put("frequenciesProducts", frequenciesProducts);
        mapOfFrequencies.put("frequenciesTypes", frequenciesTypes);

    }

    public void calculateStatistiques(String path) throws IOException {
        intialiceMaps();
        int verticalSize = 0;
        int horizontalSize = 0;
        File directory = new File(path);
        List<File> files = (List<File>) PcmUtils.getPCMFiles(directory);

        for (File file : files) {

            // System.out.println("Procesing file: " + file.getCanonicalPath());

            // Define a file representing a PCM to load
            File pcmFile = new File(file.getCanonicalPath());

            // Create a loader that can handle the file format
            PCMLoader loader = new KMFJSONLoader();

            // Create a string checker to verify if a given string is a number

            // Load the file
            // A loader may return multiple PCM containers depending on the input format
            // A PCM container encapsulates a PCM and its associated metadata
            List<PCMContainer> pcmContainers = loader.load(pcmFile);

            // String type;

            for (PCMContainer pcmContainer : pcmContainers) {

                // Get the PCM
                PCM pcm = pcmContainer.getPcm();

                horizontalSize = getFeatureFrequencies(pcm);

                // Browse the cells of the PCM
                verticalSize = getCellandProductFrequencies(pcm);
                calculateMatrixSize(pcm, horizontalSize, verticalSize);
            }

        }

        // Explore all the frequencies
        exploreFrequencies(mapOfFrequencies);
        PcmUtils.createFrequenciesFile(matrixSize, MATRIX_SIZE);
    }

    /**
     * This method is used to display the saved information in the Frequency
     * HashMaps
     *
     * @param mapOfFrequencies
     * @throws IOException
     */
    public void exploreFrequencies(Map<String, HashMap<String, Integer>> mapOfFrequencies) throws IOException {

        PcmUtils.createFrequenciesFile(mapOfFrequencies.get("frequenciesCells"), FREQUENCY_CELLS);
        PcmUtils.createFrequenciesFile(mapOfFrequencies.get("frequenciesFeatures"), FREQUENCY_FEATURES);
        PcmUtils.createFrequenciesFile(mapOfFrequencies.get("frequenciesProducts"), FREQUENCY_PRODUCTS);
        PcmUtils.createFrequenciesFile(mapOfFrequencies.get("frequenciesTypes"), FREQUENCY_TYPES);

    }

    /**
     * This method is used to calculate the frequency of a value inside a specific
     * Frequency HashMap.
     *
     * @param frequencyName
     * @param content
     */
    public void generalCountCells(String frequencyName, String content) {

        mapOfFrequencies.get(frequencyName).computeIfAbsent(content.toLowerCase(), val -> 0);
        mapOfFrequencies.get(frequencyName).computeIfPresent(content.toLowerCase(), (key, oldVal) -> oldVal + 1);

    }

    public int getFeatureFrequencies(PCM pcm) {
    	
        int horizontalSize = pcm.getConcreteFeatures().size();
        // Calculate frequencies by features
        for (Feature feature : pcm.getConcreteFeatures()) {
            if (feature.getName() != null) {
                generalCountCells("frequenciesFeatures", '"' + feature.getName() + '"');
            } else {
                generalCountCells("frequenciesFeatures", "null");
            }
        }
        return horizontalSize;
        
    }

    public void getCellFrequeancies(PCM pcm, Product product) {
    	
        for (Feature feature : pcm.getConcreteFeatures()) {

            // Find the cell corresponding to the current feature and product
            Cell cell = product.findCell(feature);
            getCelltypeFrequencies(cell);
            String content;
            try {
                // Get information contained in the cell
                content = cell.getContent();
                if (content != null) {
                    // Calculate frequencies by cells0
                    generalCountCells("frequenciesCells", '"' + content + '"');
                }
            } catch (Exception e) {
                // System.out.println("Error reading cell content");
            }

        }

    }

    public void getCelltypeFrequencies(Cell cell) {
    	
        try {
            // Calculate frequencies by type
            Value vl = cell.getInterpretation();
            if (vl != null) {
            		String typeName = vl.getClass().getName().replace(PCM_OBJECT_NAME, "");
                generalCountCells("frequenciesTypes", '"' +  typeName + '"');
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

    public void getFrequenciesByProduct(Product product) {
    	
        try {
            if (product.getKeyContent() != null) {
            		
                generalCountCells("frequenciesProducts", '"' + product.getKeyContent() + '"');
            } else {
                generalCountCells("frequenciesProducts", "null");
            }
        } catch (Exception e) {
            // System.out.println("Feature error");
        }
        
    }

    public int getCellandProductFrequencies(PCM pcm) {
    	
        int verticalSize = pcm.getProducts().size();
        for (Product product : pcm.getProducts()) {

            // Calculate frequencies by products
            getFrequenciesByProduct(product);

            getCellFrequeancies(pcm, product);
        }
        
        return verticalSize;
        
    }

    public void calculateMatrixSize(PCM pcm, int verticalSize, int horizontalSize) {
    	
        matrixSize.computeIfAbsent(pcm.getName(), val -> verticalSize + "X" + horizontalSize);
        
    }


    public static void main(String[] args) {
    	
        PcmInspector pcmInspector = new PcmInspector();
        System.out.println("Insérez la route du dossier à traiter");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.next();
        try {
            pcmInspector.calculateStatistiques(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}