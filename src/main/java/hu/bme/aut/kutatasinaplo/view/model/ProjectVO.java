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

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectVO {
	@XmlElement(name = "id")
	protected int id;
	@XmlElement(name = "name", required = true)
	protected String name;
	@XmlElement(name = "description", required = true)
	protected String description;
	@XmlElement(name = "participants")
	protected List<UserVO> participants;
	@XmlElement(name = "experiments")
	private List<ExperimentVO> experiments;
	@XmlElement(name = "experimentWithoutRight")
	private boolean experimentWithoutRight;

}
