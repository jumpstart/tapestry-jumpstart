package jumpstart.web.pages.examples.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.web.model.examples.ws.Person01;
import jumpstart.web.model.examples.ws.PersonFind01Response;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

public class PersonFind01 {

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	// The code

	StreamResponse onActivate(final Long personId) {

		return new StreamResponse() {
			private InputStream inputStream;

			@Override
			public void prepareResponse(Response response) {
				PersonFind01Response personFind01Response = findPerson(personId);

				if (personFind01Response == null) {
					try {
						response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
					}
					catch (IOException e) {
						throw new RuntimeException("Failed to redirect?", e);
					}
				}

				else {
					try {
						JAXBContext context = JAXBContext.newInstance(personFind01Response.getClass());
						Marshaller marshaller = context.createMarshaller();
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						marshaller.marshal(personFind01Response, outputStream);

						inputStream = new ByteArrayInputStream(outputStream.toByteArray());

						// Set content length to prevent chunking - see
						// http://tapestry-users.832.n2.nabble.com/Disable-Transfer-Encoding-chunked-from-StreamResponse-td5269662.html#a5269662
						response.setHeader("Content-Length", "" + outputStream.size());
					}
					catch (JAXBException e) {
						throw new IllegalStateException(e);
					}
				}

			}

			@Override
			public String getContentType() {
				return "text/xml";
			}

			@Override
			public InputStream getStream() throws IOException {
				return inputStream;
			}
		};

	}

	public PersonFind01Response findPerson(Long personId) {
		PersonFind01Response response = null;

		// Ask business layer to find the person

		Person person = personFinderService.findPerson(personId);

		// If person exists, create a response object

		if (person != null) {
			Person01 person01 = new Person01(person.getId(), person.getFirstName(), person.getLastName(),
					person.getRegion(), person.getStartDate());
			response = new PersonFind01Response(person01);
		}

		return response;
	}
}
