package com.mcal.ModdedPE.nmod;
import org.json.*;
import java.util.*;

public class JSONMerger
{
	private String json1;
	private String json2;

	public JSONMerger(String json1, String json2)
	{
		this.json1 = json1;
		this.json2 = json2;
	}

	public String merge() throws JSONException
	{
		if (isJSONObject(json1) && isJSONObject(json2))
		{
			return mergeObject(new JSONObject(json1), new JSONObject(json2)).toString();
		}
		else if (isJSONArray(json1) && isJSONArray(json2))
		{
			return mergeArray(new JSONArray(json1), new JSONArray(json2)).toString();
		}
		else
			throw new JSONException("Merging FAILED: CAMNOT JUDGE STRING TYPE");
	}

	private static boolean isJSONArray(String json)
	{
		try
		{
			new JSONArray(json);
			return true;
		}
		catch (JSONException e)
		{
			return false;
		}
	}

	private static boolean isJSONObject(String json)
	{
		try
		{
			new JSONObject(json);
			return true;
		}
		catch (JSONException e)
		{
			return false;
		}
	}

	private static JSONObject mergeObject(JSONObject object1, JSONObject object2) throws JSONException
	{
		Iterator iter = object2.keys();
		for (int index=0;iter.hasNext();++index)
		{
			String name=(String)iter.next();
			judgeTypeAndPut(object1, object2, name);
		}
		return object1;
	}

	private static JSONArray mergeArray(JSONArray array1, JSONArray array2) throws JSONException
	{
		for (int index=0;index < array2.length();++index)
		{
			judgeTypeAndPut(array1, array2, index);
		}
		return array1;
	}


	private static void judgeTypeAndPut(JSONArray array1, JSONArray array2, int index) throws JSONException
	{
		if (isTypeJSONArray(array2, index))
		{
			array1.put(array2.getJSONArray(index));
		}
		else if (isTypeJSONObject(array2, index))
		{
			array1.put(array2.getJSONObject(index));
		}
		else if (isTypeJSONString(array2, index))
		{
			array1.put(array2.getString(index));
		}
		else if (isTypeJSONInteger(array2, index))
		{
			array1.put(array2.getInt(index));
		}
		else
			throw new JSONException("ERROR: CANNOT JUDGE ITEM TYPE.");
	}

	private static void judgeTypeAndPut(JSONObject object1, JSONObject object2, String key) throws JSONException
	{
		if (isTypeJSONArray(object2, key))
		{
			if (object1.has(key))
			{
				object1.put(key, mergeArray(object1.getJSONArray(key), object2.getJSONArray(key)));
			}
			else
			{
				object1.put(key, object2.getJSONArray(key));
			}
		}
		else if (isTypeJSONObject(object2, key))
		{
			if (object1.has(key))
			{
				object1.put(key, mergeObject(object1.getJSONObject(key), object2.getJSONObject(key)));
			}
			else
			{
				object1.put(key, object2.getJSONObject(key));
			}
		}
		else if (isTypeJSONString(object2, key))
		{
			object1.put(key, object2.getString(key));
		}
		else if (isTypeJSONInteger(object2, key))
		{
			object1.put(key, object2.getInt(key));
		}
		else
			throw new JSONException("ERROR:CANNOT JUDGE ITEM TYPE.");
	}

	private static boolean isTypeJSONArray(JSONObject src, String key)
	{
		try
		{
			src.getJSONArray(key);
			return true;
		}
		catch (JSONException jsonE)
		{
			return false;
		}
	}

	private static boolean isTypeJSONObject(JSONObject src, String key)
	{
		try
		{
			src.getJSONObject(key);
			return true;
		}
		catch (JSONException jsonE)
		{
			return false;
		}
	}

	private static boolean isTypeJSONString(JSONObject src, String key)
	{
		try
		{
			src.getString(key);
			return true;
		}
		catch (JSONException jsonE)
		{
			return false;
		}
	}

	private static boolean isTypeJSONInteger(JSONObject src, String key)
	{
		try
		{
			src.getInt(key);
			return true;
		}
		catch (JSONException jsonE)
		{
			return false;
		}
	}

	private static boolean isTypeJSONArray(JSONArray src, int index)
	{
		try
		{
			src.getJSONArray(index);
			return true;
		}
		catch (JSONException jsonE)
		{
			return false;
		}
	}

	private static boolean isTypeJSONObject(JSONArray src, int index)
	{
		try
		{
			src.getJSONObject(index);
			return true;
		}
		catch (JSONException jsonE)
		{
			return false;
		}
	}

	private static boolean isTypeJSONString(JSONArray src, int index)
	{
		try
		{
			src.getString(index);
			return true;
		}
		catch (JSONException jsonE)
		{
			return false;
		}
	}

	private static boolean isTypeJSONInteger(JSONArray src, int index)
	{
		try
		{
			src.getInt(index);
			return true;
		}
		catch (JSONException jsonE)
		{
			return false;
		}
	}
}
