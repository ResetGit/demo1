package com.example.demo.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;


public class JsonUtil {

	private static Gson gson = new Gson();

	public static Map<?, ?> jsonToMap(String jsonStr) {
		Map<?, ?> objMap = null;
		if (gson != null) {
			Type type = new com.google.gson.reflect.TypeToken<Map<?, ?>>() {
			}.getType();
			objMap = gson.fromJson(jsonStr, type);
		}
		return objMap;
	}

}