package jumpstart.web.model.app.select;

import java.util.ArrayList;
import java.util.List;

import jumpstart.business.domain.security.Role;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.util.AbstractSelectModel;

public class RoleIdSelectModel extends AbstractSelectModel {
	private List<Role> roles;

	public RoleIdSelectModel(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public List<OptionGroupModel> getOptionGroups() {
		return null;
	}

	@Override
	public List<OptionModel> getOptions() {
		List<OptionModel> options = new ArrayList<OptionModel>();
		for (Role role : roles) {
			options.add(new OptionModelImpl(role.getName(), role.getId()));
		}
		return options;
	}

}
