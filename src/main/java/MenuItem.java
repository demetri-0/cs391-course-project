public class MenuItem {
    public String code;
    public String name;
    public String type;
    public double price;
    public int calories;
    public boolean spicy;
    public boolean vegan;
    public boolean seasonal;
    public String size;

    public MenuItem(String code, String name, String type, double price, int calories, boolean spicy, boolean vegan,
            boolean seasonal, String size) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.price = price;
        this.calories = calories;
        this.spicy = spicy;
        this.vegan = vegan;
        this.seasonal = seasonal;
        this.size = size;
    }

    public double priceForDay(String day, boolean happyHour, boolean campusEvent, String couponCode) {
        double p = price;
        if (type.equals("drink")) {
            if (happyHour == true) {
                p = p - 1.00;
            }
            if (size.equals("large")) {
                p = p + 1.25;
            } else if (size.equals("small")) {
                p = p - 0.50;
            } else {
                p = p + 0.00;
            }
        } else if (type.equals("meal")) {
            if (day.equals("Friday")) {
                p = p + 0.50;
            }
            if (spicy == true && campusEvent == true) {
                p = p - 0.75;
            }
            if (size.equals("large")) {
                p = p + 2.00;
            } else if (size.equals("small")) {
                p = p - 1.00;
            }
        } else if (type.equals("dessert")) {
            if (day.equals("Monday")) {
                p = p - 0.80;
            }
            if (seasonal == true) {
                p = p + 1.10;
            }
        } else if (type.equals("side")) {
            if (day.equals("Wednesday")) {
                p = p - 0.40;
            }
        } else {
            p = p + 0.25;
        }

        if (couponCode != null && couponCode.equals("SAVE2")) {
            p = p - 2.00;
        }
        if (couponCode != null && couponCode.equals("HALFOFFDRINK") && type.equals("drink")) {
            p = p * 0.5;
        }
        if (p < 0) {
            p = 0;
        }
        return p;
    }

    public double priceForOrder(int quantity, String day, boolean happyHour, boolean campusEvent, String couponCode,
            CustomerProfile customer) {
        double p = priceForDay(day, happyHour, campusEvent, couponCode);
        if ("drink".equals(type)) {
            if (happyHour) {
                p = p - 0.15;
            }
        } else if ("meal".equals(type)) {
            if (quantity >= 3) {
                p = p - 0.35;
            }
        } else if ("dessert".equals(type)) {
            if (customer.loyaltyLevel.equals("gold")) {
                p = p - 0.25;
            }
        }
        return Math.max(p, 0);
    }

    public String receiptLine(int quantity, String day, boolean happyHour, boolean campusEvent, String couponCode) {
        String line = quantity + " x " + name + " (" + type + "/" + size + ") @ "
                + priceForDay(day, happyHour, campusEvent, couponCode);
        return line + receiptTags(happyHour);
    }

    private String receiptTags(boolean happyHour) {
        String tags = "";
        if ("drink".equals(type)) {
            if (happyHour) {
                tags = tags + " happy-hour";
            }
            if (vegan) {
                tags = tags + " vegan";
            }
        } else if ("meal".equals(type)) {
            if (spicy) {
                tags = tags + " spicy";
            }
            if (vegan) {
                tags = tags + " vegan";
            }
        } else if ("dessert".equals(type)) {
            if (seasonal) {
                tags = tags + " seasonal";
            }
        } else {
            tags = tags + " ordinary";
        }
        return tags;
    }

    public String kitchenLine(int quantity) {
        if ("drink".equals(type)) {
            return drinkKitchenLine(quantity);
        }
        if ("meal".equals(type)) {
            return mealKitchenLine(quantity);
        }
        if ("dessert".equals(type)) {
            return dessertKitchenLine(quantity);
        }
        return "MISC: " + quantity + " " + name;
    }

    private String drinkKitchenLine(int quantity) {
        String line = "BAR: " + quantity + " " + name;
        if ("large".equals(size)) {
            line = line + " big cup";
        } else if ("small".equals(size)) {
            line = line + " tiny cup";
        } else {
            line = line + " normal cup";
        }
        return line;
    }

    private String mealKitchenLine(int quantity) {
        String line = "GRILL: " + quantity + " " + name;
        if (spicy == true) {
            line = line + " with warning sticker";
        }
        if (vegan == true) {
            line = line + " use clean pan";
        }
        return line;
    }

    private String dessertKitchenLine(int quantity) {
        String line = "BAKERY: " + quantity + " " + name;
        if (seasonal == true) {
            line = line + " from seasonal shelf";
        }
        return line;
    }

    public String tagLine() {
        String s = "";
        if (type.equals("drink")) {
            s = "Sip: " + name;
            if (spicy == true) {
                s = s + " spicy";
            }
            if (vegan == true) {
                s = s + " vegan";
            }
        } else if (type.equals("meal")) {
            s = "Plate: " + name;
            if (spicy == true) {
                s = s + " spicy";
            }
            if (vegan == true) {
                s = s + " vegan";
            }
        } else if (type.equals("dessert")) {
            s = "Sweet: " + name;
            if (seasonal == true) {
                s = s + " seasonal";
            }
        } else {
            s = "Item: " + name;
        }
        return s;
    }

    public boolean isMealThing() {
        if (type.equals("meal")) {
            return true;
        }
        return false;
    }
}
