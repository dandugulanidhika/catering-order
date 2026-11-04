package eventplanner;

import java.util.ArrayList;

public class CateringOrder implements Quotable {
    private int orderId;
    private String clientName;
    private int guests;
    private double perPlateCost;
    private int eventDay;
    private String venue;
    private String contactNumber;
    private String menuType;
    private boolean confirmed;
    private boolean cancelled;
    private double cancellationFee;
    private String specialRequest;

    public static double serviceChargePercent = 10.0;
    public static double gstPercent = 5.0;
    public static int minGuests = 50;
    public static int orderCount = 0;
    public static double volumeDiscountPercent = 15.0;

    public CateringOrder() {
        this.orderId = 0;
        this.clientName = "Unknown";
        this.guests = 50;
        this.perPlateCost = 0.0;
        this.eventDay = 1;
        this.venue = "Not specified";
        this.contactNumber = "Not specified";
        this.menuType = "Standard";
        this.confirmed = true;
        this.cancelled = false;
        this.cancellationFee = 0.0;
        this.specialRequest = "";
    }

    public CateringOrder(int orderId, String clientName, int guests, double perPlateCost, int eventDay)
            throws MinimumGuestsException {
        this(orderId, clientName, guests, perPlateCost, eventDay, "Not specified",
                "Not specified", "Standard");
    }

    public CateringOrder(int orderId, String clientName, int guests, double perPlateCost, int eventDay,
                         String venue, String contactNumber, String menuType) throws MinimumGuestsException {
        setOrderId(orderId);
        setClientName(clientName);
        setGuests(guests);
        setPerPlateCost(perPlateCost);
        setEventDay(eventDay);
        setVenue(venue);
        setContactNumber(contactNumber);
        setMenuType(menuType);
        this.confirmed = true;
        this.cancelled = false;
        this.cancellationFee = 0.0;
        this.specialRequest = "";
        orderCount++;
    }

    public double calculateQuote() {
        double foodCost = guests * perPlateCost;
        double withService = foodCost + foodCost * serviceChargePercent / 100.0;
        return withService + withService * gstPercent / 100.0;
    }

    public double calculateQuote(double discountPercent) throws InvalidDiscountException {
        if (guests <= 500) {
            throw new InvalidDiscountException("Volume discount is allowed only when guests are above 500.");
        }
        if (discountPercent < 0 || discountPercent > volumeDiscountPercent) {
            throw new InvalidDiscountException("Discount must be between 0 and " + volumeDiscountPercent + "%.");
        }
        double foodCost = guests * perPlateCost;
        double discountedFood = foodCost * (1.0 - discountPercent / 100.0);
        double withService = discountedFood + discountedFood * serviceChargePercent / 100.0;
        return withService + withService * gstPercent / 100.0;
    }

    public double calculateCancellation(int daysBefore) {
        double rate = daysBefore < 7 ? 0.30 : 0.10;
        return calculateQuote() * rate;
    }

    public String compareQuote(CateringOrder other) {
        if (other == null) return clientName;
        return calculateQuote() >= other.calculateQuote() ? clientName : other.clientName;
    }

    public void display() {
        System.out.printf("ID: %d | Type: Catering | Client: %s | Guests: %d | Day: %d | Quote: Rs. %.2f | Status: %s%n",
                orderId, clientName, guests, eventDay, calculateQuote(), cancelled ? "Cancelled" : (confirmed ? "Confirmed" : "Pending"));
    }

    public static void addRecord(ArrayList<CateringOrder> records, CateringOrder order)
            throws DateClashException {
        if (order == null) return;
        if (order.isConfirmed() && !order.isCancelled() && !isDateFree(records, order.getEventDay(), -1)) {
            throw new DateClashException("Another confirmed order already exists on event day " + order.getEventDay() + ".");
        }
        records.add(order);
    }

