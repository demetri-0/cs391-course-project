import java.util.ArrayList;
import java.util.List;

public class CustomerProfile {
    public String n;
    public String phone;
    public String email;
    public String street;
    public String city;
    public String state;
    public String zip;
    public String loyaltyLevel;
    public int points;
    public boolean isStudent;
    public boolean wantsTexts;
    public boolean banned;
    public List<String> notes = new ArrayList<String>();

    public CustomerProfile(String n, String phone, String email, String street, String city, String state, String zip,
            String loyaltyLevel, int points, boolean isStudent, boolean wantsTexts, boolean banned) {
        this.n = n;
        this.phone = phone;
        this.email = email;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.loyaltyLevel = loyaltyLevel;
        this.points = points;
        this.isStudent = isStudent;
        this.wantsTexts = wantsTexts;
        this.banned = banned;
    }

    public String getFullAddress() {
        return street + ", " + city + ", " + state + " " + zip;
    }

    public void addNote(String note) {
        notes.add(note);
    }

    public String tiny() {
        return n + " / " + phone;
    }

    public void updateAddress(String street, String city, String state, String zip) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getLoyaltyLevel() {
        return loyaltyLevel;
    }

    public void setLoyaltyLevel(String loyaltyLevel) {
        this.loyaltyLevel = loyaltyLevel;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean student) {
        isStudent = student;
    }

    public boolean isWantsTexts() {
        return wantsTexts;
    }

    public void setWantsTexts(boolean wantsTexts) {
        this.wantsTexts = wantsTexts;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
