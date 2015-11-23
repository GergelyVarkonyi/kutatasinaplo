package hu.bme.aut.kutatasinaplo.view.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "boolean")
@XmlAccessorType(XmlAccessType.FIELD)
public class BooleanVO {
	// @XmlElement(name = "value", required = true)
	private Boolean value;
}
