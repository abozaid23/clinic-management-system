package models;

import java.io.Serializable;

public abstract class Person implements Serializable {
    protected String id;
    protected String name;
    protected String phoneNumber;

    public Person(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setName(String name) { this.name = name; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public abstract String getDetails();
}
