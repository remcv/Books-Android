package com.example.books;

import androidx.annotation.NonNull;

class Book
{
    // fields
    private String title;
    private String author;
    private String pubDate;
    private String category;
    private double retailPrice;

    // constructor
    public Book()
    {
        // constructor for RecyclerView
    }

    public Book(String title, String author, String pubDate, String category, double retailPrice)
    {
        this.title = title;
        this.author = author;
        this.pubDate = pubDate;
        this.category = category;
        this.retailPrice = retailPrice;
    }

    // methods
    public String getTitle()
    {
        return title;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getPubDate()
    {
        return pubDate;
    }

    public String getCategory()
    {
        return category;
    }

    public double getRetailPrice()
    {
        return retailPrice;
    }

    @NonNull
    @Override
    public String toString()
    {
        return title + " " + retailPrice;
    }
}
