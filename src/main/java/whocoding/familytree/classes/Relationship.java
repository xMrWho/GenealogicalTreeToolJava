package whocoding.familytree.classes;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 
 * Beziehungs Objekt --> Die Beziehungen in der Datenbank werden als Beziehungs
 * Objekt in der
 * Anwendung umgesetzt
 * 
 * @author MrWho97
 *
 */
public class Relationship {
	// To get the Relation uuuid --> UUID is primary key in database
	private UUID uuid;

	// To save both partners of the Relationship
	// partner0 --> The person from whom the relationship object was generated
	private Person partner0, partner1;

	// save Infos about Realtionship (and date of Begin/End -> comming soon)
	private String infos, date_begin, date_end;

	// save the children of the Relationship
	private ArrayList<Person> children = new ArrayList<Person>();

	/**
	 * 
	 * Constructor --> Init a new Relationship
	 * 
	 * @param p
	 */
	public Relationship(Person partner1, Person partner2) {
		this.uuid = UUID.randomUUID();

		this.partner0 = partner1;
		this.partner1 = partner2;
	}

	/**
	 * 
	 * Constructor --> Init a new Relationship
	 * 
	 * @param p
	 */
	public Relationship(Person partner1) {
		this.uuid = UUID.randomUUID();

		this.partner0 = partner1;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the partner0
	 */
	public Person getPartner0() {
		return partner0;
	}

	/**
	 * @param partner0 the partner0 to set
	 */
	public void setPartner0(Person partner0) {
		this.partner0 = partner0;
	}

	/**
	 * @return the partner1
	 */
	public Person getPartner1() {
		return partner1;
	}

	/**
	 * @param partner1 the partner1 to set
	 */
	public void setPartner1(Person partner1) {
		this.partner1 = partner1;

		// TODO: Create a Relationship for partner
	}

	/**
	 * @return the infos
	 */
	public String getInfos() {
		return infos;
	}

	/**
	 * @param infos the infos to set
	 */
	public void setInfos(String infos) {
		this.infos = infos;
	}

	/**
	 * @return the date_begin
	 */
	public String getDate_begin() {
		return date_begin;
	}

	/**
	 * @param date_begin the date_begin to set
	 */
	public void setDate_begin(String date_begin) {
		this.date_begin = date_begin;
	}

	/**
	 * @return the date_end
	 */
	public String getDate_end() {
		return date_end;
	}

	/**
	 * @param date_end the date_end to set
	 */
	public void setDate_end(String date_end) {
		this.date_end = date_end;
	}

	/**
	 * @return the children
	 */
	public ArrayList<Person> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(ArrayList<Person> children) {
		this.children = children;
	}

	/**
	 * Add child to the Relationship
	 * 
	 * @param p
	 */
	public void addChild(Person p) {
		this.children.add(p);

	}

	/**
	 * Remove child from the Relationship
	 * 
	 * @param p
	 */
	public void removeChild(Person p) {
		if (this.children.contains(p)) {
			this.children.remove(p);
		}
	}

	/**
	 * @return a string eith infos about the relationship
	 */
	@Override
	public String toString() {
		if (partner0 != null && partner1 != null) {
			return partner0.toString() + " + " + partner1.toString();
		} else if (partner0 != null && partner1 == null) {
			return partner0.toString() + " + " + "Unbekannt";
		} else if (partner0 == null && partner1 != null) {
			return "Unbekannt" + " + " + partner1.toString();
		} else {
			return "Unbekannt";
		}
	}

	/**
	 * 
	 * @param p --> the person that should be ignored
	 * @return name of partner of a person
	 */
	public String toString(Person p) {
		if (partner0 != null && partner1 != null) {
			return partner0.toString() + " | \n" + partner1.toString();
		} else {
			return "Unbekannt";
		}

	}

}
