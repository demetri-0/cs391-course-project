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
        double adjustedPrice = price;
        if ("drink".equals(type)) {
            adjustedPrice = applyDrinkPricing(adjustedPrice, happyHour);
        } else if ("meal".equals(type)) {
            adjustedPrice = applyMealPricing(adjustedPrice, day, campusEvent);
        } else if ("dessert".equals(type)) {
            adjustedPrice = applyDessertPricing(adjustedPrice, day);
        } else if ("side".equals(type)) {
            adjustedPrice = applySidePricing(adjustedPrice, day);
        } else {
            adjustedPrice = applyDefaultPricing(adjustedPrice);
        }

        adjustedPrice = applyCoupon(adjustedPrice, couponCode);
        return Math.max(adjustedPrice, 0);
    }

    private double applyDrinkPricing(double currentPrice, boolean happyHour) {
        double updatedPrice = currentPrice;
        if (happyHour) {
            updatedPrice = updatedPrice - 1.00;
        }
        return applySizeAdjustment(updatedPrice, 1.25, -0.50);
    }

    private double applyMealPricing(double currentPrice, String day, boolean campusEvent) {
        double updatedPrice = currentPrice;
        if ("Friday".equals(day)) {
            updatedPrice = updatedPrice + 0.50;
        }
        if (spicy && campusEvent) {
            updatedPrice = updatedPrice - 0.75;
        }
        return applySizeAdjustment(updatedPrice, 2.00, -1.00);
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

    private double applySizeAdjustment(double currentPrice, double largeAdjustment, double smallAdjustment) {
        if ("large".equals(size)) {
            return currentPrice + largeAdjustment;
        }
        if ("small".equals(size)) {
            return currentPrice + smallAdjustment;
        }
        return currentPrice;
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
        double orderPrice = priceForDay(day, happyHour, campusEvent, couponCode);
        if ("drink".equals(type)) {
            if (happyHour) {
                orderPrice = orderPrice - 0.15;
            }
        } else if ("meal".equals(type)) {
            if (quantity >= 3) {
                orderPrice = orderPrice - 0.35;
            }
        } else if ("dessert".equals(type)) {
            if (customer.loyaltyLevel.equals("gold")) {
                orderPrice = orderPrice - 0.25;
            }
        }
        return Math.max(orderPrice, 0);
    }

    public String receiptLine(int quantity, String day, boolean happyHour, boolean campusEvent, String couponCode) {
        String receiptLineText = quantity + " x " + name + " (" + type + "/" + size + ") @ "
                + priceForDay(day, happyHour, campusEvent, couponCode);
        return receiptLineText + buildReceiptTags(happyHour);
    }

    private String buildReceiptTags(boolean happyHour) {
        String receiptTags = "";
        if ("drink".equals(type)) {
            if (happyHour) {
                receiptTags = receiptTags + " happy-hour";
            }
            receiptTags = appendVeganTagIfNeeded(receiptTags);
        } else if ("meal".equals(type)) {
            receiptTags = appendSpicyAndVeganTags(receiptTags);
        } else if ("dessert".equals(type)) {
            receiptTags = appendSeasonalTagIfNeeded(receiptTags);
        } else {
            receiptTags = receiptTags + " ordinary";
        }
        return receiptTags;
    }

    private String appendSpicyAndVeganTags(String text) {
        String updatedText = text;
        if (spicy) {
            updatedText = updatedText + " spicy";
        }
        return appendVeganTagIfNeeded(updatedText);
    }

    private String appendVeganTagIfNeeded(String text) {
        if (vegan) {
            return text + " vegan";
        }
        return text;
    }

    private String appendSeasonalTagIfNeeded(String text) {
        if (seasonal) {
            return text + " seasonal";
        }
        return text;
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
        String kitchenLineText = "BAR: " + quantity + " " + name;
        if ("large".equals(size)) {
            kitchenLineText = kitchenLineText + " big cup";
        } else if ("small".equals(size)) {
            kitchenLineText = kitchenLineText + " tiny cup";
        } else {
            kitchenLineText = kitchenLineText + " normal cup";
        }
        return kitchenLineText;
    }

    private String mealKitchenLine(int quantity) {
        String kitchenLineText = "GRILL: " + quantity + " " + name;
        if (spicy) {
            kitchenLineText = kitchenLineText + " with warning sticker";
        }
        if (vegan) {
            kitchenLineText = kitchenLineText + " use clean pan";
        }
        return kitchenLineText;
    }

    private String dessertKitchenLine(int quantity) {
        String kitchenLineText = "BAKERY: " + quantity + " " + name;
        if (seasonal) {
            kitchenLineText = kitchenLineText + " from seasonal shelf";
        }
        return kitchenLineText;
    }

    public String tagLine() {
        String tagLineText = "";
        if ("drink".equals(type)) {
            tagLineText = "Sip: " + name;
            tagLineText = appendSpicyAndVeganTags(tagLineText);
        } else if ("meal".equals(type)) {
            tagLineText = "Plate: " + name;
            tagLineText = appendSpicyAndVeganTags(tagLineText);
        } else if ("dessert".equals(type)) {
            tagLineText = "Sweet: " + name;
            tagLineText = appendSeasonalTagIfNeeded(tagLineText);
        } else {
            tagLineText = "Item: " + name;
        }
        return tagLineText;
    }

}
