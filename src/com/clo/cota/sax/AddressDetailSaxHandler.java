package com.clo.cota.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.clo.cota.entity.User;

public class AddressDetailSaxHandler extends DefaultHandler {
	  private User user = null;
	  private String element = null;
	
	  @Override
	  public void startElement(String ns,
              String localName,
              String qName,
              Attributes attrs) throws SAXException {
		  System.out.println("STARTELEMENT: " + localName);
		  element = localName;
		  if (localName.equals("user")){
			  user = new User();
		  }
	  }

	  @Override
	  public void characters(char[] ch, int start, int length)
	          throws SAXException {
		  String value = new String(ch, start, length);
		  System.out.println(element + "=" + value); 
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
			  System.out.println("Firma: " + value);
			  user.setFirma(value);
			  element = "";
		  }else if (element.equals("adresse")){
			  System.out.println("Adrese: " + value);
			  user.setAddress(value);
			  element = "";
		  }else if (element.equals("ort")){
			  System.out.println("Ort: " + value);
			  user.setCity(value);
			  element = "";
		  }else if (element.equals("plz")){
			  System.out.println("PLZ: " + value);
			  user.setPlz(value);
			  element = "";
		  }
		  
		  
	  }

	  @Override
	  public void endElement(String ns, String localName, String qName)
	          throws SAXException {
		  System.out.println("ENDELEMENT: " + localName);
	  }
	  
	  public User getUser() {
		  return user;
	  }

	  public void setUser(User user) {
		  this.user = user;
	  }
	  
}
