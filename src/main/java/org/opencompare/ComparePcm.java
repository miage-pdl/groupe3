package org.opencompare;

import org.opencompare.api.java.*;

import java.util.*;

public class ComparePcm {



    /*
    mision : 1 Compare two pcm by her features{
        2 list and give pourcent of similarity }
     */

    HashMap<Feature, Integer> featuresPCMA = new HashMap<>();
    HashMap<Feature, Integer> featuresPCMB = new HashMap<>();

    HashMap<Feature, Integer> featuresPCMAB = new HashMap<>();

    /*
    recuperation des pcn a comparer faire une fonction pour ca apres
     */




    /*
    recuperation des features
     */


    public void compareFeature(PCM pcmA, PCM pcmB) {


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
            for (Feature feature : featuresPCMB.keySet()) {
                if (featuresPCMA.computeIfPresent(feature, (key, oldVal) -> oldVal + 1) != null) {
                    featuresPCMAB.computeIfAbsent(feature, val -> 0);
                    featuresPCMAB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                    featuresPCMB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                }
            }
        } else {
            for (Feature feature : featuresPCMA.keySet()) {
                if (featuresPCMB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1) != null) {
                    featuresPCMAB.computeIfAbsent(feature, val -> 0);
                    featuresPCMAB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                    featuresPCMA.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                }
            }
        }

        // Affichage des resultat
        if (Aissmall) {
            for (int i = 0; i < featuresPCMB.size() + 30; i++) {
                System.out.print("__");
            }
        } else {
            for (int i = 0; i < featuresPCMA.size() + 30; i++) {
                System.out.print("__");
            }
        }
        System.out.println("");

        System.out.print("| Features of PCM A : Sieze A = " + featuresPCMA.size() + " => ");
        for (Feature feature : featuresPCMA.keySet()) {
            if (featuresPCMA.get(feature) == 0) {
                ei++;
                System.out.print(feature + " | ");
            }

        }
        System.out.println("");
        System.out.println(" Count = " + ei);
        ei = 0;
        if (Aissmall) {
            for (int i = 0; i < featuresPCMB.size() + 30; i++) {
                System.out.print("__");
            }
        } else {
            for (int i = 0; i < featuresPCMA.size() + 30; i++) {
                System.out.print("__");
            }
        }
        System.out.println("");
        System.out.print("| Features of PCM B : Sieze B = " + featuresPCMB.size() + " => ");
        for (Feature feature : featuresPCMB.keySet()) {
            if (featuresPCMB.get(feature) == 0) {
                ei++;
                System.out.print(feature + " | ");
            }
        }
        System.out.println("");
        System.out.println(" Count = " + ei);
        ei = 0;
        if (Aissmall) {
            for (int i = 0; i < featuresPCMB.size() + 30; i++) {
                System.out.print("__");
            }
        } else {
            for (int i = 0; i < featuresPCMA.size() + 30; i++) {
                System.out.print("__");
            }
        }
        System.out.println("");
        if (Aissmall) {
            System.out.print("| Features of PCM A & PCM B (A small than B): => ");
            for (Feature feature : featuresPCMAB.keySet()) {
                System.out.print(feature + " -> " + featuresPCMAB.get(feature) + " | ");
            }
        } else {
            System.out.print("| Features of PCM A & PCM B (B small than A) : => ");
            for (Feature feature : featuresPCMAB.keySet()) {
                System.out.print(feature + " -> " + featuresPCMAB.get(feature) + " | ");
            }
        }
        System.out.println("");
        System.out.println(" Count = " + featuresPCMAB.size());
        System.out.println("");
        if (Aissmall) {
            for (int i = 0; i < featuresPCMAB.size() + 30; i++) {
                System.out.print("__");
            }
        } else {
            for (int i = 0; i < featuresPCMA.size() + 30; i++) {
                System.out.print("__");
            }
        }


    }

    public void compareProduit(PCM pcmA, PCM pcmB) {
    }

}
