package hu.bme.aut.kutatasinaplo.service;

import hu.bme.aut.kutatasinaplo.model.AbstractEntity;

import java.util.List;

public interface AbstractEntityService<T extends AbstractEntity> {

	public boolean save(int id);

	public boolean delete(int id);

	public boolean save(T entity);

	public T loadById(int id);

	public List<T> loadByIds(List<Integer> ids);

	public List<T> loadAll();

}
