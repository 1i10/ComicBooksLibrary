package com.app.comicslibrary.Jsoup;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.app.comicslibrary.ImageSaveAndLoad;

import com.app.comicslibrary.MenuFragments.ListActions.ModelCollectView;

import com.app.comicslibrary.StringConverter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ParseBookInfo {
    private Context context;

    public ParseBookInfo(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<ModelCollectView> searchComicsInfo(String searchLine, int numberPage) throws IOException {

        //replace all space in string line to '+'
        String newString = searchLine.replace(' ', '+');

        //get url for parsing
        String url = "https://www.goodreads.com/search?page=" + numberPage + "&q=" +
                newString + "&search_type=books";

        //get html file
        Document doc = Jsoup.connect(url).get();

        ArrayList<ModelCollectView> listComics = new ArrayList<>();

        //if search page is not empty, get all links comics pages
        if (!Objects.equals(doc.getElementsByClass("searchSubNavContainer").text(), "No results.")){
            Elements links = doc.select("a.bookTitle");

            links.parallelStream().forEach(link -> {
                String linkHref = "https://www.goodreads.com" + link.attr("href");
                //Log.d("href", linkHref);
                ModelCollectView info = null;
                try {
                    info = parsingComicsInfoFromLinkPage(linkHref);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(info != null){
                    listComics.add(info);
                }
            });
        }

        return listComics;

    }

    public ModelCollectView parsingComicsInfoFromLinkPage(String linkPage) throws IOException{
        //get html file
        Document doc = Jsoup.connect(linkPage).get();

        //search html line, where name and image comic book
        Element htmlWithNameAndImage = doc.select("img[id='coverImage']").first();

        if(htmlWithNameAndImage == null){// DELETE WHEN SITE UPDATE UR VERSION
            return null;
        }

        //get comic book name
        String nameIncBook = htmlWithNameAndImage.attr("alt");
        //Log.d("NAMEBOOK", nameIncBook);

        //GOODREADS.COM NOW HAVE PROBLEM WITH IMAGE. NEED LOAD IMAGE OTHER SITE
        //"www.comixology.com"
        //comixology not worked too
        String urlImage = searchImageByNameInComixology(nameIncBook);

        Bitmap image;
        ImageSaveAndLoad setUrl = new ImageSaveAndLoad(context);
        if(!urlImage.isEmpty()){
            image = setUrl.getBitmapFromURL(urlImage);
        }
        else{
            //set default image
            image = setUrl.getBitmapFromURL("https://images.assetsdelivery.com/compings_v2/yehorlisnyi/yehorlisnyi2104/yehorlisnyi210400016.jpg");
        }

        //get authors

        Elements htmlComicAuthors = doc.select("a.authorName");

        StringConverter convert = new StringConverter();
        String strAuthors = "";
        for(Element comicAuthor : htmlComicAuthors){
            String authorName = comicAuthor.text();
            //Log.d("AUTHOR", authorName);
            strAuthors += authorName;
            if(htmlComicAuthors.indexOf(comicAuthor) != htmlComicAuthors.size() - 1){
                strAuthors += "; ";
            }
        }

        //search description
        Element htmlDescription = doc.select("div[id='description'] span[id~=(^freeText)[0-9]+]").first();

        String description = "";
        if(htmlDescription != null){
            description = htmlDescription.text();
            //Log.d("DESCRIPTION", description);
        }

        //get collecting
        //Search Collecting numbers
        Elements htmlCollects = doc.select("div.infoBoxRowItem a[href~=^(/series/)]");

        String strCollects = "";
        if(htmlCollects != null){
            for(Element htmlCollect : htmlCollects){
                //Log.d("COLLECT", htmlCollect.text());
                String collectName = htmlCollect.text();

                strCollects += collectName;
                if(htmlCollects.indexOf(htmlCollect) != htmlCollects.size() - 1){
                    strCollects += "; ";
                }
            }
        }

        //search date
        Element htmlPublishedDate = doc.select("div[id='details'] div.row:not(:has(span))").first();

        String date = "";
        if(htmlPublishedDate != null){
            //Log.d("DATE", htmlPublishedDate.text());
            date = htmlPublishedDate.text();//have format "Published Month(word) number(th) year ..."

            String[] splitDate = date.split(" ", 5);
            date = splitDate[1]+ " " + splitDate[2] + " " + splitDate[3];
            //Log.d("STRDATE", date);

        }

        ModelCollectView incComicInfo = new ModelCollectView(image, nameIncBook, description, strAuthors,
                strCollects, date);

        return incComicInfo;
    }

    public String searchImageByNameInComixology(String name) throws IOException{
        String urlImage = "";

        //replace all space in string line to '+'
        String newString = name.replace(' ', '+');

        //get url for parsing
        String url = "https://www.comixology.com/search?search=" + newString;

        //get html file
        Document doc = Jsoup.connect(url).get();

        //search image
        Element htmlImage = doc.select("img.content-img").first();

        if(htmlImage != null){
            urlImage = htmlImage.attr("src");
        }

        return urlImage;
    }
}
