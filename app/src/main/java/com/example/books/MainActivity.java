package com.example.books;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainActivity extends AppCompatActivity
{
    // member variables - layout
    private TextInputLayout til_searchBox;
    private ImageView imageButton_search;
    private RecyclerView rv_bookList;
    private BookAdapter adapter;
    private static final String TAG = "MainActivity";

    // member variables - database
    private static List<Book> bookList = new CopyOnWriteArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // layout variables
        initializeLayoutElements();

        // RecyclerView
        rv_bookList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv_bookList.addItemDecoration(itemDecoration);

        // buttons
        imageButton_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String searchWord = til_searchBox.getEditText().getText().toString().trim();
                if (!searchWord.equals(""))
                {
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            JSONObject obj = NetFun.urlToJson(makeStringURL(searchWord));
                            bookList = NetFun.jsonToBookList(obj, 40);

                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    rv_bookList.setAdapter(new BookAdapter(bookList));
                                }
                            });
                        }
                    }).start();
                }
            }
        });

    }

    private void initializeLayoutElements()
    {
        til_searchBox = findViewById(R.id.textInputLayout_searchBook);
        imageButton_search = findViewById(R.id.imageButton);
        rv_bookList = findViewById(R.id.recyclerView_bookList);
    }

    private String makeStringURL(String wordsToSearch)
    {
        wordsToSearch = wordsToSearch.trim().replace(' ', '+');
        return String.format("https://www.googleapis.com/books/v1/volumes?q=%s&maxResults=40", wordsToSearch);
    }
}
