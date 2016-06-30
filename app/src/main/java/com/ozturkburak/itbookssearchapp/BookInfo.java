package com.ozturkburak.itbookssearchapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;

/**
 * Created by burak on 28.06.2016.
 * <p>
 * Static bir list olusturulacak ya da veritabani cekilen kitaplar buraya kayit edilecek.
 * adapterin listi ayri tutulacak.
 */
public class BookInfo
{
    private String m_id, m_title, m_subTitle, m_descripton, m_imageUrl, m_isbnNumber;

    private String m_author, m_downloadUrl;
    private int m_year, m_page;
    private Bitmap m_img;

    public BookInfo()
    {
        m_id = "";
        m_title = "";
        m_descripton = "";
        m_imageUrl = "";
        m_isbnNumber = "";
        m_author = "";
        m_year = 0;
        m_page = 0;
        m_img = null;
    }

    public String getAuthor()
    {
        return m_author;
    }

    public String getDescripton()
    {
        return m_descripton;
    }

    public String getDownloadUrl()
    {
        return m_downloadUrl;
    }

    public String getId()
    {
        return m_id;
    }

    public Bitmap getImg()
    {
        return this.m_img;
    }

    public String getImageUrl()
    {
        return m_imageUrl;
    }

    public String getIsbnNumber()
    {
        return m_isbnNumber;
    }

    public int getPage()
    {
        return m_page;
    }

    public String getSubTitle()
    {
        return m_subTitle;
    }

    public String getTitle()
    {
        return m_title;
    }

    public int getYear()
    {
        return m_year;
    }



    public BookInfo setAuthor(String author)
    {
        m_author = author == null ? "" : author;
        return this;
    }

    public BookInfo setDescripton(String descripton)
    {
        m_descripton = descripton == null ? "" : descripton;
        return this;
    }

    public BookInfo setDownloadUrl(String downloadUrl)
    {
        m_downloadUrl = downloadUrl == null ? "" : downloadUrl;
        return this;
    }

    public BookInfo setId(String id)
    {
        m_id = id == null ? "" : id;
        return this;
    }

    public BookInfo setImg(Bitmap img)
    {
        if (img == null)
        {
            m_img = BitmapFactory.decodeByteArray(new byte[]{}, 0, 0);
        }
        else
        {
            m_img = img;
        }

        return this;
    }

    public BookInfo setImageUrl(String imageUrl)
    {

        m_imageUrl = imageUrl == null ? "" : imageUrl;
        return this;
    }

    public BookInfo setIsbnNumber(String isbnNumber)
    {
        m_isbnNumber = isbnNumber == null ? "" : isbnNumber;
        return this;
    }

    public BookInfo setPage(int page)
    {
        m_page = page;
        return this;
    }

    public BookInfo setSubTitle(String subTitle)
    {
        m_subTitle = subTitle == null ? "" : subTitle;
        return this;
    }

    public BookInfo setTitle(String title)
    {
        m_title = title == null ? "" : title;
        return this;
    }

    public BookInfo setYear(int year)
    {
        m_year = year;
        return this;
    }

    @Override
    public String toString()
    {
        return String.format("[%s]  %s", m_id, m_title);
    }




    public static class BookList
    {
        private static HashMap<String, BookInfo> ms_bookList;

        static
        {
            ms_bookList = new HashMap<>();
        }

        public static BookInfo findBookById(String id)
        {
            return ms_bookList.get(id);
        }

        public static boolean containsKey(String id)
        {
            return ms_bookList.containsKey(id);
        }

        public static int count()
        {
            return ms_bookList.size();
        }

        public static boolean addBookList(BookInfo book)
        {
            if (ms_bookList.containsKey(book.getId()))
                return false;

            ms_bookList.put(book.getId(), book);
            return true;
        }
    }
}