package com.example.books;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.content.ContentValues.TAG;

class NetFun
{
    // fields
    private static final String TAG = "Books - NetFun";

    static JSONObject urlToJson(String stringURL)
    {
        try
        {
            HttpURLConnection huc = (HttpURLConnection) (new URL(stringURL)).openConnection();
            huc.setReadTimeout(10_000);
            huc.setConnectTimeout(10_000);
            huc.setRequestMethod("GET");
            huc.connect();

            if (huc.getResponseCode() == 200)
            {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream())))
                {
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line);
                    }

                    Log.d(TAG, new JSONObject(sb.toString()).toString());
                    return new JSONObject(sb.toString());
                }
                catch (JSONException e)
                {
                    Log.d(TAG, "urlToJson >> JSONException thrown");
                    return null;
                }
            }
            else
            {
                Log.d(TAG, "urlToJson: huc.connect() wrong response code " + huc.getResponseCode());
                return null;
            }

        }
        catch (MalformedURLException e)
        {
            Log.d(TAG, "urlToJson: MalformedURLException thrown");
            return null;
        }
        catch (IOException e)
        {
            Log.d(TAG, "urlToJson: IOException thrown");
            return null;
        }
    }

    static List<Book> jsonToBookList(JSONObject obj, int limit)
    {
        if (obj == null)
        {
            return null;
        }

        List<Book> bookList = new CopyOnWriteArrayList<>();
        String title;
        String author;
        String pubDate;
        String category;
        double retailPrice;
        String imageURL;

        try
        {
            JSONArray rootArray = obj.getJSONArray("items");
            Log.d(TAG, rootArray.toString());
            int size = rootArray.length();
            Log.d(TAG, "jsonToBookList >> JSONArray size " + size + " items");

            for (int i = 0; i < size; i++)
            {
                try
                {
                    title = rootArray.getJSONObject(i).getJSONObject("volumeInfo").getString("title");
                    if (title.length() > 65)
                    {
                        title = title.substring(0, 60) + " (...)";
                    }
                }
                catch (JSONException e)
                {
                    Log.d(TAG, "jsonToBookList >> inner TITLE JSONException thrown at index " + i);
                    title = "NA";
                }

                try
                {
                    author = rootArray.getJSONObject(i).getJSONObject("volumeInfo").getJSONArray("authors").getString(0);
                }
                catch (JSONException e)
                {
                    Log.d(TAG, "jsonToBookList >> inner TITLE JSONException thrown at index " + i);
                    author = "NA";
                }

                try
                {
                    pubDate = rootArray.getJSONObject(i).getJSONObject("volumeInfo").getString("publishedDate");
                    if (pubDate.length() > 4)
                    {
                        pubDate = pubDate.substring(0, 4);
                    }
                }
                catch (JSONException e)
                {
                    Log.d(TAG, "jsonToBookList >> inner PUBDATE JSONException thrown at index " + i);
                    pubDate = "NA";
                }

                try
                {
                    category = rootArray.getJSONObject(i).getJSONObject("volumeInfo").getJSONArray("categories").getString(0);
                }
                catch (JSONException e)
                {
                    Log.d(TAG, "jsonToBookList >> inner CATEGORY JSONException thrown at index " + i);
                    category = "NA";
                }

                try
                {
                    retailPrice = rootArray.getJSONObject(i).getJSONObject("saleInfo").getJSONObject("retailPrice").getDouble("amount");
                }
                catch (JSONException e)
                {
                    Log.d(TAG, "jsonToBookList >> inner RETAILPRICE JSONException thrown at index " + i);
                    retailPrice = -1.0;
                }

                try
                {
                    imageURL = rootArray.getJSONObject(i).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail");
                }
                catch (JSONException e)
                {
                    Log.d(TAG, "jsonToBookList >> inner IMAGEURL JSONException thrown at index " + i);
                    imageURL = "NA";
                }

                bookList.add(new Book(title, author, pubDate, category, retailPrice, imageURL));
            }

            return bookList;
        }
        catch (JSONException e)
        {
            Log.d(TAG, "jsonToBookList: JSONException thrown");
            return null;
        }
    }
}
