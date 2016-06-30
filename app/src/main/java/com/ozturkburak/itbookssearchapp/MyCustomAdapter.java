package com.ozturkburak.itbookssearchapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ozturkburak.itbookssearchapp.BookInfo.BookList;

import java.io.IOException;
import java.util.List;


/**
 * Created by burak on 28.06.2016.
 */

public class MyCustomAdapter extends BaseAdapter
{
    private TextView m_rowView_title  , m_rowView_desc ;
    private ImageView m_rowView_img;

    private LayoutInflater m_inflater ;
    private List<String> m_bookList;

    public MyCustomAdapter(Activity activity , List<String> list)
    {
        if(list == null)
            throw new IllegalArgumentException("CustomArrayAdapter list could not be null");

        m_bookList = list;
        m_inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount()
    {
        return m_bookList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return m_bookList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowView;
        rowView = m_inflater.inflate(R.layout.cutomlistview_main , null);
        m_rowView_title  = (TextView) rowView.findViewById(R.id.CUSTOMLISTVIEW_TEXTVIEW_TITLE);
        m_rowView_desc  = (TextView) rowView.findViewById(R.id.CUSTOMLISTVIEW_TEXTVIEW_DESC);
        m_rowView_img = (ImageView) rowView.findViewById(R.id.CUSTOMLISTVIEW_IMAGEVIEW_IMG);

        try
        {
            BookInfo book = BookList.findBookById(m_bookList.get(position));

            m_rowView_title.setText(book.getTitle());
            m_rowView_desc.setText(book.getDescripton());

            if(book.getImg() == null)
                new ImageDownloaderTask().execute(book , m_rowView_img);
            else
                m_rowView_img.setImageBitmap(book.getImg());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d("Exception" , ex.getMessage() );
        }
        return rowView;
    }


    private class ImageDownloaderTask extends AsyncTask<Object , String , Object[]>
    {
        @Override
        protected Object[] doInBackground(Object... objs)
        {

            BookInfo book = null;
            try
            {
                book = (BookInfo)objs[0];

                Bitmap img = Utils.downloadImagefromUrl(book.getImageUrl());
                book.setImg(img);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return new Object[] {book , objs[1]} ;
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object[] objs)
        {
            ((ImageView)objs[1]).setImageBitmap(((BookInfo)objs[0]).getImg());
            super.onPostExecute(objs);
        }
    }
}
