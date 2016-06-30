package com.ozturkburak.itbookssearchapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

public class BookDetailsActivity extends AppCompatActivity
{
    private ImageView m_imageView_img;
    private TextView m_textView_title ,m_textView_author , m_textView_isbn , m_textView_page , m_textView_year , m_textView_desc ;

    private BookInfo m_book;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        this.init();
    }

    private void init()
    {
        Intent intent = getIntent();
        if (intent == null)
            this.finish();

        this.gatherViews();

        String title = intent.getStringExtra("TITLE");
        Bitmap img = intent.getParcelableExtra("IMG");
        String author = intent.getStringExtra("AUTHOR");
        String isbn = intent.getStringExtra("ISBN");
        int page = intent.getIntExtra("PAGE" , 0);
        int year = intent.getIntExtra("YEAR" , 0);
        String desc = intent.getStringExtra("DESC");
        String download = intent.getStringExtra("DOWNLOAD");

        m_book = new BookInfo()
                .setTitle(title)
                .setImg(img)
                .setAuthor(author)
                .setIsbnNumber(isbn)
                .setPage(page)
                .setYear(year)
                .setDescripton(desc)
                .setDownloadUrl(download);

        m_textView_title.setText(m_book.getTitle());
        m_textView_author.setText(m_book.getAuthor());
        m_textView_isbn.setText(m_book.getIsbnNumber());
        m_textView_page.setText(String.valueOf(m_book.getPage()));
        m_textView_year.setText(String.valueOf(m_book.getYear()));
        m_imageView_img.setImageBitmap(m_book.getImg());
        m_textView_desc.setText(m_book.getDescripton());
    }


    private void gatherViews()
    {
        m_textView_title    = (TextView) this.findViewById(R.id.BOOKDETAILSACTIVITY_TEXTVIEW_TITLE);
        m_textView_author   = (TextView) this.findViewById(R.id.BOOKDETAILSACTIVITY_TEXTVIEW_AUTHOR);
        m_textView_desc     = (TextView) this.findViewById(R.id.BOOKDETAILSACTIVITY_TEXTVIEW_DESC);
        m_textView_isbn     = (TextView) this.findViewById(R.id.BOOKDETAILSACTIVITY_TEXTVIEW_ISBN);
        m_textView_page     = (TextView) this.findViewById(R.id.BOOKDETAILSACTIVITY_TEXTVIEW_PAGE);
        m_textView_year     = (TextView) this.findViewById(R.id.BOOKDETAILSACTIVITY_TEXTVIEW_YEAR);
        m_imageView_img     = (ImageView)this.findViewById(R.id.BOOKDETAILSACTIVITY_IMAGEVIEW_IMG);
    }


    public void onClickDownloadButton(View v)
    {
        Toast.makeText(BookDetailsActivity.this, "chrome", Toast.LENGTH_SHORT).show();
        Utils.openChromeCustomTab(this , m_book.getDownloadUrl());
    }


    public void onClickGoSiteButton(View v)
    {
        //http://www.it-ebooks.info/search/?q=9780987153081&type=isbn
        String url = String.format("http://www.it-ebooks.info/search/?q=%s&type=isbn" , m_book.getIsbnNumber());
        Utils.openChromeCustomTab(this , url);
    }


    public void onClickAuthorBooksButton(View v)
    {
        try
        {
          //http://www.it-ebooks.info/search/?q=Kevin+Yank&type=author
            String url = String.format("http://www.it-ebooks.info/search/?q=%s&type=isbn", URLEncoder.encode(m_book.getAuthor(), "UTF-8"));
            Utils.openChromeCustomTab(this , url);
        }
        catch (Exception ex)
        {
            Log.d("Exception" , ex.getMessage());
            ex.printStackTrace();
        }
    }
}
