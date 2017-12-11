package org.opencompare;

import org.opencompare.api.java.*;

import java.io.*;
import java.util.*;

public class ComparePcm {



    /*
    mision OK : 1 Compare two pcm by her features{
        2 list and give pourcent of similarity }
     */

    private HashMap<Feature, Integer> featuresPCMA = new HashMap<>();
    private HashMap<Feature, Integer> featuresPCMB = new HashMap<>();
    private HashMap<Feature, Integer> featuresPCMAB = new HashMap<>();

    private HashMap<String, Integer> comprare = new HashMap<>();

    private String[][] tabUnion = null ;


    public static final String PCM_OBJECT_NAME = "org.opencompare.api.java.impl.value.";

    /*
    recuperation des pcn a comparer faire une fonction pour ca apres
     */




    /*
    recuperation des features
     */


    public void compareFeature(PCM pcmA, PCM pcmB) {

        featuresPCMAB.clear();
        int ei = 0;

        Boolean Aissmall = true;
        for (Feature feature : pcmA.getConcreteFeatures()) {
            featuresPCMA.put(feature, 0);
        }
        for (Feature feature : pcmB.getConcreteFeatures()) {
            featuresPCMB.put(feature, 0);
        }
        if (featuresPCMA.size() >= featuresPCMB.size()) {
            Aissmall = false;
            comprare.put("Features A > B ",ei);
            for (Feature feature : featuresPCMB.keySet()) {
                if (featuresPCMA.computeIfPresent(feature, (key, oldVal) -> oldVal + 1) != null) {
                    featuresPCMAB.putIfAbsent(feature, 0);
                    featuresPCMAB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                    featuresPCMB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                }
            }
        } else {
            comprare.put("Features A < B ",ei);
            for (Feature feature : featuresPCMA.keySet()) {
                if (featuresPCMB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1) != null) {
                    featuresPCMAB.putIfAbsent(feature, 0);
                    featuresPCMAB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                    featuresPCMA.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                }
            }
        }
        comprare.put(" Size A ",featuresPCMA.size());
        comprare.put(" Size B ",featuresPCMB.size());

        // Affichage des resultat
        lolAffiche(Aissmall, featuresPCMA.size() + 30, featuresPCMB.size() + 30);
        // System.out.println("");

