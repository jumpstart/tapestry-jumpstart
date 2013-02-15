package jumpstart.web.pages.examples.ajax;

import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;

import jumpstart.business.commons.IdVersion;
import jumpstart.business.domain.person.Person;
import jumpstart.business.domain.person.Regions;
import jumpstart.business.domain.person.iface.IPersonFinderServiceLocal;
import jumpstart.util.ExceptionUtil;
import jumpstart.web.encoders.examples.IdVersionsEncoder;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

public class AjaxFormLoopTailored1 {
	static private final int MAX_RESULTS = 30;

	// Screen fields

	@Property
	private List<PersonHolder> personHolders;

	private PersonHolder personHolder;

	@Property
	private final PersonHolderEncoder personHolderEncoder = new PersonHolderEncoder();

	// A snapshot of the (id, version) of each person displayed. On submit, we use it to determine which persons you
	// removed.
	@Property
	private List<IdVersion> personsDisplayed;

	@Property
	private final IdVersionsEncoder personsDisplayedEncoder = new IdVersionsEncoder();

	@Property
	private final String BAD_NAME = "Acme";

	// Work fields

	private List<Person> personsInDB;

	private boolean inFormSubmission;

	private List<PersonHolder> personHoldersSubmitted;

	private List<PersonHolder> personHoldersReconstructed;

	private List<Person> personsToCreate;

	// Other pages

	@InjectPage
	private AjaxFormLoopTailored2 page2;

	// Generally useful bits and pieces

	@InjectComponent
	private Zone personsEditZone;

	@Component(id = "personsEdit")
	private Form form;

	@EJB
	private IPersonFinderServiceLocal personFinderService;

	@Inject
	private Locale currentLocale;

	@Inject
	private Messages messages;

	@Inject
	private Request request;

	// The code

	void onActivate() {
		inFormSubmission = false;
	}

	// Form bubbles up the PREPARE_FOR_RENDER event during form render.

	void onPrepareForRender() {
		inFormSubmission = false;

		// If the page had errors and the user chose to reload it, then we detect that and clear the errors.

		if (personHoldersReconstructed == null) {
			form.clearErrors();
		}

		// If fresh start, populate screen with all persons from the database

		if (form.isValid()) {
			// Get all persons - ask business service to find them (from the database)
			personsInDB = personFinderService.findPersons(MAX_RESULTS);

			// Populate the persons to be edited, and also a snapshot of who is in that list.
			personHolders = new ArrayList<PersonHolder>();

			for (Person personInDB : personsInDB) {
				personHolders.add(new PersonHolder(personInDB.getId(), personInDB.getVersion(), personInDB));
			}
		}

		// Else, we're rendering after a redirect, so populate the screen from the reconstructed values

		else {
			personHolders = new ArrayList<PersonHolder>(personHoldersReconstructed);
		}

		personsDisplayed = new ArrayList<IdVersion>();

		for (PersonHolder personHolder : personHolders) {
			if (personHolder.getId() != null) {
				personsDisplayed.add(new IdVersion(personHolder.getId(), personHolder.getVersion()));
			}
		}
	}

	PersonHolder onAddRow() {
		// Return a skeleton person holder which AjaxFormLoop will overwrite.
		// We need a unique id so that if you remove it later we identify which one you removed and flag it removed.
		// We use a negative id so it doesn't clash with the id of an existing person.
		return new PersonHolder(0 - System.nanoTime(), null, new Person());
	}

	void onRemoveRow(PersonHolder personHolder) {
		// Nothing to do.
	}

	// Form bubbles up the PREPARE_FOR_SUBMIT event during form submission.

	void onPrepareForSubmit() {
		inFormSubmission = true;
		personHoldersSubmitted = new ArrayList<PersonHolder>();
		personsDisplayed = new ArrayList<IdVersion>();

		// Get all persons - ask business service to find them (from the database)
		personsInDB = personFinderService.findPersons(MAX_RESULTS);
	}

	void onValidateFromPersonsEdit() {

		// Reconstruct the displayed list of persons, creating dummies in place of the removed ones.

		personHoldersReconstructed = new ArrayList<PersonHolder>();

		for (IdVersion personDisplayed : personsDisplayed) {
			boolean missing = true;

			for (PersonHolder personHolderSubmitted : personHoldersSubmitted) {
				if (personHolderSubmitted.getId().equals(personDisplayed.getId())) {
					personHoldersReconstructed.add(personHolderSubmitted);
					missing = false;
					break;
				}
			}

			if (missing) {
				// Create a dummy person that won't fail field-level validation if we have to redisplay the list.
				Person dummy = new Person("dummy", "dummy", Regions.EAST_COAST, new Date());
				// Add it the list, with "removed" flagged
				PersonHolder ph = new PersonHolder(personDisplayed.getId(), personDisplayed.getVersion(), dummy, true);
				personHoldersReconstructed.add(ph);
			}
		}

		// Add in the newly-added persons

		for (PersonHolder personHolderSubmitted : personHoldersSubmitted) {
			if (personHolderSubmitted.getId() < 0) {
				boolean missing = true;

				for (IdVersion personDisplayed : personsDisplayed) {
					if (personDisplayed.getId().equals(personHolderSubmitted.getId())) {
						missing = false;
						break;
					}
				}

				if (missing) {
					personHoldersReconstructed.add(personHolderSubmitted);
				}
			}
		}

		// If any server-side validation errors detected so far, return.

		if (form.getHasErrors()) {
			return;
		}

		personsToCreate = new ArrayList<Person>();

		// Figure out which persons to create.

		for (PersonHolder holder : personHoldersReconstructed) {
			if (holder.getId() < 0) {
				if (!holder.isRemoved()) {
					personsToCreate.add(holder.getPerson());
				}
			}
		}

		// Simulate a server-side validation error: return error if anyone's first name is BAD_NAME.

		for (PersonHolder holder : personHoldersReconstructed) {
			if (holder.getPerson().getFirstName() != null && holder.getPerson().getFirstName().equals(BAD_NAME)) {
				form.recordError("First name cannot be " + BAD_NAME + ".");
				return;
			}
		}

		try {
			// In a real application you would persist them to the database instead of printing them.
			System.out.println(">>> personsToCreate = " + personsToCreate);
			// personManagerService.createPersons(personsToCreate);
		}
		catch (Exception e) {
			// Display the cause. In a real system we would try harder to get a user-friendly message.
			form.recordError(ExceptionUtil.getRootCauseMessage(e));
			return;
		}

	}

