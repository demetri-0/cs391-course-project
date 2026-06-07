public class OrderReporter {
    private final OrderTicket ticket;

    public OrderReporter(OrderTicket ticket) {
        this.ticket = ticket;
    }

    public String makeReceipt(String paymentMethod, String cashierName, String registerId, boolean rush) {
        return ticket.printTicket(paymentMethod, cashierName, registerId, rush);
    }

    public String makeKitchenMessage() {
        return ticket.kitchenMessage();
    }

    public boolean needsManagerReview() {
        return ticket.riskyCustomerCheck();
    }

}
