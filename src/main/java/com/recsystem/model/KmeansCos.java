package com.recsystem.model;

/**
 * Created by yanqing on 2016/1/8.
 */

import com.recsystem.DataHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * K��ֵ�����㷨
 */
public class KmeansCos {
    private int k;// �ֳɶ��ٴ�
    private int m;// ��������

    private int dataSetLength;// ���ݼ�Ԫ�ظ����������ݼ��ĳ���
    private ArrayList<float[]> dataSet;// ���ݼ�����
    private ArrayList<float[]> center;// ��������
    private ArrayList<ArrayList<float[]>> cluster; // ��
    private ArrayList<Float> jc;// ���ƽ���ͣ�kԽ�ӽ�dataSetLength�����ԽС
    private Random random;

    private static String path = "D:/DataSet/userOperationDataset/";
    private static File dataFile = new File(path + "smallDataset.txt");
    private static File targetFile = new File(path + "cosKmeansResult.txt");
    /**
     * ����������ԭʼ���ݼ�
     *
     * @param dataSet
     */

    public void setDataSet(ArrayList<float[]> dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * ��ȡ�������
     *
     * @return �����
     */

    public ArrayList<ArrayList<float[]>> getCluster() {
        return cluster;
    }

    /**
     * ���캯����������Ҫ�ֳɵĴ�����
     *
     * @param k
     *            ������,��k<=0ʱ������Ϊ1����k��������Դ�ĳ���ʱ����Ϊ����Դ�ĳ���
     */
    public KmeansCos(int k) {
        if (k <= 0) {
            k = 1;
        }
        this.k = k;
    }

    /**
     * ��ʼ��
     */
    private void init() {
        m = 0;
        random = new Random();
        if (dataSet == null || dataSet.size() == 0) {
            initDataSet();
            System.out.println("initDataSet...");
        }
        dataSetLength = dataSet.size();
        if (k > dataSetLength) {
            k = dataSetLength;
        }
        center = initCenters();
        cluster = initCluster();
        jc = new ArrayList<Float>();
    }

    /**
     * ���������δ��ʼ�����ݼ���������ڲ��������ݼ�
     */
    private void initDataSet() {
        DataHandler dh = new DataHandler();
        dh.dataHandling();
        dataSet = new ArrayList<float[]>();

        // ����{6,3}��һ���ģ����Գ���Ϊ15�����ݼ��ֳ�14�غ�15�ص���Ϊ0
//        float[][] dataSetArray = new float[][] { { 8, 2 }, { 3, 4 }, { 2, 5 },
//                { 4, 2 }, { 7, 3 }, { 6, 2 }, { 4, 7 }, { 6, 3 }, { 5, 3 },
//                { 6, 3 }, { 6, 9 }, { 1, 6 }, { 3, 9 }, { 4, 1 }, { 8, 6 } };
        for (Map.Entry<String, float[]> entry : DataHandler.idOperationsMap.entrySet()) {
            dataSet.add(entry.getValue());
        }
    }

    /**
     * ��ʼ���������������ֳɶ��ٴؾ��ж��ٸ����ĵ�
     *
     * @return ���ĵ㼯
     */
    private ArrayList<float[]> initCenters() {
        ArrayList<float[]> center = new ArrayList<float[]>();
        int[] randoms = new int[k];
        boolean flag;

        int temp = random.nextInt(dataSetLength);
        randoms[0] = temp;
        for (int i = 1; i < k; i++) {
            flag = true;
            while (flag) {
                temp = random.nextInt(dataSetLength);
                int j = 0;
                while (j < i) {
                    if (temp == randoms[j]) {
                        break;
                    }
                    j++;
                }
                if (j == i) {
                    flag = false;
                }
            }
            randoms[i] = temp;
        }

        for (int i = 0; i < k; i++) {
            center.add(dataSet.get(randoms[i]));// ���ɳ�ʼ����������
        }
        return center;
    }

    /**
     * ��ʼ���ؼ���
     *
     * @return һ����Ϊk�صĿ����ݵĴؼ���
     */
    private ArrayList<ArrayList<float[]>> initCluster() {
        ArrayList<ArrayList<float[]>> cluster = new ArrayList<ArrayList<float[]>>();
        for (int i = 0; i < k; i++) {
            cluster.add(new ArrayList<float[]>());
        }
        return cluster;
    }

    /**
     * ����������֮��ľ���
     * �˴����������������ƶȾ���
     * @param element
     *            ��1
     * @param center
     *            ��2
     * @return ����
     */
    private double distance(float[] element, float[] center) {
        float innerproduct = 0.0f;
        float aa = 0.0f;
        float bb = 0.0f;

        for (int i=0; i<DataHandler.DIMENSIONS; i++){
            innerproduct +=  element[i] * center[i];
        }
        for (int i=0; i<DataHandler.DIMENSIONS; i++){
            aa +=  Math.pow(element[i], 2);
            bb +=  Math.pow(center[i], 2);
        }
        double cos = innerproduct/Math.sqrt(aa*bb);
//        return  (float)Math.acos(cos);
        return cos;  //[-1,1]
    }

    /**
     * ��ȡ���뼯������С�����λ��
     * �˴��������ƶ�Խ��˵������ԽС
     * @param distance
     *            ��������
     * @return ��С�����ھ��������е�λ��
     */
    private int minDistance(double[] distance) {
        double minDistance = distance[0];
        int minLocation = 0;
        for (int i = 1; i < distance.length; i++) {
            // ����ֵcosԽ��Խ����
            if (distance[i] > minDistance) {
                minDistance = distance[i];
                minLocation = i;
            } else if (distance[i] == minDistance) // �����ȣ��������һ��λ��
            {
                if (random.nextInt(10) < 5) {
                    minLocation = i;
                }
            }
        }

        return minLocation;
    }

    /**
     * ���ģ�����ǰԪ�طŵ���С����������صĴ���
     */
    private void clusterSet() {
        double[] distance = new double[k];
        for (int i = 0; i < dataSetLength; i++) {
            for (int j = 0; j < k; j++) {
                distance[j] = distance(dataSet.get(i), center.get(j));
                // System.out.println("test2:"+"dataSet["+i+"],center["+j+"],distance="+distance[j]);
            }
            int minLocation = minDistance(distance);

            cluster.get(minLocation).add(dataSet.get(i));// ���ģ�����ǰԪ�طŵ���С����������صĴ���
        }
    }

    /**
     * ���������ƽ���ķ���
     *
     * @param element
     *            ��1
     * @param center
     *            ��2
     * @return ���ƽ��
     */
    private double errorSquare(float[] element, float[] center) {
        int count = 0;
        float innerproduct = 0.0f;
        float aa = 0.0f;
        float bb = 0.0f;
        for (int i=0; i<DataHandler.DIMENSIONS; i++){
            innerproduct +=  element[i] * center[i];
        }
        for (int i=0; i<DataHandler.DIMENSIONS; i++){
            aa +=  Math.pow(element[i], 2);
            bb +=  Math.pow(center[i], 2);
        }
        double cosSimErr = innerproduct/Math.sqrt(aa * bb);

        return cosSimErr;
    }

    /**
     * �������ƽ����׼��������
     */
    private void countRule() {
        float jcF = 0;
        for (int i = 0; i < cluster.size(); i++) {
            for (int j = 0; j < cluster.get(i).size(); j++) {
                jcF += errorSquare(cluster.get(i).get(j), center.get(i));

            }
        }
        jc.add(jcF);
    }

    /**
     * �����µĴ����ķ���
     */
    private void setNewCenter() {
        for (int i = 0; i < k; i++) {
            int n = cluster.get(i).size();
            if (n != 0) {
                float[] newCenter = new float[DataHandler.DIMENSIONS];

                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < DataHandler.DIMENSIONS; k++){
                        newCenter[k] += cluster.get(i).get(j)[k];
                    }
                }
                // ����һ��ƽ��ֵ
                for (int k = 0; k < DataHandler.DIMENSIONS; k++){
                    newCenter[k] += newCenter[k] / n;
                }
                center.set(i, newCenter);
            }
        }
    }

    /**
     * ��ӡ���ݣ�������
     *
     * @param dataArray
     *            ���ݼ�
     * @param dataArrayName
     *            ���ݼ�����
     */
    public void printDataArray(ArrayList<float[]> dataArray, String dataArrayName) {
//        for (int i = 0; i < dataArray.size(); i++) {
//            System.out.println("print:" + dataArrayName + "[" + i + "]={"
//                    + dataArray.get(i)[0] + "," + dataArray.get(i)[1] + "}");
//        }
//        System.out.println("===================================");

        //store result to file
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(targetFile));
            for (int i = 0; i < dataArray.size(); i++) {
                writer.write(dataArrayName + "[" + i + "] size is " + dataArray.size() + "\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (Exception e) {/*ignore*/}

        }
    }

    public void storeResult() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(targetFile));
            writer.write("cosErr : " + jc.get(m) + "\n");
            for(int i=0; i < cluster.size(); i++) {
                writer.write("Cluster[" + i + "] size= " +  cluster.get(i).size() + " : ");
                for (int j=0; j < cluster.get(i).size(); j++){
                    writer.write(cluster.get(i).get(j) + ",");
                }
                writer.write("\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (Exception e) {/*ignore*/}
        }
    }
    /**
     * Kmeans�㷨���Ĺ��̷���
     */
    private void kmeans() {
        init();
        // printDataArray(dataSet,"initDataSet");
        // printDataArray(center,"initCenter");

        // ѭ�����飬ֱ������Ϊֹ
        while (true) {
            clusterSet();
            countRule();

            // �����ˣ��������
            if (m != 0) {
                if (jc.get(m) - jc.get(m - 1) == 0) {
                    break;
                }
            }

            setNewCenter();
            // printDataArray(center,"newCenter");
            m++;
            cluster.clear();
            cluster = initCluster();
        }
//        for(int i=0;i<cluster.size();i++) {
//            printDataArray(cluster.get(i), "cluster[" + i + "]");
//        }
        storeResult(); //Store clustering result to file
        System.out.println("cosSimErr: " + jc.get(m));
        System.out.println("note:the times of repeat:m=" + m);//�����������
    }

    /**
     * ִ���㷨
     */
    public void execute() {
        long startTime = System.currentTimeMillis();
        System.out.println("kmeans begins");
        kmeans();
        long endTime = System.currentTimeMillis();
        System.out.println("kmeans running time=" + (endTime - startTime)
                + "ms");
        System.out.println("kmeans ends");
        System.out.println();
    }
}

