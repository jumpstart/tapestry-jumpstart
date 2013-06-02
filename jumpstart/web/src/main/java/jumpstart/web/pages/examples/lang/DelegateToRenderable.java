package jumpstart.web.pages.examples.lang;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.Renderable;
import org.apache.tapestry5.annotations.Property;

public class DelegateToRenderable {

	@Property(write = false)
	private final Renderable a = new Renderable() {
		public void render(MarkupWriter writer) {
			writer.element("div", "style", "margin: 2px; padding: 10px; background-color: #f8f8f8;");
			writer.write("This is Renderable A.");
			writer.end();
		}
	};

	@Property(write = false)
	private final Renderable b = new Renderable() {
		public void render(MarkupWriter writer) {
			writer.element("div", "style", "margin: 2px; padding: 10px; background-color: #f0f0f0;");
			writer.write("This is Renderable B.");
			writer.end();
		}
	};

	@Property(write = false)
	private final Renderable c = new Renderable() {
		public void render(MarkupWriter writer) {
			writer.element("div", "style", "margin: 2px; padding: 10px; background-color: #e8e8e8;");
			writer.write("This is Renderable C.");
			writer.end();
		}
	};

}
