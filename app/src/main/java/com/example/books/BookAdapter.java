package com.example.books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder>
{
    // ViewHolder inner class
    public class BookHolder extends RecyclerView.ViewHolder
    {
        // fields
        TextView title_TV;
        TextView price_TV;
        TextView currentNumber_TV;
        TextView author_TV;
        TextView pubDate_TV;
        TextView category_TV;
        ImageView bookCover_IV;

        // constructor
        public BookHolder(@NonNull View itemView)
        {
            super(itemView);
            title_TV = itemView.findViewById(R.id.textView_bookTitle);
            price_TV = itemView.findViewById(R.id.textView_bookPrice);
            currentNumber_TV = itemView.findViewById(R.id.textView_currentNumber);
            author_TV = itemView.findViewById(R.id.textView_bookAuthor);
            pubDate_TV = itemView.findViewById(R.id.textView_bookPubDate);
            category_TV = itemView.findViewById(R.id.textView_bookCategory);
            bookCover_IV = itemView.findViewById(R.id.imageView_BookCover);
        }
    }

    // member variables
    private List<Book> bookList;

    // constructor for BookAdapter
    public BookAdapter(List<Book> bookList)
    {
        this.bookList = bookList;
    }

    // mandatory methods to implement
    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View bookView = inflater.inflate(R.layout.book_item_layout, parent, false);

        return new BookHolder(bookView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position)
    {
        Book aBook = bookList.get(position);

        holder.title_TV.setText(aBook.getTitle());
        TextView price = holder.price_TV;

        if (aBook.getRetailPrice() == -1.0)
        {
            price.setText("NA");
        }
        else
        {
            price.setText(String.valueOf(aBook.getRetailPrice() + "\nRON"));
        }

        holder.currentNumber_TV.setText(String.valueOf(position + 1));
        holder.author_TV.setText(String.valueOf("by " + aBook.getAuthor()));
        holder.pubDate_TV.setText(aBook.getPubDate());
        holder.category_TV.setText(aBook.getCategory());

        ImageView bookCover = holder.bookCover_IV;

        String tempImageURL;

        if (!aBook.getImageURL().equals("NA"))
        {
            tempImageURL = aBook.getImageURL().replace("http:", "https:");

            Picasso
                    .get()
                    .load(tempImageURL)
                    .into(bookCover);
        }
    }

    @Override
    public int getItemCount()
    {
        return bookList.size();
    }
}
