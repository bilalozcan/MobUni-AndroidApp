package com.fmbg.moobuni;

import android.content.Context;

import com.fmbg.moobuni.Model.Department;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class XMLParserDepartment {

    public Context mContext;
    public ArrayList<Department> departmentArrayList;

    public XMLParserDepartment(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<Department> ParsDepartment() {
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = mContext.getAssets().open("university_department.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            departmentArrayList = parseDepartmentXML(parser);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return departmentArrayList;
    }
    private ArrayList<Department> parseDepartmentXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Department> departments = null;
        int eventType = parser.getEventType();
        Department department = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    departments = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if(name.equals("universite_bolum")){
                        department = new Department();
                    }else if(department != null){
                        if(name.equals("bolum_id")){
                            department.setBolum_id(parser.nextText());
                        } else if (name.equals("fakulte_id")){
                            department.setFakulte_id(parser.nextText());
                        } else if (name.equals("universite_id")){
                            department.setUniversite_id(parser.nextText());
                        } else if (name.equals("name")){
                            department.setName(parser.nextText());
                        } else if (name.equals("status")) {
                            department.setStatus(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("universite_bolum") && department != null){
                        departments.add(department);
                    }

            }
            eventType = parser.next();
        }
        return departments;
    }
}
