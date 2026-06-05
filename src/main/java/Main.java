public class Main {
    public static void main(String[] args) {
        CustomerProfile c = new CustomerProfile("Mina Patel", "5551234567", "mina@example.com", "17 College Ave",
                "Boston", "MA", "02118", "gold", 140, true, true, false);
        c.addNote("Usually asks for extra napkins");
        c.addNote("Do not ring doorbell during evening deliveries");

        MenuItem noodles = new MenuItem("N12", "Fire Noodles", "meal", 10.99, 830, true, false, false, "large");
        MenuItem tea = new MenuItem("D22", "Mint Tea", "drink", 3.75, 80, false, true, false, "medium");
        MenuItem cake = new MenuItem("S04", "Pumpkin Cake", "dessert", 5.25, 420, false, false, true, "small");
        MenuItem fries = new MenuItem("X99", "Curly Fries", "side", 4.50, 600, false, true, false, "medium");

        OrderTicket ticket = new OrderTicket(c, "delivery", "Friday", true, true, "STUDENT5");
        ticket.add(noodles, 2);
        ticket.add(tea, 3);
        ticket.add(cake, 1);
        ticket.add(fries, 2);

        OrderReporter reporter = new OrderReporter(ticket);
        System.out.println(reporter.makeReceipt("credit", "Sam", "REG-2", true));
        System.out.println(reporter.makeKitchenMessage());
        System.out.println("Needs manager review? " + reporter.isScary());

        CustomerProfile c2 = new CustomerProfile("Jay Wu", "123", "jay.example.com", "9", "Q", "Massachusetts",
                "9", "none", 3, false, false, false);
        OrderTicket questionable = new OrderTicket(c2, "delivery", "Monday", false, false, "SAVE2");
        questionable.add(new MenuItem("D77", "Cola", "drink", 2.95, 150, false, true, false, "large"), 1);
        OrderReporter secondReporter = new OrderReporter(questionable);
        System.out.println(secondReporter.makeReceipt("crypto", "Lee", "REG-4", false));
        System.out.println("Needs manager review? " + secondReporter.isScary());
    }
}
