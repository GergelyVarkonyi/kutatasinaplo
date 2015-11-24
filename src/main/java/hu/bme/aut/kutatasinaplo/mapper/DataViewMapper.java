package hu.bme.aut.kutatasinaplo.mapper;

import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.model.ExperimentType;
import hu.bme.aut.kutatasinaplo.model.Url;
import hu.bme.aut.kutatasinaplo.model.User;
import hu.bme.aut.kutatasinaplo.view.model.ExperimentVO;
import hu.bme.aut.kutatasinaplo.view.model.UrlVO;
import hu.bme.aut.kutatasinaplo.view.model.UserVO;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class DataViewMapper {

	public Experiment map(ExperimentVO view) {
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
		List<UrlVO> urls = view.getUrls();
		if (urls != null) {
			model.setUrls(Lists.transform(urls, new Function<UrlVO, Url>() {

				@Override
				public Url apply(UrlVO input) {
					return map(input);
				}
			}));
		}

		return model;
	}

	public ExperimentVO map(Experiment model) {
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
		List<Url> urls = model.getUrls();
		if (urls != null) {
			view.setUrls(Lists.transform(urls, new Function<Url, UrlVO>() {

				@Override
				public UrlVO apply(Url input) {
					return map(input);
				}
			}));
		}

		return view;
	}

	public Url map(UrlVO view) {
		Url model = new Url();

		model.setLabel(view.getLabel());
		model.setUrl(view.getUrl());

		return model;
	}

	public UrlVO map(Url model) {
		UrlVO view = new UrlVO();

		view.setLabel(model.getLabel());
		view.setUrl(model.getUrl());

		return view;
	}

	public User map(UserVO view) {
		User model = new User();

		model.setId(view.getId());
		model.setName(view.getName());
		model.setEmail(view.getEmail());
		model.setPassword(view.getPwd());
		model.setRole(view.getRole());

		return model;
	}

	public UserVO map(User model) {
		// Do not set the password!
		UserVO view = new UserVO();

		view.setId(model.getId());
		view.setName(model.getName());
		view.setEmail(model.getEmail());
		view.setRole(model.getRole());

		return view;
	}

}
