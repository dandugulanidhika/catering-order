package eventplanner;

public class WeddingOrder extends CateringOrder {
    private double decorationCost;
    private int numberOfDays;
    private boolean liveCounters;
    private String weddingTheme;
    public static double liveCounterCharge = 15000.0;

    public WeddingOrder() {
        super();
        this.decorationCost = 0.0;
        this.numberOfDays = 1;
        this.liveCounters = false;
        this.weddingTheme = "Traditional";
    }

    public WeddingOrder(int orderId, String clientName, int guests, double perPlateCost, int eventDay,
                        String venue, String contactNumber, String menuType,
                        double decorationCost, int numberOfDays, boolean liveCounters)
            throws MinimumGuestsException {
        super(orderId, clientName, guests, perPlateCost, eventDay, venue, contactNumber, menuType);
        setDecorationCost(decorationCost);
        setNumberOfDays(numberOfDays);
        this.liveCounters = liveCounters;
        this.weddingTheme = "Traditional";
    }

    @Override
    public double calculateQuote() {
        double parentQuote = super.calculateQuote();
        return parentQuote * numberOfDays + decorationCost + (liveCounters ? liveCounterCharge : 0.0);
    }

    @Override
    public double calculateQuote(double discountPercent) throws InvalidDiscountException {
        double discountedBase = super.calculateQuote(discountPercent);
        double base = super.calculateQuote();
        return (discountedBase - base) * numberOfDays + calculateQuote();
    }

    @Override
    public void display() {
        System.out.printf("ID: %d | Type: Wedding | Client: %s | Guests: %d | Day: %d | Days: %d | Decoration: Rs. %.2f | Live counters: %s | Quote: Rs. %.2f | Status: %s%n",
                getOrderId(), getClientName(), getGuests(), getEventDay(), numberOfDays,
                decorationCost, liveCounters ? "Yes" : "No", calculateQuote(),
                isCancelled() ? "Cancelled" : (isConfirmed() ? "Confirmed" : "Pending"));
    }

    @Override
    public String toFileRecord() {
        return "W," + getOrderId() + "," + clean(getClientName()) + "," + getGuests() + "," + getPerPlateCost()
                + "," + getEventDay() + "," + clean(getVenue()) + "," + clean(getContactNumber()) + ","
                + clean(getMenuType()) + "," + isConfirmed() + "," + isCancelled() + "," + getCancellationFee()
                + "," + clean(getSpecialRequest()) + "," + decorationCost + "," + numberOfDays + ","
                + liveCounters + "," + clean(weddingTheme);
    }

    public double getDecorationCost() { return decorationCost; }
    public final void setDecorationCost(double decorationCost) {
        if (!Double.isFinite(decorationCost) || decorationCost < 0) throw new IllegalArgumentException("Decoration cost cannot be negative.");
        this.decorationCost = decorationCost;
    }
    public int getNumberOfDays() { return numberOfDays; }
    public final void setNumberOfDays(int numberOfDays) {
        if (numberOfDays < 1) throw new IllegalArgumentException("Wedding must last at least one day.");
        this.numberOfDays = numberOfDays;
    }
    public boolean isLiveCounters() { return liveCounters; }
    public void setLiveCounters(boolean liveCounters) { this.liveCounters = liveCounters; }
    public String getWeddingTheme() { return weddingTheme; }
    public void setWeddingTheme(String weddingTheme) {
        if (weddingTheme == null || weddingTheme.trim().isEmpty()) throw new IllegalArgumentException("Wedding theme is required.");
        this.weddingTheme = weddingTheme.trim();
    }
}
