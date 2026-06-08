public class MenuItem {
    private final String code;
    private final String name;
    private final String type;
    private final double price;
    private final int calories;
    private final boolean spicy;
    private final boolean vegan;
    private final boolean seasonal;
    private final String size;

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

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isSpicy() {
        return spicy;
    }

    public boolean isVegan() {
        return vegan;
    }

    public boolean isSeasonal() {
        return seasonal;
    }

    public String getSize() {
        return size;
    }

    public double priceForDay(String day, boolean happyHour, boolean campusEvent, String couponCode) {
        double p = price;
        if ("drink".equals(type)) {
            p = applyDrinkPricing(p, happyHour);
        } else if ("meal".equals(type)) {
            p = applyMealPricing(p, day, campusEvent);
        } else if ("dessert".equals(type)) {
            p = applyDessertPricing(p, day);
        } else if ("side".equals(type)) {
            p = applySidePricing(p, day);
        } else {
            p = applyDefaultPricing(p);
        }

        p = applyCoupon(p, couponCode);
        return Math.max(p, 0);
    }

    private double applyDrinkPricing(double currentPrice, boolean happyHour) {
        double updatedPrice = currentPrice;
        if (happyHour) {
            updatedPrice = updatedPrice - 1.00;
        }
        if ("large".equals(size)) {
            updatedPrice = updatedPrice + 1.25;
        } else if ("small".equals(size)) {
            updatedPrice = updatedPrice - 0.50;
        }
        return updatedPrice;
    }

    private double applyMealPricing(double currentPrice, String day, boolean campusEvent) {
        double updatedPrice = currentPrice;
        if ("Friday".equals(day)) {
            updatedPrice = updatedPrice + 0.50;
        }
        if (spicy && campusEvent) {
            updatedPrice = updatedPrice - 0.75;
        }
        if ("large".equals(size)) {
            updatedPrice = updatedPrice + 2.00;
        } else if ("small".equals(size)) {
            updatedPrice = updatedPrice - 1.00;
        }
        return updatedPrice;
    }

    private double applyDessertPricing(double currentPrice, String day) {
        double updatedPrice = currentPrice;
        if ("Monday".equals(day)) {
            updatedPrice = updatedPrice - 0.80;
        }
        if (seasonal) {
            updatedPrice = updatedPrice + 1.10;
        }
        return updatedPrice;
    }

    private double applySidePricing(double currentPrice, String day) {
        double updatedPrice = currentPrice;
        if ("Wednesday".equals(day)) {
            updatedPrice = updatedPrice - 0.40;
        }
        return updatedPrice;
    }

    private double applyDefaultPricing(double currentPrice) {
        return currentPrice + 0.25;
    }

    private double applyCoupon(double currentPrice, String couponCode) {
        double updatedPrice = currentPrice;
        if ("SAVE2".equals(couponCode)) {
            updatedPrice = updatedPrice - 2.00;
        }
        if ("HALFOFFDRINK".equals(couponCode) && "drink".equals(type)) {
            updatedPrice = updatedPrice * 0.5;
        }
        return updatedPrice;
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
        if (spicy) {
            line = line + " with warning sticker";
        }
        if (vegan) {
            line = line + " use clean pan";
        }
        return line;
    }

    private String dessertKitchenLine(int quantity) {
        String line = "BAKERY: " + quantity + " " + name;
        if (seasonal) {
            line = line + " from seasonal shelf";
        }
        return line;
    }

    public String tagLine() {
        String s = "";
        if ("drink".equals(type)) {
            s = "Sip: " + name;
            if (spicy) {
                s = s + " spicy";
            }
            if (vegan) {
                s = s + " vegan";
            }
        } else if ("meal".equals(type)) {
            s = "Plate: " + name;
            if (spicy) {
                s = s + " spicy";
            }
            if (vegan) {
                s = s + " vegan";
            }
        } else if ("dessert".equals(type)) {
            s = "Sweet: " + name;
            if (seasonal) {
                s = s + " seasonal";
            }
        } else {
            s = "Item: " + name;
        }
        return s;
    }

    public boolean isMealThing() {
        if ("meal".equals(type)) {
            return true;
        }
        return false;
    }
}
