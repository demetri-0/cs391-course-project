import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RefactoringProjectTests {
    @Test
    void menuItemPricingUsesCurrentDiscountRules() {
        MenuItem tea = new MenuItem("D22", "Mint Tea", "drink", 3.75, 80, false, true, false, "medium");
        assertEquals(2.75, tea.priceForDay("Friday", true, false, null), 0.001);

        MenuItem cake = new MenuItem("S04", "Pumpkin Cake", "dessert", 5.25, 420, false, false, true, "small");
        assertEquals(5.55, cake.priceForDay("Monday", false, false, null), 0.001);

        MenuItem largeCola = new MenuItem("D77", "Cola", "drink", 2.95, 150, false, true, false, "large");
        assertEquals(2.20, largeCola.priceForDay("Monday", false, false, "SAVE2"), 0.001);
    }

    @Test
    void sampleOrderTotalAndReceiptStayTheSame() {
        CustomerProfile customer = sampleCustomer();
        OrderTicket ticket = sampleOrder(customer);

        double total = ticket.calcTotal("credit", false, "Sam", "REG-2", true, customer.street, customer.city,
                customer.state, customer.zip);
        assertEquals(52.18, total, 0.001);

        String receipt = ticket.printTicket("credit", "Sam", "REG-2", true);
        assertTrue(receipt.contains("==== Campus Spoon ===="));
        assertTrue(receipt.contains("Customer: Mina Patel"));
        assertTrue(receipt.contains("2 x Fire Noodles"));
        assertTrue(receipt.contains("2 x Curly Fries (side/medium) @ 4.5 ordinary"));
        assertTrue(receipt.contains("Total: $52.18"));
    }

    @Test
    void kitchenMessageRoutesItemsByType() {
        OrderTicket ticket = sampleOrder(sampleCustomer());
        String message = ticket.kitchenMessage();

        assertTrue(message.contains("GRILL: 2 Fire Noodles with warning sticker"));
        assertTrue(message.contains("BAR: 3 Mint Tea normal cup"));
        assertTrue(message.contains("BAKERY: 1 Pumpkin Cake from seasonal shelf"));
        assertTrue(message.contains("Bag for delivery to 17 College Ave, Boston, MA 02118"));
    }

    @Test
    void riskyCustomerCheckMatchesCurrentRules() {
        OrderTicket normalTicket = sampleOrder(sampleCustomer());
        assertFalse(normalTicket.riskyCustomerCheck());

        CustomerProfile badContactCustomer = new CustomerProfile("Jay Wu", "123", "jay.example.com", "9", "Q",
                "Massachusetts", "9", "none", 3, false, false, false);
        OrderTicket questionable = new OrderTicket(badContactCustomer, "delivery", "Monday", false, false, "SAVE2");
        questionable.add(new MenuItem("D77", "Cola", "drink", 2.95, 150, false, true, false, "large"), 1);
        assertTrue(questionable.riskyCustomerCheck());

        badContactCustomer.banned = true;
        assertTrue(questionable.riskyCustomerCheck());
    }

    @Test
    void customerAddressCanBeUpdated() {
        CustomerProfile customer = sampleCustomer();
        customer.updateAddress("44 Library Rd", "Cambridge", "MA", "02139");

        assertEquals("44 Library Rd, Cambridge, MA 02139", customer.getFullAddress());
    }

    private CustomerProfile sampleCustomer() {
        CustomerProfile customer = new CustomerProfile("Mina Patel", "5551234567", "mina@example.com",
                "17 College Ave", "Boston", "MA", "02118", "gold", 140, true, true, false);
        customer.addNote("Usually asks for extra napkins");
        customer.addNote("Do not ring doorbell during evening deliveries");
        return customer;
    }

    private OrderTicket sampleOrder(CustomerProfile customer) {
        MenuItem noodles = new MenuItem("N12", "Fire Noodles", "meal", 10.99, 830, true, false, false, "large");
        MenuItem tea = new MenuItem("D22", "Mint Tea", "drink", 3.75, 80, false, true, false, "medium");
        MenuItem cake = new MenuItem("S04", "Pumpkin Cake", "dessert", 5.25, 420, false, false, true, "small");
        MenuItem fries = new MenuItem("X99", "Curly Fries", "side", 4.50, 600, false, true, false, "medium");

        OrderTicket ticket = new OrderTicket(customer, "delivery", "Friday", true, true, "STUDENT5");
        ticket.add(noodles, 2);
        ticket.add(tea, 3);
        ticket.add(cake, 1);
        ticket.add(fries, 2);
        return ticket;
    }
}
