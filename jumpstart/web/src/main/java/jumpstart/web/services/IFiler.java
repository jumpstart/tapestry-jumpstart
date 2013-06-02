package jumpstart.web.services;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.upload.services.UploadedFile;

public interface IFiler {

	String save(UploadedFile uploadedFile, Messages messages) throws Exception;

	long getFileSizeMax();

}
