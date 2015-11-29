package hu.bme.aut.kutatasinaplo.view.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import hu.bme.aut.kutatasinaplo.model.Role;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "id", "name" })
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
	@XmlElement(name = "knowledge")
	protected List<KeyValuePairVO> knowledge;
}
