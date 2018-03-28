package com.example.jian0080.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecastActivity extends Activity {

    TextView currentText;
    TextView maxText;
    TextView minText;
    TextView windText;
    ImageView iconImage;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        currentText = (TextView) findViewById(R.id.text_current_temp);
        currentText.setPadding(15,3,15,3);
        maxText = (TextView) findViewById(R.id.text_max_temp);
        maxText.setPadding(15,3,15,3);
        minText = (TextView) findViewById(R.id.text_min_temp);
        minText.setPadding(15,3,15,3);
        windText = (TextView) findViewById(R.id.text_wind_speed);
        windText.setPadding(15,3,15,3);
        iconImage = (ImageView) findViewById(R.id.icon_image);
        progressBar = (ProgressBar) findViewById(R.id.progressing);
        progressBar.setVisibility(View.VISIBLE);

        ForecastQuery fq = new ForecastQuery();
        fq.execute();
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String[]> {
        private String windSpeed;
        private String minTemp;
        private String maxTemp;
        private String currentTemp;
        private String iconName;

        @Override
        protected String[] doInBackground(String... args) {
            URL url;
            String imageURL;

            try {
                //connect to url
                url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                //download contents
                InputStream connInputStream = conn.getInputStream();

                //parse contents
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(connInputStream, null);

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {

                    switch (eventType) {
                        case XmlPullParser.START_TAG:

                            String tagname = parser.getName();

                            if (tagname.equalsIgnoreCase("temperature")) {
                                currentTemp = parser.getAttributeValue(null, "value");
                                publishProgress(25);
                                minTemp = parser.getAttributeValue(null, "min");
                                publishProgress(50);
                                maxTemp = parser.getAttributeValue(null, "max");
                                publishProgress(75);
                            }
                            Log.i(null, "Tag name: "+tagname+" value: " + currentTemp + " max: " + maxTemp + " min: " + minTemp);
                            if (tagname.equalsIgnoreCase("speed")){
                                windSpeed = parser.getAttributeValue(null, "value");
                            }
                            if (tagname.equalsIgnoreCase("weather")) {
                                iconName = parser.getAttributeValue(null, "icon");
                                imageURL = "http://openweathermap.org/img/w/" + iconName + ".png";
                                Log.i(null, "Image url: "+imageURL);
                                Bitmap image = HttpUtils.getImage(imageURL);
                                FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                                publishProgress(100);

                            }
                            break;
                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        default:
                            break;
                    }
                    eventType = parser.next();
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] temperatures = {currentTemp, maxTemp, minTemp, iconName+".png", windSpeed};
            return temperatures;//return parsed contents as array of string
        }

        @Override
        protected void onProgressUpdate(Integer ...value){
            super.onProgressUpdate(value[0]);
            progressBar.setProgress(value[0]);
            if (value[0]==100) {
                progressBar.setVisibility(View.INVISIBLE);
            }
            Log.i(null, "onProgressUpdate() gets called");
        }

        protected void onPostExecute(String[] result){

            currentText.setTextSize(getResources().getDimension(R.dimen.fab_margin));
            maxText.setTextSize(getResources().getDimension(R.dimen.fab_margin));
            minText.setTextSize(getResources().getDimension(R.dimen.fab_margin));
            windText.setTextSize(getResources().getDimension(R.dimen.fab_margin));
            currentText.setText("Current temperature: "+result[0]+" \u2103");
            maxText.setText("Maximum: "+result[1]+" \u2103");
            minText.setText("Minimum: "+result[2]+" \u2103");
            windText.setText("Wind speed: "+result[4]+" KPH");

            FileInputStream fis = null;
            if (fileExistance(result[3])) {
                try {
                    fis = openFileInput(result[3]);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bm = BitmapFactory.decodeStream(fis);

                iconImage.setImageBitmap(bm);

                Log.i(null, "Image file "+result[3]+" has been found locally.");
            } else {
                Log.i(null,"You have to download image file "+result[3]+" again!");
            }
        }

        public boolean fileExistance(String fname){

            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

    }

}
