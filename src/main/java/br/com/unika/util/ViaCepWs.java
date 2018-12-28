package br.com.unika.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViaCepWs {
	public static Map<String, String> buscarCep(String cep) {
		String json;
		cep = cep.replace("-", "");
		try {
			URL url = new URL("http://viacep.com.br/ws/" + cep + "/json");
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			StringBuilder jsonSb = new StringBuilder();

			br.lines().forEach(l -> jsonSb.append(l.trim()));

			json = jsonSb.toString();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

		Map<String, String> mapa = new HashMap<>();

		Matcher matcher = Pattern.compile("\"\\D.*?\": \".*?\"").matcher(json);
		while (matcher.find()) {
			String[] group = matcher.group().split(":");
			mapa.put(group[0].replaceAll("\"", "").trim(), group[1].replaceAll("\"", "").trim());
		}
		return mapa;
	}
	
	

}
