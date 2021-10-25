package com.envelopepushers.envote;

public class EmailReceiver {

    private String email;
    private String firstName;
    private String lastName;
    private String party;

    /**
     * Receiver Default Constructor
     */
    public EmailReceiver() {
        email = "";
        firstName = "";
        lastName = "";
        party = "";
    }

    /**
     * Receiver Constructor
     */
    public EmailReceiver(String email, String fullName, String party) {
        this.email = email;
        this.firstName = fullName.split(" ")[0];
        this.lastName = fullName.split(" ")[1];
        this.party = party;
    }

    /**
     * Receiver Constructor
     */
    public EmailReceiver(String email, String firstName, String lastName, String party) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.party = party;
    }

    /**
     * Gets the email
     * @return email String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gwtsa the first name
     * @return f name string
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set First Name
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name
     * @return l name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the Last Name
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the persons party
     * @return party
     */
    public String getParty() {
        return party;
    }

    /**
     * Sets the party
     * @param party
     */
    public void setParty(String party) {
        this.party = party;
    }

    /**
     * Gets the Full Name
     * @return full name
     */
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    /**
     * Sets the full name
     * @param name
     */
    public void setFullName(String name) {
        this.firstName = name.split(" ")[0];
        this.lastName = name.split(" ")[1];
    }

    @Override
    public String toString() {
        return "EmailReceiver{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", party='" + party + '\'' +
                '}';
    }
}
