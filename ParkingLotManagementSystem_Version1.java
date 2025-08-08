import java.util.*;

enum VehicleType {
    CAR, BIKE, TRUCK
}

abstract class Vehicle {
    private String plateNumber;
    private VehicleType type;

    public Vehicle(String plateNumber, VehicleType type) {
        this.plateNumber = plateNumber;
        this.type = type;
    }

    public VehicleType getType() {
        return type;
    }

    public String getPlateNumber() {
        return plateNumber;
    }
}

class Car extends Vehicle {
    public Car(String plateNumber) {
        super(plateNumber, VehicleType.CAR);
    }
}

class Bike extends Vehicle {
    public Bike(String plateNumber) {
        super(plateNumber, VehicleType.BIKE);
    }
}

class ParkingSlot {
    private int slotNumber;
    private boolean isOccupied;
    private Vehicle currentVehicle;

    public ParkingSlot(int slotNumber) {
        this.slotNumber = slotNumber;
        this.isOccupied = false;
    }

    public boolean isAvailable() {
        return !isOccupied;
    }

    public void parkVehicle(Vehicle vehicle) {
        this.currentVehicle = vehicle;
        this.isOccupied = true;
    }

    public void unparkVehicle() {
        this.currentVehicle = null;
        this.isOccupied = false;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public Vehicle getCurrentVehicle() {
        return currentVehicle;
    }
}

class Ticket {
    private static int count = 1;
    private int ticketId;
    private String plateNumber;
    private long entryTime;
    private int slotNumber;

    public Ticket(String plateNumber, int slotNumber) {
        this.ticketId = count++;
        this.plateNumber = plateNumber;
        this.slotNumber = slotNumber;
        this.entryTime = System.currentTimeMillis();
    }

    public int getTicketId() {
        return ticketId;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public int getSlotNumber() {
        return slotNumber;
    }
}

class ParkingLot {
    private List<ParkingSlot> slots;
    private Map<String, Ticket> activeTickets;

    public ParkingLot(int numberOfSlots) {
        slots = new ArrayList<>();
        for (int i = 1; i <= numberOfSlots; i++) {
            slots.add(new ParkingSlot(i));
        }
        activeTickets = new HashMap<>();
    }

    public Ticket parkVehicle(Vehicle vehicle) {
        for (ParkingSlot slot : slots) {
            if (slot.isAvailable()) {
                slot.parkVehicle(vehicle);
                Ticket ticket = new Ticket(vehicle.getPlateNumber(), slot.getSlotNumber());
                activeTickets.put(vehicle.getPlateNumber(), ticket);
                System.out.println("Vehicle parked at slot: " + slot.getSlotNumber());
                return ticket;
            }
        }
        System.out.println("Parking lot full!");
        return null;
    }

    public void unparkVehicle(String plateNumber) {
        Ticket ticket = activeTickets.get(plateNumber);
        if (ticket != null) {
            long exitTime = System.currentTimeMillis();
            long duration = (exitTime - ticket.getEntryTime()) / 1000; // in seconds
            int fee = calculateFee(duration);

            ParkingSlot slot = slots.get(ticket.getSlotNumber() - 1);
            slot.unparkVehicle();
            activeTickets.remove(plateNumber);

            System.out.println("Vehicle " + plateNumber + " unparked from slot " + ticket.getSlotNumber());
            System.out.println("Parking duration: " + duration + " seconds. Fee: ₹" + fee);
        } else {
            System.out.println("Vehicle not found!");
        }
    }

    public void displayAvailableSlots() {
        System.out.println("Available slots:");
        for (ParkingSlot slot : slots) {
            if (slot.isAvailable()) {
                System.out.println("Slot " + slot.getSlotNumber());
            }
        }
    }

    private int calculateFee(long durationInSeconds) {
        // Simple fee logic: ₹10 for every started minute
        return (int) Math.ceil(durationInSeconds / 60.0) * 10;
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ParkingLot lot = new ParkingLot(5);

        Vehicle car1 = new Car("KA-01-AA-1111");
        Vehicle bike1 = new Bike("KA-02-BB-2222");

        Ticket ticket1 = lot.parkVehicle(car1);
        Thread.sleep(2000); // simulate parking time

        Ticket ticket2 = lot.parkVehicle(bike1);

        lot.displayAvailableSlots();

        lot.unparkVehicle("KA-01-AA-1111");
        lot.unparkVehicle("KA-02-BB-2222");
    }
}