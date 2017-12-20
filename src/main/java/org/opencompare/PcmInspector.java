package org.opencompare;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final String FREQUENCY_CELLS = "Cells";
    public static final String FREQUENCY_PRODUCTS = "Products";
    public static final String FREQUENCY_FEATURES = "Features";
    public static final String FREQUENCY_TYPES = "Types";
    public static final String MATRIX_SIZE = "SizeMatrix";
    public static final String PCM_OBJECT_NAME = "org.opencompare.api.java.impl.value.";
    public static final Integer OUTPUT_LENGTH = 80;

    // Create Map of Frequencies
    public Map<String, HashMap<String, Integer>> mapOfFrequencies = new HashMap<>();

    // HashMap to store the processed information from the PCM file
    public HashMap<String, Integer> frequenciesCells = new HashMap<>();
    public HashMap<String, Integer> frequenciesFeatures = new HashMap<>();
    public HashMap<String, Integer> frequenciesProducts = new HashMap<>();
    public HashMap<String, Integer> frequenciesTypes = new HashMap<>();
    public HashMap<String, String> matrixSize = new HashMap<>();
    public Integer cellCounter, cellFeatures, cellProducts = 0;

    /**
     * Calculates the size of a matrix
     * @param pcm The PCM object which is going to be analyzed to found their size
     * @param verticalSize Quantity of products
     * @param horizontalSize Quantity of features
     */
    public void calculateMatrixSize(String pcm, int verticalSize, int horizontalSize, String size) {
        String result = '"'+ pcm+'"'+ ',' + verticalSize + ',' + horizontalSize + ',' + size;
        matrixSize.computeIfAbsent(pcm, val -> result);
    }

    public void calculateStatistics(String path) throws IOException {
        intializeMaps();

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
                try{
                    verticalSize = getCellandProductFrequencies(pcm);

                    int size = horizontalSize * verticalSize;
                    calculateMatrixSize(file.getName(), horizontalSize, verticalSize,Integer.toString(size) );
                }catch (Exception e){

                }

            }
        }

        // Explore all the frequencies
        exploreFrequencies(mapOfFrequencies);
        PcmUtils.createFile(matrixSize, MATRIX_SIZE);
    }

    /**
     * This method is used to display the saved information in the Frequency
     * HashMaps
     *
     * @param mapOfFrequencies Set of frequencies 
     * @throws IOException when isn't possible to write the file
     */
    public void exploreFrequencies(Map<String, HashMap<String, Integer>> mapOfFrequencies) throws IOException {
        PcmUtils.createFile(mapOfFrequencies.get("frequenciesCells"), FREQUENCY_CELLS);
        PcmUtils.createFile(mapOfFrequencies.get("frequenciesFeatures"), FREQUENCY_FEATURES);
        PcmUtils.createFile(mapOfFrequencies.get("frequenciesProducts"), FREQUENCY_PRODUCTS);
        PcmUtils.createFile(mapOfFrequencies.get("frequenciesTypes"), FREQUENCY_TYPES);
    }

    /**
     * This method is used to calculate the frequency of a value inside a specific
     * Frequency HashMap.
     *
     * @param frequencyName Name of the frequency which is going to be processed.
     * @param content Cell content that is going to be added to the frequency map. 
     */
    public void generalCountCells(String frequencyName, String content) {
        mapOfFrequencies.get(frequencyName).computeIfAbsent(content.toLowerCase(), val -> new Integer(0));
        mapOfFrequencies.get(frequencyName).computeIfPresent(content.toLowerCase(), (key, oldVal) -> oldVal + 1);
    }

    /**
     * This is the main method which is used to explore a PCM and call the
     * procedures to storage the frequencies by cell, features products and type.
     *
     * @throws IOException when theres isn't possible to write the frequencies
     */
    public void intializeMaps() throws IOException {
        mapOfFrequencies.put("frequenciesCells", frequenciesCells);
        mapOfFrequencies.put("frequenciesFeatures", frequenciesFeatures);
        mapOfFrequencies.put("frequenciesProducts", frequenciesProducts);
        mapOfFrequencies.put("frequenciesTypes", frequenciesTypes);
    }

    /**
     * Main method of the program
     * @param args Default arguments
     * @throws IOException when the path isn't accesible
     */
    public static void main(String[] args) throws IOException {
        PcmInspector pcmInspector = new PcmInspector();
        PredominantFeature predominantFeature = new PredominantFeature();
        CountPairs countPairs = new CountPairs();
        ComparePcm comparePcm = new ComparePcm();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String defaultPath = "";
        Boolean flag = false;

        while (!verifyPath(defaultPath, flag)) {
            System.out.print("Insérez la route du dossier à traiter (Par défaut: pcms): ");
            defaultPath = "pcms";

            String pathUserInput = input.readLine();

            if (!"".equals(pathUserInput.trim())) {
                try {
                    defaultPath = pathUserInput;
                } catch (Exception e) {
                    defaultPath = "pcms";
                }
            }

            flag = true;
        }

        System.out.println("Dossier à processer: " + defaultPath);

        try {
            pcmInspector.calculateStatistics(defaultPath);
            predominantFeature.getPredonimantFeatures(defaultPath);
            countPairs.getCountOfPaireValues(defaultPath);
            //comparePcm.findPcmAndCompare(defaultPath,"Comparison_of_file_comparison_tools_2","Comparison_of_VoIP_software_1");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifies if the input directory to load the data set exist or not
     * @param path Path where the PCM files are saved
     * @param flag Status to detect if the first load or not
     * @return a boolean flags which is going to indicate if the path exists or not
     */
    public static boolean verifyPath(String path, boolean flag) {
        File f = new File(path);

        if (!f.exists() && flag) {
            System.out.println("Le dossier " + path + " n'existe pas");
        }

        return f.exists();
    }

    /**
     * Gets the frequencies for the cell's values
     * @param pcm A PCM object from the API
     * @param product A Product object from the API
     */ 
    public void getCellFrequeancies(PCM pcm, Product product) throws Exception {
        for (Feature feature : pcm.getConcreteFeatures()) {

            // Find the cell corresponding to the current feature and product
            Cell cell = product.findCell(feature);
            String content;
            getCelltypeFrequencies(cell);
            // Get information contained in the cell
            content = cell.getContent();

            if (content != null) {

                // Calculate frequencies by cells0
                generalCountCells("frequenciesCells", '"' + content.replace("\n", "").replace("\r", "").replace("\"", "") + '"');
            }

        }
    }

    /**
     * Returns the vertical size of a pcm
     * @param pcm A PCM object from the API
     * @return the number of products
     */
    public int getCellandProductFrequencies(PCM pcm) throws Exception {
        int verticalSize = pcm.getProducts().size();

        for (Product product : pcm.getProducts()) {

            // Calculate frequencies by products
            getFrequenciesByProduct(product);
            getCellFrequeancies(pcm, product);
        }

        return verticalSize;
    }

    /**
     * Gets the frequencies of the types in the cells of a PCM
     * @param cell A single cell
     */
    public void getCelltypeFrequencies(Cell cell) throws Exception {


        // Calculate frequencies by type
        Value vl = cell.getInterpretation();

        if (vl != null) {
            String typeName = vl.getClass().getName().replace(PCM_OBJECT_NAME, "");

            generalCountCells("frequenciesTypes", '"' + typeName.replace("\n", "").replace("\r", "").replace("\"", "") + '"');
        }

    }

    /**
     *  Count the frequencies of the features and also return the horizontal size of the PCM
     * @param pcm A PCM object from the API
     * @return The number of features
     */
    public int getFeatureFrequencies(PCM pcm) {
        int horizontalSize = pcm.getConcreteFeatures().size();

        // Calculate frequencies by features
        for (Feature feature : pcm.getConcreteFeatures()) {
            if (feature.getName() != null) {
                generalCountCells("frequenciesFeatures", '"' + feature.getName().replace("\n", " ").replace("\r", "").replace("\"", "") + '"');
            } else {
                generalCountCells("frequenciesFeatures", "null");
            }
        }

        return horizontalSize;
    }

    /**
     * Gets the frequencies of products values inside a pcm
     * @param product A product object from the API
     */
    public void getFrequenciesByProduct(Product product) {
        try {
            if (product.getKeyContent() != null) {
                generalCountCells("frequenciesProducts", '"' + product.getKeyContent().replace("\n", "").replace("\r", "").replace("\"", "") + '"');
            } else {
                generalCountCells("frequenciesProducts", "null");
            }
        } catch (Exception e) {

        }
    }


}
