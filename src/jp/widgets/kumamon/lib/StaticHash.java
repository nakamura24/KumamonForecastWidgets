/*
 * Copyright (C) 2012 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.widgets.kumamon.lib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StaticHash {
	private Context context;

	public StaticHash(Context context) {
		this.context = context.getApplicationContext();
	}

	public void put(String key, boolean value) {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		// Commit the edits!
		editor.commit();
	}

	public void put(String key, float value) {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(key, value);
		// Commit the edits!
		editor.commit();
	}

	public void put(String key, int value) {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		// Commit the edits!
		editor.commit();
	}

	public void put(String key, long value) {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);
		// Commit the edits!
		editor.commit();
	}

	public void put(String key, String value) {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		// Commit the edits!
		editor.commit();
	}

	public boolean get(String key, boolean defValue) {
		// Restore preferences
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defValue);
	}

	public float get(String key, float defValue) {
		// Restore preferences
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getFloat(key, defValue);
	}

	public int get(String key, int defValue) {
		// Restore preferences
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getInt(key, defValue);
	}

	public long get(String key, long defValue) {
		// Restore preferences
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getLong(key, defValue);
	}

	public String get(String key, String defValue) {
		// Restore preferences
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getString(key, defValue);
	}

	public boolean contains(String key) {
		// Restore preferences
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.contains(key);
	}

	public void remove(String key) {
		// Restore preferences
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		// Commit the edits!
		editor.commit();
	}

	public void put(String name, String key, boolean value) {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		// Commit the edits!
		editor.commit();
	}

	public void put(String name, String key, float value) {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(key, value);
		// Commit the edits!
		editor.commit();
	}

	public void put(String name, String key, int value) {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		// Commit the edits!
		editor.commit();
	}

	public void put(String name, String key, long value) {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);
		// Commit the edits!
		editor.commit();
	}

	public void put(String name, String key, String value) {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		// Commit the edits!
		editor.commit();
	}

	public boolean get(String name, String key, boolean defValue) {
		// Restore preferences
		SharedPreferences settings = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		return settings.getBoolean(key, defValue);
	}

	public float get(String name, String key, float defValue) {
		// Restore preferences
		SharedPreferences settings = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		return settings.getFloat(key, defValue);
	}

	public int get(String name, String key, int defValue) {
		// Restore preferences
		SharedPreferences settings = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		return settings.getInt(key, defValue);
	}

	public long get(String name, String key, long defValue) {
		// Restore preferences
		SharedPreferences settings = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		return settings.getLong(key, defValue);
	}

	public String get(String name, String key, String defValue) {
		// Restore preferences
		SharedPreferences settings = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		return settings.getString(key, defValue);
	}

	public boolean contains(String name, String key) {
		// Restore preferences
		SharedPreferences settings = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		return settings.contains(key);
	}

	public void remove(String name, String key) {
		// Restore preferences
		SharedPreferences settings = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		// Commit the edits!
		editor.commit();
	}

	public ArrayList<String> keys(String name) {
		ArrayList<String> keys = new ArrayList<String>();
		try {
			// Restore preferences
			SharedPreferences settings = context.getSharedPreferences(name,
					Context.MODE_PRIVATE);
			Map<String, ?> map = settings.getAll();
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext()) {
				keys.add(iterator.next());
			}
		} catch (Exception e) {
		}
		return keys;
	}

	public int size(String name) {
		try {
			// Restore preferences
			SharedPreferences settings = context.getSharedPreferences(name,
					Context.MODE_PRIVATE);
			Map<String, ?> map = settings.getAll();
			return map.size();
		} catch (Exception e) {
		}
		return 0;
	}
}
