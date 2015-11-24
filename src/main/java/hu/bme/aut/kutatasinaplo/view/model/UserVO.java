package hu.bme.aut.kutatasinaplo.view.model;

import hu.bme.aut.kutatasinaplo.model.Role;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
	@XmlElement(name = "id")
	protected int id;
	@XmlElement(name = "name", required = true)
	protected String name;
	@XmlElement(name = "pwd", required = true)
	protected String pwd;
	@XmlElement(name = "email")
	protected String email;
	@XmlElement(name = "role")
	protected Role role;
}
