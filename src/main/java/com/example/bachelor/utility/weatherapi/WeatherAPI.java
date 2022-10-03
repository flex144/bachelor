package com.example.bachelor.utility.weatherapi;

/**
 * This Code was copied from the example provided by VisualCrossing
 * See https://github.com/visualcrossing/WeatherApi/blob/master/Java/com/visualcrossing/weather/samples/TimelineApiForecastSample.java
 */

/*
 * TimelineApiSample
 * Example to show how to call the Visual Crossing Timeline Weather API using Java.
 * See https://www.visualcrossing.com/resources/documentation/weather-api/how-to-use-timeline-weather-api-to-retrieve-historical-weather-data-and-weather-forecast-data-in-java/
 */

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


public class WeatherAPI {



    public static WeatherApiResult timelineRequestHttpClient() throws Exception {
        //set up the end point
        String apiEndPoint="https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
        String location="Passau";
        String startDate=null;
        String endDate=null;

        String unitGroup="metric";
        String apiKey="Y93UZH25AZW28KAEVGTP6K24Q";

        StringBuilder requestBuilder=new StringBuilder(apiEndPoint);
        requestBuilder.append(URLEncoder.encode(location, StandardCharsets.UTF_8.toString()));

        if (startDate!=null && !startDate.isEmpty()) {
            requestBuilder.append("/").append(startDate);
            if (endDate!=null && !endDate.isEmpty()) {
                requestBuilder.append("/").append(endDate);
            }
        }

        URIBuilder builder = new URIBuilder(requestBuilder.toString());

        builder.setParameter("unitGroup", unitGroup)
                .setParameter("key", apiKey);



        HttpGet get = new HttpGet(builder.build());

        CloseableHttpClient httpclient = HttpClients.createDefault();

        CloseableHttpResponse response = httpclient.execute(get);

        String rawResult=null;
        try {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                System.out.printf("Bad response status code:%d%n", response.getStatusLine().getStatusCode());
                return null;
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                rawResult=EntityUtils.toString(entity, Charset.forName("utf-8"));
            }


        } finally {
            response.close();
        }

        return parseTimelineJson(rawResult);

    }
    private static WeatherApiResult parseTimelineJson(String rawResult) throws JSONException {

        if (rawResult==null || rawResult.isEmpty()) {
            System.out.printf("No raw data%n");
            return null;
        }

        JSONObject timelineResponse = new JSONObject(rawResult);

        ZoneId zoneId=ZoneId.of(timelineResponse.getString("timezone"));

        System.out.printf("Weather data for: %s%n", timelineResponse.getString("resolvedAddress"));


        /**
         * Eigener Code
         */

        JSONObject currentConditions = timelineResponse.getJSONObject("currentConditions");

        WeatherApiResult weatherApiResult = new WeatherApiResult();
        weatherApiResult.setHumidity(currentConditions.getDouble("humidity"));
        weatherApiResult.setIcon(currentConditions.getString("icon"));
        weatherApiResult.setPrecip(currentConditions.getDouble("precip"));
        weatherApiResult.setTemp(currentConditions.getDouble("temp"));
        weatherApiResult.setWindspeed(currentConditions.getDouble("windspeed"));

        return weatherApiResult;

        /**
         * Eigener Code Ende
         */

        /*JSONArray values=timelineResponse.getJSONArray("days");

        System.out.printf("Date\tMaxTemp\tMinTemp\tPrecip\tSource%n");
        for (int i = 0; i < values.length(); i++) {
            JSONObject dayValue = values.getJSONObject(i);

            ZonedDateTime datetime=ZonedDateTime.ofInstant(Instant.ofEpochSecond(dayValue.getLong("datetimeEpoch")), zoneId);

            double maxtemp=dayValue.getDouble("tempmax");
            double mintemp=dayValue.getDouble("tempmin");
            double pop=dayValue.getDouble("precip");
            String source=dayValue.getString("source");
            System.out.printf("%s\t%.1f\t%.1f\t%.1f\t%s%n", datetime.format(DateTimeFormatter.ISO_LOCAL_DATE), maxtemp, mintemp, pop,source );
        }*/
    }


    public static void main(String[] args)  throws Exception {
        WeatherAPI.timelineRequestHttpClient();
    }
}
