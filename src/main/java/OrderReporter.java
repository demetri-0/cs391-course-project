public class OrderReporter {
    private OrderTicket ticket;

    public OrderReporter(OrderTicket ticket) {
        this.ticket = ticket;
    }

    public String makeReceipt(String paymentMethod, String cashierName, String registerId, boolean rush) {
        return ticket.printTicket(paymentMethod, cashierName, registerId, rush);
    }

    public String makeKitchenMessage() {
        return ticket.kitchenMessage();
    }

    public boolean isScary() {
        return ticket.riskyCustomerCheck();
    }

    public void printSummaryTwiceForNoGoodReason(String paymentMethod, String cashierName, String registerId,
            boolean rush) {
        System.out.println(ticket.printTicket(paymentMethod, cashierName, registerId, rush));
        System.out.println(ticket.printTicket(paymentMethod, cashierName, registerId, rush));
    }
}
