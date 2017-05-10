
# BOOK SEARCH TUTORIAL 

## IT Books Search App
In this tutorial, we'll make an app that searches the [it-ebooks.info](http://it-ebooks.info/) to search books and display cover images.

[it-ebooks-api.info](it-ebooks-api.info) Api's using and search books authors isbn code and download books


[it-ebooks-api.info](it-ebooks-api.info) Api'siyle programlama kitaplarını isime , yazara , isbn koduna göre aramanızı ve indirmenizi sağlar.
Search ekranında arama kriterine göre arama yapılır ve ilk 10 sonuç listelenir.
Listede seçilen uygulamaya ait kitap koduyla yeni bir sorgu yapılır ve bookdetails sayfasında gösterilir.
Bu sayfada Kitap adı , Yazar Adı ve ISBN kodu seçilebilir durumdadır ve bunlarla ilgili de arama oluşturulabilir.
Captcha kodundan dolayı kitabın download işlemi browsera yönlendirilir.
Browser Chrome CustomTabs kullanıldı.

Arama işleminde çekilen tüm kitap bilgileri geçici olarak tutulur.Eğer kayıt edilen kitap tekrar aranırsa bu bilgiler kullanılır.Bu bilgiler localde veritabanına da kayıt edilebilir.

![](img/out10-300.gif)


### Örnek Arama Sorgusu Sonucu:
``` http://it-ebooks-api.info/v1/search/aa ```

```JSON
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

```JSON
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


## SEARCH EKRANI
Search ekranında arama kriterine göre arama yapılır ve ilk 10 sonuç listelenir.

![](/img/search.png)![](/img/search_isbn.png)

## BOOKDETAILS EKRANI
Listede seçilen uygulamaya ait kitap koduyla yeni bir sorgu yapılır ve bookdetails sayfasında gösterilir.
Bu sayfada Kitap adı , Yazar Adı ve ISBN kodu seçilebilir durumdadır ve bunlarla ilgili de arama oluşturulabilir.

![](/img/book_details00.png)![](/img/book_details01.png)![](/img/book_details02.png)

## DOWNLOAD AUTHOR VE SITE BUTONLARI
Captcha kodundan dolayı kitabın download işlemi browsera yönlendirilir.
Browser olarak Chrome CustomTabs kullanıldı.

![](/img/author_books.png)![](/img/downloadScreen.png)

## HttpUrlBuilder sınıfı
HttpUrlBuilder sınıfı genel bir sınıf olarak yazıldı.Fakat ```it-ebooks.info``` apisine uyumlu hale getirmek için genel kalıptan çıkarıldı.
Build metodunda parametreler için yazılacak ```URLEncoder.encode(paramsStr , "UTF-8");``` ile tekrar genel bir sınıf haline getirilebilir.
Fluent tasarım kalıbıyla yazıldı.
```java
String url = (new HttpUrlBuilder("http://...")
 .setAssignmentOperator('=')
 .setSeparationOperator('/')
 .setParameter(new String[]{"word" , "door"}))
 .build();
```

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

### UTILS SINIFI
Utils sınıfında çoğu projede işe yarayacak metodlar mevcut ve kullanılabilir durumda.

* ``` getJsonDatafromUrl ``` Metodu Api üzerinden Json datasını String olarak döndürür.
* ``` openHttpConnection ``` Metodu HttpURLConnection işlemlerini içeride yapar ve HttpURLConnection döndürür.
* ``` readInputStream ``` Streami chunk chunk okuyarak String döndürür.
* ``` downloadImagefromUrl ``` Verilen url`den Bitmap döndürür. AsyncTask ile kullanıldığı için ayrı Thread yaratılmadı.
* ``` openWebBrowser ``` Verilen url`i browserda açmak için Intent'i geri döndürür.
* ``` openChromeCustomTab ``` Verilen url`i Chrome CustomTab ile açar.Chrome yüklü olmaması durumunda Default Browser kullanılır.
* ``` hideKeyboard ``` Search işleminden sonra listeleme işlemi için Soft Keyboard gizlendi.

```java

/**
 * Created by burak on 28.06.2016.
 */
public class Utils
{
    public static String getJsonDatafromUrl(String urlStr) throws Exception
    {
        String result;
        HttpURLConnection connection = null;
        try
        {
            connection = openHttpConnection(urlStr);

            result = readInputSream(connection.getInputStream());
            connection.disconnect();

        }
        catch (Exception ex)
        {
            Log.d("Exception" , ex.getMessage());
            throw ex;
        }finally
        {
            connection.disconnect();
        }

        return result;
    }


    public static HttpURLConnection openHttpConnection(String urlStr) throws IOException
    {
        if (!HttpUrlBuilder.urlIsValid(urlStr))
            throw  new IllegalArgumentException("Url is invalid");

        URL url = null;
        HttpURLConnection conn = null;

        try
        {
            url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
        }
        catch (Exception ex)
        {
            Log.d("Exception" , ex.getMessage());
            throw ex;
        }

        return conn;
    }


    private static String readInputStream(InputStream is)throws Exception
    {
        StringBuilder result = new StringBuilder();
        try
        {
            InputStreamReader isr = new InputStreamReader(is);

            char[] buffer = new char[1024];
            int readCount;

            while ( (readCount = isr.read(buffer)) >0 )
            {
                result.append(buffer , 0 , readCount);
                buffer = new char[1024];
            }

        }
        catch (Exception ex)
        {
            Log.d("Exception" , ex.getMessage());
            throw ex;
        }
        finally
        {
            try
            {is.close();}
            catch (IOException e){   e.printStackTrace();}
        }

        return result.toString();
    }


    public static Bitmap saveImagefromUrl(Context context , String urlStr , String imageName) throws IOException
    {
        Bitmap result = null;
        HttpURLConnection conn = openHttpConnection(urlStr);

        InputStream bis = new BufferedInputStream(conn.getInputStream());
        result = BitmapFactory.decodeStream(bis);

        OutputStream os = context.openFileOutput( imageName , Context.BIND_AUTO_CREATE);
        result.compress(Bitmap.CompressFormat.JPEG , 100 , os);


        return  result;
    }


    public static Bitmap downloadImagefromUrl(String urlStr) throws IOException
    {
        Bitmap result = null;
        HttpURLConnection conn = openHttpConnection(urlStr);

        InputStream bis = new BufferedInputStream(conn.getInputStream());
        result = BitmapFactory.decodeStream(bis);

        return  result;
    }


    public static Intent openWebBrowser(String url)
    {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        return new Intent(Intent.ACTION_VIEW , Uri.parse(url));
    }

    public static void openChromeCustomTab(Activity activity , String url)
    {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        CustomTabsIntent customTabsIntent = builder
                .setToolbarColor(Color.parseColor("#303f9f"))
                .setShowTitle(true)
                .build();

        customTabsIntent.launchUrl(activity , Uri.parse(url));

    }


    public static void hideKeyboard(Activity activity)
    {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    
} 
```

