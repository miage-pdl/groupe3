package org.opencompare;

import org.junit.Test;
import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.CSVExporter;
import org.opencompare.api.java.io.PCMLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* Program to calculate the absolute frequencies from a PCM object.
* It calculates the frequences by cells, features, products and types.
*
* @author  Group #3 PDL
* @version 1.0
* @since   2017-10-04 
*/
public class PcmInspector {
	
	// Create Map of Frequencies
	public Map<String,HashMap<String, Integer>> mapOfFrequencies = new HashMap<String, HashMap<String, Integer>>();	
	
    // HashMap to store the processed information from the PCM file
    public HashMap<String, Integer> frequenciesCells = new HashMap<>();
    public HashMap<String, Integer> frequenciesFeatures = new HashMap<>();
    public HashMap<String, Integer> frequenciesProducts = new HashMap<>();
    public HashMap<String, Integer> frequenciesTypes = new HashMap<>();
	
    /**
     * This is the main method which is used to explore a PCM 
     * and call the procedures to storage the frequencies by cell, features
     * products and type.
     * @throws IOException
     */
    @Test
    public void testGettingStarted() throws IOException {

    		mapOfFrequencies.put("frequenciesCells", frequenciesCells);
    		mapOfFrequencies.put("frequenciesFeatures", frequenciesFeatures);
    		mapOfFrequencies.put("frequenciesProducts", frequenciesProducts);
    		mapOfFrequencies.put("frequenciesTypes", frequenciesTypes);
 		
        // Define a file representing a PCM to load
        File pcmFile = new File("pcms/Comparison_of_memory_cards_0.pcm");

        // Create a loader that can handle the file format
        PCMLoader loader = new KMFJSONLoader();

        // Create a string checker to verify if a given string is a number
        StringChecker checker = new StringChecker();
        // Load the file
        // A loader may return multiple PCM containers depending on the input format
        // A PCM container encapsulates a PCM and its associated metadata
        List<PCMContainer> pcmContainers = loader.load(pcmFile);

        String type;
       
        for (PCMContainer pcmContainer : pcmContainers) {

            // Get the PCM
            PCM pcm = pcmContainer.getPcm();
           
            // Calculate frequencies by features
            for (Feature feature : pcm.getConcreteFeatures()) {
            		generalCountCells("frequenciesFeatures", feature.getName());
        		}
            
            // Browse the cells of the PCM
            for (Product product : pcm.getProducts()) {
            		
            		// Calculate frequencies by products
            		generalCountCells("frequenciesProducts", product.getKeyContent());
            		
                for (Feature feature : pcm.getConcreteFeatures()) {

                    // Find the cell corresponding to the current feature and product
                    Cell cell = product.findCell(feature);
                   
                    // Get information contained in the cell
                    String content = cell.getContent();
                    
                    // Calculate frequencies by cells
                    generalCountCells("frequenciesCells", content);
                    
                    // Calculate frequencies by type
                    boolean isNumeric = checker.isValidNumeric(content);
                    //boolean isNumeric = cell.getContent().chars().allMatch( Character::isDigit );
                    type = isNumeric ? "Integer": "Float" ;
                    generalCountCells("frequenciesTypes", type);
                    
                }
            }
            
            // Explore all the frequencies
            exploreFrequencies(mapOfFrequencies);
            
            // Export the PCM container to CSV
            CSVExporter csvExporter = new CSVExporter();
            String csv = csvExporter.export(pcmContainer);

            // Write CSV content to file
            Path outputFile = Files.createTempFile("oc-", ".csv");
            Files.write(outputFile, csv.getBytes());
            System.out.println("PCM exported to " + outputFile);
        }

    }

    /**
     * This method is used to display the saved information in the Frequency HashMaps
     * @param mapOfFrequencies
     */
    public void exploreFrequencies(Map<String, HashMap<String, Integer>> mapOfFrequencies) {
    	
        showFrequencesHash(mapOfFrequencies.get("frequenciesCells"));
        showFrequencesHash(mapOfFrequencies.get("frequenciesFeatures"));
        showFrequencesHash(mapOfFrequencies.get("frequenciesProducts"));
        showFrequencesHash(mapOfFrequencies.get("frequenciesTypes"));    	
    	
    }
    
	/**
	 * This method is used to calculate the frequency of a value
	 * inside an specific Frequency HashMap.
	 * @param frequencyName
	 * @param content
	 */
	public void generalCountCells(String frequencyName, String content) {

		mapOfFrequencies.get(frequencyName).computeIfAbsent(content, val -> 0);
		mapOfFrequencies.get(frequencyName).computeIfPresent(content, (key, oldVal) -> oldVal + 1);		
		
    }
	
	/**
	 * This method is used to explore an specific HashMap of the 
	 * registered frequencies.
	 * @param frequency
	 */
	private void showFrequencesHash(HashMap<String, Integer> frequency) {

    		for (String key : frequency.keySet()) {
    			System.out.format("%n%-100s%-10s", key, frequency.get(key));
    		}
    		
	}

}
