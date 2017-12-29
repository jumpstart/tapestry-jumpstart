package jumpstart.web.services;

import java.io.File;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.slf4j.Logger;

public class Filer implements IFiler {
	static final String FILE_SEPARATOR = System.getProperty("file.separator");

	@SuppressWarnings("unused")
	private final Logger logger;
	private String dirPathSymbol;
	private String dirPath;
	private String fileSizeMaxSymbol;
	private long fileSizeMax;

	public Filer(Logger logger, String dirPathSymbol, String dirPath, String fileSizeMaxSymbol, long fileSizeMax) {
		super();
		this.logger = logger;
		this.dirPathSymbol = dirPathSymbol;
		this.dirPath = sanitiseDirPath(dirPath);
		this.fileSizeMaxSymbol = fileSizeMaxSymbol;
		this.fileSizeMax = sanitiseFileSizeMax(fileSizeMax);
	}

	@Override
	public String save(UploadedFile uploadedFile, Messages messages) throws Exception {
		try {
			String targetFileName = sanitiseFileName(uploadedFile.getFileName());

			// This check is optional: Error if sanitised file name is different to original file name.

			if (!targetFileName.equals(uploadedFile.getFileName())) {
				// In a real system we would throw a exception of our own
				throw new Exception(messages.format("file-name-illegal-characters", uploadedFile.getFileName()));
			}

			File targetFile = new File(dirPath + targetFileName);

			// This check is optional: Error if the file already exists (else it will be overwritten).

			if (targetFile.exists()) {
				// In a real system we would throw a exception of our own
				throw new Exception(messages.format("file-already-exists", uploadedFile.getFileName(), targetFileName));
			}

			uploadedFile.write(targetFile);
			return targetFileName;
		}
		catch (Exception e) {
			// In a real system we would throw a user-friendly message
			throw e;
		}
	}

	@Override
	public long getFileSizeMax() {
		return fileSizeMax;
	}

	private String sanitiseDirPath(String dirPath) {
		String path = dirPath.trim();

		if (!path.endsWith("/") || path.endsWith("\\") || path.endsWith(":")) {
			path += FILE_SEPARATOR;
		}

		File dir = new File(path);

		if (!dir.exists()) {
			throw new IllegalStateException(
					"File uploads cannot proceed because silly directory specified by system property " + dirPathSymbol
							+ " does not exist. Value = " + path + ".");
		}

		return path;
	}

	private long sanitiseFileSizeMax(long fileSizeMax) {

		if (fileSizeMax <= 10240 || fileSizeMax > 100000000) {
			throw new IllegalStateException(
					"File uploads cannot proceed because silly value found for system property " + fileSizeMaxSymbol
							+ ", value = " + fileSizeMax + ".");
		}

		return fileSizeMax;
	}

	private String sanitiseFileName(String fileName) {
		String s = fileName.replaceAll("[\\:\\*\\?\\<\\>\\|\\'\\\"\\/\\\\]+", "_");
		return s;
	}

}
