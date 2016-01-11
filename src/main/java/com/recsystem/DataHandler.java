package com.recsystem;

import com.recsystem.enumeration.UserDataFieldEnum;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sunny on 16/1/5.
 */
public class DataHandler {
    private static String path = "D:/DataSet/userOperationDataset/";
    private static File targetFile = new File(path + "dataWeka.txt");
//    private static File targetFile = new File(path + "smallDataset.txt");
    private static File mergeResultFiile = new File(path + "mergeData.txt");

    private static int idCnt = 0;
    private static Map<String, Integer> uuIds = new HashMap<String, Integer>();
    private static Map<String, Integer> guids = new HashMap<String, Integer>();
    //    public static Map<String, ArrayList<Integer>> idOperationsMap = new HashMap<String, ArrayList<Integer>>();
    public static Map<String, float[]> idOperationsMap = new HashMap<String, float[]>();
    public static int DIMENSIONS = 16;
    //根据该数据集最早记录时间 2012/12/01 00:00:00
    private static long baseTimeStamp = 1354291200;
    private static long previousEntryTime;

    public static void dataHandling (){

        BufferedReader reader = null;
        BufferedWriter writer = null;
        try{
            reader = new BufferedReader(new FileReader(path + "data.csv"));
            writer = new BufferedWriter(new FileWriter(targetFile));

            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = Pattern.compile("\\{.*\\}").matcher(line);
                while (matcher.find()){
                    // 修正数据
                    line = matcher.group();
                    line = line.replaceAll(" +", "").replaceAll("\\u0000", "");
                    line = line.replaceAll("\"end\":\\}", "\"end\":0\\}");   //修复数据没有value错误 "end": -> "end":0
                    line = line.replaceAll("\"begin:\"", "\"begin\":");      //修复数据错误 "begin:" -> "begin":
                    // "open":[0] "open":[(null)] ->"open":[]
                    line = line.replaceAll("\"open\":\\[0\\]", "\"open\":[]").replaceAll("\"open\":\\[\\(null\\)\\]", "\"open\":[]");

                    try {
                        JSONObject jsonData = new JSONObject(line);

                        boolean isReg = jsonData.getBoolean(UserDataFieldEnum.REGUSER.desc());
                        String idStr;
                        if (!isReg) idStr = jsonData.getString(UserDataFieldEnum.UUID.desc());
                        else idStr = jsonData.getString(UserDataFieldEnum.GUID.desc());
                        if ("null".equals(idStr) || "\"null\"".equals(idStr))
                            continue;
                        int uid = generateUserId(idStr, isReg);

                        String timeStr = jsonData.getString(UserDataFieldEnum.OPEN_TIME.desc());   //"open":[1356009904]
                        timeStr = timeStr.replaceAll("\\[", "").replaceAll("\\]", "");
                        long time;
                        try{
                            if (timeStr.length() > 10)      // 原始数据错误 "open":[1359635691652]
                                timeStr = timeStr.substring(0, 10);
                            time = Long.valueOf(timeStr) - baseTimeStamp;
                            if ((time / (3600 * 24) > 365 * 10) || (time / (3600 * 24) < 0))   //解决不了的数据错误 "open":[316087647421]
                                throw new NumberFormatException();
                        } catch (NumberFormatException e){
                            time = previousEntryTime;      // 用之前记录的数据填充缺失(错误)数据 "open"=[] "open"=[0]
                        }
                        previousEntryTime = time;

                        //write new file
//                        writer.write(   String.valueOf(uid) + "\t" +
////                                        (!isReg? "0" : "1") + "\t" +    // 1-注册 0-未注册
////                                        String.valueOf(time/(3600 * 24)) + "\t" +
//                                        "0:" + jsonData.getString(UserDataFieldEnum.CLICK_ADV.desc()) + "\t" +
//                                        "1:" + jsonData.getString(UserDataFieldEnum.RECORD.desc()) + "\t" +
//                                        "2:" + jsonData.getString(UserDataFieldEnum.WEIBOXIU_BROWSER.desc()) + "\t" +
//                                        "3:" + jsonData.getString(UserDataFieldEnum.WEIBOXIU_SHARE.desc()) + "\t" +
//                                        "4:" + jsonData.getString(UserDataFieldEnum.WEIBOXIU_URL.desc()) + "\t" +
//                                        "5:" + jsonData.getString(UserDataFieldEnum.FEILAIXUN_BROWSER.desc()) + "\t" +
//                                        "6:" + jsonData.getString(UserDataFieldEnum.FEILAIXUN_SHARE.desc()) + "\t" +
//                                        "7:" + jsonData.getString(UserDataFieldEnum.CAIJINGYUAN_BROWSER.desc()) + "\t" +
//                                        "8:" + jsonData.getString(UserDataFieldEnum.CAIJINGYUAN_SHARE.desc()) + "\t" +
//                                        "9:" + jsonData.getString(UserDataFieldEnum.ZHIYINBANG_BROWSER.desc()) + "\t" +
//                                        "10:" + jsonData.getString(UserDataFieldEnum.ZHIYINBANG_SHARE.desc()) + "\t" +
//                                        "11:" + jsonData.getString(UserDataFieldEnum.ZHIYINBANG_PHONE.desc()) + "\t" +
//                                        "12:" + jsonData.getString(UserDataFieldEnum.HUOBAODIAN_BROWSER.desc()) + "\t" +
//                                        "13:" + jsonData.getString(UserDataFieldEnum.HUOBAODIAN_SHARE.desc()) + "\t" +
//                                        "14:" + jsonData.getString(UserDataFieldEnum.HUOBAODIAN_PHONE.desc()) + "\t" +
//                                        "15:" + jsonData.getString(UserDataFieldEnum.QIANGPIANYI_BROWSER.desc()) + "\n"
//                        );
                        writer.write(   String.valueOf(uid) + "," +
                                        jsonData.getString(UserDataFieldEnum.CLICK_ADV.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.RECORD.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.WEIBOXIU_BROWSER.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.WEIBOXIU_SHARE.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.WEIBOXIU_URL.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.FEILAIXUN_BROWSER.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.FEILAIXUN_SHARE.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.CAIJINGYUAN_BROWSER.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.CAIJINGYUAN_SHARE.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.ZHIYINBANG_BROWSER.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.ZHIYINBANG_SHARE.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.ZHIYINBANG_PHONE.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.HUOBAODIAN_BROWSER.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.HUOBAODIAN_SHARE.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.HUOBAODIAN_PHONE.desc()) + "," +
                                        jsonData.getInt(UserDataFieldEnum.QIANGPIANYI_BROWSER.desc()) + "\n"
                        );


                    } catch (JSONException e){
                        System.out.println(line);
                        e.printStackTrace();
                    }
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {writer.close();} catch (Exception e) {/*ignore*/}
        }
        mergeSameUserOperations(targetFile);

    }
    // merge same uuid operations and remove id column
    // format id,0,0... to 0,0
    public static void mergeSameUserOperations(File targetFile){
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try{
            reader = new BufferedReader(new FileReader(targetFile));
            writer = new BufferedWriter(new FileWriter(mergeResultFiile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitArr = line.split(",");
                // Clear data, deleting data whose operations are all 0
                int ii;
                for (ii=1; ii < splitArr.length; ii++){
                    if (splitArr[ii].equals("0")) {
                       continue;
                    }
                    else break;
                }
                if (ii==splitArr.length)  continue; // if one record has no browse behavior, then skip
                String uid = splitArr[0];
                if (idOperationsMap.containsKey(uid)){
                    for (int j=0; j<idOperationsMap.get(uid).length; j++){
                        idOperationsMap.get(uid)[j] = idOperationsMap.get(uid)[j] + Float.parseFloat(splitArr[j+1]); //merge
                    }
                }
                else {
                    float[] operationsArr = new float[DIMENSIONS];
                    int k=0;
                    for (int i=1; i<splitArr.length; i++){
                        operationsArr[k++] = Float.parseFloat(splitArr[i]);
                    }
                    idOperationsMap.put(splitArr[0], operationsArr);
                }

            }
            //Store result to mergeResultFile
            for (Map.Entry<String, float[]> entry : idOperationsMap.entrySet()) {
                int i;
                for (i=0; i<entry.getValue().length-1; i++){
                    writer.write(entry.getValue()[i] + ",");
                }
                writer.write(entry.getValue()[i] + "\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
        try {writer.close();} catch (Exception e) {/*ignore*/}
    }
    }

    private static int generateUserId(String uid, boolean isReg){
        int ret;
        if (!isReg){
            if (uuIds.containsKey(uid)){
                ret = uuIds.get(uid);
            }
            else {
                uuIds.put(uid, idCnt);
                ret = idCnt++;
            }
        }
        else {
            if (guids.containsKey(uid)){
                ret = guids.get(uid);
            }
            else {
                guids.put(uid, idCnt);
                ret = idCnt++;
            }
        }
        return ret;
    }

}
