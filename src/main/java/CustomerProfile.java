import java.util.ArrayList;
import java.util.List;

public class CustomerProfile {
    private String name;
    private String phone;
    private String email;
    private Address address;
    private String loyaltyLevel;
    private int points;
    private boolean isStudent;
    private boolean wantsTexts;
    private boolean banned;
    private final List<String> notes = new ArrayList<String>();

    public CustomerProfile(String name, String phone, String email, Address address,
            String loyaltyLevel, int points, boolean isStudent, boolean wantsTexts, boolean banned) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.loyaltyLevel = loyaltyLevel;
        this.points = points;
        this.isStudent = isStudent;
        this.wantsTexts = wantsTexts;
        this.banned = banned;
    }

    public String getFullAddress() {
        return address.getFullAddress();
    }

    public void addNote(String note) {
        notes.add(note);
    }

    public String tiny() {
        return name + " / " + phone;
    }

    public Address getAddress() {
        return address;
    }

    public void updateAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
