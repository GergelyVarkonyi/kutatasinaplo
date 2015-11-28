package hu.bme.aut.kutatasinaplo.view.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

@XmlRootElement(name = "file")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlobFileVO {
	@XmlElement(name = "id")
	protected int id;

	@XmlElement(name = "name")
	protected String name;
	@XmlElement(name = "type")
	protected String type;
	@XmlElement(name = "size")
	protected Long size;
	@XmlElement(name = "data")
	protected byte[] data;
}
