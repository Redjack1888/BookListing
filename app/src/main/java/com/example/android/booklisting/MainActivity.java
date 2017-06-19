package com.example.android.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final String BOOK_LIST_VALUES = "bookListValues";

    /**
     * List that stores the books
     */
    private BookAdapter mBookAdapter;

    /**
     * List that stores the books
     */
    private ListView mListView;

    /**
     * Create the Array where books will be stored
     */
    ArrayList<Book> books = new ArrayList<>();

    /**
     * The keyword entered for book search
     */
    private String mKeyword = "";

    private ProgressBar progressBar;

    private View emptyView;
    private ImageView emptyImage;
    private TextView emptyTitleText;
    private TextView emptySubTitleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create Adapter for book list
        if (savedInstanceState != null) {
            books = savedInstanceState.getParcelableArrayList(BOOK_LIST_VALUES);
        }

        mBookAdapter = new BookAdapter(this, books);

        progressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) findViewById(R.id.books_list_view);
        emptyView = findViewById(R.id.empty_view);
        emptyImage = (ImageView) findViewById(R.id.empty_image);
        emptyTitleText = (TextView) findViewById(R.id.empty_title_text);
        emptySubTitleText = (TextView) findViewById(R.id.empty_subtitle_text);

        mListView.setEmptyView(emptyView); // ListView when empty
        mListView.setAdapter(mBookAdapter); // ListView when filled

    }

    private void searchBooks() {
        progressBar.setVisibility(View.VISIBLE);
        // verify if internet connection is available
        if (isInternetConnectionAvailable()) {
            // If Available fetch books list according User query keywords
            BooksFetchTask bookListTask = new BooksFetchTask(this, this);
            bookListTask.execute(mKeyword);
        } else {
            // Display internet connection error
            // Hide progress bar so error message will be visible
            progressBar.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mListView.setEmptyView(emptyView); // ListView when empty
            emptyImage.setImageResource(R.drawable.no_connection);
            emptyTitleText.setText(R.string.error_no_internet);
            emptySubTitleText.setText(R.string.no_connection_subtext);

            mBookAdapter.clear();
        }

    }

    /**
     * This Method get Connectivity and return Network Info
     */
    private boolean isInternetConnectionAvailable() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void refreshBookList(ArrayList<Book> result) {
        mBookAdapter.clear();
        for (Book book : result) {
            mBookAdapter.add(book);
        }
        mBookAdapter.notifyDataSetChanged();
        progressBar.setVisibility(ProgressBar.GONE);
        // If query search returns no results
        if (mBookAdapter.isEmpty()){

            mListView.setEmptyView(emptyView); // ListView when empty
            emptyImage.setImageResource(R.drawable.empty_query);
            emptyTitleText.setText(R.string.no_query_result);
            emptySubTitleText.setText(R.string.no_query_solution_message);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the book list values
        savedInstanceState.putParcelableArrayList(BOOK_LIST_VALUES, books);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu for SearchView .
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mKeyword = query;
                searchBooks();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String mKeyword) {
                return false;
            }
        });

        return true;
    }
}