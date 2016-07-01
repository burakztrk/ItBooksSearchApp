package com.ozturkburak.itbookssearchapp;

import android.webkit.URLUtil;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by burak on 27.06.2016.
 *
 *  //http://services.aonaware.com/dictservice/dictservice.asmx/Define?word=door
 *
 * HttpUrlBuilder httpConnectHelper = new HttpUrlBuilder("http://services.aonaware.com/dictservice/dictservice.asmx/Define?")
 * .setAssignmentOperator('=')
 * .setSeparationOperator('/')
 * .setParameter(new String[]{"word" , "door"});
 */


public class HttpUrlBuilder
{
    private String m_resourceUrl ;
    private List <String[]> m_paramList;
    private char m_separationOperator , m_assignmentOperator;


    public HttpUrlBuilder(String resourceUrl)
    {
        m_resourceUrl=resourceUrl;
        m_paramList = new ArrayList<>();
        m_separationOperator = '&';
        m_assignmentOperator = '=';

    }


    public HttpUrlBuilder()
    {
        m_resourceUrl="";
        m_paramList = new ArrayList<>();
        m_separationOperator = '&';
        m_assignmentOperator = '=';
    }

    public static boolean urlIsValid(String urlStr)
    {

        if(urlStr == null || urlStr.isEmpty() || urlStr.startsWith("https://"))
            return false;


        if(!urlStr.startsWith("http://"))
            urlStr = "http://" + urlStr;


        if (!URLUtil.isHttpUrl(urlStr))
            return false;

        return true;
    }

    public String getResourceUrl()
    {
        return m_resourceUrl;
    }


    public HttpUrlBuilder setResourceUrl(String m_resourceUrl)
    {
        this.m_resourceUrl = m_resourceUrl;
        return this;
    }


    public List<String[]> getParameters()
    {
        List<String[]> result = new ArrayList<>(m_paramList);
        return result;
    }

    public HttpUrlBuilder setParameter(List<String[]> list)
    {
        if(list ==null)
            throw new IllegalArgumentException("list must not be null");

        String[] array = new String[list.size()];
        setParameters(array);

        return this;
    }

    public HttpUrlBuilder setParameter(String arg0 , String args1)
    {
        if(arg0.isEmpty() || args1.isEmpty())
                throw new IllegalArgumentException("params must not be null");

        m_paramList.add(new String[]{arg0 , args1});

        return this;
    }


    public HttpUrlBuilder setParameters(String[] ...params)
    {
        if(params == null)
            throw new IllegalArgumentException("params must not be null");

        for (int i = 0; i < params.length ; i++)
            setParameter(params[i][0] , params[i][1]);

        return this;
    }


    public char getSeparationOperator()  { return m_separationOperator; }


    public HttpUrlBuilder setSeparationOperator(char separationOperator)
    {
        this.m_separationOperator = separationOperator;
        return this;
    }


    public char getAssignmentOperator()
    {
        return m_assignmentOperator;
    }


    public HttpUrlBuilder setAssignmentOperator(char assignmentOperator)
    {
        m_assignmentOperator = assignmentOperator;
        return this;
    }



    public String build() throws IllegalArgumentException
    {

        if (!urlIsValid(m_resourceUrl))
            throw new IllegalArgumentException("Url is invalid");

        StringBuilder result = new StringBuilder(m_resourceUrl);

        int listSize = m_paramList.size();
        for (int i = 0; i < listSize ; i++)
        {
            result.append(String.format("%s%s%s",m_paramList.get(i)[0]
                    , m_assignmentOperator
                    , m_paramList.get(i)[1].replace(" " ,"%20")  ));

            if(i < listSize-1)
                result.append(m_separationOperator);
        }

        return result.toString();
    }


    @Override
    public String toString()
    {
        return this.build();
    }
}
