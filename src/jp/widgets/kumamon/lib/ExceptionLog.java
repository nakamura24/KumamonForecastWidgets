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

import android.util.Log;

public class ExceptionLog {
	public static void Log(String tag, Exception e) {
		for (int i = 0; i < e.getStackTrace().length; i++) {
			Log.e(tag,
					e.getStackTrace()[i].getFileName()
							+ ":"
							+ String.valueOf(e.getStackTrace()[i]
									.getLineNumber()) + ":"
							+ e.getStackTrace()[i].getMethodName());
		}
	}
}
