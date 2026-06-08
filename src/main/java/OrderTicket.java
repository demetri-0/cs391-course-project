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

    private static final double TAX_RATE = 0.0825;
    private static final String STORE_NAME = "Campus Spoon";
    private static int nextNumber = 1000;

    private final int ticketNumber;
    private final CustomerProfile customer;
    private final List<OrderLine> lines = new ArrayList<OrderLine>();
    private final String orderType;
    private final String day;
    private final boolean happyHour;
    private final boolean campusEvent;
    private final String couponCode;

    public OrderTicket(CustomerProfile customer, String orderType, String day, boolean happyHour, boolean campusEvent,
            String couponCode) {
        this.ticketNumber = nextTicketNumber();
        this.customer = customer;
        this.orderType = orderType;
        this.day = day;
        this.happyHour = happyHour;
        this.campusEvent = campusEvent;
        this.couponCode = couponCode;
    }

    private static int nextTicketNumber() {
        return nextNumber++;
    }

    public void add(MenuItem item, int q) {
        lines.add(new OrderLine(item, q));
    }

    public double calcTotal(String paymentMethod, boolean printDebug, String cashierName, String registerId,
            boolean rush, Address address) {
        double subtotal = calculateSubtotal();
        subtotal = applyOrderDiscounts(subtotal);

        double deliveryFee = calculateDeliveryFee(rush, address);
        double serviceFee = calculateServiceFee(paymentMethod, subtotal);
        double tax = (subtotal + deliveryFee + serviceFee) * TAX_RATE;

        double total = subtotal + deliveryFee + serviceFee + tax;

        if (printDebug) {
            System.out.println("DEBUG " + cashierName + " " + registerId + " " + subtotal + " " + deliveryFee + " "
                    + serviceFee + " " + tax + " " + total);
        }
        return Math.round(total * 100.0) / 100.0;
    }

    private double calculateSubtotal() {
        double subtotal = 0;
        for (OrderLine line : lines) {
            double price = line.item.priceForOrder(line.quantity, day, happyHour, campusEvent, couponCode, customer);
            subtotal = subtotal + (price * line.quantity);
        }
        return subtotal;
    }

    private double applyOrderDiscounts(double subtotal) {
        if ("SAVE10".equals(couponCode)) {
            subtotal = subtotal - 10;
        }
        if ("STUDENT5".equals(couponCode) && customer.isStudent()) {
            subtotal = subtotal - 5;
        }
        if (customer.isGoldMember()) {
            subtotal = subtotal * 0.90;
        } else if (customer.isSilverMember()) {
            subtotal = subtotal * 0.95;
        } else if (customer.isBronzeMember()) {
            subtotal = subtotal * 0.98;
        }
        return Math.max(subtotal, 0);
    }

    private double calculateDeliveryFee(boolean rush, Address address) {
        double deliveryFee = 0;
        if ("delivery".equals(orderType)) {
            deliveryFee = 4.99;
            if (address.getZip().startsWith("9")) {
                deliveryFee = deliveryFee + 2.50;
            }
            if (rush) {
                deliveryFee = deliveryFee + 3.00;
            }
            if (address.getStreet().length() < 3 || address.getCity().length() < 2 || address.getState().length() != 2
                    || address.getZip().length() < 5) {
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
        receipt = receipt + "Total: $" + calcTotal(paymentMethod, false, cashierName, registerId, rush,
                customer.getAddress()) + "\n";
        receipt = receipt + "Thanks " + customer.getName() + "!\n";
        return receipt;
    }

    private String buildReceiptHeader() {
        String receipt = "";
        receipt = receipt + "==== " + STORE_NAME + " ====\n";
        receipt = receipt + "Ticket: " + ticketNumber + "\n";
        receipt = receipt + "Customer: " + customer.getName() + "\n";
        receipt = receipt + "Phone: " + customer.getContactInfo().getPhone() + "\n";
        receipt = receipt + "Address: " + customer.getFullAddress() + "\n";
        receipt = receipt + "Type: " + orderType + "\n";
        return receipt;
    }

    private String formatReceiptItems() {
        String receiptItems = "";
        for (OrderLine orderLine : lines) {
            MenuItem item = orderLine.item;
            int quantity = orderLine.quantity;
            String receiptLine = item.receiptLine(quantity, day, happyHour, campusEvent, couponCode);
            receiptItems = receiptItems + receiptLine + "\n";
        }
        return receiptItems;
    }

    public String kitchenMessage() {
        String msg = "Kitchen ticket " + ticketNumber + "\n";
        for (OrderLine line : lines) {
            msg = msg + line.item.kitchenLine(line.quantity) + "\n";
        }
        if (orderType.equals("delivery")) {
            msg = msg + "Bag for delivery to " + customer.getFullAddress() + "\n";
        }
        return msg;
    }

    public boolean riskyCustomerCheck() {
        if (customer.isBanned()) {
            return true;
        }
        ContactInfo contactInfo = customer.getContactInfo();
        if (contactInfo.getPhone() == null || contactInfo.getPhone().length() < 7) {
            return true;
        }
        if ((contactInfo.getEmail() == null || !contactInfo.getEmail().contains("@")) && orderType.equals("delivery")) {
            return true;
        }
        return lines.size() > 7 && customer.getPoints() < 20;
    }
}
