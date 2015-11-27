package hu.bme.aut.kutatasinaplo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class User implements AbstractEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(unique = true)
	private String name;
	@Column
	private String email;
	@Column
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role;
	@OneToMany(cascade = CascadeType.ALL)
	private List<KeyValuePair> knowledge;

	public void setKnowledge(List<KeyValuePair> knowledge) {
		List<KeyValuePair> eager = new ArrayList<KeyValuePair>(knowledge);
		this.knowledge = eager;
	}

	public Set<String> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

}
