package com.mwiacek.istqb.glossary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.util.DisplayMetrics;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {     
	AutoCompleteTextView edittext;
	ListView listview1, listview2;        
	ArrayAdapter<CharSequence> adapter1, adapter2;
	final Activity MyActivity = this;   
	List<String> tipy = new ArrayList<String>();//tipy
	List<String> jumpto = new ArrayList<String>();//przy jumpto
	List<String> f1 = new ArrayList<String>();
	List<String> f2 = new ArrayList<String>();
	List<String> history = new ArrayList<String>();
    SharedPreferences sp;	
    private AdView mAdView;
    Boolean Prefs;
	ProgressDialog pd = null;
	
    
    @Override	            		
	public void onBackPressed () {
    	if (history.size()==0) {
    		finish();
    	} else {
//    		history.remove(0);
	    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
	    		Toast.makeText(getApplicationContext(), "Cofnij do '"+history.get(0)+"'", Toast.LENGTH_SHORT).show();
	    	} else {
	    		Toast.makeText(getApplicationContext(), "Back to '"+history.get(0)+"'", Toast.LENGTH_SHORT).show();	    		
	    	}
    		SpannableString s;
    		int i,j;
    		for (Integer z=0;z<listview1.getCount();z++) {
				s = (SpannableString)listview1.getItemAtPosition(z);
				
				i = s.nextSpanTransition (-1, 50000, null);
				j = s.nextSpanTransition (i, 50000, null);
				
				if (s.toString().substring(i, j).equals(history.get(0))) {
					listview1.setSelection(z);									
					break;
				}
	        }
	        if (sp.getBoolean("SyncMode", false)) {
	    		for (Integer z=0;z<listview2.getCount();z++) {
					s = (SpannableString)listview2.getItemAtPosition(z);
					
					i = s.nextSpanTransition (-1, 50000, null);
					j = s.nextSpanTransition (i, 50000, null);
					
					if (s.toString().substring(i, j).equals(history.get(0))) {
						listview2.setSelection(z);									
						break;
					}
		        }
		        
		    }
    		history.remove(0);
    		//super.onBackPressed();
    	}
    }


    void RefreshData() {
  	  	if (pd!=null) return;
	  	  	
    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
  	  	
    		pd = ProgressDialog.show(this,"","Odœwie¿anie",true,false);
    	} else {
    		pd = ProgressDialog.show(this,"","Refreshing",true,false);
    	}
    	new Thread(new Runnable(){
    		public void run(){
    			InputStream stream;	
    			try {
    				stream = getAssets().open(sp.getString("up", "24us.txt"));		
    				BufferedReader buffreader = new BufferedReader(new InputStreamReader(stream));
    				String s;
    				while ((s=buffreader.readLine()) != null) {
    					f1.add(s);
    				}
    				stream.close();
    			} catch (IOException e) {
    			}			
    			try {
    				stream = getAssets().open(sp.getString("down", "none.txt"));		
    				BufferedReader buffreader = new BufferedReader(new InputStreamReader(stream));
    				String s;
    				while ((s=buffreader.readLine()) != null) {
    					f2.add(s);
    				}
    				stream.close();
    			} catch (IOException e) {
    			}
    			
    			if (!sp.getString("down", "none.txt").equals("none.txt") && sp.getBoolean("DiffMode", false)) {
    				List<String> f11 = new ArrayList<String>();
    				List<String> f21 = new ArrayList<String>();
    				Iterator<String> listit1;
    				Iterator<String> listit2;
    				String s,ss,sss;
    				Integer i1,i2,i1s,i2s;

    				listit1 = f1.iterator();
    				while(listit1.hasNext()) {
    					s = listit1.next();
    					i1 = s.indexOf("<b>");
    					i2 = s.indexOf("</b>");
    					ss = s.substring(i1+3, i2);
    					listit2 = f2.iterator();
    					while(listit2.hasNext()) {
    						sss = listit2.next();
    						i1s = sss.indexOf("<b>");
    						i2s = sss.indexOf("</b>");
    						if (sss.substring(i1s+3, i2s).equals(ss)) {
    							ss = "";
    							if (sss.equals(s)) {
    								break;									
    							}
    							f11.add(s);
    							f21.add(sss);
    							break;
    						}
    					}
    					if (ss.length()!=0) {
    						f11.add(s);
    					}
    				}
    				listit2 = f2.iterator();
    				while(listit2.hasNext()) {
    					sss = listit2.next();
    					i1s = sss.indexOf("<b>");
    					i2s = sss.indexOf("</b>");
    					ss = sss.substring(i1s+3, i2s);
    					listit1 = f1.iterator();
    					while(listit1.hasNext()) {
    						s = listit1.next();
    						i1 = s.indexOf("<b>");
    						i2 = s.indexOf("</b>");
    						if (s.substring(i1+3, i2).equals(ss)) {
    							if (s.equals(sss)) {
    								break;									
    							}
    							f21.add(sss);
    							break;
    						}
    						
    					}
    				}

    				f1.clear();
    				f1.addAll(f11);
    				f2.clear();
    				f2.addAll(f21);
    				
    			}


            	MyActivity.runOnUiThread(new Runnable() {
            		public void run() {
            			AddContent(f1, adapter1);		
            			AddContent(f2, adapter2);
                		edittext.setAdapter(new ArrayAdapter<String>(MyActivity,android.R.layout.simple_list_item_1,tipy));            		

                		if (pd.isShowing()) {
              			  try {
              				  pd.cancel();
              			  } catch (Exception e) {    				  
              			  }
              	    	}
              	    	pd=null;    		  
            			try {
            	    		String s;
            		    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
            		    		s = "S³ownik ISTQB";
            		    	} else {
            		    		s = "ISTQB glossary";	
            		    	}
            	    		s = s+" - "+sp.getString("up", "24us.txt").replace("22us.txt", "2.2").replace("23us.txt", "2.3").replace("24us.txt", "2.4").replace("222plus.txt", "2.2.2 PL");
            	    		if (!sp.getString("down", "none.txt").contains("none.txt")) {
            	    			s = s+ " / "+sp.getString("down", "none.txt").replace("22us.txt", "2.2").replace("23us.txt", "2.3").replace("24us.txt", "2.4").replace("222plus.txt", "2.2.2 PL");
            	    			if (sp.getBoolean("DiffMode", false)) {
            	    				if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
            	    					s = s+ " (ró¿nice)";
            	    				} else {
            	    					s = s+ " (diff)";
            	    				}
            	        		}    			
            	    		}
            	        	setTitle(s);	
            	        } catch (Exception e) {
            	        	  
            	        }		              			
            		}
            	});
    			
    		}
    	}).start();;

    }
    
	void AddContent(List<String> list, ArrayAdapter<CharSequence> adapter) {
		SpannableString spannablecontent;
		Integer i1,i2,i3;
		List<Integer> intList1 = new ArrayList<Integer>();
		List<Integer> intList2 = new ArrayList<Integer>();
		Iterator<Integer> it1, it2;
		Iterator<String> listit;
		String s;
		
		adapter.clear();
		
		if (edittext.getText().length()==0) {
			listit = list.iterator();
			while(listit.hasNext()) {
				s = listit.next();
				i1 = s.indexOf("<b>");
				i2 = s.indexOf("</b>"); 
				if (tipy.indexOf(s.substring(i1+3, i2))==-1) {
					tipy.add(s.substring(i1+3, i2));
				}
				
				s=s.replace("<b>", "").replace("</b>", "");
				
				s=s.replace("<p>", "\r\n\r\n").replace("<br>", "\r\n");
								
				i3 = s.indexOf("<i>");
				while (i3!=-1) {
					intList1.add(i3);
					intList2.add(s.indexOf("</i>")-3);

					s=s.replaceFirst("<i>", "").replaceFirst("</i>", "");
					
					i3 = s.indexOf("<i>");
				}
				
				spannablecontent=new SpannableString(s);

				spannablecontent.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 
                         i1,i2-3, 0);
				
				if (intList1.size()!=0) {
					it1 = intList1.iterator();
					it2 = intList2.iterator();
					while(it1.hasNext()) {
						spannablecontent.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 
		                         it1.next(),it2.next(), 0);
					}
					intList1.clear();
					intList2.clear();					
				}
				
				adapter.add(spannablecontent);								
			}
			
			return;
		}		
		List<String> related = new ArrayList<String>();
		
		String tosearch = "(?![^<]+>)((?i:\\Q"+edittext.getText()+"\\E))";					
		int i;
		    
		listit = list.iterator();
		while(listit.hasNext()) {
			s = listit.next();
			if (!s.replaceFirst(tosearch,"<mark>").contains("<mark>")) continue;              													
			
			i = s.indexOf("See <i>");
			if (i==-1) continue;
			related.add(s.substring(i+7,s.indexOf("</i>",i+7)));
		}

		listit = list.iterator();
		while(listit.hasNext()) {
			s = listit.next();
			if (!s.replaceFirst(tosearch,"<mark>").contains("<mark>")) {              													
				Iterator<String> iterator = related.iterator();
				i=0;
				while (iterator.hasNext()) {
					if (iterator.next().equals(s.substring(s.indexOf("<b>")+3,s.indexOf("</b>")))) {
						//strings.add(s);

//						adapter.add(Html.fromHtml(s));
						s=s.replace("<p>", "\r\n\r\n").replace("<br>", "\r\n");
						
						i1 = s.indexOf("<b>");
						i2 = s.indexOf("</b>")-3; 
						s=s.replace("<b>", "").replace("</b>", "");
						
						
						i3 = s.indexOf("<i>");
						while (i3!=-1) {
							intList1.add(i3);
							intList2.add(s.indexOf("</i>")-3);

							s=s.replaceFirst("<i>", "").replaceFirst("</i>", "");
							
							i3 = s.indexOf("<i>");
						}
						
						spannablecontent=new SpannableString(s);

						spannablecontent.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 
		                         i1,i2, 0);
						
						if (intList1.size()!=0) {
							it1 = intList1.iterator();
							it2 = intList2.iterator();
							while(it1.hasNext()) {
								spannablecontent.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 
				                         it1.next(),it2.next(), 0);
							}
							intList1.clear();
							intList2.clear();
							
						}
						
						adapter.add(spannablecontent);
						
						related.remove(i);
						break;
					}
					i++;
				}
				continue;
			}
			s=s.replace("<p>", "\r\n\r\n").replace("<br>", "\r\n");
			
			i1 = s.indexOf("<b>");
			i2 = s.indexOf("</b>")-3; 
			s=s.replace("<b>", "").replace("</b>", "");
			
			
			i3 = s.indexOf("<i>");
			while (i3!=-1) {
				intList1.add(i3);
				intList2.add(s.indexOf("</i>")-3);

				s=s.replaceFirst("<i>", "").replaceFirst("</i>", "");
				
				i3 = s.indexOf("<i>");
			}
			
			spannablecontent=new SpannableString(s);

			spannablecontent.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 
                     i1,i2, 0);
			
			if (intList1.size()!=0) {
				it1 = intList1.iterator();
				it2 = intList2.iterator();
				while(it1.hasNext()) {
					spannablecontent.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 
	                         it1.next(),it2.next(), 0);
				}
				intList1.clear();
				intList2.clear();
				
			}
			
			adapter.add(spannablecontent);
		}
		
	}
	
    @Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        sp=PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getString("wyglad", "").length()==0) {
            if (android.os.Build.VERSION.SDK_INT < 11) {
            	setTheme(android.R.style.Theme_Light);
            	SharedPreferences.Editor editor1 = sp.edit();
            	editor1.putString("wyglad","pusty");
            	editor1.commit();
            } else if (android.os.Build.VERSION.SDK_INT > 13) {
            	setTheme(android.R.style.Theme_DeviceDefault);            	
            	SharedPreferences.Editor editor1 = sp.edit();
            	editor1.putString("wyglad","domyslnyurzadzenie");
            	editor1.commit();
            } else {
            	setTheme(android.R.style.Theme_Holo);	
            	SharedPreferences.Editor editor1 = sp.edit();
            	editor1.putString("wyglad","holo");
            	editor1.commit();
            }
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
		setContentView(R.layout.activity_main);

        if (sp.getBoolean("No_Lock", false)) {
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
        if (sp.getBoolean("Obrot", false)) {
        	setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);                	            		
        }
        
        mAdView = (AdView) findViewById(R.id.adView1);
      //  mAdView.setAdListener(new AdListener(this));
     //   mAdView.loadAd(new AdRequest.Builder().addTestDevice("").build());
        mAdView.loadAd(new AdRequest.Builder().build());
		
		listview1 = (ListView) findViewById(R.id.listView1);
		listview2 = (ListView) findViewById(R.id.listView2);
		edittext = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		
		//adapter1 = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_list_item_1  );		
		adapter1 = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1) {
		    @Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		        View view = super.getView(position, convertView, parent);
		        TextView text = (TextView) view.findViewById(android.R.id.text1);
		        if (sp.getBoolean("OwnTextSize", false)) {
		        	text.setTextSize(Float.parseFloat(sp.getString("TextSize", "22")));
		        }
		        
		        return view;
		    }
		};
		
		//adapter2 = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_list_item_1  );
		adapter2 = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1) {
		    @Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		        View view = super.getView(position, convertView, parent);
		        TextView text = (TextView) view.findViewById(android.R.id.text1);
		        if (sp.getBoolean("OwnTextSize", false)) {
		        	text.setTextSize(Float.parseFloat(sp.getString("TextSize", "22")));
		        }
		        text.setTextColor(Color.BLUE);
		        return view;
		    }
		};
		
		listview1.setAdapter(adapter1);
		listview2.setAdapter(adapter2);

        handleIntent(getIntent());            

		RefreshData();
	  	if (sp.getString("down", "none.txt").equals("none.txt")) {  		  
	  		LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
	  		p1.weight=2.0f;  		
			listview1.setLayoutParams(p1);
		} else {
			LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
	  		p1.weight=1.0f;  		
			listview1.setLayoutParams(p1);
		}
	  	
	  	//szukanie blednych linkow
	  	/*
		Iterator<String> listit1;
		Iterator<String> listit2;
		Integer i1,i2,i3,i4,start;
		String s,s2,ss,sss;
		
		listit1 = f1.iterator();
		while(listit1.hasNext()) {
			s = listit1.next();
			
			i3 = s.indexOf("<i>");
			if (i3==-1) {
				continue;			
			}
			start = 0;
			while (i3!=-1) {
				i3 = s.indexOf("<i>",start);
				i4 = s.indexOf("</i>",i3+1);
				ss = s.substring(i3+3,i4);
				start = i4+1;
				i3 = s.indexOf("<i>",start);
				
				listit2 = f1.iterator();
				while(listit2.hasNext()) {
					s2 = listit2.next();
					i1 = s2.indexOf("<b>");
					i2 = s2.indexOf("</b>");
					sss = s2.substring(i1+3, i2);
					if (sss.equals(ss)) {
						ss = "";
						break;
					}
				}					
				if (ss.length()!=0) {
					Log.d("com.mwiacek.istqb.glossary",s+"- wrong link '"+ss+"'");
				}
			}			
		}
		*/
		
		listview1.setClickable(true);
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				jumpto.clear();

				SpannableString s = (SpannableString)listview1.getItemAtPosition(position);
				
				Integer i=-1,j;
				i = s.nextSpanTransition (i, 50000, null);
				i = s.nextSpanTransition (i, 50000, null);
				i = s.nextSpanTransition (i, 50000, null);
				while (i!=50000) {
					j = s.nextSpanTransition (i,50000, null);
					jumpto.add(s.toString().substring(i,j));
					i = s.nextSpanTransition (j,50000, null);
				}
				if (jumpto.size()!=0) {
					s = (SpannableString)listview1.getItemAtPosition(position);
					i=-1;
					i = s.nextSpanTransition (i, 50000, null);
					j = s.nextSpanTransition (i, 50000, null);					
					history.add(0,s.toString().substring(i,j));
					
					AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
					builder.setOnCancelListener(new OnCancelListener() {

					    public void onCancel(DialogInterface dialog) {
					        history.remove(0);                
					    }
					});
					
					
			    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
						builder.setTitle("Skocz do...");
			    	} else {			    	
						builder.setTitle("Jump to...");
			    	}
					builder.setItems( jumpto.toArray(new CharSequence[jumpto.size()]), new DialogInterface.OnClickListener() {

					   public void onClick(DialogInterface dialog, int item) {
					        Integer i,j;
							SpannableString s;
							
					        for (Integer z=0;z<listview1.getCount();z++) {
								s = (SpannableString)listview1.getItemAtPosition(z);
								
								i = s.nextSpanTransition (-1, 50000, null);
								j = s.nextSpanTransition (i, 50000, null);
								
								if (s.toString().substring(i, j).equals(jumpto.get(item))) {
									listview1.setSelection(z);									
									break;
								}
					        }
					        if (sp.getBoolean("SyncMode", false)) {
						        for (Integer z=0;z<listview2.getCount();z++) {
									s = (SpannableString)listview2.getItemAtPosition(z);
									
									i = s.nextSpanTransition (-1, 50000, null);
									j = s.nextSpanTransition (i, 50000, null);
									
									if (s.toString().substring(i, j).equals(jumpto.get(item))) {
										listview2.setSelection(z);									
										break;
									}
						        }
					        }					        
							jumpto.clear();
					   }

					});

					AlertDialog alert = builder.create();

					alert.show();									
				}
			}
		});
		listview2.setClickable(true);
		listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				jumpto.clear();

				SpannableString s = (SpannableString)listview2.getItemAtPosition(position);
				
				Integer i=-1,j;
				i = s.nextSpanTransition (i, 50000, null);
				i = s.nextSpanTransition (i, 50000, null);
				i = s.nextSpanTransition (i, 50000, null);
				while (i!=50000) {
					j = s.nextSpanTransition (i,50000, null);
					jumpto.add(s.toString().substring(i,j));
					i = s.nextSpanTransition (j,50000, null);
				}
				if (jumpto.size()!=0) {
					s = (SpannableString)listview1.getItemAtPosition(position);
					i=-1;
					i = s.nextSpanTransition (i, 50000, null);
					j = s.nextSpanTransition (i, 50000, null);					
					history.add(0,s.toString().substring(i,j));
						
					AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
					builder.setOnCancelListener(new OnCancelListener() {

					    public void onCancel(DialogInterface dialog) {
					        history.remove(0);                
					    }
					});
					
			    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
						builder.setTitle("Skocz do...");
			    	} else {			    	
						builder.setTitle("Jump to...");
			    	}
					builder.setItems( jumpto.toArray(new CharSequence[jumpto.size()]), new DialogInterface.OnClickListener() {

					   public void onClick(DialogInterface dialog, int item) {
					        Integer i,j;
							SpannableString s;
							
					        if (sp.getBoolean("SyncMode", false)) {
						        for (Integer z=0;z<listview1.getCount();z++) {
									s = (SpannableString)listview1.getItemAtPosition(z);
									
									i = s.nextSpanTransition (-1, 50000, null);
									j = s.nextSpanTransition (i, 50000, null);
									
									if (s.toString().substring(i, j).equals(jumpto.get(item))) {
										listview1.setSelection(z);									
										break;
									}
						        }
						    }
					        for (Integer z=0;z<listview2.getCount();z++) {
								s = (SpannableString)listview2.getItemAtPosition(z);
								
								i = s.nextSpanTransition (-1, 50000, null);
								j = s.nextSpanTransition (i, 50000, null);
								
								if (s.toString().substring(i, j).equals(jumpto.get(item))) {
									listview2.setSelection(z);									
									break;
								}
					        }					       
					       
							jumpto.clear();
					   }

					});

					AlertDialog alert = builder.create();

					alert.show();									
				}
			}
		});

		
		edittext.setOnKeyListener(new OnKeyListener() {
        	public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                	tipy.clear();
                	adapter1.clear();
                	AddContent(f1, adapter1);
                	if (!sp.getString("down", "none.txt").contains("none.txt")) {
                    	adapter2.clear();
                    	AddContent(f2, adapter2);                		
                	}
                	
                 	return true;
                }
                return false;
            }
        });
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {     
    	super.onActivityResult(requestCode, resultCode, data);
      
  		if (requestCode == 1 && resultCode == RESULT_OK) {
			if (!sp.getString("wyglad", "").equals(data.getStringExtra("wyglad")) ||
                !sp.getString("lang", "auto").equals(data.getStringExtra("lang"))) {
				MyActivity.finish();
				MyActivity.startActivity(new Intent(MyActivity, MyActivity.getClass()));    			
				return;
			}

            if (!sp.getString("up", "24us.txt").equals(data.getStringExtra("up")) ||
            	!sp.getString("down", "none.txt").equals(data.getStringExtra("down")) ||
            	sp.getBoolean("DiffMode", false)!=data.getBooleanExtra("DiffMode",false)) {  	
            	f1.clear();
      		  	f2.clear();
      		  	tipy.clear();
      		  	history.clear();
      		  	RefreshData();        		  
      		  
      		  	/*adapter1.clear();
      		  	AddContent(f1, adapter1);
      		  	adapter2.clear();
      		  	
      		  	if (!sp.getString("down", "none.txt").contains("none.txt")) {                
      		  		AddContent(f2, adapter2);
      		  	}   
      		  	
            	listview1.invalidateViews();
          		if (!sp.getString("down", "none.txt").contains("none.txt")) {
          			listview2.invalidateViews();
          		}*/
          	  
              	if (sp.getString("down", "none.txt").equals("none.txt")) {        		  
              		LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
                	p1.weight=2.0f;  		
              		listview1.setLayoutParams(p1);    		  
              	} else {
              		LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
              		p1.weight=1.0f;  		
              		listview1.setLayoutParams(p1);
              	}
      	  	}

      	  
      	  	if (sp.getBoolean("Obrot", false)) {
            	setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);                	            		
            } else {
            	setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }                  		
			            
      	  	if (sp.getBoolean("No_Lock", true)) {
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
  		}
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {     
        case R.id.sett:
        	Intent intent = new Intent(this,PreferencesActivity.class);
            intent.putExtra("lang", sp.getString("lang","auto"));
            intent.putExtra("wyglad", sp.getString("wyglad",""));
            intent.putExtra("down",sp.getString("down", "none.txt"));
            intent.putExtra("up",sp.getString("up", "24us.txt"));
            intent.putExtra("DiffMode",sp.getBoolean("DiffMode", false));        	
        	startActivityForResult(intent, 1);
            return true;
        case R.id.info:
    		AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity);
            builder.setIcon(R.drawable.ic_launcher);

	    	if ((sp.getString("lang", "auto").equals("auto") && Locale.getDefault ().getDisplayLanguage ().equals("polski")) || sp.getString("lang", "auto").equals("pl_PL")) {
	    		builder.setTitle("Znaczenie hase³");
	    	} else {    		
	    		builder.setTitle("Keywords meaning");
	    	}
    		builder.setMessage("F : ISTQB Foundation syllabus\r\n"+
    				"F-AT : ISTQB Foundation Extension Agile Tester syllabus\r\n"+
    				"ATM : ISTQB Advanced – Test Management syllabus\r\n"+
    				"ATA : ISTQB Advanced – Test Analyst syllabus\r\n"+
    				"ATT : ISTQB Advanced – Technical Test Analyst syllabus\r\n"+
    				"EITP : ISTQB Expert – Improving the Testing Process syllabus\r\n"+
    				"ETAE : ISTQB Expert – Test Automation - Engineering syllabus\r\n" +
    				"ETM : ISTQB Expert – Test Management syllabus");
    		AlertDialog alert = builder.create();

    		alert.show();									
            return true;
        case R.id.exit:
        	finish();
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    protected void onNewIntent (Intent intent) {
    	handleIntent(intent);
    }
        
    private void handleIntent(Intent intent) {
        if (intent.getData()!=null) {
    		edittext.setText(intent.getData().toString());
        }
    }
    
}
