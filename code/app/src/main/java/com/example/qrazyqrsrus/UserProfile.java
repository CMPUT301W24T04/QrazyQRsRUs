package com.example.qrazyqrsrus;

public class UserProfile {
    private String name;
    private String age;
    private String email;
    private boolean geolocation_on;

    // Empty constructor is required for Firestore data mapping
    public UserProfile() {
    }

    // Full constructor is provided for creating instances if needed
    public UserProfile(String name, String age, String email, boolean geolocation_on) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.geolocation_on = geolocation_on;
    }

    // Standard getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Boolean getter is prefixed with 'is' which is standard Java convention
    public boolean isGeolocationOn() {
        return geolocation_on;
    }

    public void setGeolocationOn(boolean geolocation_on) {
        this.geolocation_on = geolocation_on;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", email='" + email + '\'' +
                ", geolocation_on=" + geolocation_on +
                '}';
    }
}