	Object onSuccess() {
		page2.set(personsToCreate);
		return page2;
	}

	Object onFailure() {

		if (request.isXHR()) {
			return personsEditZone.getBody();
		}
		else {
			// Not an AJAX request, so don't bother. Just refresh the screen and it will display "JavaScript required".
			return onRefresh();
		}
	}

	Object onRefresh() {
		form.clearErrors();

		return request.isXHR() ? personsEditZone.getBody() : null;
	}

	// The AjaxFormLoop component will automatically call this for every row as it is rendered.

	public PersonHolder getPersonHolder() {
		return personHolder;
	}

	// The AjaxFormLoop component will automatically call this for every row on submit.

	public void setPersonHolder(PersonHolder personHolder) {
		this.personHolder = personHolder;

		if (inFormSubmission) {
			personHoldersSubmitted.add(personHolder);
		}
	}

	// We use PersonHolder to hold the person and any extra info we need. Its id field allows us to distinguish which persons 
	// you have added and/or removed, and which persons we started with even if they have been deleted from the database by others.

	public class PersonHolder {
		private Long id;
		private Integer version;
		private Person person;
		private boolean removed;

		public String toString() {
			final String DIVIDER = ", ";

			StringBuilder buf = new StringBuilder();
			buf.append(this.getClass().getSimpleName() + ": ");
			buf.append("[");
			buf.append("id=" + id + DIVIDER);
			buf.append("version=" + version + DIVIDER);
			buf.append("person=" + person + DIVIDER);
			buf.append("removed=" + removed);
			buf.append("]");
			return buf.toString();
		}

		PersonHolder(Long id, Integer version, Person person) {
			this(id, version, person, false);
		}

		PersonHolder(Long id, Integer version, Person person, boolean removed) {
			if (person == null) {
				throw new IllegalArgumentException(id + ", " + person);
			}

			this.id = id;
			this.version = version;
			this.person = person;
			this.removed = removed;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Integer getVersion() {
			return version;
		}

		public void setVersion(Integer version) {
			this.version = version;
		}

		public Person getPerson() {
			return person;
		}

		public boolean isRemoved() {
			return removed;
		}

		public void setRemoved(boolean removed) {
			this.removed = removed;
		}
	}

	// This encoder is used by our AjaxFormLoop:
	// - during render, to convert each person holder to an id (AjaxFormLoop then stores the ids in the form, hidden).
	// - during form submission, to convert each id back to a person holder which it puts in our personHolder field.
	// AjaxFormLoop will overwrite several fields of the person returned.

	private class PersonHolderEncoder implements ValueEncoder<PersonHolder> {

		@Override
		public String toClient(PersonHolder personHolder) {
			Long id = personHolder.getId();
			return id == null ? null : id.toString();
		}

		@Override
		public PersonHolder toValue(String idAsString) {
			PersonHolder holder = null;
			Long id = new Long(idAsString);

			if (id < 0) {
				holder = new PersonHolder(id, null, new Person());
			}
			else {
				holder = findPersonHolder(id);

				// If person has since been deleted from the DB. Create a skeleton person holder.
				if (holder == null) {
					holder = new PersonHolder(id, null, new Person());
				}
			}

			// AjaxFormLoop will overwrite several fields of the person holder returned.
			return holder;
		}

		private PersonHolder findPersonHolder(Long id) {

			// If in submit, we could find the person in the database but it's cheaper to search the list we got in
			// onPrepareForSubmit().

			if (inFormSubmission) {
				for (Person person : personsInDB) {
					if (person.getId().equals(id)) {
						return new PersonHolder(id, person.getVersion(), person);
					}
				}
			}

			// Else, find the person in the database.

			else {
				Person person = personFinderService.findPerson(id);
				if (person != null) {
					return new PersonHolder(id, person.getVersion(), person);
				}
			}

			return null;
		}

	}

	public boolean isNewPerson() {
		return personHolder.getId() < 0;
	}

	public String getPersonRegion() {
		if (personHolder.getPerson().getRegion() == null) {
			return "";
		}
		else {
			// Follow the same naming convention that the Select component uses
			return messages.get(Regions.class.getSimpleName() + "." + personHolder.getPerson().getRegion().name());
		}
	}

	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
	}

	public String getHideIfRemoved() {
		return personHolder.isRemoved() ? "display: none;" : "";
	}

}
