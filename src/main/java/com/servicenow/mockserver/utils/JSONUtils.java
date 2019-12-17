package com.servicenow.mockserver.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JSONUtils {

	private static final Gson gson = new Gson();
	private static final Gson gsonp = new GsonBuilder().setPrettyPrinting().create();
	private static JSONObject fJSONObject = new JSONObject();


	private JSONUtils() {
		//do nothing
	}

	/**
	 * To check whether the JSON is valid
	 *
	 * @param test
	 * @return
	 */
	public static boolean isJSONValid(final String payload) {
		try {
			new JSONObject(payload);
		} catch (JSONException ex) {
			try {
				new JSONArray(payload);
			} catch (JSONException error) {
				//error.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public static void parse(final String json) {

		JSONObject object = new JSONObject(json);
		String[] keys = JSONObject.getNames(object);

		for (String key : keys) {

			System.out.println("keys :: " + key + " value :: " + object.get(key).toString());

			if (!isJSONValid(object.get(key).toString())) {
				fJSONObject.put(key, object.get(key).toString());
			} else {
				parse(object.get(key).toString());
				JSONObject tmp = fJSONObject;
				fJSONObject = new JSONObject();
				fJSONObject.put(key, tmp);
				System.out.println("** json :: " + fJSONObject);
			}

		}
	}

	public static String createTemplate(String inputString) {
		JsonObject root = fromJson(inputString);
		JsonObject newObject = new JsonObject();
		Set<Map.Entry<String, JsonElement>> set = root.entrySet();
		for (Map.Entry<String, JsonElement> entry : set) {
			String key = entry.getKey();
			JsonElement value = entry.getValue();
			if (value.isJsonPrimitive()) {
				newObject.addProperty(key, "${" + key + "}");
			} else {
				if (value.isJsonArray()) {
					newObject.add(key, parseObject(key, value.getAsJsonArray()));
				}
				if (value.isJsonObject()) {
					newObject.add(key, parseObject(key, value.getAsJsonObject()));
				}
			}
		}
		return toJsonString(newObject, true);
	}

	private static JsonArray parseObject(String prefix, JsonArray ja) {
		JsonArray ret = new JsonArray();
		for (int i = 0; i < ja.size(); i++) {
			JsonElement child = ja.get(i);
			if (child.isJsonPrimitive()) {
				//System.out.println("${" + prefix + "." + (i + 1) + "}" + "\t" + child.toString());
				ret.add("${" + prefix + "." + (i + 1) + "}");
			} else {
				if (child.isJsonArray()) {
					ret = parseObject(prefix + "." + (i + 1), child.getAsJsonArray());
					if (!prefix.equals("")) {
						//System.out.println(prefix + "." + (i + 1));
					}
				}
				if (child.isJsonObject()) {
					ret.add(parseObject(prefix + "." + (i + 1), child.getAsJsonObject()));
					//System.out.println(prefix + "." + (i + 1));
				}
			}
		}
		return ret;
	}

	private static JsonObject parseObject(String prefix, JsonObject jo) {
		JsonObject newObject = new JsonObject();
		Set<Map.Entry<String, JsonElement>> set = jo.entrySet();
		for (Map.Entry<String, JsonElement> entry : set) {
			String key = entry.getKey();
			JsonElement value = entry.getValue();
			if (!value.isJsonPrimitive()) {
				if (value.isJsonArray()) {
					newObject.add(key, parseObject(prefix + "." + key, value.getAsJsonArray()));
				}
				if (value.isJsonObject()) {
					newObject.add(key, parseObject(prefix + "." + key, value.getAsJsonObject()));
				}
			} else {
				//System.out.println("${" + prefix + "." + key + "}" + "\t" + value.toString());
				newObject.addProperty(key, "${" + prefix + "." + key + "}");
			}
		}
		return newObject;
	}


	public static JsonObject fromJson(String json) {
		return gson.fromJson(json, JsonObject.class);
	}

	public static String pretty(String json) {
		return toJsonString(gson.fromJson(json, JsonObject.class), true);
	}

	public static String toJsonString(JsonObject json, boolean pretty) {
		if (pretty) {
			return gsonp.toJson(json);
		} else {
			return gson.toJson(json);
		}
	}

	private static Map<String, String> getMap(String prefix, JsonArray ja) {
		Map<String, String> retval = new HashMap<>();
		for (int i = 0; i < ja.size(); i++) {
			JsonElement child = ja.get(i);
			if (child.isJsonArray()) {
				retval.putAll(getMap(prefix + "." + (i + 1), child.getAsJsonArray()));
			}
			if (child.isJsonObject()) {
				retval.putAll(getMap(prefix + "." + (i + 1), child.getAsJsonObject()));
			}
		}
		return retval;
	}

	private static Map<String, String> getMap(String prefix, JsonObject jo) {
		Map<String, String> retval = new HashMap<>();
		Set<Map.Entry<String, JsonElement>> set = jo.entrySet();
		for (Map.Entry<String, JsonElement> entry : set) {
			String key = entry.getKey();
			JsonElement value = entry.getValue();
			if (!value.isJsonPrimitive()) {
				if (value.isJsonArray()) {
					retval.putAll(getMap(prefix + "." + key, value.getAsJsonArray()));
				}
				if (value.isJsonObject()) {
					retval.putAll(getMap(prefix + "." + key, value.getAsJsonObject()));
				}
			} else {
				retval.put(prefix + "." + key, value.toString().replace("\"", ""));
			}
		}
		return retval;
	}

	public static Map<String, String> readJson(String jsonString) {
		Map<String, String> retval = new LinkedHashMap<>();
		if (null != jsonString) {
			JsonObject root = fromJson(jsonString);
			Set<Map.Entry<String, JsonElement>> set = root.entrySet();
			for (Map.Entry<String, JsonElement> entry : set) {
				String key = entry.getKey();
				JsonElement value = entry.getValue();
				if (value.isJsonPrimitive()) {
					retval.put(key, value.toString().replace("\"", ""));
				} else {
					if (value.isJsonArray()) {
						retval.putAll(getMap(key, value.getAsJsonArray()));
					}
					if (value.isJsonObject()) {
						retval.putAll(getMap(key, value.getAsJsonObject()));
					}
				}
			}
		}
		return retval;
	}

	public static void main(String... args) {
		String payload = "";
		Map<String, String> ot = readJson(payload);
		System.out.println(ot);
	}

}
