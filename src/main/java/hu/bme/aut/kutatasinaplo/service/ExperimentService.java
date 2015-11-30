package hu.bme.aut.kutatasinaplo.service;

import java.util.List;

import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.model.validate.ValidateException;
import hu.bme.aut.kutatasinaplo.view.model.BlobFileVO;
import hu.bme.aut.kutatasinaplo.view.model.ExperimentVO;
import hu.bme.aut.kutatasinaplo.view.model.UserVO;

public interface ExperimentService extends AbstractEntityService<Experiment> {

	public boolean create(ExperimentVO view) throws ValidateException;

	public boolean setParticipants(int experimentId, List<Integer> participantsIds);

	public boolean attachImages(int experimentId, List<BlobFileVO> imges);

	public boolean attachFiles(int experimentId, List<BlobFileVO> files);

	public boolean save(ExperimentVO view) throws ValidateException;

	boolean hasReaderRight(ExperimentVO experiment, UserVO currentUser);

}
