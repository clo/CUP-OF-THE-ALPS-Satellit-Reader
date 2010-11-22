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

	private final String URL = "http://www.cupofthealps.ch/soa/web-service-cotadb.php";
	private String search = null;
	private String answer = null;
	private boolean done = false;
	
	private boolean fake = true;
	
	public HttpRequest(String search) {
		super();
		this.search = search;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		executeHttpGet();
	}
	
    public void executeHttpGet() {
    	done = false;
    	BufferedReader in = null;
        try {
        	if (!fake){
	        	Log.i("HttpRequest","starting HTTP request");
        		HttpClient client = new DefaultHttpClient();
	            HttpGet request = new HttpGet();
	            String url = URL + "?user=" + this.search;
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
        		answer="<users><post><PersonendatenID>1232</PersonendatenID><Vorname>Christian</Vorname><Name>Lochmatter</Name></post></users>";
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
