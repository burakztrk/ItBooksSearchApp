package com.ozturkburak.itbookssearchapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ozturkburak.itbookssearchapp.BookInfo.BookList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener , AdapterView.OnItemClickListener
{
    private EditText m_editText_searchText;
    private RadioGroup m_radioGroup_searchBy;

    private ListView m_listView_books;
    private MyCustomAdapter m_adapter_booksListView;
    private List<String> m_list_searchedBooksID;

    private String m_oldSearchText ;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        this.init();
    }


    private void init()
    {
        this.gatherViews();

        m_oldSearchText = "";

    }


    private void gatherViews()
    {
        m_editText_searchText = (EditText) this.findViewById(R.id.MAINACTIVITY_EDITTEXT_SEARCHTEXT);
        m_radioGroup_searchBy = (RadioGroup) this.findViewById(R.id.MAINACTIVITY_RADIOGROUP_SEARCHBY);
        m_listView_books = (ListView) this.findViewById(R.id.MAINACTIVITY_LISTVIEW_BOOKLIST);

        m_radioGroup_searchBy.setOnCheckedChangeListener(this);
        ((RadioButton) m_radioGroup_searchBy.getChildAt(0)).setChecked(true);


        m_list_searchedBooksID = new ArrayList<>();
        m_adapter_booksListView = new MyCustomAdapter(MainActivity.this, m_list_searchedBooksID);
        m_listView_books.setAdapter(m_adapter_booksListView);
        m_listView_books.setOnItemClickListener(this);

    }


    public void onClickSearchButton(View v)
    {
        String searchStr = m_editText_searchText.getText().toString().trim();

        if(searchStr.isEmpty() || m_oldSearchText.equals(searchStr))
        {
            m_oldSearchText = searchStr;
            return;
        }

        Utils.hideKeyboard(this);
        m_oldSearchText = searchStr;


        //API , SITEDEKI SEARCH ISLEMI GIBI OLMADIGI ICIN BURASI DEAKTIF
        RadioButton rb =  (RadioButton) m_radioGroup_searchBy.findViewById(m_radioGroup_searchBy.getCheckedRadioButtonId());
        String rbFilterStr = rb.getTag().toString();


        //HttpUrlBuilder sınıfı itebooks un apisine uygun yazıldı. Url Encode eklenerek genel bir sınıf haline getirilebilir.

        //http://it-ebooks-api.info/v1/search/php/page/3
        HttpUrlBuilder urlBuilder =
                new HttpUrlBuilder("http://it-ebooks-api.info/v1/")
                        .setAssignmentOperator('/')
                        .setSeparationOperator('/')
                        .setParameter("search" , searchStr);

        new searchBookListTask().execute(urlBuilder.build());

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        BookInfo book = BookList.findBookById(m_list_searchedBooksID.get(position));

        new getBookDetailsTask().execute(book);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        RadioButton rb = (RadioButton) group.findViewById(checkedId);

        switch (rb.getTag().toString())
        {
            case "Title":
                m_editText_searchText.setHint(R.string.MAINACTIVITY_EDITTEXT_SEARCHTEXT_FROMTITLE);
                break;

            case "Author":
                m_editText_searchText.setHint(R.string.MAINACTIVITY_EDITTEXT_SEARCHTEXT_FROMAUTHOR);
                break;

            case "ISBN":
                m_editText_searchText.setHint(R.string.MAINACTIVITY_EDITTEXT_SEARCHTEXT_FROMISBN);
                break;

            default:
                m_editText_searchText.setHint(R.string.MAINACTIVITY_EDITTEXT_SEARCHTEXT_FROMANY);
                break;
        }

    }


    private class searchBookListTask extends AsyncTask<String , String , Nullable>
    {
        @Override
        protected Nullable doInBackground(String... params)
        {
            String result = null;
            try
            {
                result = Utils.getJsonDatafromUrl(params[0]);

                m_list_searchedBooksID.clear();
                publishProgress("NOTIFY_ADAPTER");

                JSONObject jsonData = new JSONObject(result);

                int totalBookNum = jsonData.getInt("Total");
                if(totalBookNum == 0)
                {
                    publishProgress("Kayit Bulunamadi");
                    return null;
                }

                int pageNum = jsonData.getInt("Page");
                publishProgress(String.format("Bulunan Kitap Sayisi:%d" , totalBookNum));

                JSONArray jsonBooks = jsonData.getJSONArray("Books");
                for (int i = 0; i < jsonBooks.length() ; i++)
                {
                    JSONObject jsonBook = jsonBooks.getJSONObject(i);
                    BookInfo book = new BookInfo();

                    //Bu idye sahip bir kitap varsa sonraki kayıda geç
                    String id = jsonBook.getString("ID");
                    if(BookList.containsKey(id))
                    {
                        m_list_searchedBooksID.add(id);
                        publishProgress("NOTIFY_ADAPTER");
                        continue;
                    }

                    String title = jsonBook.getString("Title");
                    String description = jsonBook.getString("Description");
                    String imageUrl = jsonBook.getString("Image");
                    String isbnNum = jsonBook.getString("isbn");

                    book.setId(id);
                    book.setTitle(title);
                    book.setDescripton(description);
                    book.setImageUrl(imageUrl);
                    book.setIsbnNumber(isbnNum);


                    //Subtitle opsiyonel olduğu için
                    if(jsonBook.has("SubTitle"))
                    {
                        String subTitle = jsonBook.getString("SubTitle");
                        book.setSubTitle(subTitle);
                    }

                    BookList.addBookList(book);
                    m_list_searchedBooksID.add(book.getId());
                    publishProgress("NOTIFY_ADAPTER");
                }

            }catch (Exception ex)
            {
                ex.printStackTrace();
                publishProgress(ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            String value = values[0];

            if(value.equals("NOTIFY_ADAPTER"))
                m_adapter_booksListView.notifyDataSetChanged();
            else
               Toast.makeText(MainActivity.this , value , Toast.LENGTH_SHORT).show();

            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(Nullable nullable)
        {
            super.onPostExecute(nullable);
        }
    }


    private class getBookDetailsTask extends AsyncTask< BookInfo , String , BookInfo>
    {
        @Override
        protected BookInfo doInBackground(BookInfo... params)
        {
            BookInfo book = params[0];

            try
            {
                //http://it-ebooks-api.info/v1/book/2279690981
                HttpUrlBuilder urlBuilder =
                        new HttpUrlBuilder("http://it-ebooks-api.info/v1/")
                                .setAssignmentOperator('/')
                                .setSeparationOperator('/')
                                .setParameter("book" , book.getId());

                String JsonStr = Utils.getJsonDatafromUrl(urlBuilder.build());

                JSONObject jsonObject = new JSONObject(JsonStr);


                //Error tagi her JSONda var
                if(jsonObject.has("Error"))
                {
                    int errorStr = jsonObject.getInt("Error");

                    if(errorStr != 0)
                    {
                        publishProgress("Json is invalid : " + errorStr);
                        return null;
                    }

                    book.setTitle(jsonObject.getString("Title"));
                    book.setAuthor(jsonObject.getString("Author"));
                    book.setYear(jsonObject.getInt("Year"));
                    book.setPage(jsonObject.getInt("Page"));
                    book.setDownloadUrl(jsonObject.getString("Download"));
                    book.setDescripton(jsonObject.getString("Description"));
                }else
                {
                    publishProgress("Url is invalid");
                    return null;
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                publishProgress(ex.getMessage());
            }

            return book;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            Toast.makeText(MainActivity.this, values[0] , Toast.LENGTH_SHORT).show();
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(BookInfo book)
        {
            if (book == null)
                return;

            Intent intent  = new Intent(MainActivity.this , BookDetailsActivity.class );

            intent.putExtra("TITLE" , book.getTitle() );
            intent.putExtra("ISBN" , book.getIsbnNumber() );
            intent.putExtra("IMG", book.getImg() );
            intent.putExtra("PAGE" , book.getPage() );
            intent.putExtra("AUTHOR" , book.getAuthor() );
            intent.putExtra("YEAR" , book.getYear() );
            intent.putExtra("DESC" , book.getDescripton() );
            intent.putExtra("DOWNLOAD" , book.getDownloadUrl() );

            startActivity(intent);
            super.onPostExecute(book);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        String[]array = new String[m_list_searchedBooksID.size()];
        m_list_searchedBooksID.toArray(array);
        outState.putStringArray("SEARCHEDBOOKS" , array);
        outState.putString("EDITTEXT" , m_editText_searchText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        String[] strArray = savedInstanceState.getStringArray("SEARCHEDBOOKS");
        m_list_searchedBooksID = Arrays.asList(strArray);

        m_editText_searchText.setText(savedInstanceState.getString("EDITTEXT"));


    }
}


