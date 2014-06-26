/*
系級:資訊103
姓名:白孟哲
學號:F74996065
*/


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TocHw4 {

        public static void main(String[] args) throws JSONException, IOException {
                int chunksize = 4096;
        byte[] chunk = new byte[chunksize];
        int count2;
        try  {
            URL pageUrl = new URL(args[0]);

            // 讀入網頁(位元串流)
            BufferedInputStream bis = new BufferedInputStream(pageUrl.openStream());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("URL2.txt", false));
            System.out.println("read2() running " );
            while ((count2 = bis.read(chunk, 0, chunksize)) != -1) {
                bos.write(chunk, 0, count2); // 寫入檔案
            }
            bos.close();
            bis.close();

          System.out.println("Done");
         }catch (IOException e) {
             e.printStackTrace();
      }

      //讀取input檔並儲存JSONArray
        JSONArray houses = new JSONArray(new JSONTokener(new FileReader(new File("URL2.txt"))));

        String road1;
        int idx1=0;
        //create JSONArray datas
        JSONArray datas = new JSONArray();

        int firstObject = 0;
        JSONObject house;
        for(;firstObject<houses.length();firstObject++){
        //取出資料的第一筆
        house = houses.getJSONObject(firstObject);
        //儲存第一筆資料的address
        String address1 = house.getString("土地區段位置或建物區門牌");
        //找出是否有路或街或巷
        if(address1.indexOf("路") >= 0){
                idx1 = address1.indexOf("路");
        }else if(address1.indexOf("街") >= 0){
                idx1 = address1.indexOf("街");
        }else if(address1.indexOf("道") >= 0){
                idx1 = address1.indexOf("道");
        }else if(address1.indexOf("巷") >=0){
                idx1 = address1.indexOf("巷");
        }
        else{
                idx1 = -1;
        }
        //若有路或街或巷
        if(idx1 >= 0){
                //就把路或街或巷之前的字元存到 road1 字串裡
                road1 = Character.toString((address1.charAt(0)));
                for(int z=1;z <= idx1;z++){
                        road1 = road1 + address1.charAt(z);
                }
                int tradedprice1 = house.getInt("總價元");
                //int tradedyear1 =  house.getJSONObject("fields").getJSONObject("交易年月").getInt("year");
                //int tradedmonth1 =  house.getJSONObject("fields").getJSONObject("交易年月").getInt("month");
                //String tradeddate1 = tradedyear1 + "" + tradedmonth1;
                //以路名和price作為參數create一個JSONObject
                JSONObject jsonObject = TocHw4.createJSONObject(road1,tradedprice1);
                //把JSONObject放進datas這個JSONArray裡
                datas.put(jsonObject);
                break;
        }
        }
        //JSONObject tempp = datas.getJSONObject(0);
        //tempp.remove("number");
        //tempp.put("number",100);
        //System.out.println(tempp);

        int i=firstObject+1;
        //從第一筆資料開始讀取
        for(;i<houses.length();i++){
                house = houses.getJSONObject(i);
                //儲存此筆資料的address
                String address = house.getString("土地區段位置或建物區門牌");
                String road;
                int idx=0;
                //判斷是否有路或街或巷
                if(address.indexOf("路") >= 0){
                }else if(address.indexOf("街") >= 0){
                        idx = address.indexOf("街");
                }else if(address.indexOf("道") >= 0){
                    idx = address.indexOf("道");
                }else if(address.indexOf("巷") >= 0){
                        idx = address.indexOf("巷");
                }else{
                        idx = -1;
                }
                //若有路或街或巷
                if(idx >= 0){
                        //把路或街或巷以前的字元存到 road 字串裡
                        road = Character.toString((address.charAt(0)));
                        for(int j=1;j <= idx;j++){
                                road = road + address.charAt(j);
                        }
                        //把從input檔讀的此筆資料和之前出現過的資料做比較
                        for(int k=0;k<datas.length();k++){
                                JSONObject data = datas.getJSONObject(k);
                                //儲存之前出現過的路名
                                String savedroad = data.getString("road");
                                //和現在這筆資料比較路名是否相同
                                if(road.equals(savedroad)){
                                        //String newdate = house.getJSONObject("fields").getJSONObject("交易年月").getInt("year") + "" +house.getJSONObject("fields").getJSONObject("交易年月").getInt("month");
                                        //JSONArray Date = data.getJSONArray("date");
                                        //int datenum = Date.length();
                                        //int x = 0;
                                        //for(x=0;x < datenum;x++){
                                        //      String saveddate = data.getJSON("date").getString(x);
                                        //      if(newdate.equals(saveddate))
                                        //              break;
                                        //}
                                        //if(x==datenum){

                                        //number是紀錄此路名出現的次數
                                        int count = data.getInt("number");
                                        count++;
                                        data.remove("number");
                                        data.put("number",count);
                                                //data.put("date",newdate);
                                        //}
                                        //儲存之前的資料的最高價格
                                        int maxpri = data.getInt("Maxprice");
                                        //儲存之前的資料的最低價格
                                        int minpri = data.getInt("Minprice");
                                        data.remove("Maxprice");
                                        data.remove("Minprice");
                                        //判斷此筆input讀來的資料的價格是否比最高價格還高或比最低價格還低
                                        if(minpri > house.getInt("總價元")){
                                                minpri = house.getInt("總價元");
                                        }
                                        else if(maxpri < house.getInt("總價元")){
                                                maxpri = house.getInt("總價元");
                                        }
                                        data.put("Minprice",minpri);
                                        data.put("Maxprice",maxpri);
                                        break;
                                }
                                //如果是JSONArray datas 的最後一筆資料，代表路名沒有出現過
                                else if(k==(datas.length()-1)){
                                        int tradedprice = house.getInt("總價元");
                                        //int tradedyear =  house.getJSONObject("fields").getJSONObject("交易年月").getInt("year");
                                        //int tradedmonth =  house.getJSONObject("fields").getJSONObject("交易年月").getInt("month");
                                        //String tradeddate = tradedyear + "" + tradedmonth;

                                        //create一個新的JSONObject
                                        JSONObject temp = TocHw4.createJSONObject(road,tradedprice);
                                        datas.put(temp);
                                }
                        }
                }   //if end
        }  //for loop end

        //讀取之前出現過的資料
        for(int x=0;x<datas.length();x++){
                JSONObject data = datas.getJSONObject(x);
                //路名存到road2
                String road2 = data.getString("road");
                Pattern pattern = Pattern.compile(road2 + ".*");
                int recordnum = 0;
                String[] record;
                record = new String[100];
                record[0] = "000";
                //和input檔的資料比較
                for(int y=0;y<houses.length();y++){
                        house =  houses.getJSONObject(y);
                        String address = house.getString("土地區段位置或建物區門牌");
                        Matcher matcher = pattern.matcher(address);
                        //int recordnum = 0;
                        //如果找到相符的路名
                        if(matcher.matches()){
                                    int tradedate =  house.getInt("交易年月");
                                //int tradedyear =  house.getJSONObject("交易年月").getInt("year");
                                //int tradedmonth =  house.getJSONObject("交易年月").getInt("month");
                                    int tradedmonth = tradedate%100;
                                    int tradedyear = tradedate/100;
                                //把此input檔裡的資料的年月存成String
                                String tradeddate = tradedyear + "" + tradedmonth;
                                //System.out.println("tradeddate:" + tradeddate);
                                //String[] record;
                                //record = new String[100];
                                //record[0] = "000";
                                //System.out.println("record[0]:" + record[0]);
                                //int recordnum = 0;
                                int b = 0;
                                //用來判斷和之前的年月是否重複
                                for(b=0;b<=recordnum;b++){
                                        if(record[b]!=null){
                                                //System.out.println("not null");
                                                //System.out.println("record[" + b + "] : " + record[b]);
                                                //如果此input的資料的年月和之前的相同
                                                if(record[b].equals(tradeddate)){
                                                        int count1 = data.getInt("number");
                                                        //System.out.println("number : " + count1);
                                                        //減掉一次該筆出現的次數
                                                        count1--;
                                                        data.remove("number");
                                                        data.put("number",count1);
                                                        //System.out.println("save-into-number : " + data.getInt("number"));
                                                        //System.out.println("samedate:" + record[b]);
                                                        break;
                                                }
                                        }
                                }
                                //如果年月和之前的沒有相同
                                if(b==recordnum+1){
                                        //紀錄有幾個年月的次數+1
                                        recordnum++;
                                        //把這個年月儲存到array裡
                                        record[recordnum] = tradeddate;
                                        //System.out.println("new no-same date:" + record[recordnum]);
                                }
                        }
                }
        }
        int MaxNumIdx = 0;
        JSONObject temp1 = datas.getJSONObject(0);
        int Maxnum = temp1.getInt("number");
        //System.out.println("5th number : " + Maxnum);
        //找出最大次數
        for(int m=1;m<datas.length();m++){
                temp1 = datas.getJSONObject(m);
                if(Maxnum < temp1.getInt("number")){
                        Maxnum = temp1.getInt("number");
                        MaxNumIdx = m;
                }
        }
        temp1 = datas.getJSONObject(MaxNumIdx);
        //System.out.println("all in all : " + Maxnum);
        //System.out.println("most month road " + temp1.getString("road"));
        //印出最大次數的資料
        for(int n=0;n<datas.length();n++){
                JSONObject temp3 = datas.getJSONObject(n);
                if(Maxnum == temp3.getInt("number")){
                        System.out.println(temp3.getString("road") + ", 最高成交價 : " + temp3.getInt("Maxprice") + ", 最低成交價
 : " + temp3.getInt("Minprice"));
                }
        }


        }

         private static JSONObject createJSONObject(String road,int price)throws JSONException,IOException{
         JSONObject jsonObject = new JSONObject();
         jsonObject.put("road", road);
         //date = "[" + date + "]";
         //JSONArray jsonArray = JSONArray.fromObject(date);
         //jsonObject.accumulate("date", new String[] {date});
         //jsonObject.put("year", year);
         //jsonObject.put("month", month);
         jsonObject.put("Maxprice", price);
         jsonObject.put("Minprice", price);
         jsonObject.put("number", 1);
         //System.out.println(jsonObject);
         return jsonObject;
 }

}
