import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SpiderTest {
    public static void main(String[] args)
    {
        long start = System.currentTimeMillis();
        Spider spider = new Spider();
        spider.search("https://yummly.com/recipes", 1000);
        long end = System.currentTimeMillis();
        long time = end - start;
        NumberFormat formatter = new DecimalFormat("#0.00000");
        System.out.println("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");
    }
}
