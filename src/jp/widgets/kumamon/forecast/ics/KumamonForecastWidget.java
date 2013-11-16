/*
 * Copyright (C) 2012 M.Nakamura
 *
 * This software is licensed under a Creative Commons
 * Attribution-NonCommercial-ShareAlike 2.1 Japan License.
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://creativecommons.org/licenses/by-nc-sa/2.1/jp/legalcode
 */
package jp.widgets.kumamon.forecast.ics;

import java.util.ArrayList;

import jp.library.weatherforecast.WeatherForecast;
import jp.library.weatherforecast.WeatherForecast.*;
import jp.widgets.kumamon.forecast.R;
import jp.widgets.kumamon.lib.*;
import static jp.widgets.kumamon.forecast.ForecastWidgetConstant.*;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;


@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class KumamonForecastWidget extends WidgetBase {
	public static final String TAG = "ics.WidgetWeekly";
	private WeatherForecast mWeatherForecast;

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Log.i(TAG, "onEnabled");
		try {
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
	}

	@Override
	public void onUpdate(final Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.i(TAG, "onUpdate");
		try {
			mWeatherForecast = new WeatherForecast();
			StaticHash hash = new StaticHash(context);
			for (int i = 0; i < appWidgetIds.length; i++) {
				final int appWidgetId = appWidgetIds[i];
				int id = hash.get(LOCATEID,
						String.valueOf(appWidgetId), INIT_ID);
				mWeatherForecast.getForecast(context, id);
				mWeatherForecast.setOnPostExecute(new OnPostExecute() {
					@Override
					public void onPostExecute() {
						updateAppWidget(context, appWidgetId);
					}
				});
			}
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.i(TAG, "onDeleted");
		super.onDeleted(context, appWidgetIds);
		try {
			context = context.getApplicationContext();
			StaticHash hash = new StaticHash(context);
			for (int i = 0; i < appWidgetIds.length; i++) {
				Log.d(TAG, "onDeleted - " + String.valueOf(appWidgetIds[i]));
				hash.remove(LOCATEID,
						String.valueOf(appWidgetIds[i]));
				hash.remove(POSITION,
						String.valueOf(appWidgetIds[i]));
			}
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
	}

	@Override
	public void onDisabled(Context context) {
		Log.i(TAG, "onDisabled");
		super.onDisabled(context);
		try {
			context = context.getApplicationContext();
			StaticHash hash = new StaticHash(context);
			ArrayList<String> appWidgetIds = hash
					.keys(LOCATEID);
			for (int i = 0; i < appWidgetIds.size(); i++) {
				Log.d(TAG, "onDeleted - " + appWidgetIds);
				hash.remove(LOCATEID, appWidgetIds.get(i));
				hash.remove(POSITION, appWidgetIds.get(i));
			}
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
	}

	@Override
	public void onReceive(final Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.i(TAG, "onReceive - " + intent.getAction());
		try {
			if (CONFIG_DONE.equals(intent.getAction())) {
				Bundle extras = intent.getExtras();
				if (extras != null) {
					final int appWidgetId = extras.getInt(
							AppWidgetManager.EXTRA_APPWIDGET_ID,
							AppWidgetManager.INVALID_APPWIDGET_ID);
					int id = extras.getInt(LOCATEID, INIT_ID);
					Log.d(TAG,
							"CONFIG_DONE appWidgetId="
									+ String.valueOf(appWidgetId) + "id="
									+ String.valueOf(id));
					mWeatherForecast = new WeatherForecast();
					mWeatherForecast.getForecast(context, id);
					mWeatherForecast.setOnPostExecute(new OnPostExecute() {
						@Override
						public void onPostExecute() {
							updateAppWidget(context, appWidgetId);
						}
					});
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
	}

	public void updateAppWidget(Context context, int appWidgetId) {
		try {
			Log.i(TAG, "updateAppWidget - " + String.valueOf(appWidgetId));
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_forecast_ics);

			Intent stackViewIntent = new Intent(context,
					ForecastWidgetService.class);
			stackViewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetId);
			// When intents are compared, the extras are ignored, so we need to
			// embed the extras
			// into the data so that the extras will not be ignored.
			stackViewIntent.setData(Uri.parse(stackViewIntent
					.toUri(Intent.URI_INTENT_SCHEME)));
			remoteViews.setRemoteAdapter(R.id.forecast_widget_StackView, stackViewIntent);

			// ボタンが押された時に発行されるインテントを準備する
			Intent congigIntent = new Intent(context, jp.widgets.kumamon.forecast.ForecastWidgetConfigure.class);
			congigIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			congigIntent.putExtra(APPWIDGET_CALLER, APPWIDGET_STACK);
			congigIntent.setAction(APPWIDGET_CONFIGURE);
			PendingIntent pendingIntent = PendingIntent.getActivity(context,
					appWidgetId, congigIntent, 0);
			remoteViews.setOnClickPendingIntent(R.id.forecast_icon_ImageView,
					pendingIntent);

			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
					R.id.forecast_widget_StackView);
			appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
	}
}
