public class Recipe {


    protected String recipeUrl;
    public String recipeName;
    public String recipePhoto;
    public String[] ingredients;
    public float ratings;
    public String cookTime;
    public int serve;

    public Recipe(String url)
    {
        this.recipeUrl = url;
    }
}
