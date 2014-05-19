package com.baibutao.apps.linkshop.uxiang.ruiserver.biz.common;


import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

import wint.lang.magic.Property;
import wint.lang.magic.Transformer;
import wint.lang.magic.reflect.ReflectMagicObject;
import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;

import com.google.gson.Gson;

public class JsonUtil {
	
	public static <T> String mfields(final String fields, List<T> items) {
		if(CollectionUtil.isEmpty(items)) {
			items = CollectionUtil.newArrayList();
		}
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Transformer<T, String> transformer = new Transformer<T, String>() {
			public String transform(Object object) {
				if (object == null) {
					return StringUtil.EMPTY;
				}
				return fields(fields, object, false);
			}
		};
		sb.append(joinNew(items, ",", transformer));
		sb.append(']');
		return sb.toString();
	}
	
	
	public static <T> String joinNew(Collection<T> c, String token, Transformer<T, String> valueTransformer) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (T object : c) {
			if (first) {
				first = false;
			} else {
				sb.append(token);
			}
			if (valueTransformer != null) {
				sb.append(valueTransformer.transform(object));
			} else {
				sb.append(object);
			}
		}
		return sb.toString();
	}
	
	
	public static <T> String mfieldsHasSub(final String fields, List<T> items) {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Transformer<T, String> transformer = new Transformer<T, String>() {
			public String transform(Object object) {
				if (object == null) {
					return StringUtil.EMPTY;
				}
				return fields(fields, object, true);
			}
		};
		sb.append(CollectionUtil.join(items, ",", transformer));
		sb.append(']');
		return sb.toString();
	}
	
	public static String fields(String fields,  Object item) {
		StringBuilder sb = new StringBuilder();
		List<String> propertyNames = StringUtil.splitTrim(fields, ",");
//		fieldsImpl(propertyNames, item, sb, null);
		fieldsImpl(propertyNames, item, sb, false);
		return sb.toString();
	}
	
	
	
	
	public static String fields(String fields,  Object item, boolean hasSub) {
		StringBuilder sb = new StringBuilder();
		List<String> propertyNames = StringUtil.splitTrim(fields, ",");
//		fieldsImpl(propertyNames, item, sb, null);
		fieldsImpl(propertyNames, item, sb, hasSub);
		return sb.toString();
	}
	
	private static void fieldsImpl(List<String> propertyNames, Object item, StringBuilder sb, boolean hasSub) {
		ReflectMagicObject ref = new ReflectMagicObject(item);
		Map<String, Property> properties = new HashMap<String, Property>();
		//存放json的key,可能有重复
		ArrayList<String> jsonKey = CollectionUtil.newArrayList();
		for(String field : propertyNames) {
			int pointSplitIndex = field.indexOf(".");
			if( pointSplitIndex> 0) {
				String objName = field.substring(0,pointSplitIndex);
				String objAttributeName = field.substring(pointSplitIndex + 1);
				Property property = ref.getMagicClass().getProperty(objName);
                properties.put(objAttributeName, property);
			} else {
				Property property = ref.getMagicClass().getProperty(field);
				properties.put(field, property);
			}
			//生成json的key
			String attributeName = field.substring(field.lastIndexOf(".") + 1);
			jsonKey.add(attributeName);
		}
		try {
			sb.append(toJson(properties, item, jsonKey));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		
	}

	private static String toJson(Map<String, Property> properties, Object item, ArrayList<String> jsonKey) throws JSONException {
        StringWriter stringWriter = new StringWriter();
        JSONWriter jsonWriter = new JSONWriter(stringWriter);
        jsonWriter.object();
        for (Map.Entry<String, Property> propertyEntry : properties.entrySet()) {
            String fieldName = propertyEntry.getKey();
            Property property = propertyEntry.getValue();
            
            //add by wull 2014-03-27 支持三级属性
            int thirdIndex = fieldName.indexOf(".");
            String thirdObjAttributeName = "";
            if(thirdIndex > 0){
                String thirdObjName = fieldName.substring(0, thirdIndex);
                thirdObjAttributeName = fieldName.substring(thirdIndex + 1);
                //如果key中存在多个一样的名称需要增加前面的类名
                if(jsonKey.indexOf(thirdObjAttributeName) != jsonKey.lastIndexOf(thirdObjAttributeName)){
                    jsonWriter.key(replacePointToUpper(fieldName));
                }else{
                    jsonWriter.key(thirdObjAttributeName);
                }
                
                fieldName = thirdObjName;
            }else{
                jsonWriter.key(fieldName);
            }
            
            Object value = property.getValue(item);
            if (value instanceof Date) {
                jsonWriter.value(DateUtil.format((Date) value, DateUtil.DEFAULT_DATE_FMT));
            } else {
                if (value == null) {
                    jsonWriter.value(StringUtil.EMPTY);
                } /*else if(value instanceof ItemDO){
                    Object object = getObjctAttributeValue(value, fieldName);
                    if (object instanceof ProductDO || object instanceof CashCouponDO || object instanceof PromotionDO) {
                        jsonWriter.value(getObjctAttributeValue(object, thirdObjAttributeName));
                    }else{
                        jsonWriter.value(object);
                    }
                }else if(value instanceof ProductDO || value instanceof CashCouponDO || value instanceof PromotionDO){
                    jsonWriter.value(getObjctAttributeValue(value, fieldName));
                }*/ else {
                    jsonWriter.value(value);
                }
            }
        }
        jsonWriter.endObject();
        return stringWriter.toString();
    }
	
	
	 public static String replacePointToUpper(String srcStr){
	        int index = srcStr.indexOf(".");
	        String str = srcStr;
	        if(index > 0){
	            char firstChar = srcStr.charAt(index + 1);
	            str = srcStr.replaceFirst("\\.([a-z]|[A-Z]){1}", String.valueOf(Character.toUpperCase(firstChar)));
	        }
	        
	        return str;
	    }
	
	
	private static Object getObjctAttributeValue(Object object, String attribute) {
		ReflectMagicObject ref = new ReflectMagicObject(object);
		Property property = ref.getMagicClass().getProperty(attribute);
		if (property != null) {
			Object attributeValue = property.getValue(object);
			if(attributeValue == null) {
				return StringUtil.EMPTY;
			}
			if (attributeValue instanceof Date) {
				return DateUtil.format((Date) attributeValue, DateUtil.DEFAULT_DATE_FMT);
			}
			return attributeValue;
		}
		return StringUtil.EMPTY;
	}
	
	public static JSONObject getJsonObject(Object obj) {
		if (obj == null) {
			return null;
		}
		
		if(obj instanceof JSONObject) {
			return (JSONObject)obj;
		}
		
		String content = (String) obj;
		if (StringUtil.isBlank(content)) {
			return null;
		}
		try {
			JSONObject json = new JSONObject(content);
			return json;
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static JSONArray getJsonArray(Object obj) {
		if (obj == null) {
			return null;
		}
		
		if(obj instanceof JSONArray) {
			return (JSONArray)obj;
		}
		
		String content = (String) obj;

		if (StringUtil.isBlank(content)) {
			return null;
		}
		try {
			JSONArray json = new JSONArray(content);
			return json;
		} catch (JSONException e) {
			return null;
		}
	}
	
	
	public static JSONArray getJsonArray(JSONObject json, String name) {
		if (json == null || name == null) {
			return null;
		}
		if (!json.has(name)) {
			return null;
		}
		try {
			return json.getJSONArray(name);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static JSONObject getJSONObject(JSONObject json, String name) {
		if (json == null || name == null) {
			return null;
		}
		if (!json.has(name)) {
			return null;
		}
		try {
			return json.getJSONObject(name);
		} catch (JSONException e) {
			return null;
		}
	}

	public static String getString(JSONObject json, String name, String defaultValue) {
		if (json == null || name == null) {
			return defaultValue;
		}
		if (!json.has(name)) {
			return defaultValue;
		}
		try {
			return json.getString(name);
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	public static int getInt(JSONObject json, String name, int defaultValue) {
		if (json == null || name == null) {
			return defaultValue;
		}
		if (!json.has(name)) {
			return defaultValue;
		}
		try {
			return json.getInt(name);
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	public static long getLong(JSONObject json, String name, long defaultValue) {
		if (json == null || name == null) {
			return defaultValue;
		}
		if (!json.has(name)) {
			return defaultValue;
		}
		try {
			return json.getLong(name);
		} catch (JSONException e) {
			return defaultValue;
		}
	}

	public static double getDouble(JSONObject json, String name, double defaultValue) {
		if (json == null || name == null) {
			return defaultValue;
		}
		if (!json.has(name)) {
			return defaultValue;
		}
		try {
			return json.getDouble(name);
		} catch (JSONException e) {
			return defaultValue;
		}
	}
	
	public static boolean getBoolean(JSONObject json, String name, boolean defaultValue) {
		if (json == null || name == null) {
			return defaultValue;
		}
		if (!json.has(name)) {
			return defaultValue;
		}
		try {
			return json.getBoolean(name);
		} catch (JSONException e) {
			return defaultValue;
		}
	}
	
	public static <T> String beanToJson(T t) {
//		return net.sf.json.JSONObject.fromObject(t).toString();
		Gson gson = new Gson();
		return gson.toJson(t);
	}
	
	public static <T> String listToJson(T o) {
//		return net.sf.json.JSONArray.fromObject(o).toString();
		Gson gson = new Gson();
		return gson.toJson(o);
	}
	
	public static <T> T jsonToBean(String jsonString, Class<T> beanCalss) {
//		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonString);
//		T bean = (T) net.sf.json.JSONObject.toBean(jsonObject, beanCalss);
//		return bean;
		 Gson gson = new Gson();
		T jsonCat = gson.fromJson(jsonString, beanCalss);
		 return jsonCat;
	}
	
//	public static <T> List<T> jsonToList(String jsonString, CacheTypeEnum cacheTypeEnum) {
//		Gson gson = new Gson();
//		return gson.fromJson(jsonString, cacheTypeEnum.getType());
//	}

	public static void main(String[] args) {}

}