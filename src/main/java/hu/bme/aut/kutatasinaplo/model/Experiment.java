package hu.bme.aut.kutatasinaplo.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Experiment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@OneToOne
	private User owner;
	@Column
	private String name;
	@Column
	private String description;
	@Enumerated
	private ExperimentType type;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Url> urls;

	@ManyToMany
	private List<User> participants;

	// TODO képek és fájlok
}
