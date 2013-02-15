// Based on org.apache.tapestry5.integration.app1.pages.TreeDemo

package jumpstart.web.model.examples.tree;

import java.util.List;
import java.util.UUID;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

public class Stuff {

	public static final Stuff ROOT = new Stuff("<root>");

	static {
		ROOT.addChild(new Stuff("Pets").addChildrenNamed("Oscar", "Gromit", "Max", "Roger", "Cooper"));

		ROOT.addChild(new Stuff("Games").addChild(
				new Stuff("Board Games").addChildrenNamed("Settlers of Catan", "Agricola", "Ra", "Risk", "Dvonn"))
				.addChild(new Stuff("Card Games").addChildrenNamed("Magic the Gathering", "Dominion", "Mu")));

		Stuff numbers = new Stuff("Numbers");
		for (int i = 0; i < 10000; i++) {
			numbers.addChild(new Stuff(Integer.toString(i)));
		}
		ROOT.addChild(numbers);
	}

	public final String uuid = UUID.randomUUID().toString();
	public final String name;
	public List<Stuff> children;

	public Stuff(String name) {
		this.name = name;
	}

	public Stuff addChild(Stuff child) {
		if (children == null) {
			children = CollectionFactory.newList();
		}

		children.add(child);

		return this;
	}

	public Stuff addChildrenNamed(String... names) {
		for (String name : names) {
			addChild(new Stuff(name));
		}

		return this;
	}

	public Stuff searchSubTree(String uuid) {

		if (this.uuid.equals(uuid)) {
			return this;
		}

		for (Stuff child : children) {
			Stuff match = child.searchSubTree(uuid);

			if (match != null) {
				return match;
			}
		}

		return null;
	}

}
