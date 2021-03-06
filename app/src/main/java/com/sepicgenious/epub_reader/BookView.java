package com.sepicgenious.epub_reader;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.core.view.MotionEventCompat;

import com.sepicgenious.R;

// Panel specialized in visualizing EPUB pages
public class BookView extends SplitPanel {
    public ViewStateEnum state = ViewStateEnum.books;
    protected String viewedPage;
    protected WebView view;
    protected float swipeOriginX, swipeOriginY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.activity_book_view, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle saved) {
        super.onActivityCreated(saved);
        view = (WebView) getView().findViewById(R.id.Viewport);

        view.getSettings().setJavaScriptEnabled(true);

        // ----- SWIPE PAGE
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (state == ViewStateEnum.books)
                    swipePage(v, event, 0);

                WebView view = (WebView) v;
                return view.onTouchEvent(event);
            }
        });

        // ----- VOLUME BUTTON SCROLL
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        view.pageUp(false);
                    }
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        view.pageDown(false);
                    }
                    return true;
                }
                return false;
            }
        });

        // ----- NOTE & LINK
        view.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Message msg = new Message();
                msg.setTarget(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        String url = msg.getData().getString(
                                getString(R.string.url));
                        if (url != null)
                            navigator.setNote(url, index);
                    }
                });
                view.requestFocusNodeHref(msg);

                return false;
            }
        });

        view.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    navigator.setBookPage(url, index);
                } catch (Exception e) {
                    errorMessage(getString(R.string.error_LoadPage));
                }
                return true;
            }
        });

        loadPage(viewedPage);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        view = (WebView) getView().findViewById(R.id.Viewport);
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            view.pageUp(true);
        }
        return true;
    }

    public void loadPage(String path) {
        viewedPage = path;
        if (created)
            view.loadUrl(path);
    }

    // Change page
    protected void swipePage(View v, MotionEvent event, int book) {
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                swipeOriginX = event.getX();
                swipeOriginY = event.getY();
                break;

            case (MotionEvent.ACTION_UP):
                int quarterWidth = (int) (screenWidth * 0.25);
                float diffX = swipeOriginX - event.getX();
                float diffY = swipeOriginY - event.getY();
                float absDiffX = Math.abs(diffX);
                float absDiffY = Math.abs(diffY);

                if ((diffX > quarterWidth) && (absDiffX > absDiffY)) {
                    try {
                        navigator.goToNextChapter(index);
                    } catch (Exception e) {
                        errorMessage(getString(R.string.error_cannotTurnPage));
                    }
                } else if ((diffX < -quarterWidth) && (absDiffX > absDiffY)) {
                    try {
                        navigator.goToPrevChapter(index);
                    } catch (Exception e) {
                        errorMessage(getString(R.string.error_cannotTurnPage));
                    }
                }
                break;
        }

    }

    @Override
    public void saveState(Editor editor) {
        super.saveState(editor);
        editor.putString("state" + index, state.name());
        editor.putString("page" + index, viewedPage);
    }

    @Override
    public void loadState(SharedPreferences preferences) {
        super.loadState(preferences);
        loadPage(preferences.getString("page" + index, ""));
        state = ViewStateEnum.valueOf(preferences.getString("state" + index, ViewStateEnum.books.name()));
    }

}
