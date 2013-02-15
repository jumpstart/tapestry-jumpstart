package jumpstart.web.model.app.select;

import java.util.ArrayList;
import java.util.List;

import jumpstart.business.domain.security.User;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;

public class UserIdSelectModel extends AbstractSelectModel {
	private List<User> users;

	public UserIdSelectModel(List<User> users) {
		this.users = users;
	}

	@Override
	public List<OptionGroupModel> getOptionGroups() {
		return null;
	}

	@Override
	public List<OptionModel> getOptions() {
		List<OptionModel> options = new ArrayList<OptionModel>();
		for (User user : users) {
			options.add(new OptionModelImpl(user.getLoginId(), user.getId()));
		}
		return options;
	}

}
