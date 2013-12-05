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
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ForecastWidgetService extends RemoteViewsService {
	private static final String TAG = "ForecastWidgetService";
	private WeatherForecast mWeatherForecast;
	private ArrayList<WeeklyForecast> mWeeklyForecasts;

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		Log.i(TAG, "onGetViewFactory");
		return new ForecastRemoteViewsFactory(this.getApplicationContext(),
				intent);
	}

	class ForecastRemoteViewsFactory implements
			RemoteViewsService.RemoteViewsFactory {
		private Context mContext;
		private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
		private String mLocationName;

		public ForecastRemoteViewsFactory(Context context, Intent intent) {
			mContext = context.getApplicationContext();
			mAppWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		@Override
		public void onCreate() {
			Log.i(TAG, "onCreate");
			try {
				// In onCreate() you setup any connections / cursors to your
				// data source. Heavy lifting,
				// for example downloading or creating content etc, should be
				// deferred to onDataSetChanged()
				// or getViewAt(). Taking more than 20 seconds in this call will
				// result in an ANR.
				StaticHash hash = new StaticHash(mContext);
				int id = hash.get(LOCATEID,
						String.valueOf(mAppWidgetId), INIT_ID);
				mWeatherForecast = new WeatherForecast();
				mLocationName = mWeatherForecast.getLocationName(id);
				mWeeklyForecasts = mWeatherForecast.getWeeklyForecast(mContext,
						id);

				// We sleep for 3 seconds here to show how the empty view
				// appears in the interim.
				// The empty view is set in the StackWidgetProvider and should
				// be a sibling of the
				// collection view.
				Thread.sleep(3000);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}

		@Override
		public void onDataSetChanged() {
			Log.i(TAG, "getViewAt");
			// This is triggered when you call AppWidgetManager
			// notifyAppWidgetViewDataChanged
			// on the collection view corresponding to this factory. You can do
			// heaving lifting in
			// here, synchronously. For example, if you need to process an
			// image, fetch something
			// from the network, etc., it is ok to do it here, synchronously.
			// The widget will remain
			// in its current state while work is being done here, so you don't
			// need to worry about
			// locking up the widget.
			StaticHash hash = new StaticHash(mContext);
			int id = hash.get(LOCATEID,
					String.valueOf(mAppWidgetId), INIT_ID);
			mWeatherForecast = new WeatherForecast();
			mLocationName = mWeatherForecast.getLocationName(id);
			mWeeklyForecasts = mWeatherForecast.getWeeklyForecast(mContext, id);
		}

		@Override
		public void onDestroy() {
			Log.i(TAG, "onDestroy");
			// In onDestroy() you should tear down anything that was setup for
			// your data source,
			// eg. cursors, connections, etc.
		}

		@Override
		public int getCount() {
			return mWeeklyForecasts.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public RemoteViews getLoadingView() {
			// You can create a custom loading view (for instance when
			// getViewAt() is slow.) If you
			// return null here, you will get the default loading view.
			return null;
		}

		@Override
		public RemoteViews getViewAt(int position) {
			Log.i(TAG, "getViewAt");
			// position will always range from 0 to getCount() - 1.

			// We construct a remote views item based on our widget item xml
			// file, and set the
			// text based on the position.
			RemoteViews remoteviews = new RemoteViews(
					mContext.getPackageName(), R.layout.widget_forecast_ics_item);
			try {
				if (mWeeklyForecasts.size() <= 0)
					return remoteviews;
				remoteviews.setTextViewText(R.id.forecast_location_TextView,
						mLocationName);
				remoteviews.setTextViewText(R.id.forecast_date_TextView,
						mWeeklyForecasts.get(position).Date);
				remoteviews.setImageViewResource(R.id.forecast_ImageView,
						mWeatherForecast.getBitmapResource(mWeeklyForecasts
								.get(position).Forecast));
				remoteviews.setTextViewText(R.id.forecast_max_TextView,
						mWeeklyForecasts.get(position).MaxTemp);
				remoteviews.setTextViewText(R.id.forecast_min_TextView,
						mWeeklyForecasts.get(position).MinTemp);
				remoteviews.setTextViewText(R.id.forecast_probability_TextView,
						mWeeklyForecasts.get(position).Probability);

				// Next, we set a fill-intent which will be used to fill-in the
				// pending intent template
				// which is set on the collection view in StackWidgetProvider.
				Bundle extras = new Bundle();
				extras.putInt(EXTRA_ITEM, position);
				Intent fillInIntent = new Intent();
				fillInIntent.putExtras(extras);
				remoteviews.setOnClickFillInIntent(R.id.forecast_RelativeLayout,
						fillInIntent);

				// You can do heaving lifting in here, synchronously. For
				// example, if you need to
				// process an image, fetch something from the network, etc., it
				// is ok to do it here,
				// synchronously. A loading view will show up in lieu of the
				// actual contents in the
				// interim.
				Log.d(TAG, "Loading view=" + String.valueOf(position));
				Thread.sleep(500);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}

			// Return the remote views object.
			return remoteviews;
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}
	}
}
