package com.mwiacek.istqb.glossary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;

public class SearchProvider extends ContentProvider {
	    
	public static final String AUTHORITY = "com.mwiacek.istqb.glossary.SearchProvider";
	
	private static final String[] COLUMN_NAMES = new String[] { 
		BaseColumns._ID, 
		SearchManager.SUGGEST_COLUMN_TEXT_1,
		SearchManager.SUGGEST_COLUMN_TEXT_2, 
		SearchManager.SUGGEST_COLUMN_INTENT_DATA,
		SearchManager.SUGGEST_COLUMN_SHORTCUT_ID};

	private static final int SEARCH_SUGGEST = 1;
    private static final int SHORTCUT_REFRESH = 0;

	private static UriMatcher uriMatcher = buildUriMatcher();
	
    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher =  new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT, SHORTCUT_REFRESH);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", SHORTCUT_REFRESH);
        return matcher;
    }
	

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
				
		MatrixCursor cursor = new MatrixCursor(COLUMN_NAMES);
		
        switch (uriMatcher.match(uri)) {
        case SEARCH_SUGGEST:
        case SHORTCUT_REFRESH:
        	break;
            //String shortcutId = null;
            //if (uri.getPathSegments().size() > 1) {
                //shortcutId = uri.getLastPathSegment();
            //}
            //return refreshShortcut(shortcutId, projection);
        default:
            throw new IllegalArgumentException("Unknown URL " + uri);
        }
            
        if (uri.getPath().toString().equals("/search_suggest_query/")) {
        	return cursor;
        }
        
		String searchString = uri.getLastPathSegment().toLowerCase();
		if (searchString == null || searchString.length()==0) return cursor;
		
		String tosearch = "(?![^<]+>)((?i:\\Q"+uri.getLastPathSegment().toString().replace("\\E", "\\E\\\\E\\Q").replace("a", "\\E[aπ•]\\Q").replace("c", "\\E[cÊ∆]\\Q").replace("e", "\\E[eÍ ]\\Q").replace("l", "\\E[l≥£]\\Q").replace("n", "\\E[nÒ—]\\Q").replace("o", "\\E[oÛ”]\\Q").replace("s", "\\E[súå]\\Q").replace("z", "\\E[züøØè]\\Q")+"\\E))";    		
		SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this.getContext());		
		InputStream stream;	
		String s;
		BufferedReader buffreader;
		
		try {
			stream = getContext().getAssets().open(sp.getString("up", "24us.txt"));		
			buffreader = new BufferedReader(new InputStreamReader(stream));
			while ((s=buffreader.readLine()) != null) {
				if (!s.replaceFirst(tosearch,"<mark>").contains("<mark>")) continue;
		    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
					Object[] rowObject = new Object[] { 
							String.valueOf(3),
							"Has≥a", 
							"Znaleziono co najmniej raz " + uri.getLastPathSegment(),						
							uri.getLastPathSegment(),
							SearchManager.SUGGEST_NEVER_MAKE_SHORTCUT};
					cursor.addRow(rowObject);
					break;
				} else {
					Object[] rowObject = new Object[] { 
							String.valueOf(3),
							"Topics", 
							"Found at least once " + uri.getLastPathSegment(),						
							uri.getLastPathSegment(),
							SearchManager.SUGGEST_NEVER_MAKE_SHORTCUT};
					cursor.addRow(rowObject);
					break;
		    	}
			}
			stream.close();
		} catch (IOException e) {
		}			

		if (cursor.getCount()==0) {
			try {
				stream = getContext().getAssets().open(sp.getString("down", "none.txt"));		
				buffreader = new BufferedReader(new InputStreamReader(stream));
				while ((s=buffreader.readLine()) != null) {
					if (!s.replaceFirst(tosearch,"<mark>").contains("<mark>")) continue;              													
			    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
						Object[] rowObject = new Object[] { 
								String.valueOf(3),
								"Has≥a", 
								"Znaleziono co najmniej raz " + uri.getLastPathSegment(),						
								uri.getLastPathSegment(),
								SearchManager.SUGGEST_NEVER_MAKE_SHORTCUT};
						cursor.addRow(rowObject);
						break;
					} else {
						Object[] rowObject = new Object[] { 
								String.valueOf(3),
								"Topics", 
								"Found at least once " + uri.getLastPathSegment(),						
								uri.getLastPathSegment(),
								SearchManager.SUGGEST_NEVER_MAKE_SHORTCUT};
						cursor.addRow(rowObject);
						break;
			    	}
				}
				stream.close();
			} catch (IOException e) {
			}						
		}
		
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
        case SEARCH_SUGGEST:
            return SearchManager.SUGGEST_MIME_TYPE;
        case SHORTCUT_REFRESH:
            return SearchManager.SHORTCUT_MIME_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}
}
