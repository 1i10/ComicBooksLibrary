package com.app.comicslibrary;

import com.app.comicslibrary.Model.Author;
import com.app.comicslibrary.Model.CollectingComicNumbers;

import java.util.List;

public class StringConverter {

    public String[] getFirstAndLastName(String fullName){
        String[] name = new String[2];

        //if first space character
        fullName = fullName.startsWith(" ") ? fullName.substring(1) : fullName;

        String[] splitFullName = fullName.split(" ", 2);

        //Log.d("checkName", name[0] + "");
        if(splitFullName.length != 0){
            name[0] = splitFullName[0];
            if(splitFullName.length > 1){
                name[1] = splitFullName[1];
            }
        }

        return name;
    }

    public String convertListOfAuthorsToString(List<Author> authors){
        String strAuthors = "";
        for(Author author : authors){
            String firstName = author.getFirstName();
            if(firstName == null || firstName.length() == 0){
                continue;
            }
            String lastName = author.getLastName();
            if(lastName == null){
                lastName = "";
            }
            strAuthors += firstName + " " + lastName;
            if(authors.indexOf(author) != authors.size() - 1){
                strAuthors += "; ";
            }
        }

        return strAuthors;
    }

    public String convertListOfCollectsToString(List<CollectingComicNumbers> collects){
        String strCollects = "";
        for(CollectingComicNumbers collect : collects){
            String collectName = collect.getNameCollectBook();
            if(collectName == null || collectName.length() == 0){
                continue;
            }
            strCollects += collectName;
            if(collects.indexOf(collect) != collects.size() - 1){
                strCollects += "; ";
            }
        }

        return strCollects;
    }
}
