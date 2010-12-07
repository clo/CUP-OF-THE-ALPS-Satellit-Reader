package com.clo.cota.sax;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.clo.cota.entity.User;

public class MySaxHandler extends DefaultHandler {
	  static final String LOG_SAX_HANDLER = "address list sax handler";
	  private List<User> users = new ArrayList<User>();
	  private User user = null;
	  private String element = null;
	
	  @Override
	  public void startElement(String ns,
              String localName,
              String qName,
              Attributes attrs) throws SAXException {
		  Log.v(LOG_SAX_HANDLER,"STARTELEMENT: " + localName);
		  element = localName;
		  if (localName.equals("post")){
			  user = new User();
		  }
	  }

	  @Override
	  public void characters(char[] ch, int start, int length)
	          throws SAXException {
		  String value = new String(ch, start, length);
		  Log.v(LOG_SAX_HANDLER,element + "=" + value); 
		  if (element.equals("Vorname")){
			  user.setFirstname(value);
			  element = "";
		  }else if (element.equals("Name")){
			  user.setLastname(value);	
			  element = "";
		  }else if (element.equals("PersonendatenID")){
			  user.setId(new Integer(value));
			  element = "";
		  }
	  }

	  @Override
	  public void endElement(String ns, String localName, String qName)
	          throws SAXException {
		  Log.v(LOG_SAX_HANDLER,"ENDELEMENT: " + localName);
		  if (localName.equals("post")){
			  if (!user.equals(null)){
				  users.add(user);
				  user = null;
			  }
		  }
//		  if("user".equals(qName)){
//			  users.add(user);
//	      }else if("Vorname".equals(qName)){
//	    	  user.setFirstname(firstname)
//	      }else if("Nachname".eqals(qName)){
//	    	  user.setLastname(uri);
//	      }
	  }
	  
	  public List<User> getUsers() {
		  return users;
	  }

	  public void setUsers(List<User> users) {
		  this.users = users;
	  }

	
}
