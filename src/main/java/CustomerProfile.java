import java.util.ArrayList;
import java.util.List;

public class CustomerProfile {
    private String name;
    private ContactInfo contactInfo;
    private Address address;
    private String loyaltyLevel;
    private int points;
    private boolean isStudent;
    private boolean wantsTexts;
    private boolean banned;
    private final List<String> notes = new ArrayList<String>();

    public CustomerProfile(String name, ContactInfo contactInfo, Address address,
            String loyaltyLevel, int points, boolean isStudent, boolean wantsTexts, boolean banned) {
        this.name = name;
        this.contactInfo = contactInfo;
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
        return name + " / " + contactInfo.getPhone();
    }

    public Address getAddress() {
        return address;
    }

    public void updateAddress(Address address) {
        this.address = address;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void updateContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoyaltyLevel() {
        return loyaltyLevel;
    }

    public void setLoyaltyLevel(String loyaltyLevel) {
        this.loyaltyLevel = loyaltyLevel;
    }

    public boolean isGoldMember() {
        return "gold".equals(loyaltyLevel);
    }

    public boolean isSilverMember() {
        return "silver".equals(loyaltyLevel);
    }

    public boolean isBronzeMember() {
        return "bronze".equals(loyaltyLevel);
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
