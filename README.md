# Webcrawler (For Yummly Recipes)

## Installation
Open the project via IntelliJ IDEA. A popup dialog will appear in the lower right

![Maven projects need to be imported](https://www.jetbrains.com/help/img/idea/2018.3/maven_popup.png)

Click [Import Changes](). The code is under src/main/java. Set the page limit in main method in SpiderTest.
```
spider.search(url, page_limit);
```
Build and run the project.

## Instructions
Run the main method from the SpiderTest class. To specify how many pages to parse, add the desired integer to the second argument of spider.search. Currently, the value is set to 1000 (Runtime: 800s). This webcrawler parses each page in roughly 1 second.

## External Libraries
Add via Maven.
* [com.google.code.gson:gson:2.8.5](https://github.com/google/gson/blob/master/UserGuide.md)
* [org.apache.commons:commons-text:1.6](https://commons.apache.org/proper/commons-text/)
* [org.jsoup:jsoup:1.11.3](https://jsoup.org/)