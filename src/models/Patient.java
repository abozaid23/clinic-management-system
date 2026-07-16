package models;

public class Patient extends Person {
    private int age;

    public Patient(String id, String name, int age, String phoneNumber) {
        super(id, name, phoneNumber);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String getDetails() {
        return "Patient ID: " + id + ", Name: " + name + ", Age: " + age + ", Phone Number: " + phoneNumber;
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}
