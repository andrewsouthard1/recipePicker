package com.hassan.recipesbyingredients;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    TextView input, error;
    Button button;
    ProgressBar progressbar;
    String ingredients = "";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);

        input = (TextView)fragmentView.findViewById(R.id.input);
        error = (TextView)fragmentView.findViewById(R.id.error);
        progressbar = (ProgressBar)fragmentView.findViewById(R.id.progressBar);


        button = (Button) fragmentView.findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if ((input.getText()).toString().trim().equals("")){
                    error.setText("You must enter at at least an ingredient");
                    error.setVisibility(View.VISIBLE);
                }else{
                    String allingredients[] = (input.getText()).toString().split("\\r?\\n");
                    if (allingredients.length<1){
                        error.setText("You must enter at at least an ingredient");
                        error.setVisibility(View.VISIBLE);
                    }else {
                        for (int i = 0; i < allingredients.length; i++){
                            if (!allingredients[i].trim().equals("")){
                                ingredients += allingredients[i].trim();
                                if(i != allingredients.length-1)ingredients += ",";
                            }
                        }

                        if (ingredients.equals("")){
                            error.setText("You must enter at at least an ingredient");
                            error.setVisibility(View.VISIBLE);
                        }else {
                            try{
                                new GetRecipes().execute();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        return fragmentView;
    }

    public class GetRecipes extends AsyncTask<String, Void, String> {
        private static final String API_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients";
        private static final String API_KEY = "iogrcokopJmshQlfZUmJdwRwwxG2p1Mn65HjsnaIDjPP96ZLIE";

        @Override
        protected void onPreExecute() {
            progressbar.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try{
                URL url = new URL(API_URL + "?fillIngredients=false" + "&ingredients="+ URLEncoder.encode(ingredients, "UTF-8") + "&limitLicense=false&number=2&ranking=1" + "&dataType=json&mashape-key=" + API_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

                return stringBuilder.toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String response) {
            progressbar.setVisibility(View.INVISIBLE);
            button.setVisibility(View.VISIBLE);
//            System.out.println(response);
            if (response.length() < 10){
                error.setText("Your search did not match any recipe. Please add some new ingredients and try again.");
                error.setVisibility(View.VISIBLE);
            }else {
                final Intent recipesactivity = new Intent(getActivity(), RecipesActivity.class);
                recipesactivity.putExtra("JSON", response);
                startActivity(recipesactivity);
            }
        }
    }
}
