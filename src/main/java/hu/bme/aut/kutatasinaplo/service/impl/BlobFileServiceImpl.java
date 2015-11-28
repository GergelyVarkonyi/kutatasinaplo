package hu.bme.aut.kutatasinaplo.service.impl;

import hu.bme.aut.kutatasinaplo.model.BlobFile;
import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.service.BlobFileService;
import hu.bme.aut.kutatasinaplo.service.ExperimentService;

import java.util.Iterator;
import java.util.List;

import com.google.inject.Inject;

public class BlobFileServiceImpl extends AbstractEntityServiceImpl<BlobFile> implements BlobFileService {

	@Inject
	private ExperimentService experimentService;

	@Override
	protected Class<BlobFile> getEntityClass() {
		return BlobFile.class;
	}

	@Override
	public boolean deleteAttachment(int experimentId, int id) {
		Experiment experiment = experimentService.loadById(experimentId);
		List<BlobFile> attachments = experiment.getAttachments();
		Iterator<BlobFile> iterator = attachments.iterator();
		while (iterator.hasNext()) {
			BlobFile file = iterator.next();
			if (file.getId() == id) {
				iterator.remove();
			}
		}

		return experimentService.save(experiment);
	}

	@Override
	public boolean deleteImage(int experimentId, int id) {
		Experiment experiment = experimentService.loadById(experimentId);
		List<BlobFile> images = experiment.getImages();
		Iterator<BlobFile> iterator = images.iterator();
		while (iterator.hasNext()) {
			BlobFile file = iterator.next();
			if (file.getId() == id) {
				iterator.remove();
			}
		}

		return experimentService.save(experiment);
	}

}
