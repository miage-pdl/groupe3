package org.opencompare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

public class PcmUtils {
    private static final String[] extensions = { "pcm" };

    public static Collection<File> getPCMFiles(File directory) {
        return FileUtils.listFiles(directory, extensions, true);
    }

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
}
