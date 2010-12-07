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

import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

import com.clo.cota.cotaDbReader;

public class HttpRequest {

	private final String LOG_COTA_HTTPREQUEST = "HTTP_REQUEST";
	private final String host = "http://www.cupofthealps.ch";
	private final String urlList = "soa/ws-list-address.php";
	private final String urlDetail = "soa/ws-address-detail.php";
	private final String urlCategory = "soa/ws-address-category.php";
	private String search = null;
	private int personendatenid=0;
	private int categoryid=0;
	private String answer = null;
	private boolean done = false;
	public enum requestEnum { NAME, ID, CATEGORY };
	private requestEnum request = requestEnum.NAME;
	
	private boolean fake = false;
	
	public HttpRequest(String search) {
		this.search = search;
//		init();
	}
	
	public HttpRequest(int personendatenid) {
		this.personendatenid = personendatenid;
	}
	
	public HttpRequest(requestEnum request){
		this.request = request;
	}
	
//	public void init(){
//		props = getPreferences(PreferenceActivity.MODE_PRIVATE);
//		editor = props.edit();
//	}

	public void run() {
		Log.v(LOG_COTA_HTTPREQUEST,"starting thread " + Thread.currentThread().getName() + " ...");
		if (request == request.ID && this.personendatenid>0){
			Log.v(LOG_COTA_HTTPREQUEST,"TASK: address information: " + this.personendatenid);
			executeHttpGetDetail();
		}else if(request == request.NAME && !this.search.equals(null)){
			Log.v(LOG_COTA_HTTPREQUEST,"TASK: list of addresses: " + search);
			executeHttpGet();
		}else if(request == request.CATEGORY && this.categoryid>0){
			Log.v(LOG_COTA_HTTPREQUEST,"TASK: list of addresses: " + search);
			executeHttpGetCategory();
		}else{
			Log.e(LOG_COTA_HTTPREQUEST,"TASK: request is not defined correctly!!");
		}
		Log.v(LOG_COTA_HTTPREQUEST,"stopping thread " + Thread.currentThread().getName() + "[ Time to get: TODO! ]");
	}
	
    public void executeHttpGet() {
    	done = false;
    	BufferedReader in = null;
        try {
        	if (!fake){
	        	Log.v(LOG_COTA_HTTPREQUEST,"starting HTTP request");
        		HttpClient client = new DefaultHttpClient();
	            HttpGet request = new HttpGet();
	            String url = host + "/" + urlList + "?user=" + this.search;
	            Log.i(LOG_COTA_HTTPREQUEST,url);
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
	            Log.v(LOG_COTA_HTTPREQUEST,answer);
	            Log.v(LOG_COTA_HTTPREQUEST,"starting HTTP done");
        	}else{
        	    Thread.sleep(2000);
        		answer="<users>";
        		answer+="<post><PersonendatenID>571</PersonendatenID><Vorname>Christian</Vorname><Name>Lochmatter</Name></post>";
        		answer+="<post><PersonendatenID>572</PersonendatenID><Vorname>Stefan</Vorname><Name>Meichtry</Name></post>";
        	    answer+="</users>";
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
    	Log.v(LOG_COTA_HTTPREQUEST,"start of function executeHttpGetDetail");
    	done = false;
    	BufferedReader in = null;
        try {
        	if (!fake){
	        	Log.v(LOG_COTA_HTTPREQUEST,"starting HTTP request");
        		HttpClient client = new DefaultHttpClient();
	            HttpGet request = new HttpGet();
	            String url = host + "/" + urlDetail + "?personendatenid=" + this.personendatenid;
	            Log.v(LOG_COTA_HTTPREQUEST,url);
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
	            Log.v(LOG_COTA_HTTPREQUEST,answer);
	            Log.v(LOG_COTA_HTTPREQUEST,"starting HTTP done");
        	}else{
        	    Thread.sleep(1000);
        		if (this.personendatenid==571){
        			answer="<data><user><personendatenid>571</personendatenid><vorname>Christian</vorname><name>Lochmatter</name><email>christian.lochmatter@gmx.ch</email><firma>Swisscom AG</firma></user></data>";
        		}else if (this.personendatenid==572){
        			answer="<data><user><personendatenid>572</personendatenid><vorname>Stefan</vorname><name>Meichtry</name><email>stefan.meichtry@gmail.com</email><firma>Swisscom AG</firma></user></data>";
        		}else{
        			answer="";
        			Log.w(LOG_COTA_HTTPREQUEST,"Nothing found for this id");
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
		Log.v(LOG_COTA_HTTPREQUEST,"stop of function executeHttpGetDetail");    	
    }
    
    public void executeHttpGetCategory() {
    	Log.v(LOG_COTA_HTTPREQUEST,"start of function executeHttpGetCategory");
    	done = false;
    	BufferedReader in = null;
        try {
        	if (!fake){
	        	Log.v(LOG_COTA_HTTPREQUEST,"starting HTTP request");
        		HttpClient client = new DefaultHttpClient();
	            HttpGet request = new HttpGet();
	            String url = host + "/" + urlCategory + "?categoryid=" + this.categoryid;
	            Log.v(LOG_COTA_HTTPREQUEST,url);
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
	            Log.v(LOG_COTA_HTTPREQUEST,answer);
	            Log.v(LOG_COTA_HTTPREQUEST,"starting HTTP done");
        	}else{
        	    Thread.sleep(1000);
        		if (this.personendatenid==571){
        			answer="<data><user><personendatenid>571</personendatenid><vorname>Christian</vorname><name>Lochmatter</name><email>christian.lochmatter@gmx.ch</email><firma>Swisscom AG</firma></user></data>";
        		}else if (this.personendatenid==572){
        			answer="<data><user><personendatenid>572</personendatenid><vorname>Stefan</vorname><name>Meichtry</name><email>stefan.meichtry@gmail.com</email><firma>Swisscom AG</firma></user></data>";
        		}else{
        			answer="";
        			Log.w(LOG_COTA_HTTPREQUEST,"Nothing found for this id");
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
		Log.v(LOG_COTA_HTTPREQUEST,"stop of function executeHttpGetDetail");    	
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

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public int getPersonendatenid() {
		return personendatenid;
	}

	public void setPersonendatenid(int personendatenid) {
		this.personendatenid = personendatenid;
	}

	public int getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}
	
	
	
	
	

}
