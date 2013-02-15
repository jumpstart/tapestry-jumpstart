package jumpstart.web.pages.examples.navigation;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Response;

public class ReturnTypes1 {

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String message;

	void onReturnVoid() {
		message = "You chose to return void.";
	}

	Object onReturnNull() {
		message = "You chose to return null.";
		return null;
	}

	Class<ReturnTypesClass> onReturnClass() {
		return ReturnTypesClass.class;
	}

	@InjectPage
	private ReturnTypesPageObject pageObject;

	Object onReturnPageObject() {
		pageObject.set("Hello");
		return pageObject;
	}

	String onReturnString() {
		return "examples/navigation/ReturnTypesString";
	}

	@Inject
	private PageRenderLinkSource pageRenderLinkSource;

	Link onReturnLink() {
		String parameters = "Howdy";
		Link link = pageRenderLinkSource.createPageRenderLinkWithContext(ReturnTypesLink.class, parameters);
		return link;
	}

	StreamResponse onReturnStreamResponse() {
		return new StreamResponse() {
			InputStream inputStream;

			@Override
			public void prepareResponse(Response response) {
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				inputStream = classLoader.getResourceAsStream("jumpstart/web/text/examples/ReturnTypeText.txt");

				// Set content length to prevent chunking - see
				// http://tapestry-users.832.n2.nabble.com/Disable-Transfer-Encoding-chunked-from-StreamResponse-td5269662.html#a5269662

				try {
					response.setHeader("Content-Length", "" + inputStream.available());
				}
				catch (IOException e) {
					// Ignore the exception in this simple example.
				}
			}

			@Override
			public String getContentType() {
				return "text/plain";
			}

			@Override
			public InputStream getStream() throws IOException {
				return inputStream;
			}
		};
	}
	
	Object onReturnJSONObject() {
		JSONObject myData = new JSONObject();

		myData.put("id", 42);
		myData.put("name", "Arthur Dent");

		return myData;
	}

	URL onReturnURL() throws MalformedURLException {
		return new URL("http://tapestry.apache.org");
	}

	boolean onReturnTrue() {
		message = "You chose to return true.";
		return true;
	}

	boolean onReturnFalse() {
		message = "You chose to return false.";
		return false;
	}
}
