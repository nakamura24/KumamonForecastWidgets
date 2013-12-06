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

import jp.library.weatherforecast.StaticHash;
import jp.library.weatherforecast.WeatherForecast;
import jp.library.weatherforecast.WeatherForecast.*;
import jp.widgets.kumamon.forecast.ForecastWidgetConfigure;
import jp.widgets.kumamon.forecast.R;
import jp.widgets.kumamon.lib.*;
import static jp.widgets.kumamon.forecast.ForecastWidgetConstant.*;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class KumamonForecastWidget extends WidgetBase {
	public static final String TAG = "ics.WidgetWeekly";

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Log.i(TAG, "onEnabled");
		try {
			Intent intent = new Intent(context, WidgetService.class);
			context.startService(intent);
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
	}

	@Override
	public void onUpdate(final Context context,
			AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.i(TAG, "onUpdate");
		try {
			final WeatherForecast weatherForecast = new WeatherForecast();
			StaticHash hash = new StaticHash(context);
			for (int i = 0; i < appWidgetIds.length; i++) {
				final int appWidgetId = appWidgetIds[i];
				int id = hash.get(LOCATEID, String.valueOf(appWidgetId),
						INIT_ID);
				hash.put(LOCATEID, String.valueOf(appWidgetId), id);
				weatherForecast.setOnPostExecute(new OnPostExecute() {
					@Override
					public void onPostExecute() {
						updateAppWidget(context, appWidgetId, weatherForecast);
					}
				});
				weatherForecast.getForecast(context, id);
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
				hash.remove(LOCATEID, String.valueOf(appWidgetIds[i]));
				hash.remove(POSITION, String.valueOf(appWidgetIds[i]));
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
			ArrayList<String> appWidgetIds = hash.keys(LOCATEID);
			for (int i = 0; i < appWidgetIds.size(); i++) {
				Log.d(TAG, "onDeleted - " + appWidgetIds);
				hash.remove(LOCATEID, appWidgetIds.get(i));
				hash.remove(POSITION, appWidgetIds.get(i));
			}
			Intent intent = new Intent(context, WidgetService.class);
			context.stopService(intent);
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
					StaticHash hash = new StaticHash(context);
					hash.put(LOCATEID, String.valueOf(appWidgetId), id);
					Log.d(TAG,
							"CONFIG_DONE appWidgetId="
									+ String.valueOf(appWidgetId) + "id="
									+ String.valueOf(id));
					final WeatherForecast weatherForecast = new WeatherForecast();
					weatherForecast.setOnPostExecute(new OnPostExecute() {
						@Override
						public void onPostExecute() {
							updateAppWidget(context, appWidgetId,
									weatherForecast);
						}
					});
					weatherForecast.getForecast(context, id);
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
	}

	public static void updateAppWidget(Context context, int appWidgetId,
			WeatherForecast weatherForecast) {
		try {
			Log.i(TAG, "updateAppWidget - " + String.valueOf(appWidgetId));
			// ボタンが押された時に発行されるインテントを準備する
			Intent intent = new Intent(context, ForecastWidgetConfigure.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			intent.putExtra(APPWIDGET_CALLER, APPWIDGET_NORMAL);
			intent.setAction(APPWIDGET_CONFIGURE);
			PendingIntent pendingIntent = PendingIntent.getActivity(context,
					appWidgetId, intent, 0);
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_forecast_ics);
			remoteViews.setOnClickPendingIntent(R.id.forecast_icon_ImageView,
					pendingIntent);

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

			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
					R.id.forecast_widget_StackView);
			appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		} catch (Exception ex) {
			Log.e(TAG, ex.getMessage());
		}
	}

	public static class WidgetService extends Service {
		@Override
		public IBinder onBind(Intent in) {
			return null;
		}

		@Override
		public void onCreate() {
			super.onCreate();
			Log.i(TAG, "onCreate");
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_USER_PRESENT);
			registerReceiver(mReceiver, filter);
		}

		@Override
		public void onDestroy() {
			Log.i(TAG, "onDestroy");
			unregisterReceiver(mReceiver);
			super.onDestroy();
		}

		private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, Intent intent) {
				Log.i(TAG, "mReceiver onReceive = " + intent.getAction());
				if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
					StaticHash hash = new StaticHash(context);
					ArrayList<String> appWidgetIds = hash.keys(LOCATEID);
					for (int i = 0; i < appWidgetIds.size(); i++) {
						final int appWidgetId = Integer.parseInt(appWidgetIds
								.get(i));
						int id = hash.get(LOCATEID,
								String.valueOf(appWidgetId), INIT_ID);
						final WeatherForecast weatherForecast = new WeatherForecast();
						weatherForecast.setOnPostExecute(new OnPostExecute() {
							@Override
							public void onPostExecute() {
								updateAppWidget(context, appWidgetId,
										weatherForecast);
							}
						});
						weatherForecast.getForecast(context, id);
					}
				}
			}
		};
	}
}
