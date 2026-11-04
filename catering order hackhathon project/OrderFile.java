package eventplanner;

import java.io.*;
import java.util.ArrayList;

public class OrderFile {
    private static final String FILE_NAME = "orders.txt";
    private static final String CANCEL_FILE = "cancelled.log";

    public void saveAll(ArrayList<CateringOrder> records) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (CateringOrder order : records) {
                writer.write(order.toFileRecord());
                writer.newLine();
            }
            System.out.println("All orders saved to " + FILE_NAME + ".");
        } catch (IOException e) {
            System.out.println("Could not save orders: " + e.getMessage());
        }
    }

    public void loadAll(ArrayList<CateringOrder> records) {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    CateringOrder order = parseRecord(line);
                    if (order != null) {
                        records.add(order);
                        Main.updateNextId(order.getOrderId());
                    }
                } catch (Exception e) {
                    System.out.println("Skipping damaged order record.");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Order file not found; starting with an empty register.");
        } catch (IOException e) {
            System.out.println("Could not read orders: " + e.getMessage());
        }
    }

    private CateringOrder parseRecord(String line) throws Exception {
        String[] p = line.split(",", -1);
        if (p.length < 13) return null;
        int id = Integer.parseInt(p[1]);
        String name = p[2];
        int guests = Integer.parseInt(p[3]);
        double plate = Double.parseDouble(p[4]);
        int day = Integer.parseInt(p[5]);
        String venue = p[6], contact = p[7], menu = p[8];
        boolean confirmed = Boolean.parseBoolean(p[9]);
        boolean cancelled = Boolean.parseBoolean(p[10]);
        double fee = Double.parseDouble(p[11]);
        String request = p[12];

        CateringOrder order;
        if ("D".equals(p[0])) {
            if (p.length < 21) return null;
            DestinationWeddingOrder d = new DestinationWeddingOrder(id, name, guests, plate, day, venue, contact, menu,
                    Double.parseDouble(p[13]), Integer.parseInt(p[14]), Boolean.parseBoolean(p[15]),
                    p[17], Integer.parseInt(p[18]), Double.parseDouble(p[19]));
            d.setWeddingTheme(p[16]);
            d.setTravelMode(p[20]);
            order = d;
        } else if ("W".equals(p[0])) {
            if (p.length < 17) return null;
            WeddingOrder w = new WeddingOrder(id, name, guests, plate, day, venue, contact, menu,
                    Double.parseDouble(p[13]), Integer.parseInt(p[14]), Boolean.parseBoolean(p[15]));
            w.setWeddingTheme(p[16]);
            order = w;
        } else if ("C".equals(p[0])) {
            order = new CateringOrder(id, name, guests, plate, day, venue, contact, menu);
        } else return null;

        order.loadStatus(confirmed, cancelled, fee, request);
        return order;
    }

    public void logCancellation(CateringOrder order, int daysBefore, double fee) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CANCEL_FILE, true))) {
            writer.write("ID=" + order.getOrderId() + ", Client=" + order.getClientName()
                    + ", Days before event=" + daysBefore + ", Cancellation fee=Rs. "
                    + String.format("%.2f", fee));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Could not write cancellation log: " + e.getMessage());
        }
    }

    public void displayCancellationLog() {
        File file = new File(CANCEL_FILE);
        if (!file.exists()) {
            System.out.println("No cancellations logged.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) System.out.println(line);
        } catch (IOException e) {
            System.out.println("Could not read cancellation log: " + e.getMessage());
        }
    }
}
