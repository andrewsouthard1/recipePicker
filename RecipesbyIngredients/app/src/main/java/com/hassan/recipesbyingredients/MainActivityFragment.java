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
    JSONObject recipes_result = new JSONObject();

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
                                new GetRecipes().execute().get();

                                final Intent recipesactivity = new Intent(getActivity(), RecipesActivity.class);
                                startActivity(recipesactivity);


                            }catch (Exception e){

                            }

//                        error.setText("Gooood");
//                        error.setTextColor(Color.GREEN);
//                        error.setVisibility(View.VISIBLE);
//                        error.setVisibility(View.INVISIBLE);

                        }

//                        HttpResponse<JsonNode> response = Unirest.get(
//                                "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?fillIngredients=false&" +
//                                        "ingredients=apples%2Cflour%2Csugar&limitLicense=false&number=5&ranking=1")
//                                .header("X-Mashape-Key", "<required>")
//                                .header("Accept", "application/json")
//                                .asJson();
                    }
                }

//                try {
//                    usamount = Double.parseDouble(usdollarEditText.getText().toString());
//
//                    double franc = 0;
//                    franc = usamount * 439.36;
//                    DecimalFormat format = new DecimalFormat("#.00");
//                    result.setText(usamount + " US Dollar equal " + Double.valueOf(format.format(franc)) + " Comorian Francs");
//                }catch (NumberFormatException e){
//                    result.setText("Please check your input");
//                }
            }
        });

        return fragmentView;
    }

    public class GetRecipes extends AsyncTask<String, Void, String> {
        private static final String API_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients";
        private static final String API_KEY = "";

        @Override
        protected void onPreExecute() {
            progressbar.setVisibility(View.VISIBLE);
            button.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try{
                URL url = new URL(API_URL + "?fillIngredients=false" + "&ingredients="+ URLEncoder.encode(ingredients, "UTF-8") + "&limitLicense=false&number=5&ranking=1" + "&dataType=json&mashape-key=" + API_KEY);

//                HttpResponse<JsonNode> response = Unirest.get(url)
//                    .header("X-Mashape-Key", "<required>")
//                    .header("Accept", "application/json")
//                    .asJson();
//                JSONObject myObject = new JSONObject(result);

            }catch (Exception e){
                Log.e("Image", "Failed to load URL", e);
                Log.e("error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String response) {
            progressbar.setVisibility(View.INVISIBLE);
            button.setVisibility(View.VISIBLE);
        }
    }
}
