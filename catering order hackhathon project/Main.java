package eventplanner;

import java.util.ArrayList;
import java.util.Scanner;

public class EventPlanner {
    private static final Scanner sc = new Scanner(System.in);
    private static final ArrayList<CateringOrder> records = new ArrayList<CateringOrder>();
    private static final OrderFile file = new OrderFile();
    private static int nextOrderId = 1;

    public static void updateNextId(int id) {
        if (id >= nextOrderId) nextOrderId = id + 1;
    }

    public static void main(String[] args) {
        file.loadAll(records);
        boolean running = true;
        while (running) {
            System.out.println("\n===== SPICEROUTE: CATERING & WEDDING EVENT PLANNER =====");
            System.out.println("1. Add catering order");
            System.out.println("2. Add wedding order");
            System.out.println("3. Add destination wedding order");
            System.out.println("4. Display all orders");
            System.out.println("5. Search order by ID");
            System.out.println("6. Delete order by ID");
            System.out.println("7. Cancel order");
            System.out.println("8. Show largest order");
            System.out.println("9. Show confirmed-order revenue");
            System.out.println("10. Show cancellation log");
            System.out.println("11. Save data and exit");
            int choice = readInt("Choose option: ");

            switch (choice) {
                case 1: addCatering(); break;
                case 2: addWedding(); break;
                case 3: addDestination(); break;
                case 4: CateringOrder.displayAll(records); break;
                case 5: search(); break;
                case 6: delete(); break;
                case 7: cancel(); break;
                case 8: largest(); break;
                case 9: System.out.printf("Total confirmed revenue: Rs. %.2f%n", CateringOrder.totalRevenue(records)); break;
                case 10: file.displayCancellationLog(); break;
                case 11:
                    file.saveAll(records);
                    running = false;
                    System.out.println("Thank you for using SpiceRoute.");
                    break;
                default: System.out.println("Invalid option. Enter 1 to 11.");
            }
        }
        sc.close();
    }

