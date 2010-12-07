package com.clo.cota.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.clo.cota.entity.User;

public class AddressDetailSaxHandler extends DefaultHandler {
	  static final String LOG_ADRESS_DETAIL_HANDLER = "sax handler address detail";
	  private User user = null;
	  private String element = null;
	
	  @Override
	  public void startElement(String ns,
              String localName,
              String qName,
              Attributes attrs) throws SAXException {
		  Log.v(LOG_ADRESS_DETAIL_HANDLER,"STARTELEMENT: " + localName);
		  element = localName;
		  if (localName.equals("user")){
			  user = new User();
		  }
	  }

	  @Override
	  public void characters(char[] ch, int start, int length)
	          throws SAXException {
		  String value = new String(ch, start, length);
		  Log.v(LOG_ADRESS_DETAIL_HANDLER,element + "=" + value); 
		  if (element.equals("vorname")){
			  user.setFirstname(value);
			  element = "";
		  }else if (element.equals("name")){
			  user.setLastname(value);	
			  element = "";
		  }else if (element.equals("personendatenid")){
			  user.setId(new Integer(value));
			  element = "";
		  }else if (element.equals("email")){
			  user.setEmail(value);
			  element = "";
		  }else if (element.equals("firma")){
			  user.setFirma(value);
			  element = "";
		  }else if (element.equals("adresse")){
			  user.setAddress(value);
			  element = "";
		  }else if (element.equals("ort")){
			  user.setCity(value);
			  element = "";
		  }else if (element.equals("plz")){
			  user.setPlz(value);
			  element = "";
		  }else if (element.equals("mobile")){
			  user.setMobile(value);
			  element = "";
		  }else if (element.equals("funktion")){
			  user.setFunction(value);
			  element = "";
		  }
	  }

	  @Override
	  public void endElement(String ns, String localName, String qName)
	          throws SAXException {
		  Log.v(LOG_ADRESS_DETAIL_HANDLER,"ENDELEMENT: " + localName);
	  }
	  
	  public User getUser() {
		  return user;
	  }

	  public void setUser(User user) {
		  this.user = user;
	  }
	  
}
