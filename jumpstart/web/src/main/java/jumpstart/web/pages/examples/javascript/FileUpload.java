package jumpstart.web.pages.examples.javascript;

import jumpstart.util.ExceptionUtil;
import jumpstart.web.services.IFiler;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Any;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.upload.components.Upload;
import org.apache.tapestry5.upload.services.UploadedFile;

// The @Import tells Tapestry to put a link to the file in the head of the page so that the browser will pull it in. 
@Import(library = "js_imports/pages/examples/javascript/FileUpload.js")
public class FileUpload {

	private final String demoModeStr = System.getProperty("jumpstart.demo-mode");

	// Screen fields

	@Property
	private UploadedFile file;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String successMessage;

	// Generally useful bits and pieces.
	
	@Inject
	private IFiler filer;

	@Inject
	private ComponentResources componentResources;

	@InjectComponent
	// FIX!
//	private CustomForm uploadForm;
	private Form uploadForm;

	@Inject
	private Messages messages;

	@Inject
	private JavaScriptSupport javaScriptSupport;
	
	@Component(id = "file")
	private Upload fileField;

	@Component
	private Any progress;

	@Component
	private Any result;

	// The code
	
	void afterRender() {
		// Tell Tapestry to add some javascript that sets up our event handling.
		// Tapestry will put it at the end of the page in a section that runs once the DOM has been loaded.
		JSONObject spec = new JSONObject();
		spec.put("fileId", fileField.getClientId());
		spec.put("progressId", progress.getClientId());
		spec.put("resultId", result.getClientId());
		javaScriptSupport.addInitializerCall("fileUpload", spec);
	}

	void onValidateFromUploadForm() {
		try {
			String savedAsFileName = filer.save(file, messages);
			successMessage = "Successfully uploaded file \"" + savedAsFileName + "\".";
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			uploadForm.recordError(ExceptionUtil.getRootCauseMessage(e));
		}
	}

	Object onUploadException(FileUploadException e) {
		// Display the cause. In a real system we would try harder to get a user-friendly message.
		uploadForm.recordError(ExceptionUtil.getRootCauseMessage(e));
		return this;
	}
	
	public String getFileSizeMaxMessage() {
		double fileSizeMaxMB = filer.getFileSizeMax() / 1048576.0;
		return messages.format("file-size-max", (Double) fileSizeMaxMB);
	}
	
	public boolean isDemoMode() {
		return (demoModeStr != null && demoModeStr.equals("true"));
	}

	public String getThisPageName() {
		return componentResources.getPageName();
	}
	
	public long getFileSizeMax() {
		return filer.getFileSizeMax();
	}

}
