package com.example.telegrambot2;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TelegramApiService {

    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";
    public static final String BASE_API_ADDRESS = "http://51.38.98.6/api/v1/";
    Context context;
    String cityId;
    //2111456058:AAEzuJZ5NeneByBj5zcx7XfgruvEFaoZ6R8
    //2109005971:AAEams-FpYZl6gzA49un5DodS6lQnN9JKFw

    public TelegramApiService(Context context) {
        this.context = context;
    }

    public interface ValleyResponseListener {
        void onError(String message);

        void onResponse(String cityId);
    }

    public void getCityId(String cityName, ValleyResponseListener valleyResponseListener) {
// Instantiate the RequestQueue.
        //RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://51.38.98.6/api/v1/first";
        String url2 = QUERY_FOR_CITY_ID + cityName;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("Response", "Response: " + response.toString());
                cityId = "";
                try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityId = cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(context, "City ID = " + cityId, Toast.LENGTH_LONG).show();
                valleyResponseListener.onResponse(cityId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "That didn't work!", Toast.LENGTH_SHORT).show();
                valleyResponseListener.onError("Something wrong");
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);

        //return cityId;
    }

    public interface ForecastByIdResponse {
        void onError(String message);

        void onResponse(List<WeatherReportModel> weatherReportModels);
    }

    public void getCityForecastById(String cityId, ForecastByIdResponse forecastByIdResponse) {
        List<WeatherReportModel> weatherReportModels = new ArrayList<>();
        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(context,response.toString(), Toast.LENGTH_LONG).show();
                try {
                    JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");
                    for (int i = 0; i < consolidated_weather_list.length(); i++) {
                        WeatherReportModel one_day_weather = new WeatherReportModel();
                        JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(i);
                        one_day_weather.setId(first_day_from_api.getInt("id"));
                        one_day_weather.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                        one_day_weather.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                        one_day_weather.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                        one_day_weather.setCreated(first_day_from_api.getString("created"));
                        one_day_weather.setApplicable_date(first_day_from_api.getString("applicable_date"));
                        one_day_weather.setMin_temp(first_day_from_api.getLong("min_temp"));
                        one_day_weather.setMax_temp(first_day_from_api.getLong("max_temp"));
                        one_day_weather.setThe_temp(first_day_from_api.getLong("the_temp"));
                        one_day_weather.setWind_speed(first_day_from_api.getLong("wind_speed"));
                        one_day_weather.setAir_pressure(first_day_from_api.getLong("air_pressure"));
                        one_day_weather.setHumidity(first_day_from_api.getInt("humidity"));
                        one_day_weather.setVisibility(first_day_from_api.getInt("visibility"));
                        one_day_weather.setPredictability(first_day_from_api.getInt("predictability"));
                        weatherReportModels.add(one_day_weather);
                    }

                    forecastByIdResponse.onResponse(weatherReportModels);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    /*public List<WeatherReportModel> getCityForecastByName(String cityName) {

    }*/
    public interface BotInfoInterface {
        void onError(String message);

        void onResponse(List<String> updates);
    }


    public void checkConnection(String endpoint, String token, BotInfoInterface botInfoInterface) {
        String url2 = BASE_API_ADDRESS + endpoint + "?token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            ArrayList<String> info = new ArrayList<String>();
            @Override
            public void onResponse(JSONObject response) {
                String botInfo = null;
                try {
                    botInfo = "Bot Id: " + response.getInt("id") + System.getProperty("line.separator") +
                            "First Name: " + response.getString("first_name") + System.getProperty("line.separator") +
                            "Username: " + response.getString("username");
                    Toast.makeText(context, "Connected to: " + response.getString("username"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                info.add(botInfo);
                //Toast.makeText(context, "response = " + response.toString(), Toast.LENGTH_LONG).show();
                /*try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityId = cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                //Toast.makeText(context, "City ID = " + cityId, Toast.LENGTH_LONG).show();
                botInfoInterface.onResponse(info);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response", "Response error: " + error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                botInfoInterface.onError("Something wrong");
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface UpdateList {
        void onError(String message);

        void onResponse(List<UpdateModel> updates);
    }

    public void getUpdates(String endpoint, String token, UpdateList updateList) {
        String url2 = BASE_API_ADDRESS + endpoint + "?token=" + token;
        //Toast.makeText(context, "url = " + url2, Toast.LENGTH_LONG).show();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url2, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                List<UpdateModel> updates = new ArrayList<>();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject update = response.getJSONObject(i);
                        UpdateModel updateModel = new UpdateModel();
                        updateModel.setUpdateId(update.getInt("update_id"));
                        updateModel.setMessageId(update.getJSONObject("channel_post").getInt("message_id"));
                        updateModel.setChatTitle(update.getJSONObject("channel_post").getJSONObject("chat").getString("title"));
                        updateModel.setChatUserName(update.getJSONObject("channel_post").getJSONObject("chat").getString("username"));
                        updateModel.setChatId(update.getJSONObject("channel_post").getJSONObject("chat").getString("id"));
                        updateModel.setDateTime(update.getJSONObject("channel_post").getInt("date"));
                        updateModel.setText(update.getJSONObject("channel_post").getString("text"));
                        updates.add(updateModel);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, "Updates count = " + updates.size(), Toast.LENGTH_SHORT).show();
                updateList.onResponse(updates);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response", "Response error: " + error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                updateList.onError("Something wrong");
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface ChatsListInterface {
        void onError(String message);

        void onResponse(List<ChatModel> chatModelList);
    }

    public void getAllChats(String endpoint, String token, ChatsListInterface allChatsList) {
        String url2 = BASE_API_ADDRESS + endpoint + "?token=" + token;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url2, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<ChatModel> chatModels = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject chat = null;
                        ChatModel chatModel = new ChatModel();
                        chat = response.getJSONObject(i);
                        chatModel.setChatId(chat.getString("id"));
                        chatModel.setTitle(chat.getString("title"));
                        chatModel.setUser_name(chat.getString("username"));
                        chatModel.setInvite_link(chat.getString("invite_link"));
                        chatModels.add(chatModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                allChatsList.onResponse(chatModels);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response", "Response error: " + error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                allChatsList.onError("Something wrong: " + error.toString());
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);

        //return cityId;
    }

    public interface StringResponseListener {
        void onError(String message);

        void onResponse(String message);
    }

    public void sendMessage(String endpoint, String token, String chatId, String message, StringResponseListener stringResponseListener) {
        String url2 = BASE_API_ADDRESS + endpoint + "?token=" + token + "&chat_id=" + chatId + "&message=" + message;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject responseChatMessage = null;
                String responseMessage = "Message send to ";

                try {
                    responseChatMessage = response.getJSONObject("chat");
                    responseMessage += responseChatMessage.getString("title") + " chat";

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                stringResponseListener.onResponse(responseMessage);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response", "Response error: " + error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                stringResponseListener.onError("Something wrong:" + error.toString());
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);

    }

}
