package com.hassan.recipesbyingredients;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipesActivityFragment extends Fragment {
    View fragmentView;
    public RecipesActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_recipes, container, false);

        Bundle intent = getActivity().getIntent().getExtras();
        String jsonStr = intent.getString("JSON");

        final ListView listview = (ListView)fragmentView.findViewById(R.id.listview);
        final ArrayList<Recipe> recipeList = Recipe.getRecipes(jsonStr, this.getContext());

        RecipeAdapter adapter = new RecipeAdapter(this.getContext(), recipeList);
        listview.setAdapter(adapter);

        return fragmentView;
    }

    public class RecipeAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private ArrayList<Recipe> dataSource;

        public RecipeAdapter(Context cont, ArrayList<Recipe> recipes) {
            context = cont;
            dataSource = recipes;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return dataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return dataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = inflater.inflate(R.layout.recipe_list_item, parent, false);

            TextView titleTextView = (TextView) rowView.findViewById(R.id.recipe_title);
            TextView subtitleTextView = (TextView) rowView.findViewById(R.id.recipe_subtitle);
            ImageView thumbnailImageView = (ImageView) rowView.findViewById(R.id.recipe_image);

            Recipe recipe = (Recipe) getItem(position);

            titleTextView.setText(recipe.title);
            subtitleTextView.setText("You have " + recipe.usedIngredientCount + " ingredient(s) out of " + (recipe.usedIngredientCount+recipe.missedIngredientCount) + " needed.");
            Picasso.with(context).load(recipe.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);

            return rowView;
        }
    }

    public static class Recipe {
        public String title;
        public String imageUrl;
        public int usedIngredientCount;
        public int missedIngredientCount;

        public static ArrayList<Recipe> getRecipes(String jsonStr, Context context){
            final ArrayList<Recipe> recipeList = new ArrayList<>();

            try {
                JSONArray recipes = new JSONArray(jsonStr);
                for(int i = 0; i < recipes.length(); i++){
                    Recipe recipe = new Recipe();

                    recipe.title = recipes.getJSONObject(i).getString("title");
                    recipe.imageUrl = recipes.getJSONObject(i).getString("image");
                    recipe.usedIngredientCount = recipes.getJSONObject(i).getInt("usedIngredientCount");
                    recipe.missedIngredientCount = recipes.getJSONObject(i).getInt("missedIngredientCount");

                    recipeList.add(recipe);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return recipeList;
        }
    }
}