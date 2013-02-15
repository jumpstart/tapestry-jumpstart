package jumpstart.web.pages.examples.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ejb.EJB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.web.model.examples.ws.Person01;
import jumpstart.web.model.examples.ws.Persons01;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.services.Response;

public class PersonsFind {
	static private final int MAX_RESULTS = 30;

	// Query string parameters

	@ActivationRequestParameter(value = "namefilter")
	private String partialName;

	// Generally useful bits and pieces

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	// The code

	StreamResponse onActivate() {

		return new StreamResponse() {
			private InputStream inputStream;

			@Override
			public void prepareResponse(Response response) {
				Persons01 persons = null;

				if (partialName == null) {
					persons = findPersons();
				}
				else {
					persons = findPersons(partialName);
				}

				try {
					JAXBContext context = JAXBContext.newInstance(persons.getClass());
					Marshaller marshaller = context.createMarshaller();
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					marshaller.marshal(persons, outputStream);

					inputStream = new ByteArrayInputStream(outputStream.toByteArray());

					// Set content length to prevent chunking - see
					// http://tapestry-users.832.n2.nabble.com/Disable-Transfer-Encoding-chunked-from-StreamResponse-td5269662.html#a5269662
					response.setHeader("Content-Length", "" + outputStream.size());
				}
				catch (JAXBException e) {
					throw new IllegalStateException(e);
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

	public Persons01 findPersons() {

		// Ask business layer to find the persons

		List<Person> persons = personFinderService.findPersons(MAX_RESULTS);

		// Create a response object

		Persons01 response = new Persons01();

		// Populate the response object from the list

		for (Person person : persons) {
			Person01 person01 = new Person01(person.getId(), person.getFirstName(), person.getLastName(),
					person.getRegion(), person.getStartDate());
			response.add(person01);
		}

		return response;
	}

	public Persons01 findPersons(String partialName) {

		// Ask business layer to find the persons

		List<Person> persons = personFinderService.findPersons(partialName, MAX_RESULTS);

		// Create a response object

		Persons01 response = new Persons01();

		// Populate the response object from the list

		for (Person person : persons) {
			Person01 person01 = new Person01(person.getId(), person.getFirstName(), person.getLastName(),
					person.getRegion(), person.getStartDate());
			response.add(person01);
		}

		return response;
	}
}
