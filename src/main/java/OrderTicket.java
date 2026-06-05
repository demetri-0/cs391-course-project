import java.util.ArrayList;
import java.util.List;

public class OrderTicket {
    private static class OrderLine {
        MenuItem item;
        int quantity;

        OrderLine(MenuItem item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }
    }

    public static double GLOBAL_TAX_RATE = 0.0825;
    public static int GLOBAL_NEXT_NUMBER = 1000;
    public static String GLOBAL_STORE_NAME = "Campus Spoon";

    public int ticketNumber;
    public CustomerProfile customer;
    private List<OrderLine> lines = new ArrayList<OrderLine>();
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
        lines.add(new OrderLine(item, q));
    }

    public double calcTotal(String paymentMethod, boolean printDebug, String cashierName, String registerId,
            boolean rush, String street, String city, String state, String zip) {
        double subtotal = calculateSubtotal();
        subtotal = applyOrderDiscounts(subtotal);

        double deliveryFee = calculateDeliveryFee(rush, street, city, state, zip);
        double serviceFee = calculateServiceFee(paymentMethod, subtotal);
        double tax = (subtotal + deliveryFee + serviceFee) * GLOBAL_TAX_RATE;

        double total = subtotal + deliveryFee + serviceFee + tax;

        if (printDebug) {
            System.out.println("DEBUG " + cashierName + " " + registerId + " " + subtotal + " " + deliveryFee + " "
                    + serviceFee + " " + tax + " " + total);
        }
        tempRiskScore = total;
        return Math.round(total * 100.0) / 100.0;
    }

    private double calculateSubtotal() {
        double subtotal = 0;
        for (OrderLine line : lines) {
            MenuItem item = line.item;
            int quantity = line.quantity;
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

    private double applyOrderDiscounts(double subtotal) {
        if ("SAVE10".equals(couponCode)) {
            subtotal = subtotal - 10;
        }
        if ("STUDENT5".equals(couponCode) && customer.isStudent) {
            subtotal = subtotal - 5;
        }
        if (customer.loyaltyLevel.equals("gold")) {
            subtotal = subtotal * 0.90;
        } else if (customer.loyaltyLevel.equals("silver")) {
            subtotal = subtotal * 0.95;
        } else if (customer.loyaltyLevel.equals("bronze")) {
            subtotal = subtotal * 0.98;
        }
        return Math.max(subtotal, 0);
    }

    private double calculateDeliveryFee(boolean rush, String street, String city, String state, String zip) {
        double deliveryFee = 0;
        if ("delivery".equals(orderType)) {
            deliveryFee = 4.99;
            if (zip.startsWith("9")) {
                deliveryFee = deliveryFee + 2.50;
            }
            if (rush) {
                deliveryFee = deliveryFee + 3.00;
            }
            if (street.length() < 3 || city.length() < 2 || state.length() != 2 || zip.length() < 5) {
                deliveryFee = deliveryFee + 10.00;
            }
        } else if ("pickup".equals(orderType)) {
            deliveryFee = 0;
        } else if ("dinein".equals(orderType)) {
            deliveryFee = 1.25;
        } else {
            deliveryFee = 2.00;
        }
        return deliveryFee;
    }

    private double calculateServiceFee(String paymentMethod, double subtotal) {
        if ("credit".equals(paymentMethod)) {
            return subtotal * 0.03;
        }
        if ("cash".equals(paymentMethod)) {
            return 0;
        }
        if ("mealplan".equals(paymentMethod)) {
            return 0.45;
        }
        if ("crypto".equals(paymentMethod)) {
            return 8.88;
        }
        return 1.00;
    }

    public String printTicket(String paymentMethod, String cashierName, String registerId, boolean rush) {
        String receipt = "";
        receipt = receipt + buildReceiptHeader();
        receipt = receipt + formatReceiptItems();
        receipt = receipt + "Total: $" + calcTotal(paymentMethod, false, cashierName, registerId, rush, customer.street,
                customer.city, customer.state, customer.zip) + "\n";
        receipt = receipt + "Thanks " + customer.n + "!\n";
        return receipt;
    }

    private String buildReceiptHeader() {
        String receipt = "";
        receipt = receipt + "==== " + GLOBAL_STORE_NAME + " ====\n";
        receipt = receipt + "Ticket: " + ticketNumber + "\n";
        receipt = receipt + "Customer: " + customer.n + "\n";
        receipt = receipt + "Phone: " + customer.phone + "\n";
        receipt = receipt + "Address: " + customer.getFullAddress() + "\n";
        receipt = receipt + "Type: " + orderType + "\n";
        return receipt;
    }

    private String formatReceiptItems() {
        String receiptItems = "";
        for (OrderLine orderLine : lines) {
            MenuItem item = orderLine.item;
            int quantity = orderLine.quantity;
            String receiptLine = formatReceiptLine(item, quantity);
            receiptItems = receiptItems + receiptLine + "\n";
            tempLastPrintedLine = receiptLine;
        }
        return receiptItems;
    }

    private String formatReceiptLine(MenuItem item, int quantity) {
        String line = quantity + " x " + item.name + " (" + item.type + "/" + item.size + ") @ "
                + item.priceForDay(day, happyHour, campusEvent, couponCode);
        if ("drink".equals(item.type)) {
            if (happyHour) {
                line = line + " happy-hour";
            }
            if (item.vegan) {
                line = line + " vegan";
            }
        } else if ("meal".equals(item.type)) {
            if (item.spicy) {
                line = line + " spicy";
            }
            if (item.vegan) {
                line = line + " vegan";
            }
        } else if ("dessert".equals(item.type)) {
            if (item.seasonal) {
                line = line + " seasonal";
            }
        } else {
            line = line + " ordinary";
        }
        return line;
    }

    public String kitchenMessage() {
        String msg = "Kitchen ticket " + ticketNumber + "\n";
        for (OrderLine line : lines) {
            MenuItem m = line.item;
            int q = line.quantity;
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
                    if (lines.size() > 7 && customer.points < 20) {
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
