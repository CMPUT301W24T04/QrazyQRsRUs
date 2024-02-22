package com.example.qrazyqrsrus;
public class UserProfile {
    private String fullName;
    private String age;
    private String emailAddress;

    // Constructor
    public UserProfile(String fullName, String age, String emailAddress) {
        this.fullName = fullName;
        this.age = age;
        this.emailAddress = emailAddress;
    }

    // Getters
    public String getFullName() {
        return fullName;
    }

    public String getAge() {
        return age;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    // Setters
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    // ToString method to print the user profile details
    @Override
    public String toString() {
        return "UserProfile{" +
                "fullName='" + fullName + '\'' +
                ", age='" + age + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
