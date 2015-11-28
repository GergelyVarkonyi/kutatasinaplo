package hu.bme.aut.kutatasinplo.security;

import hu.bme.aut.kutatasinaplo.model.AbstractEntity;
import hu.bme.aut.kutatasinaplo.model.Experiment;
import hu.bme.aut.kutatasinaplo.model.Role;
import hu.bme.aut.kutatasinaplo.model.User;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class SecurityUtils {
	public enum Action {
		SAVE_EXPERIMENT,
		CREATE_EXPERIMENT,
		DELETE_EXPERIMENT,
		ADD_PARTICIPANT,
		UPLOAD_FILE,
		DELETE_FILE;
	}

	public static boolean hasPermission(Action action, User user, AbstractEntity entity) {
		if (isAdmin(user)) {
			return true;
		}

		switch (action) {
		case ADD_PARTICIPANT:
		case DELETE_EXPERIMENT:
			if (isUser(user) && isOwner(user, entity)) {
				return true;
			}
		case CREATE_EXPERIMENT:
			if (isUser(user)) {
				return true;
			}
		case DELETE_FILE:
		case SAVE_EXPERIMENT:
		case UPLOAD_FILE:
			if (isUser(user) && (isOwner(user, entity) || isParticipant(user, entity))) {
				return true;
			}
		default:
			return false;
		}
	}

	private static boolean isAdmin(User user) {
		return user != null && user.getRole() == Role.ADMIN;
	}

	private static boolean isUser(User user) {
		return user != null && user.getRole() == Role.USER;
	}

	private static boolean isOwner(User user, AbstractEntity experiment) {
		return experiment != null && experiment instanceof Experiment && ((Experiment) experiment).getOwner().getId() == user.getId();
	}

	private static boolean isParticipant(User user, AbstractEntity entity) {
		if (entity != null && entity instanceof Experiment) {
			Experiment experiment = ((Experiment) entity);
			List<User> participants = experiment.getParticipants();
			if (participants != null) {
				List<Integer> participantIds = Lists.transform(participants, new Function<User, Integer>() {
					@Override
					public Integer apply(User input) {
						return input.getId();
					}
				});
				if (participantIds.contains(user.getId())) {
					return true;
				}
			}
		}
		return false;
	}
}
