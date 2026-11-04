package eventplanner;

public class DestinationWeddingOrder extends WeddingOrder {
    private String venueCity;
    private int guestsTravelling;
    private double accommodationPerGuest;
    private String travelMode;
    public static final double travelCostPerGuest = 3000.0;

    public DestinationWeddingOrder() {
        super();
        this.venueCity = "Not specified";
        this.guestsTravelling = 0;
        this.accommodationPerGuest = 0.0;
        this.travelMode = "Not specified";
    }

    public DestinationWeddingOrder(int orderId, String clientName, int guests, double perPlateCost, int eventDay,
                                   String venue, String contactNumber, String menuType,
                                   double decorationCost, int numberOfDays, boolean liveCounters,
                                   String venueCity, int guestsTravelling, double accommodationPerGuest)
            throws MinimumGuestsException {
        super(orderId, clientName, guests, perPlateCost, eventDay, venue, contactNumber, menuType,
                decorationCost, numberOfDays, liveCounters);
        setVenueCity(venueCity);
        setGuestsTravelling(guestsTravelling);
        setAccommodationPerGuest(accommodationPerGuest);
        this.travelMode = "Standard";
    }

    @Override
    public double calculateQuote() {
        return super.calculateQuote() + guestsTravelling * travelCostPerGuest
                + guestsTravelling * accommodationPerGuest;
    }

    @Override
    public double calculateQuote(double discountPercent) throws InvalidDiscountException {
        double discountedBase = super.calculateQuote(discountPercent);
        double base = super.calculateQuote();
        return calculateQuote() + (discountedBase - base);
    }

    @Override
    public void display() {
        System.out.printf("ID: %d | Type: Destination Wedding | Client: %s | Guests: %d | Day: %d | City: %s | Travelling: %d | Quote: Rs. %.2f | Status: %s%n",
                getOrderId(), getClientName(), getGuests(), getEventDay(), venueCity,
                guestsTravelling, calculateQuote(),
                isCancelled() ? "Cancelled" : (isConfirmed() ? "Confirmed" : "Pending"));
    }

    @Override
    public String toFileRecord() {
        return "D," + getOrderId() + "," + clean(getClientName()) + "," + getGuests() + "," + getPerPlateCost()
                + "," + getEventDay() + "," + clean(getVenue()) + "," + clean(getContactNumber()) + ","
                + clean(getMenuType()) + "," + isConfirmed() + "," + isCancelled() + "," + getCancellationFee()
                + "," + clean(getSpecialRequest()) + "," + getDecorationCost() + "," + getNumberOfDays() + ","
                + isLiveCounters() + "," + clean(getWeddingTheme()) + "," + clean(venueCity) + ","
                + guestsTravelling + "," + accommodationPerGuest + "," + clean(travelMode);
    }

    public String getVenueCity() { return venueCity; }
    public final void setVenueCity(String venueCity) {
        if (venueCity == null || venueCity.trim().isEmpty()) throw new IllegalArgumentException("Venue city is required.");
        this.venueCity = venueCity.trim();
    }
    public int getGuestsTravelling() { return guestsTravelling; }
    public final void setGuestsTravelling(int guestsTravelling) {
        if (guestsTravelling < 0 || guestsTravelling > getGuests()) throw new IllegalArgumentException("Travelling guests must be between 0 and total guests.");
        this.guestsTravelling = guestsTravelling;
    }
    public double getAccommodationPerGuest() { return accommodationPerGuest; }
    public void setAccommodationPerGuest(double accommodationPerGuest) {
        if (!Double.isFinite(accommodationPerGuest) || accommodationPerGuest < 0) throw new IllegalArgumentException("Accommodation cost cannot be negative.");
        this.accommodationPerGuest = accommodationPerGuest;
    }
    public String getTravelMode() { return travelMode; }
    public void setTravelMode(String travelMode) {
        if (travelMode == null || travelMode.trim().isEmpty()) throw new IllegalArgumentException("Travel mode is required.");
        this.travelMode = travelMode.trim();
    }
}
