package com.example.android.datafrominternet.tasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.datafrominternet.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class GetUrlResponse extends AsyncTask<URL, Void, String>
{
    private TextView mSearchResultsTextView;
    private TextView errorTextView;
    private ProgressBar progressBar;

    public GetUrlResponse(
            TextView mSearchResultsTextView,
            TextView errorTextView,
            ProgressBar progressBar)
    {
        this.mSearchResultsTextView = mSearchResultsTextView;
        this.errorTextView = errorTextView;
        this.progressBar = progressBar;
    }

    @Override
    protected String doInBackground(URL... urls)
    {
        String urlResults = "";

        try
        {
            urlResults = NetworkUtils.getResponseFromHttpUrl(urls[0]);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return urlResults;
    }

    @Override
    protected void onPreExecute()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String urlResult)
    {
        progressBar.setVisibility(View.INVISIBLE);

        if(urlResult != null && !urlResult.equals(""))
        {
            showJsonDataView(urlResult);
        }
        else
        {
            showErrorMessage();
        }
    }

    private void showJsonDataView(String urlResult)
    {
        errorTextView.setVisibility(View.INVISIBLE);
        printJsonData(urlResult);
    }

    private void showErrorMessage()
    {
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void printJsonData(String urlResult)
    {
        try
        {
            mSearchResultsTextView.setText("");

            JSONObject data = new JSONObject(urlResult);
            JSONArray items = data.getJSONArray("items");

            for(int i = 0 ; i < items.length() ; i++)
            {
                JSONObject item = (JSONObject) items.get(i);

                mSearchResultsTextView.append(item.getString("name") + " (" + item.getString("id") + ") " + "\n");
            }
        }
        catch(JSONException $ex)
        {
            $ex.printStackTrace();
        }

        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }
}
