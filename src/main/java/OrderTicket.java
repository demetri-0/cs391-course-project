import java.util.ArrayList;
import java.util.List;

public class OrderTicket {
    public static double GLOBAL_TAX_RATE = 0.0825;
    public static int GLOBAL_NEXT_NUMBER = 1000;
    public static String GLOBAL_STORE_NAME = "Campus Spoon";

    public int ticketNumber;
    public CustomerProfile customer;
    public List<MenuItem> items = new ArrayList<MenuItem>();
    public List<Integer> quantities = new ArrayList<Integer>();
    public String orderType;
    public String day;
    public boolean happyHour;
    public boolean campusEvent;
    public String couponCode;
    public boolean paid;
    public String tempLastPrintedLine;
    public double tempRiskScore;
    public String futureKitchenRobotInstruction;

    public OrderTicket(CustomerProfile customer, String orderType, String day, boolean happyHour, boolean campusEvent,
            String couponCode) {
        this.ticketNumber = GLOBAL_NEXT_NUMBER++;
        this.customer = customer;
        this.orderType = orderType;
        this.day = day;
        this.happyHour = happyHour;
        this.campusEvent = campusEvent;
        this.couponCode = couponCode;
        this.paid = false;
        this.futureKitchenRobotInstruction = "";
    }

    public void add(MenuItem item, int q) {
        items.add(item);
        quantities.add(q);
    }

    public double calcTotal(String paymentMethod, boolean printDebug, String cashierName, String registerId,
            boolean rush, String street, String city, String state, String zip) {
        double subtotal = calculateSubtotal();

        if (couponCode != null && couponCode.equals("SAVE10")) {
            subtotal = subtotal - 10;
        }
        if (couponCode != null && couponCode.equals("STUDENT5") && customer.isStudent == true) {
            subtotal = subtotal - 5;
        }
        if (customer.loyaltyLevel.equals("gold")) {
            subtotal = subtotal * 0.90;
        } else if (customer.loyaltyLevel.equals("silver")) {
            subtotal = subtotal * 0.95;
        } else if (customer.loyaltyLevel.equals("bronze")) {
            subtotal = subtotal * 0.98;
        }
        if (subtotal < 0) {
            subtotal = 0;
        }

        double deliveryFee = 0;
        if (orderType.equals("delivery")) {
            deliveryFee = 4.99;
            if (zip.startsWith("9")) {
                deliveryFee = deliveryFee + 2.50;
            }
            if (rush == true) {
                deliveryFee = deliveryFee + 3.00;
            }
            if (street.length() < 3 || city.length() < 2 || state.length() != 2 || zip.length() < 5) {
                deliveryFee = deliveryFee + 10.00;
            }
        } else if (orderType.equals("pickup")) {
            deliveryFee = 0;
        } else if (orderType.equals("dinein")) {
            deliveryFee = 1.25;
        } else {
            deliveryFee = 2.00;
        }

        double serviceFee = 0;
        if (paymentMethod.equals("credit")) {
            serviceFee = subtotal * 0.03;
        } else if (paymentMethod.equals("cash")) {
            serviceFee = 0;
        } else if (paymentMethod.equals("mealplan")) {
            serviceFee = 0.45;
        } else if (paymentMethod.equals("crypto")) {
            serviceFee = 8.88;
        } else {
            serviceFee = 1.00;
        }

        double tax = (subtotal + deliveryFee + serviceFee) * GLOBAL_TAX_RATE;
        double total = subtotal + deliveryFee + serviceFee + tax;
        if (printDebug == true) {
            System.out.println("DEBUG " + cashierName + " " + registerId + " " + subtotal + " " + deliveryFee + " "
                    + serviceFee + " " + tax + " " + total);
        }
        tempRiskScore = total;
        return Math.round(total * 100.0) / 100.0;
    }

