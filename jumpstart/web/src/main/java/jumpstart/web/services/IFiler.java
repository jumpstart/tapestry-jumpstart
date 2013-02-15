package jumpstart.web.services;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.upload.services.UploadedFile;

public interface IFiler {

	public abstract String save(UploadedFile uploadedFile, Messages messages) throws Exception;

	public abstract long getFileSizeMax();

}
