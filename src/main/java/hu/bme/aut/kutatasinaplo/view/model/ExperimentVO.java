package hu.bme.aut.kutatasinaplo.view.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "experiment")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExperimentVO {
	@XmlElement(name = "id")
	protected int id;
	@XmlElement(name = "name")
	protected String name;
	@XmlElement(name = "description")
	protected String description;
	@XmlElement(name = "owner")
	protected UserVO owner;
	@XmlElement(name = "public")
	protected boolean isPublic;
	@XmlElement(name = "urls")
	protected List<UrlVO> urls;
	@XmlElement(name = "participants")
	protected List<UserVO> participants;
}
