package jumpstart.web.encoders.examples;

import java.util.ArrayList;
import java.util.List;

import jumpstart.business.commons.IdVersion;

import org.apache.tapestry5.ValueEncoder;

// This encoder is intended for use storing a list of (id, version) in a single Hidden field:
// - during render, to convert a list of (id, version) to a String to be stored by the Hidden.
// - during form submission, to convert the String back to a list of (id, version) for use server-side.

public class IdVersionsEncoder implements ValueEncoder<List<IdVersion>> {
	private final String DELIMITER = ":"; 

	@Override
	public String toClient(List<IdVersion> idVersions) {
		String s = "";
		
		for (IdVersion idVersion : idVersions) {
			s += idVersion.getId() + DELIMITER + idVersion.getVersion() + DELIMITER;
		}
		
		return s;
	}

	@Override
	public List<IdVersion> toValue(String idVersionsAsString) {
		List<IdVersion> idVersions = new ArrayList<IdVersion>();

		String[] chunks = idVersionsAsString.split(DELIMITER);

		for (int i = 0; i < chunks.length; i = i + 2) {
			Long id = Long.parseLong(chunks[i]);
			Integer version = chunks[i + 1].equals("null") ? null : Integer.parseInt(chunks[i + 1]);
			idVersions.add(new IdVersion(id, version));
		}

		return idVersions;
	}

}
