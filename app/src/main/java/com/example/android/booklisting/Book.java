package com.example.android.booklisting;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {

    /**
     * The author of the book
     */
    private String mAuthors;

    /**
     * The book title
     */
    private String mTitle;

    /**
     * Create a new Book object.
     *
     * @param authors is the author of the book
     * @param title   is the title of the book
     */
    public Book(String authors, String title) {
        mAuthors = authors;
        mTitle = title;
    }

    private Book(Parcel in) {
        mAuthors = in.readString();
        mTitle = in.readString();
    }


    /**
     * Get the author of the book.
     */
    public String getAuthors() {
        return mAuthors;
    }

    /**
     * Get the title of the bool.
     */
    public String getTitle() {
        return mTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthors);
        dest.writeString(mTitle);
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