    public static void displayAll(ArrayList<CateringOrder> records) {
        if (records.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        for (CateringOrder order : records) order.display();
    }

    public static CateringOrder searchById(ArrayList<CateringOrder> records, int id) {
        for (CateringOrder order : records) if (order.getOrderId() == id) return order;
        return null;
    }

    public static boolean deleteById(ArrayList<CateringOrder> records, int id) {
        CateringOrder order = searchById(records, id);
        return order != null && records.remove(order);
    }

    public static boolean isDateFree(ArrayList<CateringOrder> records, int day, int ignoredId) {
        for (CateringOrder order : records) {
            if (order.getOrderId() != ignoredId && order.getEventDay() == day
                    && order.isConfirmed() && !order.isCancelled()) return false;
        }
        return true;
    }

    public static CateringOrder largestOrder(ArrayList<CateringOrder> records) {
        CateringOrder largest = null;
        for (CateringOrder order : records) {
            if (!order.isCancelled() && (largest == null || order.calculateQuote() > largest.calculateQuote()))
                largest = order;
        }
        return largest;
    }

    public static double totalRevenue(ArrayList<CateringOrder> records) {
        double total = 0.0;
        for (CateringOrder order : records) {
            if (order.isConfirmed() && !order.isCancelled()) total += order.calculateQuote();
        }
        return total;
    }

    public int getOrderId() { return orderId; }
    public final void setOrderId(int orderId) {
        if (orderId <= 0) throw new IllegalArgumentException("Order ID must be positive.");
        this.orderId = orderId;
    }
    public String getClientName() { return clientName; }
    public final void setClientName(String clientName) {
        if (clientName == null || clientName.trim().isEmpty()) throw new IllegalArgumentException("Client name is required.");
        this.clientName = clientName.trim();
    }
    public final int getGuests() { return guests; }
    public final void setGuests(int guests) throws MinimumGuestsException {
        if (guests < minGuests) throw new MinimumGuestsException("Minimum " + minGuests + " guests are required.");
        this.guests = guests;
    }
    public double getPerPlateCost() { return perPlateCost; }
    public final void setPerPlateCost(double perPlateCost) {
        if (!Double.isFinite(perPlateCost) || perPlateCost < 0) throw new IllegalArgumentException("Per-plate cost must be non-negative.");
        this.perPlateCost = perPlateCost;
    }
    public int getEventDay() { return eventDay; }
    public void setEventDay(int eventDay) {
        if (eventDay < 1 || eventDay > 365) throw new IllegalArgumentException("Event day must be from 1 to 365.");
        this.eventDay = eventDay;
    }
    public String getVenue() { return venue; }
    public void setVenue(String venue) {
        if (venue == null || venue.trim().isEmpty()) throw new IllegalArgumentException("Venue is required.");
        this.venue = venue.trim();
    }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) {
        if (contactNumber == null || contactNumber.trim().isEmpty()) throw new IllegalArgumentException("Contact number is required.");
        this.contactNumber = contactNumber.trim();
    }
    public String getMenuType() { return menuType; }
    public void setMenuType(String menuType) {
        if (menuType == null || menuType.trim().isEmpty()) throw new IllegalArgumentException("Menu type is required.");
        this.menuType = menuType.trim();
    }
    public boolean isConfirmed() { return confirmed; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    public double getCancellationFee() { return cancellationFee; }
    public void setCancellationFee(double cancellationFee) {
        if (!Double.isFinite(cancellationFee) || cancellationFee < 0) throw new IllegalArgumentException("Invalid cancellation fee.");
        this.cancellationFee = cancellationFee;
    }
    public String getSpecialRequest() { return specialRequest; }
    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest == null ? "" : specialRequest.trim();
    }

    public String toFileRecord() {
        return "C," + orderId + "," + clean(clientName) + "," + guests + "," + perPlateCost + "," + eventDay + ","
                + clean(venue) + "," + clean(contactNumber) + "," + clean(menuType) + "," + confirmed + ","
                + cancelled + "," + cancellationFee + "," + clean(specialRequest);
    }

    protected static String clean(String value) {
        return value == null ? "" : value.replace(",", " ").replace("\n", " ").replace("\r", " ");
    }

    protected void loadStatus(boolean confirmed, boolean cancelled, double fee, String request) {
        this.confirmed = confirmed;
        this.cancelled = cancelled;
        this.cancellationFee = fee;
        this.specialRequest = request;
    }
}
