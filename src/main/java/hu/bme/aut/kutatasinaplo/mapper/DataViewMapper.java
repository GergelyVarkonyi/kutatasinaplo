package hu.bme.aut.kutatasinaplo.mapper;

import hu.bme.aut.kutatasinaplo.model.AbstractEntity;
import hu.bme.aut.kutatasinaplo.model.BlobFile;
import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.model.ExperimentType;
import hu.bme.aut.kutatasinaplo.model.KeyValuePair;
import hu.bme.aut.kutatasinaplo.model.Project;
import hu.bme.aut.kutatasinaplo.model.Url;
import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.view.model.BlobFileVO;
import hu.bme.aut.kutatasinaplo.view.model.ExperimentVO;
import hu.bme.aut.kutatasinaplo.view.model.IdVO;
import hu.bme.aut.kutatasinaplo.view.model.KeyValuePairVO;
import hu.bme.aut.kutatasinaplo.view.model.ProjectVO;
import hu.bme.aut.kutatasinaplo.view.model.UrlVO;
import hu.bme.aut.kutatasinaplo.view.model.UserVO;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class DataViewMapper {

	public Experiment map(ExperimentVO view) {
		if (isViewMappable(view)) {
			Experiment model = new Experiment();

			model.setId(view.getId());
			model.setName(view.getName());
			model.setOwner(map(view.getOwner()));
			model.setType(view.isPublic() ? ExperimentType.PUBLIC : ExperimentType.PRIVATE);
			model.setDescription(view.getDescription());

			List<UserVO> participants = view.getParticipants();
			if (participants != null) {
				model.setParticipants(Lists.transform(participants, new Function<UserVO, User>() {

					@Override
					public User apply(UserVO input) {
						return map(input);
					}
				}));
			}
			List<KeyValuePairVO> urls = view.getUrls();
			if (urls != null) {
				model.setUrls(Lists.transform(urls, new Function<KeyValuePairVO, KeyValuePair>() {

					@Override
					public KeyValuePair apply(KeyValuePairVO input) {
						return map(input);
					}
				}));
			}
			List<BlobFileVO> attachments = view.getAttachments();
			if (attachments != null) {
				model.setAttachments(Lists.transform(attachments, new Function<BlobFileVO, BlobFile>() {

					@Override
					public BlobFile apply(BlobFileVO input) {
						return map(input);
					}
				}));
			}
			List<BlobFileVO> images = view.getImages();
			if (images != null) {
				model.setImages(Lists.transform(images, new Function<BlobFileVO, BlobFile>() {

					@Override
					public BlobFile apply(BlobFileVO input) {
						return map(input);
					}
				}));
			}

			return model;
		} else {
			return null;
		}
	}

	public ExperimentVO map(Experiment model) {
		if (isModelMappable(model)) {
			ExperimentVO view = new ExperimentVO();

			view.setId(model.getId());
			view.setName(model.getName());
			view.setOwner(map(model.getOwner()));
			view.setPublic(model.getType() == ExperimentType.PUBLIC);
			view.setDescription(model.getDescription());

			List<User> participants = model.getParticipants();
			if (participants != null) {
				view.setParticipants(Lists.transform(participants, new Function<User, UserVO>() {

					@Override
					public UserVO apply(User input) {
						return map(input);
					}
				}));
			}
			List<KeyValuePair> urls = model.getUrls();
			if (urls != null) {
				view.setUrls(Lists.transform(urls, new Function<KeyValuePair, KeyValuePairVO>() {

					@Override
					public KeyValuePairVO apply(KeyValuePair input) {
						return map(input);
					}
				}));
			}
			List<BlobFile> attachments = model.getAttachments();
			if (attachments != null) {
				view.setAttachments(Lists.transform(attachments, new Function<BlobFile, BlobFileVO>() {

					@Override
					public BlobFileVO apply(BlobFile input) {
						return map(input);
					}
				}));
			}
			List<BlobFile> images = model.getImages();
			if (images != null) {
				view.setImages(Lists.transform(images, new Function<BlobFile, BlobFileVO>() {

					@Override
					public BlobFileVO apply(BlobFile input) {
						return map(input);
					}
				}));
			}

			return view;
		} else {
			return null;
		}
	}

	public Url map(UrlVO view) {
		if (isViewMappable(view)) {
			Url model = new Url();

			model.setLabel(view.getLabel());
			model.setUrl(view.getUrl());

			return model;
		} else {
			return null;
		}
	}

	public UrlVO map(Url model) {
		if (isModelMappable(model)) {
			UrlVO view = new UrlVO();

			view.setLabel(model.getLabel());
			view.setUrl(model.getUrl());

			return view;
		} else {
			return null;
		}
	}

	public User map(UserVO view) {
		if (isViewMappable(view)) {
			User model = new User();

			model.setId(view.getId());
			model.setName(view.getName());
			model.setEmail(view.getEmail());
			model.setPassword(view.getPwd());
			model.setRole(view.getRole());

			model.setKnowledge(Lists.transform(view.getKnowledge(), new Function<KeyValuePairVO, KeyValuePair>() {

				@Override
				public KeyValuePair apply(KeyValuePairVO input) {
					return map(input);
				}
			}));

			return model;
		} else {
			return null;
		}
	}

	public UserVO map(User model) {
		if (isModelMappable(model)) {
			// Do not set the password!
			UserVO view = new UserVO();

			view.setId(model.getId());
			view.setName(model.getName());
			view.setEmail(model.getEmail());
			view.setRole(model.getRole());

			view.setKnowledge(Lists.transform(model.getKnowledge(), new Function<KeyValuePair, KeyValuePairVO>() {

				@Override
				public KeyValuePairVO apply(KeyValuePair input) {
					return map(input);
				}
			}));

			return view;
		} else {
			return null;
		}
	}

	public Project map(ProjectVO view) {
		if (isViewMappable(view)) {
			Project model = new Project();

			model.setId(view.getId());
			model.setDescription(view.getDescription());
			model.setName(view.getName());
			model.setExperiments(Lists.transform(view.getExperiments(), new Function<ExperimentVO, Experiment>() {

				@Override
				public Experiment apply(ExperimentVO input) {
					return map(input);
				}
			}));
			model.setParticipants(Lists.transform(view.getParticipants(), new Function<UserVO, User>() {

				@Override
				public User apply(UserVO input) {
					return map(input);
				}
			}));

			return model;
		} else {
			return null;
		}
	}

	public ProjectVO map(Project model) {
		if (isModelMappable(model)) {
			ProjectVO view = new ProjectVO();

			view.setId(model.getId());
			view.setDescription(model.getDescription());
			view.setName(model.getName());
			view.setExperiments(Lists.transform(model.getExperiments(), new Function<Experiment, ExperimentVO>() {

				@Override
				public ExperimentVO apply(Experiment input) {
					return map(input);
				}
			}));
			view.setParticipants(Lists.transform(model.getParticipants(), new Function<User, UserVO>() {

				@Override
				public UserVO apply(User input) {
					return map(input);
				}
			}));

			return view;
		} else {
			return null;
		}
	}

	public KeyValuePair map(KeyValuePairVO view) {
		if (isViewMappable(view)) {
			KeyValuePair model = new KeyValuePair();

			model.setId(view.getId());
			model.setKeyOfInstance(view.getKeyOfInstance());
			model.setValue(view.getValue());

			return model;
		} else {
			return null;
		}
	}

	public KeyValuePairVO map(KeyValuePair model) {
		if (isModelMappable(model)) {
			KeyValuePairVO view = new KeyValuePairVO();

			view.setId(model.getId());
			view.setKeyOfInstance(model.getKeyOfInstance());
			view.setValue(model.getValue());

			return view;
		} else {
			return null;
		}
	}

	public BlobFile map(BlobFileVO view) {
		if (isViewMappable(view)) {
			BlobFile model = new BlobFile();

			model.setId(view.getId());
			model.setData(view.getData());
			model.setType(view.getType());
			;
			model.setSize(view.getSize());
			model.setName(view.getName());

			return model;
		} else {
			return null;
		}
	}

	public BlobFileVO map(BlobFile model) {
		if (isModelMappable(model)) {
			BlobFileVO view = new BlobFileVO();

			view.setId(model.getId());
			view.setData(model.getData());
			view.setType(model.getType());
			view.setSize(model.getSize());
			view.setName(model.getName());

			return view;
		} else {
			return null;
		}
	}

	private boolean isModelMappable(AbstractEntity model) {
		return model != null;
	}

	private boolean isViewMappable(Object view) {
		return view != null;
	}

	public static final class IdVOToIntegerFunction implements Function<IdVO, Integer> {
		public static final IdVOToIntegerFunction INSTANCE = new IdVOToIntegerFunction();

		private IdVOToIntegerFunction() {
		}

		@Override
		public Integer apply(IdVO input) {
			return input.getId();
		}
	}
}
