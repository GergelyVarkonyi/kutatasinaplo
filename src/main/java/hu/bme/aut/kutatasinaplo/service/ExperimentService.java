package hu.bme.aut.kutatasinaplo.service;

import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.view.model.BlobFileVO;
import hu.bme.aut.kutatasinaplo.view.model.ExperimentVO;

import java.util.List;

public interface ExperimentService {
	public List<Experiment> loadAll();

	public Experiment loadById(int id);

	public boolean create(ExperimentVO view);

	public boolean delete(int id);

	public boolean addParticipants(int experimentId, List<Integer> participantsIds);

	public boolean attachImages(int experimentId, List<BlobFileVO> imges);

	public boolean attachFiles(int experimentId, List<BlobFileVO> files);
}
