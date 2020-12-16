package client;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.*;

public class Weather
{
	public static String getWeather() throws IOException
	{
		/*���� ��ǥ ��¥�� ���� �� ��¥*/
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		/*23�� ���Ŀ� �����ϸ� ���� 23���� ���� �����͸� �޾ƿ� �� ���� ( API�� 1��ġ�� ���� ) ���� ���� ��¥�� �̿�*/
		if (calendar.get(Calendar.HOUR_OF_DAY) == 23)
		{
			calendar.add(calendar.DATE, 0);
		}
		else
		{
			calendar.add(calendar.DATE, -1);
		}
		
		String result = "";
		String serviceKey = "49f1u%2BNPwv1IknmkOA2tI%2FXbNJnkdlkVxoKNnBDGc6BfPSzwqW4e5Gl7BvK3edmhg9Q7SSH8o4vRIJAUv3WbgA%3D%3D"; // ����Ű
		String dataType = "JSON"; // ��û�ڷ�����
		String baseDate = dateFormat.format(calendar.getTime()); // ��ǥ��¥ : ����
		String baseTime = "2300"; // ��ǥ�ð� 23��
		String nX = "63"; // ����3�� ����X
		String nY = "124"; // ����3�� ����Y
		
		/*���α׷� ���� ���� 23�� ���� ���� �����ͷ�, ���α׷� ���� ���� 06�ñ����� ���� ����� ������� ��û�������� url�� encode��*/
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst");
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("21", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nX, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(nY, "UTF-8"));
        
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader rd;
        
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)
        {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        else
        {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        
        StringBuilder sb = new StringBuilder();
        while ((result = rd.readLine()) != null)
        {
            sb.append(result);
        }
        
        rd.close();
        conn.disconnect();

        String we = doParse(sb.toString());
        /*����3���� ����Ȯ���� ��ħ ��������� ����*/
        return we;
	}
	
	public static String doParse(String dataString) 
    {
		JsonObject item;
		
		JsonParser jsonParser = new JsonParser(); // json parser ��ü ����
    	JsonObject jsonObject = (JsonObject) jsonParser.parse(dataString); // �����͸� json ��ü�� ��ȯ
    	JsonObject jsonResult = (JsonObject) jsonObject.get("response"); // response Ű�� �̿��� �Ľ�
    	JsonObject jsonBody = (JsonObject) jsonResult.get("body"); // response���� body ã��
    	JsonObject jsonItems = (JsonObject) jsonBody.get("items"); // body���� items ã��
    	JsonArray jsonItem = (JsonArray) jsonItems.get("item"); // items���� item �Ʒ� ������ �迭�� ����
    	String result = "";
    	
    	for (int i = 0; i < jsonItem.size(); i++)
    	{
    		item = (JsonObject) jsonItem.get(i);
    		
    		String fcstTime = item.get("fcstTime").toString();
    		String category = item.get("category").toString();
    		String fcstValue = item.get("fcstValue").toString();
    		
    		/*06�� �ڷᱸ���ڵ忡 ���� ���*/
    		if (fcstTime.equals("\"0600\""))
    		{
    			switch(category)
    			{
    			case "\"POP\"" :
    				result += "���� ����3���� ��󿹺���";
    				result +=" ����Ȯ�� " + fcstValue.substring(1, fcstValue.length()-1) + "% �̰�, ";
    				break;
    			//case "\"PTY\"" :
    				//result += "�������� " + findPTY(fcstValue.substring(1, fcstValue.length()-1) + " ");
    				//break;
    			//case "\"SKY\"" :
    				//result += "�ϴ� " + findSKY(fcstValue.substring(1, fcstValue.length()-1)+ " ");
    				//break;
    			case "\"TMN\"" :
    				result += "��ħ ������� " + fcstValue.substring(1, fcstValue.length()-1) + "��";
    				break;
    			//case "\"WSD\"" :
    				//result += "ǳ�� " + fcstValue.substring(1, fcstValue.length()-1) + "m/s";
    				//break;
    			default:
    				break;
    			}
    		}	
    	}
    	return result;
    }
	
	/*�ڵ尪���� �޾ƿ��� �������¸� �ؼ�*/
	public static String findPTY(String ptyCode)
	{
		String status = "";
		
		switch(ptyCode)
		{
			case "0" :
				status = "����";
				break;
			case "1" :
				status = "��";
				break;
			case "2" :
				status = "��������";
				break;
			case "3" :
				status = "��";
				break;
			case "4" :
				status = "�ҳ���";
				break;
			case "5" :
				status = "�����";
				break;
			case "6" :
				status = "�����/������";
				break;
			case "7" :
				status = "������";
				break;
			default :
				status = "�˼�����";
				break;
		}
		return status;
	}
	
	/*�ڵ尪���� �޾ƿ��� �ϴû��¸� �ؼ�*/
	public static String findSKY(String skyCode)
	{
		String status = "";
		
		switch(skyCode)
		{
			case "1" :
				status = "����";
				break;
			case "3" :
				status = "��������";
				break;
			case "4" :
				status = "�帲";
				break;
			default :
				status = "�˼�����";
				break;
		}
		return status;
	}
}