    private double calculateSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            int quantity = quantities.get(i);
            double price = item.priceForDay(day, happyHour, campusEvent, couponCode);
            if (item.type.equals("drink")) {
                if (happyHour) {
                    price = price - 0.15;
                }
            } else if (item.type.equals("meal")) {
                if (quantity >= 3) {
                    price = price - 0.35;
                }
            } else if (item.type.equals("dessert")) {
                if (customer.loyaltyLevel.equals("gold")) {
                    price = price - 0.25;
                }
            }
            subtotal = subtotal + (price * quantity);
        }
        return subtotal;
    }

    public String printTicket(String paymentMethod, String cashierName, String registerId, boolean rush) {
        String receipt = "";
        receipt = receipt + "==== " + GLOBAL_STORE_NAME + " ====\n";
        receipt = receipt + "Ticket: " + ticketNumber + "\n";
        receipt = receipt + "Customer: " + customer.n + "\n";
        receipt = receipt + "Phone: " + customer.phone + "\n";
        receipt = receipt + "Address: " + customer.street + ", " + customer.city + ", " + customer.state + " "
                + customer.zip + "\n";
        receipt = receipt + "Type: " + orderType + "\n";
        for (int i = 0; i < items.size(); i++) {
            MenuItem m = items.get(i);
            int q = quantities.get(i);
            String line = q + " x " + m.name + " (" + m.type + "/" + m.size + ") @ "
                    + m.priceForDay(day, happyHour, campusEvent, couponCode);
            if (m.type.equals("drink")) {
                if (happyHour == true) {
                    line = line + " happy-hour";
                }
                if (m.vegan == true) {
                    line = line + " vegan";
                }
            } else if (m.type.equals("meal")) {
                if (m.spicy == true) {
                    line = line + " spicy";
                }
                if (m.vegan == true) {
                    line = line + " vegan";
                }
            } else if (m.type.equals("dessert")) {
                if (m.seasonal == true) {
                    line = line + " seasonal";
                }
            } else {
                line = line + " ordinary";
            }
            receipt = receipt + line + "\n";
            tempLastPrintedLine = line;
        }
        receipt = receipt + "Total: $" + calcTotal(paymentMethod, false, cashierName, registerId, rush, customer.street,
                customer.city, customer.state, customer.zip) + "\n";
        receipt = receipt + "Thanks " + customer.n + "!\n";
        return receipt;
    }

    public String kitchenMessage() {
        String msg = "Kitchen ticket " + ticketNumber + "\n";
        for (int i = 0; i < items.size(); i++) {
            MenuItem m = items.get(i);
            int q = quantities.get(i);
            if (m.type.equals("drink")) {
                msg = msg + "BAR: " + q + " " + m.name;
                if (m.size.equals("large")) {
                    msg = msg + " big cup";
                } else if (m.size.equals("small")) {
                    msg = msg + " tiny cup";
                } else {
                    msg = msg + " normal cup";
                }
            } else if (m.type.equals("meal")) {
                msg = msg + "GRILL: " + q + " " + m.name;
                if (m.spicy == true) {
                    msg = msg + " with warning sticker";
                }
                if (m.vegan == true) {
                    msg = msg + " use clean pan";
                }
            } else if (m.type.equals("dessert")) {
                msg = msg + "BAKERY: " + q + " " + m.name;
                if (m.seasonal == true) {
                    msg = msg + " from seasonal shelf";
                }
            } else {
                msg = msg + "MISC: " + q + " " + m.name;
            }
            msg = msg + "\n";
        }
        if (orderType.equals("delivery")) {
            msg = msg + "Bag for delivery to " + customer.getFullAddress() + "\n";
        }
        return msg;
    }

    public boolean riskyCustomerCheck() {
        boolean risky = false;
        if (customer.banned == true) {
            risky = true;
        } else {
            if (customer.phone == null || customer.phone.length() < 7) {
                risky = true;
            } else {
                if (customer.email == null || customer.email.indexOf("@") < 0) {
                    if (orderType.equals("delivery")) {
                        risky = true;
                    } else {
                        risky = false;
                    }
                } else {
                    if (items.size() > 7 && customer.points < 20) {
                        risky = true;
                    } else {
                        risky = false;
                    }
                }
            }
        }
        return risky;
    }
}