        // System.out.print("| Features of PCM A : Sieze A = " + featuresPCMA.size() + " => ");
        for (Feature feature : featuresPCMA.keySet()) {
            if (featuresPCMA.get(feature) == 0) {
                ei++;
                // System.out.print(feature + " | ");
            }

        }
        comprare.put(pcmA.getName(),ei);
        // System.out.println("");
        // System.out.println(" Count = " + ei);
        ei = 0;
        lolAffiche(Aissmall, featuresPCMA.size() + 30, featuresPCMB.size() + 30);
        // System.out.println("");
        // System.out.print("| Features of PCM B : Sieze B = " + featuresPCMB.size() + " => ");
        for (Feature feature : featuresPCMB.keySet()) {
            if (featuresPCMB.get(feature) == 0) {
                ei++;

                // System.out.print(feature + " | ");
            }
        }
        // System.out.println("");
        // System.out.println(" Count = " + ei);
        comprare.put(pcmB.getName(),ei);
        lolAffiche(Aissmall, featuresPCMA.size() + 30, featuresPCMB.size() + 30);
        // System.out.println("");
        if (Aissmall) {
            // System.out.print("| Features of PCM A & PCM B (A small than B): => ");
            for (Feature feature : featuresPCMAB.keySet()) {
                // System.out.print(feature + " -> " + featuresPCMAB.get(feature) + " | ");
            }
        } else {
            // System.out.print("| Features of PCM A & PCM B (B small than A) : => ");
            for (Feature feature : featuresPCMAB.keySet()) {
                // // System.out.print(feature + " -> " + featuresPCMAB.get(feature) + " | ");
            }
        }
        // System.out.println("");
        // System.out.println(" Count = " + featuresPCMAB.size());
        if (featuresPCMAB.size() == 0) {
            // System.out.println(ConsoleColors.BLUE + " Count = " + featuresPCMAB.size() + " Ces desux PCM n'ont  aucune Correspondence of Feature " + ConsoleColors.RESET);
        }
        // System.out.println("");
        comprare.put("Ensemble ",featuresPCMAB.size());
        lolAffiche(Aissmall, featuresPCMA.size() + 30, featuresPCMB.size() + 30);


    }

    public void lolAffiche(Boolean small, int e, int j) {
        if (small) {
            for (int i = 0; i < e + 30; i++) {
                // System.out.print("__");
            }
        } else {
            for (int i = 0; i < j + 30; i++) {
                // System.out.print("__");
            }
        }
    }

    public void compareProduit(PCM pcmA, PCM pcmB) throws IOException {
        Boolean AiSmall = true;
        compareFeature(pcmA, pcmB);

        List<Product> productsA = new ArrayList<>();
        List<Product> productsB = new ArrayList<>();


        HashMap<Feature, Integer> featuresPCMA = new HashMap<>();
        HashMap<Feature, Integer> featuresPCMB = new HashMap<>();

        HashMap<Feature, Integer> localfeaturesPCMAB = (HashMap<Feature, Integer>) featuresPCMAB.clone();

        /*
        verifier i les pcm on des features en commun avant de commencer
         */
        if (localfeaturesPCMAB.size() != 0) {
            productsA = pcmA.getProducts();
            productsB = pcmB.getProducts();
            // System.out.println("");
            // System.out.println(" - A pr Sieze = " + productsA.size());
            // System.out.println(" - B pr Sieze = " + productsB.size());
            int i1 = 0;
            int i2 = 0;

            if (productsA.size() >= productsB.size()) {
                AiSmall = false;
                comprare.putIfAbsent("Product A < B", 0);
                for (Product product : productsB) {
                    comprare.putIfAbsent(product.getKeyContent(), 0);
                    i1++;
                    findbreak:
                    for (Product product1 : productsA) {
                        i2++;
                         if ( compareTwoProduc(i1,i2,product,product1,localfeaturesPCMAB.keySet())){
                       // if (compareTwoProducByTypeOfCell(i1, i2, product, product1, localfeaturesPCMAB.keySet())) {
                            comprare.computeIfPresent(product.getKeyContent(), (key, oldVal) -> oldVal + 1);
                            break; // all analys

                        }
                    }
                    i2 = 0;
                }

            } else {
                comprare.putIfAbsent("Product A  > B", 0);
                for (Product product : productsA) {
                    comprare.putIfAbsent(product.getKeyContent(), 0);
                    i1++;
                    findbreak:
                    for (Product product1 : productsB) {
                        i2++;
                            if ( compareTwoProduc(i1, i2, product,product1,localfeaturesPCMAB.keySet())){
                     //   if (compareTwoProducByTypeOfCell(i1, i2, product, product1, localfeaturesPCMAB.keySet())) {
                            comprare.computeIfPresent(product.getKeyContent(), (key, oldVal) -> oldVal + 1);
                            break;
                        }
                    }
                    i2 = 0;
                }
            }

      /*      i1 = 0;
            i2 = 0;
            if (productsA.size() >= productsB.size()) {
                AiSmall = false;
                comprare.putIfAbsent("Product A < B", 0);
                for (Product product : productsB) {
                    comprare.putIfAbsent(product.getKeyContent()+" type", 0);
                    i1++;
                    findbreak:
                    for (Product product1 : productsA) {
                        i2++;
                      //  if ( compareTwoProduc(i1,i2,product,product1,localfeaturesPCMAB.keySet())){
                             if (compareTwoProducByTypeOfCell(i1, i2, product, product1, localfeaturesPCMAB.keySet())) {
                            comprare.computeIfPresent(product.getKeyContent()+" type", (key, oldVal) -> oldVal + 1);
                            break; // all analys

                        }
                    }
                    i2 = 0;
                }

            } else {
                comprare.putIfAbsent("Product A  > B", 0);
                for (Product product : productsA) {
                    comprare.putIfAbsent(product.getKeyContent()+" type", 0);
                    i1++;
                    findbreak:
                    for (Product product1 : productsB) {
                        i2++;
                       // if ( compareTwoProduc(i1, i2, product,product1,localfeaturesPCMAB.keySet())){
                               if (compareTwoProducByTypeOfCell(i1, i2, product, product1, localfeaturesPCMAB.keySet())) {
                            comprare.computeIfPresent(product.getKeyContent()+" type", (key, oldVal) -> oldVal + 1);
                            break;
                        }
                    }
                    i2 = 0;
                }
            }
            */



            // System.out.println("");
            for (Product product1 : pcmA.getProducts()) {
                // System.out.println(" - " + product1.getCells() + " - ");
                // System.out.println("");
            }


            // System.out.println("");
            for (Product product1 : pcmB.getProducts()) {
                // System.out.println(" - " + product1.getCells() + " - ");
                // System.out.println("");
            }
        } else {

            // System.out.println("");
            // System.out.println(ConsoleColors.BLUE + " Ces desux PCM n'ont  aucune Correspondence of Product by Feature " + ConsoleColors.RESET);

        }

        for (String s:comprare.keySet()){
            System.out.println("KEy : " + s + " - Value => " + comprare.get(s));
        }
        System.out.println("Resultat Similarite: " );
        File fout = new File(pcmA.getName()+"-"+pcmB.getName()  + ".csv");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (String s : comprare.keySet()) {
            String data = s.toString() + "," + comprare.get(s).toString();
            bw.write(data);
            bw.newLine();
        }
        bw.close();

    }

    /**
     * Compare deux produits par la valeur des cells pour la quelle il partage le meme feature
     *
     * @param i1
     * @param i2
     * @param productA produit A
     * @param productB produit A
     * @param features liste des features
     * @return true si les produit Correspondence
     */
    public Boolean compareTwoProduc(int i1, int i2, Product productA, Product productB, Set<Feature> features) {
        int i = 0;
        /*
        pour ameliorer on peut retirer les produits deja trouver
        Mais si un produit existe en deux examplaires ??? on fais quoi ??
        ??? probleme d'unicite des product
         */
        for (Feature feature : features) {
            if (productA.findCell(feature).equals(productB.findCell(feature))) {
                i++;
            }
        }
        if (i != features.size()) {
            if (i >= (features.size() / 2)) {
                // System.out.println(ConsoleColors.BLUE + "taux  Correspondence Produit " + i1 + " Produit =" + i2 + " => " + i + " / " + features.size() + ConsoleColors.RESET);
            } else {
                // System.out.println(ConsoleColors.RED + "taux  Correspondence Produit " + i1 + " Produit =" + i2 + " => " + i + " / " + features.size() + ConsoleColors.RESET);
            }
        } else {
            // System.out.println(ConsoleColors.GREEN + "taux  Correspondence Produit Parfait " + i1 + " Produit =" + i2 + " => " + i + " / " + features.size() + ConsoleColors.RESET);
            return true;
        }

        return false;

    }


    public Boolean compareTwoProducByTypeOfCell(int i1, int i2, Product productA, Product productB, Set<Feature> features) {
        int i = 0;

        /*
        pour ameliorer on peut retirer les produits deja trouver
        Mais si un produit existe en deux examplaires ??? on fais quoi ??
        ??? probleme d'unicite des product
         */
String s1 = "" ;
String s2 = ""+null ;
String[] ss1 = null ;
String[] ss2 = null ;
        for (Feature feature : features) {
            System.out.println(" features  -> "+feature+" size : " +features.size());
            s1 =""+ productA.findCell(feature).getInterpretation() ;
            System.out.println(" ?  -> "+s1.length());
            ss1 = s1.split("@");
            if (s1.length()>4){
                System.out.println(" ?  -> "+ss1[0]);
                System.out.println(" features  -> "+feature+" type : " +ss1[0].replace(PCM_OBJECT_NAME, "")+ " - "+ss1[1]);
            }
            s2 =""+ productB.findCell(feature).getInterpretation() ;
            ss2 = s2.split("@");
            if (ss2[0]!=null || ss2[0]!="null"){

                System.out.println(" features  -> "+feature+" type : " +ss2[0].replace(PCM_OBJECT_NAME, "")+ " - "+ss2[1]);
            }

        if ((s1!=null) || (s2!=null)){
            if (
                    (productA.findCell(feature).getInterpretation())
                            ==(
                                    (productB.findCell(feature).getInterpretation()))) {
                // System.out.println(i1 + " -> " + productA.findCell(feature).getInterpretation().getClass().getName().replace(PCM_OBJECT_NAME, "") + " | " + i2 + " -> " + productB.findCell(feature).getInterpretation().getClass().getName().replace(PCM_OBJECT_NAME, ""));
                i++;
            } else {
                // System.out.println(ConsoleColors.RED + i1 + " -> " + productA.findCell(feature).getInterpretation().getClass().getName().replace(PCM_OBJECT_NAME, "") + " | " + i2 + " -> " + productB.findCell(feature).getInterpretation().getClass().getName().replace(PCM_OBJECT_NAME, "") + ConsoleColors.RESET);

            }
        }
        }
        if (i != features.size()) {
            if (i >= (features.size() / 2)) {
                // System.out.println(ConsoleColors.BLUE + "taux type Correspondence Produit " + i1 + " Produit =" + i2 + " => " + i + " / " + features.size() + ConsoleColors.RESET);
            } else {
                // System.out.println(ConsoleColors.RED + "taux type Correspondence Produit " + i1 + " Produit =" + i2 + " => " + i + " / " + features.size() + ConsoleColors.RESET);
            }
        } else {
            // // System.out.println(ConsoleColors.GREEN + "taux type Correspondence Produit Parfait " + i1 + " Produit =" + i2 + " => " + i + " / " + features.size() + ConsoleColors.RESET);
            return true;
        }

        return false;

    }


}
