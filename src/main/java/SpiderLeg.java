import com.google.gson.Gson;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SpiderLeg {
    //Fields
    private static final String USER_AGENT =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
    private List<String> links = new LinkedList<String>();
    private Document htmlDoc;

    public boolean crawl(String url)
    {
        try
        {
            Connection conn = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDoc = conn.get();
            this.htmlDoc = htmlDoc;
            if (conn.response().statusCode() == 200)
            {
                //System.out.println("\nVISITING Received web page at " + url);
            }
            else
                System.out.println(String.format("Code %d", conn.response().statusCode()));
            if (!conn.response().contentType().contains("text/html"))
            {
                System.out.println("ERROR Retrieved something other than HTML");
                return false;
            }
            Elements linksOnPage = htmlDoc.select("a[href*=/recipe/]");
            //System.out.println("Found (" + linksOnPage.size() + ") links");
            for (Element link: linksOnPage)
                this.links.add(link.absUrl("href"));
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    public String parseRecipe()
    {
        if (this.htmlDoc == null)
        {
            System.out.println("ERROR Call crawl() first");
            return null;
        }
        //System.out.println("Parsing recipe ...");
        String json = null;
        Recipe recipe = new Recipe(this.htmlDoc.location());

        //Find Recipe Name
        Element e = this.htmlDoc.selectFirst("h1.recipe-title");
        if (e != null)
            recipe.recipeName = StringEscapeUtils.unescapeHtml4(e.html());
        else
            return null;
        //Find Recipe Photo url
        e = this.htmlDoc.selectFirst("*.recipe-image");
        if (e != null)
            recipe.recipePhoto = e.absUrl("src");
        //Find Recipe Ingredients
        Elements ings = this.htmlDoc.select("li.IngredientLine");
        if (ings != null)
        {
            ArrayList<String> ingredients = new ArrayList<String>();
            for (Element ing : ings) {
                Element i = ing.selectFirst("span.Ingredient");
                ingredients.add(i.html().replace("\n<!-- -->",""));
            }
            recipe.ingredients = ingredients.toArray(new String[ingredients.size()]);
        }
        //Find Recipe ratings
        Element ratings = this.htmlDoc.selectFirst("a.recipe-details-rating");
        if (ratings != null)
        {
            Elements rs = ratings.children();
            float rating = 0;
            for (Element r: rs)
            {
                if (r.hasClass("full-star"))
                    rating += 1;
                if (r.hasClass("half-star"))
                    rating += 0.5;
                if (r.hasClass("empty-star"))
                    rating += 0;
            }
            recipe.ratings = rating;
        }
        else
            recipe.ratings = 0f;
        //Find Recipe cook time
        e = this.htmlDoc.selectFirst("div.recipe-summary-item.unit");
        if (e != null)
        {
            Elements times = e.children();
            String time = times.get(0).html() + " " + times.get(1).html();
            recipe.cookTime = time;
        }
        //Find Recipe servings
        e = this.htmlDoc.selectFirst("div.servings");
        if (e != null)
        {
            int servings = Integer.parseInt(e.child(0).child(0).val());
            recipe.serve = servings;
        }
        json = new Gson().toJson(recipe);
        //System.out.println(json);
        return json;
    }

    public List<String> getLinks()
    {
        return this.links;
    }
}
