package com.clo.cota.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import android.widget.Toast;

import com.clo.cota.cotaDbReader;

public class HttpRequest implements Runnable {

	private final String host = "http://www.cupofthealps.ch";
	private final String urlList = "soa/ws-list-address.php";
	private final String urlDetail = "soa/ws-address-detail.php";
	private String search = null;
	private String answer = null;
	private boolean done = false;
	private int personendatenid=0; 
	
	private boolean fake = false;
	
	public HttpRequest(String search) {
		super();
		this.search = search;
	}
	
	public HttpRequest(int personendatenid) {
		super();
		this.personendatenid = personendatenid;
	}

	@Override
	public void run() {
		Log.v("HttpRequest","start of function run");
		if (this.personendatenid>0){
			Log.v("HttpRequest","Get detail information for address: " + this.personendatenid);
			executeHttpGetDetail();
		}else if(!this.search.equals(null)){
			Log.v("HttpRequest","Get list of address: " + search);
			executeHttpGet();
		}else{
			Log.i("HttpRequest","Request is not defined correctly");
		}
		Log.v("HttpRequest","stop of function run");
	}
	
    public void executeHttpGet() {
    	done = false;
    	BufferedReader in = null;
        try {
        	if (!fake){
	        	Log.i("HttpRequest","starting HTTP request");
        		HttpClient client = new DefaultHttpClient();
	            HttpGet request = new HttpGet();
	            String url = host + "/" + urlList + "?user=" + this.search;
	            Log.i("HttpRequest",url);
        		request.setURI(new URI(url));
	            HttpResponse response = client.execute(request);
	            in = new BufferedReader
	            (new InputStreamReader(response.getEntity().getContent()));
	            StringBuffer sb = new StringBuffer("");
	            String line = "";
	            String NL = System.getProperty("line.separator");
	            while ((line = in.readLine()) != null) {
	                sb.append(line + NL);
	            }
	            in.close();
	            String page = sb.toString();
	            answer=page;
	            done = true;
	            Log.i("HttpRequest",answer);
	            Log.i("HttpRequest","starting HTTP done");
        	}else{
        	    Thread.sleep(1000);
        		answer="<users>";
        		answer+="<post><PersonendatenID>571</PersonendatenID><Vorname>Christian</Vorname><Name>Lochmatter</Name></post>";
        		answer+="<post><PersonendatenID>572</PersonendatenID><Vorname>Stefan</Vorname><Name>Meichtry</Name></post>";
        	    answer+="<users>";
        	    done = true;
        	}
        }catch(URISyntaxException e){
        	e.printStackTrace();
        }catch(IOException e){
        	e.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
            if (in != null) {
                try {
                    in.close();
                    } catch (IOException e) {
                    	e.printStackTrace();
                }
            }
        }
    }
    
    public void executeHttpGetDetail() {
    	Log.v("HttpRequest","start of function executeHttpGetDetail");
    	done = false;
    	BufferedReader in = null;
        try {
        	if (!fake){
	        	Log.i("HttpRequest","starting HTTP request");
        		HttpClient client = new DefaultHttpClient();
	            HttpGet request = new HttpGet();
	            String url = host + "/" + urlDetail + "?personendatenid=" + this.personendatenid;
	            Log.i("HttpRequest",url);
        		request.setURI(new URI(url));
	            HttpResponse response = client.execute(request);
	            in = new BufferedReader
	            (new InputStreamReader(response.getEntity().getContent()));
	            StringBuffer sb = new StringBuffer("");
	            String line = "";
	            String NL = System.getProperty("line.separator");
	            while ((line = in.readLine()) != null) {
	                sb.append(line + NL);
	            }
	            in.close();
	            String page = sb.toString();
	            answer=page;
	            done = true;
	            Log.i("HttpRequest",answer);
	            Log.i("HttpRequest","starting HTTP done");
        	}else{
        	    Thread.sleep(1000);
        		if (this.personendatenid==571){
        			answer="<data><user><PersonendatenID>571</PersonendatenID><Vorname>Christian</Vorname><Name>Lochmatter</Name><email>christian.lochmatter@gmx.ch</email></user></data>";
        		}
        		if (this.personendatenid==572){
        			answer="<data><user><PersonendatenID>572</PersonendatenID><Vorname>Stefan</Vorname><Name>Meichtry</Name><email>stefan.meichtry@gmail.com</email></user></data>";
        		}
        	    done = true;
        	}
        }catch(URISyntaxException e){
        	e.printStackTrace();
        }catch(IOException e){
        	e.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
            if (in != null) {
                try {
                    in.close();
                    } catch (IOException e) {
                    	e.printStackTrace();
                }
            }
        }
		Log.v("HttpRequest","stop of function executeHttpGetDetail");    	
    }
    
    public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	

}
