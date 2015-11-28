package hu.bme.aut.kutatasinaplo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Experiment implements AbstractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@OneToOne
	private User owner;
	@Column
	private String name;
	@Column
	private String description;
	@Enumerated(EnumType.STRING)
	private ExperimentType type;

	@OneToMany(cascade = CascadeType.ALL)
	private List<KeyValuePair> urls;

	@ManyToMany(cascade = CascadeType.MERGE)
	private List<User> participants;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "experiment_attachment")
	private List<BlobFile> attachments;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "experiment_image")
	private List<BlobFile> images;

	public void setParticipants(List<User> participants) {
		List<User> eager = new ArrayList<User>(participants);
		this.participants = eager;
	}

	public void setAttachments(List<BlobFile> attachments) {
		List<BlobFile> eager = new ArrayList<BlobFile>(attachments);
		this.attachments = eager;
	}

	public void setImages(List<BlobFile> images) {
		List<BlobFile> eager = new ArrayList<BlobFile>(images);
		this.images = eager;
	}

}
