package hu.bme.aut.kutatasinaplo.service;

import hu.bme.aut.kutatasinaplo.model.BlobFile;

public interface BlobFileService extends AbstractEntityService<BlobFile> {
	public boolean deleteImage(int experimentId, int id);

	public boolean deleteAttachment(int experimentId, int id);
}
