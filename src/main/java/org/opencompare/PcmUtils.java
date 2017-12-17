package org.opencompare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

/**
 * Class to create files with the information produced by the PCM Inspector.
 *
 * @author Group #3 PDL
 * @version 1.0
 * @since 2017-10-04
 */
public class PcmUtils {
    private static final String[] extensions = { "pcm" };

    /**
     * Create the output file for the frequencies methods
     * @param frequency List of frequencies from the PCMs
     * @param frequencyName Name of the frequency (key name)
     * @param <K> Name of the frequency key
     * @param <V> Value of the frequencies
     * @throws IOException when isn't possible to write the file
     */
    public static <K, V> void createFile(HashMap<K, V> frequency, String frequencyName) throws IOException {
        System.out.println("Writing frequence: " + frequencyName);

        File fout2 = new File(frequencyName + ".csv");
        FileOutputStream fos2 = new FileOutputStream(fout2);
        BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(fos2));

        for (K key : frequency.keySet()) {
            String data2 = key.toString() + "," + frequency.get(key).toString();

            bw2.write(data2);
            bw2.newLine();
        }

        bw2.close();
    }

    /**
     * Imports into the program the data set to load
     * @param directory Path
     * @return A list of files
     */
    public static Collection<File> getPCMFiles(File directory) {
        return FileUtils.listFiles(directory, extensions, true);
    }
}