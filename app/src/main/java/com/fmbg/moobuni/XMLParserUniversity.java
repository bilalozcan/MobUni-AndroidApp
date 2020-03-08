package com.fmbg.moobuni;

import android.content.Context;

import com.fmbg.moobuni.Model.University;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class XMLParserUniversity {


    public Context mContext;
    public ArrayList<University> universityArrayList;

    public XMLParserUniversity(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<University> ParsUniversity(){

        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = mContext.getAssets().open("university.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            universityArrayList = parseUniversityXML(parser);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return universityArrayList;
    }
    private ArrayList<University> parseUniversityXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<University> universities = null;
        int eventType = parser.getEventType();
        University university = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    universities = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if(name.equals("universite")){
                        university = new University();
                    }else if(university != null){
                        if(name.equals("universite_id")){
                            university.setUniversite_id(parser.nextText());
                        } else if (name.equals("name")){
                            university.setName(parser.nextText());
                        } else if (name.equals("status")){
                            university.setStatus(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("universite") && university != null){
                        universities.add(university);
                    }

            }
            eventType = parser.next();
        }
        return universities;
    }
}
