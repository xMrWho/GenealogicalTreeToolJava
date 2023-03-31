package whocoding.familytree.classes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 
 * Personen Objekt --> Die Personen in der Datenbank werden als Personen Objekt
 * in der
 * Anwendung umgesetzt
 * 
 * @author MrWho97
 *
 */
public class Person {
	// To get the Person uuuid --> UUID is primary key in database
	private UUID uuid;

	/**
	 * Strings --> Infos about a person
	 */
	private String name, firstname, gender, birthday, deathday, data;

	// save the age of a person
	private int age;

	// save the relationshipps of a person
	private ArrayList<Relationship> relationships;

	/**
	 * Constructor with parameter
	 * 
	 * @param uuid
	 * @param name
	 * @param firstname
	 * @param gender
	 * @param birthday
	 * @param deathday
	 * @param data
	 * @param age
	 */
	public Person(UUID uuid, String name, String firstname, String gender, String birthday, String deathday,
			String data, int age) {
		super();
		this.uuid = uuid;
		this.name = name;
		this.firstname = firstname;
		this.gender = gender;
		this.birthday = birthday;
		this.deathday = deathday;
		this.data = data;
		this.age = age;
		this.relationships = new ArrayList<Relationship>();
	}

	/**
	 * constructor without params
	 */
	public Person() {
		this(null, null, null, null, null, null, null, 0);
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the birthday
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the deathday
	 */
	public String getDeathday() {
		return deathday;
	}

	/**
	 * @param deathday the deathday to set
	 */
	public void setDeathday(String deathday) {
		this.deathday = deathday;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the relationships
	 */
	public ArrayList<Relationship> getRelationships() {
		return relationships;
	}

	/**
	 * @param relationships the relationships to set
	 */
	public void setRelationships(ArrayList<Relationship> relationships) {
		this.relationships = relationships;
	}

	/**
	 * 
	 * @param r_uuid
	 * @return a relationship with the given r_uuid that is in the relationship list
	 *         of a person
	 */
	public Relationship getRelationship(String r_uuid) {
		for (Relationship rs : this.relationships) {
			if (rs.getUuid().toString().equals(r_uuid)) {

				return rs;

			}
		}
		return null;
	}

	/**
	 * 
	 * @param partner
	 * @return the relationship with the given partner
	 */
	public Relationship getRelationship(Person partner) {
		for (Relationship rs : this.relationships) {
			if (rs.getPartner1().equals(partner)) {

				return rs;

			} else if (rs.getPartner0().equals(partner)) {

				return rs;

			}
		}
		return null;
	}

	/**
	 * adds a new Relationship with a partner
	 * 
	 * @param partner
	 */
	public void addRelationship(Relationship r) {
		this.relationships.add(r);
	}

	/**
	 * Remove a relationship with a partner
	 * 
	 * @param partner
	 */
	public void removeRelationship(Person partner) {
		for (Relationship rs : this.relationships) {
			if (rs.getPartner1().equals(partner)) {
				this.relationships.remove(rs);

			}
		}
	}

	/**
	 * calculates the age of a person
	 * 
	 * @return the age
	 */
	public int calculateAge() {
		int age = -1;
		if (this.birthday != null && !(this.birthday.equals("NULL"))) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			LocalDate bday, day;

			bday = LocalDate.parse(birthday, formatter);
			// System.err.println(bday.toString());

			if (this.deathday != null && !(this.deathday.equals("NULL"))) {
				day = LocalDate.parse(deathday, formatter);
			} else {
				day = LocalDate.now();// For reference
			}
			// System.err.println(day.toString());

			if (day.getMonthValue() >= bday.getMonthValue()) {
				if (day.getDayOfMonth() >= bday.getDayOfMonth()) {
					age = day.getYear() - bday.getYear();
				} else {
					age = day.getYear() - bday.getYear() - 1;
				}
			} else {
				age = day.getYear() - bday.getYear() - 1;
			}

		} else {
			age = -1;
		}

		return age;
	}

	/**
	 * @return the complete name of a person
	 */
	@Override
	public String toString() {
		return firstname + " " + name;
	}

}
