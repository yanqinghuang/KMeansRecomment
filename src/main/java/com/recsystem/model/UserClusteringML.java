package com.recsystem.model;

import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.CosineDistance;
import net.sf.javaml.tools.data.FileHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter.*;
import net.sf.javaml.clustering.Clusterer;


/**
 * Created by yanqing on 2016/1/7.
 */
public class UserClusteringML {
    /* Load a dataset */
    private static String path = "D:/DataSet/userOperationDataset/";
    private static File targetFile = new File(path + "mlResult.txt");

    public static void main(String[] args) {
        int k = 6;
        BufferedWriter writer = null;
        try {
//            Dataset data = FileHandler.loadSparseDataset(new File(path + "dataTest.txt"), 0, "\t", ":");
            Dataset data = FileHandler.loadDataset(new File("D:/DataSet/userOperationDataset/mergeData.txt"), ",");
            writer = new BufferedWriter(new FileWriter(targetFile));
            Clusterer km = new KMeans(k, 500, new CosineDistance());
//            Clusterer km = new KMeans(k);
            Dataset[] clusters = km.cluster(data);
            ClusterEvaluation sse= new SumOfSquaredErrors(new CosineDistance());
            double score=sse.score(clusters);
            System.out.println("Clustering sse: " + score + "\n");
            writer.write("Clustering sse: " + score + "\n");

            for (int i=0; i<k; i++){
                writer.write("Cluster" + i + " size= " + clusters[i].size() + " : " + clusters[i] + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {writer.close();} catch (Exception e) {/*ignore*/}
        }
    }
}