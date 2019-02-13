import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Spider {
    //Fields
    private static int MAX_PAGES_SEARCH;
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();


    public void search(String url, int depth)
    {
        MAX_PAGES_SEARCH = depth;
        ArrayList<String> recipes = new ArrayList<String>();
        while (this.pagesVisited.size() < MAX_PAGES_SEARCH)
        {
            //System.out.println(String.format("Visited: %d", this.pagesVisited.size()));
            String currUrl;
            SpiderLeg leg = new SpiderLeg();
            if (this.pagesToVisit.isEmpty())
            {
                currUrl = url;
                this.pagesVisited.add(url);
            }
            else
                currUrl = this.nextUrl();

            boolean error = leg.crawl(currUrl);
            if (error)
                recipes.add(leg.parseRecipe());

            this.pagesToVisit.addAll(leg.getLinks());
        }
        recipes.remove(0);
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            writer.write(Arrays.toString(recipes.toArray(new String[recipes.size()])));
            writer.flush();
            writer.close();
        }
        catch (IOException e) {}
        System.out.println(String.format("\nDONE Visited %s pages", this.pagesVisited.size()));
    }

    private String nextUrl()
    {
        String nextUrl;
        do
        {
            nextUrl = this.pagesToVisit.remove(0);
        } while (this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }
}
