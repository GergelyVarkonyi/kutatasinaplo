package hu.bme.aut.kutatasinaplo.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum Role {
	VISITOR, USER, ADMIN;
}