    private static void addCatering() {
        try {
            int id = nextOrderId;
            String name = readText("Client name: ");
            int guests = readInt("Number of guests (minimum 50): ");
            if (guests < CateringOrder.minGuests) throw new MinimumGuestsException("At least 50 guests are required.");
            double plate = readDouble("Cost per plate (Rs.): ");
            int day = readInt("Event day (1-365): ");
            String venue = readText("Venue: ");
            String contact = readText("Contact number: ");
            String menu = readText("Menu type: ");
            CateringOrder order = new CateringOrder(id, name, guests, plate, day, venue, contact, menu);
            if (readYesNo("Apply volume discount? (y/n): ")) {
                double discount = readDouble("Discount percentage (0-15): ");
                try {
                    System.out.printf("Discounted quote: Rs. %.2f%n", order.calculateQuote(discount));
                } catch (InvalidDiscountException e) {
                    System.out.println("InvalidDiscountException: " + e.getMessage());
                    System.out.printf("Regular quote will be used: Rs. %.2f%n", order.calculateQuote());
                }
            }
            CateringOrder.addRecord(records, order);
            nextOrderId++;
            order.display();
        } catch (MinimumGuestsException e) {
            System.out.println("MinimumGuestsException: " + e.getMessage());
        } catch (DateClashException e) {
            System.out.println("DateClashException: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } finally {
            System.out.println("Catering order operation completed.");
        }
    }

    private static void addWedding() {
        try {
            int id = nextOrderId;
            String name = readText("Client name: ");
            int guests = readInt("Number of guests (minimum 50): ");
            if (guests < CateringOrder.minGuests) throw new MinimumGuestsException("At least 50 guests are required.");
            double plate = readDouble("Cost per plate (Rs.): ");
            int day = readInt("Event day (1-365): ");
            String venue = readText("Venue: ");
            String contact = readText("Contact number: ");
            String menu = readText("Menu type: ");
            int days = readInt("Number of wedding days: ");
            double decoration = readDouble("Decoration cost (Rs.): ");
            boolean live = readYesNo("Live counters? (y/n): ");
            WeddingOrder order = new WeddingOrder(id, name, guests, plate, day, venue, contact, menu, decoration, days, live);
            if (readYesNo("Apply volume discount? (y/n): ")) {
                double discount = readDouble("Discount percentage (0-15): ");
                try {
                    System.out.printf("Discounted wedding quote: Rs. %.2f%n", order.calculateQuote(discount));
                } catch (InvalidDiscountException e) {
                    System.out.println("InvalidDiscountException: " + e.getMessage());
                    System.out.printf("Regular quote will be used: Rs. %.2f%n", order.calculateQuote());
                }
            }
            CateringOrder.addRecord(records, order);
            nextOrderId++;
            order.display();
        } catch (MinimumGuestsException e) {
            System.out.println("MinimumGuestsException: " + e.getMessage());
        } catch (DateClashException e) {
            System.out.println("DateClashException: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } finally {
            System.out.println("Wedding order operation completed.");
        }
    }

    private static void addDestination() {
        try {
            int id = nextOrderId;
            String name = readText("Client name: ");
            int guests = readInt("Number of guests (minimum 50): ");
            if (guests < CateringOrder.minGuests) throw new MinimumGuestsException("At least 50 guests are required.");
            double plate = readDouble("Cost per plate (Rs.): ");
            int day = readInt("Event day (1-365): ");
            String venue = readText("Venue: ");
            String contact = readText("Contact number: ");
            String menu = readText("Menu type: ");
            int days = readInt("Number of wedding days: ");
            double decoration = readDouble("Decoration cost (Rs.): ");
            boolean live = readYesNo("Live counters? (y/n): ");
            String city = readText("Destination city: ");
            int travelling = readInt("Number of travelling guests: ");
            double accommodation = readDouble("Accommodation cost per travelling guest (Rs.): ");
            DestinationWeddingOrder order = new DestinationWeddingOrder(id, name, guests, plate, day, venue, contact,
                    menu, decoration, days, live, city, travelling, accommodation);
            if (readYesNo("Apply volume discount? (y/n): ")) {
                double discount = readDouble("Discount percentage (0-15): ");
                try {
                    System.out.printf("Discounted destination wedding quote: Rs. %.2f%n",
                            order.calculateQuote(discount));
                } catch (InvalidDiscountException e) {
                    System.out.println("InvalidDiscountException: " + e.getMessage());
                    System.out.printf("Regular quote will be used: Rs. %.2f%n", order.calculateQuote());
                }
            }
            CateringOrder.addRecord(records, order);
            nextOrderId++;
            order.display();
        } catch (MinimumGuestsException e) {
            System.out.println("MinimumGuestsException: " + e.getMessage());
        } catch (DateClashException e) {
            System.out.println("DateClashException: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } finally {
            System.out.println("Destination wedding operation completed.");
        }
    }

    private static void search() {
        int id = readInt("Enter order ID: ");
        CateringOrder order = CateringOrder.searchById(records, id);
        if (order == null) System.out.println("Order not found.");
        else order.display();
    }

    private static void delete() {
        int id = readInt("Enter order ID to delete: ");
        System.out.println(CateringOrder.deleteById(records, id) ? "Order deleted." : "Order not found.");
    }

    private static void cancel() {
        try {
            int id = readInt("Enter order ID to cancel: ");
            CateringOrder order = CateringOrder.searchById(records, id);
            if (order == null) {
                System.out.println("Order not found.");
                return;
            }
            if (order.isCancelled()) {
                System.out.println("This order is already cancelled.");
                return;
            }
            int days = readInt("Days remaining until event: ");
            if (days < 0) throw new IllegalArgumentException("Days remaining cannot be negative.");
            double fee = order.calculateCancellation(days);
            order.setCancelled(true);
            order.setConfirmed(false);
            order.setCancellationFee(fee);
            file.logCancellation(order, days, fee);
            System.out.printf("Order cancelled. Cancellation fee: Rs. %.2f%n", fee);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid cancellation input: " + e.getMessage());
        } finally {
            System.out.println("Cancellation operation completed.");
        }
    }

    private static void largest() {
        CateringOrder order = CateringOrder.largestOrder(records);
        if (order == null) System.out.println("No active orders.");
        else {
            System.out.println("Largest order:");
            order.display();
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid whole number."); }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(sc.nextLine().trim());
                if (Double.isFinite(value) && value >= 0) return value;
            } catch (NumberFormatException e) { }
            System.out.println("Enter a valid non-negative number.");
        }
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static boolean readYesNo(String prompt) {
        while (true) {
            String answer = readText(prompt);
            if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) return true;
            if (answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")) return false;
            System.out.println("Enter y or n.");
        }
    }
}

