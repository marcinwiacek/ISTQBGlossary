package com.mwiacek.istqb.glossary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class PreferencesActivity extends PreferenceActivity {
    final Activity MyActivity5 = this;
    SharedPreferences sp;
    List<String> listItems = new ArrayList<String>();
    List<String> listItems2 = new ArrayList<String>();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        sp=PreferenceManager.getDefaultSharedPreferences(this);

        if (sp.getString("wyglad", "").length()==0) {
            //if (android.os.Build.VERSION.SDK_INT < 11) {
            	setTheme(android.R.style.Theme);
            //} else if (android.os.Build.VERSION.SDK_INT > 13) {
//            	setTheme(android.R.style.Theme_DeviceDefault);            	
            //} else {
//            	setTheme(android.R.style.Theme_Holo);	
            //}
        } else if (sp.getString("wyglad", "").equals("pusty")) {
        	setTheme(android.R.style.Theme_Light);
        } else if (sp.getString("wyglad", "").equals("holo")) {
        	setTheme(android.R.style.Theme_Holo);
        } else if (sp.getString("wyglad", "").equals("holo2")) {
        	setTheme(android.R.style.Theme_Holo_Light);
        } else if (sp.getString("wyglad", "").equals("domyslnyurzadzenie")) {
        	setTheme(android.R.style.Theme_DeviceDefault);
        } else if (sp.getString("wyglad", "").equals("domyslnyurzadzenie2")) {
        	setTheme(android.R.style.Theme_DeviceDefault_Light);
        }
        
        if (!sp.getString("lang", "auto").equals("auto")) {
            Locale myLocale = new Locale(sp.getString("lang", "auto"));
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            //onConfigurationChanged(conf);
        }    

		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.sett2);
	      
    	try {
        	  PackageManager manager = getPackageManager();
        	  PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
        	  
        	  if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
        		  setTitle("S³ownik ISTQB "+info.versionName);
        	  } else {
        		  setTitle("ISTQB glossary "+info.versionName);
        	  }
        } catch (Exception e) {
        	  
        }
    	
    	Bundle extras = getIntent().getExtras();
    	if (extras != null) {
    		Intent intent = new Intent();     
        	intent.putExtra("lang", extras.getString("lang"));
            intent.putExtra("wyglad", extras.getString("wyglad"));
            intent.putExtra("down",extras.getString("down"));
            intent.putExtra("up",extras.getString("up"));
            intent.putExtra("DiffMode",extras.getBoolean("DiffMode", false));            
            setResult(RESULT_OK, intent);
    	}

  	                
    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
            listItems.add("Android 2.x (jasny)");
    	} else {
            listItems.add("Android 2.x (light)");    		
    	}
        listItems2.add("pusty");
        if (android.os.Build.VERSION.SDK_INT > 10) {
        	listItems.add("Holo");
        	listItems2.add("holo");
        	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
            	listItems.add("Holo (jasny)");
        	} else {
            	listItems.add("Holo (light)");        		
        	}
        	listItems2.add("holo2");
        }
        if (android.os.Build.VERSION.SDK_INT > 13) {
        	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
            	listItems.add("producenta");
        	} else {
            	listItems.add("manufacturer");        		
        	}
        	listItems2.add("domyslnyurzadzenie");
        	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
            	listItems.add("producenta (jasny)");
        	} else {
            	listItems.add("manufacturer (light)");        		
        	}
        	listItems2.add("domyslnyurzadzenie2");
        }
        
        ListPreference customPref2 = (ListPreference) findPreference("wyglad");
        customPref2.setEntries(listItems.toArray(new CharSequence[listItems.size()]));
        customPref2.setEntryValues(listItems2.toArray(new CharSequence[listItems2.size()]));
		Iterator<String> it1, it2;
		it1 = listItems.iterator();
		it2 = listItems2.iterator();
		while(it1.hasNext()) {
			if (it2.next().equals(PreferenceManager.getDefaultSharedPreferences(MyActivity5).getString("wyglad", ""))) {
		    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
			        customPref2.setSummary("Wybrany wygl¹d: "+it1.next());		    		
		    	} else {
			        customPref2.setSummary("Selected theme: "+it1.next());		    		
		    	}
		        break;
			} else {
				it1.next();
			}
		}				
		customPref2.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {

				if (!newValue.equals(PreferenceManager.getDefaultSharedPreferences(MyActivity5).getString("wyglad", ""))) {
					MyActivity5.finish();
					MyActivity5.startActivity(new Intent(MyActivity5, MyActivity5.getClass())); 					
				}

    			return true;
    		}
		});

        if (sp.getBoolean("Obrot", false)) {
        	setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);                	            		
        }
    	Preference customPref = (Preference) findPreference("Obrot");
		customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
    		public boolean onPreferenceClick(Preference preference) {
        		if (!preference.getSharedPreferences().getBoolean("Obrot", false)) {
        			setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        		} else {
        			setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);        				  
        		}        			
    			return true;
    		}
		});

        customPref = (Preference) findPreference("up");
    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
    		customPref.setSummary("Wybierz s³ownik pokazywany na górze. Aktualny: "+sp.getString("up", "24us.txt").replace("22us.txt", "2.2").replace("23us.txt", "2.3").replace("24us.txt", "2.4").replace("222plus.txt", "2.2.2 PL"));    		
    	} else {
    		customPref.setSummary("Select glossary displayed on the top. Current: "+sp.getString("up", "24us.txt").replace("22us.txt", "2.2").replace("23us.txt", "2.3").replace("24us.txt", "2.4").replace("222plus.txt", "2.2.2 PL"));    		
    	}
		customPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
    		public boolean onPreferenceChange(Preference preference, Object newValue) {
		    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
    	    		preference.setSummary("Wybierz s³ownik pokazywany na górze. Aktualny: "+newValue.toString().replace("22us.txt", "2.2").replace("23us.txt", "2.3").replace("24us.txt", "2.4").replace("222plus.txt", "2.2.2 PL"));    		
    	    	} else {
    	    		preference.setSummary("Select glossary displayed on the top. Current: "+newValue.toString().replace("22us.txt", "2.2").replace("23us.txt", "2.3").replace("24us.txt", "2.4").replace("222plus.txt", "2.2.2 PL"));    		
    	    	}
    			return true;
    		}
		});

						
		customPref = (Preference) findPreference("down");
    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
    		customPref.setSummary("Wybierz s³ownik pokazywany na dole. Aktualny: "+sp.getString("down", "none.txt").replace("none.txt","¿aden").replace("22us.txt", "2.2").replace("23us.txt", "2.3").replace("24us.txt", "2.4").replace("222plus.txt", "2.2.2 PL"));
    	} else {
    		customPref.setSummary("Select glossary displayed on the bottom. Current: "+sp.getString("down", "none.txt").replace("none.txt","none").replace("22us.txt", "2.2").replace("23us.txt", "2.3").replace("24us.txt", "2.4").replace("222plus.txt", "2.2.2 PL"));    		
    	}
		customPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
    		public boolean onPreferenceChange(Preference preference, Object newValue) {
		    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
    	    		preference.setSummary("Wybierz s³ownik pokazywany na dole. Aktualny: "+newValue.toString().replace("none.txt","¿aden").replace("22us.txt", "2.2").replace("23us.txt", "2.3").replace("24us.txt", "2.4").replace("222plus.txt", "2.2.2 PL"));
    	    	} else {
    	    		preference.setSummary("Select glossary displayed on the bottom. Current: "+newValue.toString().replace("none.txt","none").replace("22us.txt", "2.2").replace("23us.txt", "2.3").replace("24us.txt", "2.4").replace("222plus.txt", "2.2.2 PL"));    		
    	    	}
				Preference customPref = (Preference) findPreference("DiffMode");
		        customPref.setEnabled(!newValue.toString().equals("none.txt"));		        
				customPref = (Preference) findPreference("SyncMode");
		        customPref.setEnabled(!newValue.toString().equals("none.txt"));		        
    			return true;
    		}
		});
	    
		customPref = (Preference) findPreference("lang");
    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
    		customPref.setSummary("Wybierz jêzyk. Aktualny: "+sp.getString("lang", "auto").replace("auto","automatycznie").replace("pl_PL", "polski").replace("en_US", "angielski"));    		
    	} else {
    		customPref.setSummary("Select language. Current: "+sp.getString("lang", "auto").replace("auto","automatic").replace("pl_PL", "polski").replace("en_US", "English"));    		
    	}
		customPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
    		public boolean onPreferenceChange(Preference preference, Object newValue) {
				MyActivity5.finish();
				MyActivity5.startActivity(new Intent(MyActivity5, MyActivity5.getClass())); 					
    			return true;
    		}
		});
		
        customPref = (Preference) findPreference("Czyszczenie");
        customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        		public boolean onPreferenceClick(Preference preference) {
        			Intent intent = new Intent(Intent.ACTION_VIEW);
        			intent.setData(Uri.parse("http://mwiacek.com/www/?q=node/98"));
        			MyActivity5.startActivity(intent);

        			return true;
        		}

        });       

		customPref = (Preference) findPreference("SyncMode");
		customPref.setEnabled(!sp.getString("down","none.txt").equals("none.txt"));		        
        
		customPref = (Preference) findPreference("DiffMode");
		customPref.setEnabled(!sp.getString("down","none.txt").equals("none.txt"));		        
		customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
    		public boolean onPreferenceClick(Preference preference) {
    			return true;
    		}
		});

		customPref = (Preference) findPreference("OwnTextSize");		
		customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
    		public boolean onPreferenceClick(Preference preference) {
    			Preference customPref = (Preference) findPreference("TextSize");
    	        customPref.setEnabled(sp.getBoolean("OwnTextSize", false));
    			
    			return true;
    		}
		});
		customPref = (Preference) findPreference("TextSize");
    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
    		customPref.setSummary("Wybierz w³asn¹ wielkoœæ tekstu. Aktualna: "+sp.getString("TextSize", "22"));    		
    	} else {
    		customPref.setSummary("Select user text size. Current: "+sp.getString("TextSize", "22"));    		    		
    	}
        customPref.setEnabled(sp.getBoolean("OwnTextSize", false));
        customPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
    		public boolean onPreferenceChange(Preference preference, Object newValue) {
		    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
    	    		preference.setSummary("Wybierz w³asn¹ wielkoœæ tekstu. Aktualna: "+newValue.toString());    		
    	    	} else {
    	    		preference.setSummary("Select user text size. Current: "+newValue.toString());    		    		
    	    	}
    			return true;
    		}
		});
        
        customPref = (Preference) findPreference("Czyszczenie2");
        customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        		public boolean onPreferenceClick(Preference preference) {
        			Intent emailIntent=new Intent(Intent.ACTION_SEND);
        		    String subject = "";
        		    try {
        		    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
            		        subject="S³ownik ISTQB "+getPackageManager().getPackageInfo(getPackageName(), 0).versionName+" / Android "+Build.VERSION.RELEASE;        		    		
        		    	} else {
            		        subject="ISTQB glossary "+getPackageManager().getPackageInfo(getPackageName(), 0).versionName+" / Android "+Build.VERSION.RELEASE;        		    		
        		    	}
        		    } catch (Exception e) {          	  
        		    }
        	    	
        	    	String[] extra = new String[]{"marcin@mwiacek.com"};
        	    	emailIntent.putExtra(Intent.EXTRA_EMAIL, extra);
        	    	emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        	    	emailIntent.setType("message/rfc822");
        	    	try {
        	    		startActivity(emailIntent);
        	    	} catch (Exception e) {
        		   		AlertDialog alertDialog;
        		   		alertDialog = new AlertDialog.Builder(MyActivity5).create();
        		   		alertDialog.setIcon(R.drawable.ic_launcher);

        		    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
        		    		alertDialog.setTitle("Informacja");
        		    		alertDialog.setMessage("B³¹d stworzenia maila");
        		    	} else {
        		    		alertDialog.setTitle("Info");
        		    		alertDialog.setMessage("Error creating mail");        		    		
        		    	}
        		   		alertDialog.show();   	   		        		
        	    	}

        			return true;
        		}

        });       
        
        customPref = (Preference) findPreference("No_Lock");
        customPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((Boolean)newValue) {
                	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                			WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                			WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                			WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                	
                } else {
                	getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                			WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                			WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                			WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);        	
                }            	
    	        return true;
         	}
 		});

        
		
 //       if (android.os.Build.VERSION.SDK_INT < 19) {
            //customPref = (Preference) findPreference("Kontrolki");           
        	//customPref.setEnabled(false);
        //}
	}
	
}	
    	