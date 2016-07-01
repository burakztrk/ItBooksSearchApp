#BOOK SEARCH TUTORIAL 

##IT Books Search App
In this tutorial, we'll make an app that searches the [it-ebooks.info](http://it-ebooks.info/) to search books and display cover images.
[it-ebooks-api.info](it-ebooks-api.info) Api's using and search books authors isbn code and download books


[it-ebooks-api.info](it-ebooks-api.info) Api'siyle programlama kitaplarını isime , yazara , isbn koduna göre aramanızı ve indirmenizi sağlar.

Search ekranında arama kriterine göre arama yapılır ve ilk 10 sonuç listelenir.
Listede seçilen uygulamaya ait kitap koduyla yeni bir sorgu yapılır ve bookdetails sayfasında gösterilir.
Bu sayfada Kitap adı , Yazar Adı ve ISBN kodu seçilebilir durumdadır ve bunlarla ilgili de arama oluşturulabilir.
Captcha kodundan dolayı kitabın download işlemi browsera yönlendirilir.
Browser Chrome CustomTabs kullanıldı.

###Örnek Arama Sorgusu Sonucu:
``` http://it-ebooks-api.info/v1/search/aa ```

```json
{
  "Error": "0",
  "Time": 0.0067,
  "Total": "1",
  "Page": 1,
  "Books": [
    {
      "ID": 1311713395,
      "Title": "Asset Accounting Configuration in SAP ERP",
      "SubTitle": "A Step-by-Step Guide",
      "Description": "In this book, noted expert Andrew Okungbowa explains SAP Asset Accounting (FI-AA) in SAP-ERP, including its associated business benefits, and guides you through the considerable complexities of SAP-ERP configuration. Using FI-AA for fixed asset manag ...",
      "Image": "http://s.it-ebooks-api.info/6/asset_accounting_configuration_in_sap_erp.jpg",
      "isbn": "9781484213667"
    }
  ]
}
```

### Örnek Kitap Detayları Sonucu
``` http://it-ebooks-api.info/v1/book/2279690981 ```

```json
{
  "Error": "0",
  "Time": 0.00106,
  "ID": 2279690981,
  "Title": "PHP & MySQL: The Missing Manual",
  "SubTitle": "",
  "Description": "If you can build websites with CSS and JavaScript, this book takes you to the next level-creating dynamic, database-driven websites with PHP and MySQL. Learn how to build a database, manage your content, and interact with users through queries and web forms. With step-by-step tutorials, real-world examples, and jargon-free explanations, youu2019ll soon discover the power of server-side programming.",
  "Author": "Brett McLaughlin",
  "ISBN": "9780596515867",
  "Year": "2011",
  "Page": "498",
  "Publisher": "O'Reilly Media",
  "Image": "http://s.it-ebooks-api.info/3/php__mysql_the_missing_manual.jpg",
  "Download": "http://filepi.com/i/qqkNNW2"
}
```

![](img/out10-300.gif)


##SEARCH EKRANI
Search ekranında arama kriterine göre arama yapılır ve ilk 10 sonuç listelenir.

![](/img/search.png)![](/img/search_isbn.png)

##BOOKDETAILS EKRANI
Listede seçilen uygulamaya ait kitap koduyla yeni bir sorgu yapılır ve bookdetails sayfasında gösterilir.
Bu sayfada Kitap adı , Yazar Adı ve ISBN kodu seçilebilir durumdadır ve bunlarla ilgili de arama oluşturulabilir.

![](/img/book_details00.png)![](/img/book_details01.png)![](/img/book_details02.png)

##DOWNLOAD AUTHOR VE SITE BUTONLARI
Captcha kodundan dolayı kitabın download işlemi browsera yönlendirilir.
Browser olarak Chrome CustomTabs kullanıldı.

![](/img/author_books.png)![](/img/downloadScreen.png)

##HttpUrlBuilder sınıfı
HttpUrlBuilder sınıfı genel bir sınıf olarak yazıldı.Fakat ```it-ebooks.info``` apisine uyumlu hale getirmek için
build metodunda parametreler için yazılacak ```URLEncoder.encode(urlStr , "UTF-8");``` ile tekrar genel bir sınıf haline getirilebilir.

```java
package com.ozturkburak.itbookssearchapp;

import android.webkit.URLUtil;

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


```
