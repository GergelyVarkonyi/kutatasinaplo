package hu.bme.aut.kutatasinaplo.service;

import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.view.model.ExperimentVO;

import java.util.List;

public interface ExperimentService {
	public List<Experiment> loadAll();

	public Experiment loadById(int id);

	public boolean create(ExperimentVO view);
}
