package hu.bme.aut.kutatasinaplo.service.impl;

import hu.bme.aut.kutatasinaplo.model.BlobFile;
import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.model.validate.ValidateException;
import hu.bme.aut.kutatasinaplo.service.AuthService;
import hu.bme.aut.kutatasinaplo.service.BlobFileService;
import hu.bme.aut.kutatasinaplo.service.ExperimentService;
import hu.bme.aut.kutatasinplo.security.SecurityUtils;
import hu.bme.aut.kutatasinplo.security.SecurityUtils.Action;

import java.util.Iterator;
import java.util.List;

import com.google.inject.Inject;

public class BlobFileServiceImpl extends AbstractEntityServiceImpl<BlobFile> implements BlobFileService {

	@Inject
	private ExperimentService experimentService;
	@Inject
	private AuthService authService;

	@Override
	protected Class<BlobFile> getEntityClass() {
		return BlobFile.class;
	}

	@Override
	public boolean deleteAttachment(int experimentId, int id) {
		Experiment experiment = experimentService.loadById(experimentId);
		User currentUser = authService.getCurrentUser();
		if (!SecurityUtils.hasPermission(Action.DELETE_FILE, currentUser, experiment)) {
			return false;
		}

		List<BlobFile> attachments = experiment.getAttachments();
		Iterator<BlobFile> iterator = attachments.iterator();
		while (iterator.hasNext()) {
			BlobFile file = iterator.next();
			if (file.getId() == id) {
				iterator.remove();
			}
		}

		try {
			return experimentService.save(experiment);
		} catch (ValidateException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteImage(int experimentId, int id) {
		Experiment experiment = experimentService.loadById(experimentId);
		User currentUser = authService.getCurrentUser();
		if (!SecurityUtils.hasPermission(Action.DELETE_FILE, currentUser, experiment)) {
			return false;
		}

		List<BlobFile> images = experiment.getImages();
		Iterator<BlobFile> iterator = images.iterator();
		while (iterator.hasNext()) {
			BlobFile file = iterator.next();
			if (file.getId() == id) {
				iterator.remove();
			}
		}

		try {
			return experimentService.save(experiment);
		} catch (ValidateException e) {
			e.printStackTrace();
			return false;
		}
	}

}
