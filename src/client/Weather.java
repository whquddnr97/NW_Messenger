package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.*;


public class Weather {
	
	public static StringBuilder temp = new StringBuilder();
	
	public static void main(String[] args) throws IOException {
		getInfo();	
	}
	
	public static String getInfo() throws IOException {
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat hour = new SimpleDateFormat("HH00");
		Date time = new Date();
		String nx = "36"; //����
		String ny = "128"; //�浵
//		String baseDate = date.format(time); //��ȸ�ϰ���� ��¥
//		String baseTime = hour.format(time); //��ȸ�ϰ���� �ð�
		String baseDate = "20201215";
		String baseTime = "0500";
		String type = "JSON"; //Ÿ�� xml, json ��� ..
		String serviceKey = "Pe7chXCUPk%2FBnVerISrk2EuEfXgkQH2%2BsaLQCirYWac8AS8rMSH9pccLM%2Bd%2BeaHPr%2F6OTWa07pSZQhWFZDerag%3D%3D"; // ����Ű
		String URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"; // URL

		StringBuilder urlBuilder = new StringBuilder(URL); /*URL*/
		urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+serviceKey); /*Service Key*/
		urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*��������ȣ*/
		urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*�� ������ ��� ��*/
		urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")); /*��û�ڷ�����(XML/JSON)Default: XML*/
		urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /*20�� 11�� 24�� ��ǥ*/
		urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /*05�� ��ǥ(���ô���)*/
		urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*���������� X ��ǥ��*/
		urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*�������� Y ��ǥ*/
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
//		System.out.println("Response code: " + conn.getResponseCode());
		BufferedReader rd;
		if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();
		String result=sb.toString();
//		System.out.println(sb.toString());


//		StringBuilder temp = new StringBuilder();
		
		JsonParser parser = new JsonParser(); 
		JsonObject obj = (JsonObject) parser.parse(result); 
		
		// first_parse�� �Ľ�
		JsonObject first_parse = (JsonObject) obj.get("response"); 
		
		// first_parse���� second_parse�� ��������
		JsonObject second_parse = (JsonObject) first_parse.get("body"); 
		
		// second_parse���� items�� ��������
		JsonObject parse_items = (JsonObject) second_parse.get("items");
		
		// items ���� parse_Array�� ��������
		JsonArray parse_Array = (JsonArray) parse_items.get("item");
		
		// String �������� �ٽ� ��������
		String contents;
		
		// ��������� weather��� JSONObject�� ������
		JsonObject weather; 
	
		sb.append(baseDate+"/"+ baseTime);
		
		temp.append("��¥ : " + baseDate);
		temp.append(" ");
		temp.append("�ð� : " + baseTime);
		temp.append("\n");
		
		for(int i = 0 ; i < parse_Array.size(); i++) {
			weather = (JsonObject) parse_Array.get(i);
			Object fcstValue = weather.get("fcstValue");
			Object fcstDate = weather.get("fcstDate");
			Object fcstTime = weather.get("fcstTime");
			contents = weather.get("category").toString(); 
			

			// ī�װ����� �Ľ��� ���� �־��ش�
			if(contents.equals("POP"))
				temp.append("����Ȯ�� : " + fcstValue + "%");
			else if(contents.equals("PTY"))
				temp.append(", �������� : " + fcstValue);
			else if(contents.equals("SKY"))
				temp.append(", �ϴû��� : " + fcstValue);
			else if(contents.equals("WSD"))
				temp.append(", ǳ��  : " + fcstValue);
			else if(contents.equals("R06"))
				temp.append(", 6�ð� ������ : " + fcstValue + "mm");
			else if(contents.equals("REH"))
				temp.append(", ���� : " + fcstValue + "%");
			else if(contents.equals("S06"))
				temp.append(", 6�ð� ������ : " + fcstValue + "cm");
			else if(contents.equals("WAV"))
				temp.append(", �İ� : " + fcstValue + "M");
			else if(contents.equals("T3H") || contents.equals("TMN") || contents.equals("TMX")) {
				temp.append(", 3�ð� ��� : " + fcstValue + "��");
				temp.append("\n");
			}
			else if(contents.equals("UUU") || contents.equals("VVV"))
				temp.append(", ǳ�� : " + fcstValue + "m/s");
			else if(contents.equals("VEC"))
				temp.append(", ǳ�� : " + fcstValue + "m/s");
			else
				temp.append(", " + fcstValue);
		}
		return temp.toString();
	}
}