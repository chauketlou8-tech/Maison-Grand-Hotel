package sockets;

import models.*;
import records.Payment;
import records.RoomHistory;
import records.Address;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class Client {
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    private final LinkedBlockingQueue<String> outQueue = new LinkedBlockingQueue<>();

    private final Consumer<Room[]> setRooms;
    private final Consumer<Notification[]> setNotifications;
    private final Consumer<Reservation[]> setReservations;
    private final Consumer<Payment[]> setPayments;
    private final Consumer<RoomHistory[]> setRoomHistory;
    private final Consumer<Guest[]> setGuests;
    private final Consumer<Address[]> setAddresses;
    private final Consumer<Booking[]> setBookings;
    private final Consumer<Service[]> setServices;
    private final Consumer<ServiceTask[]> setServiceTasks;
    private final Consumer<ServiceItem[]> setServiceItems;
    private final Consumer<Employee[]> setEmployees;

    public Client(
            Consumer<Room[]> setRooms,
            Consumer<Notification[]> setNotifications,
            Consumer<Reservation[]> setReservations,
            Consumer<Payment[]> setPayments,
            Consumer<RoomHistory[]> setRoomHistory,
            Consumer<Guest[]> setGuests,
            Consumer<Address[]> setAddresses,
            Consumer<Booking[]> setBookings,
            Consumer<Service[]> setServices,
            Consumer<ServiceTask[]> setServiceTasks,
            Consumer<ServiceItem[]> setServiceItems,
            Consumer<Employee[]> setEmployees
    ) throws IOException {

        this.setRooms = setRooms;
        this.setNotifications = setNotifications;
        this.setReservations = setReservations;
        this.setPayments = setPayments;
        this.setRoomHistory = setRoomHistory;
        this.setGuests = setGuests;
        this.setAddresses = setAddresses;
        this.setBookings = setBookings;
        this.setServices = setServices;
        this.setServiceTasks = setServiceTasks;
        this.setServiceItems = setServiceItems;
        this.setEmployees = setEmployees;

        Socket client = new Socket("localhost", 3000);

        this.out = new ObjectOutputStream(client.getOutputStream());
        this.in = new ObjectInputStream(client.getInputStream());

        System.out.println("Connecting to the server...");

        this.out.writeObject("Connecting to the server...");
        this.out.flush();

        new Thread(this::writeLoop).start();
    }

    public void listen() {
        while (true) {
            try {
                Object response = in.readObject();

                if (response instanceof Room[]) {
                    this.setRooms.accept((Room[]) response);
                }

                else if (response instanceof Notification[]) {
                    this.setNotifications.accept((Notification[]) response);
                }

                else if (response instanceof Reservation[]) {
                    this.setReservations.accept((Reservation[]) response);
                }

                else if (response instanceof Payment[]) {
                    this.setPayments.accept((Payment[]) response);
                }

                else if (response instanceof RoomHistory[]) {
                    this.setRoomHistory.accept((RoomHistory[]) response);
                }

                else if (response instanceof Guest[]) {
                    this.setGuests.accept((Guest[]) response);
                }

                else if (response instanceof Address[]) {
                    this.setAddresses.accept((Address[]) response);
                }

                else if (response instanceof Booking[]) {
                    this.setBookings.accept((Booking[]) response);
                }

                else if (response instanceof Service[]) {
                    this.setServices.accept((Service[]) response);
                }

                else if (response instanceof ServiceTask[]) {
                    this.setServiceTasks.accept((ServiceTask[]) response);
                }

                else if (response instanceof ServiceItem[]) {
                    this.setServiceItems.accept((ServiceItem[]) response);
                }

                else if (response instanceof Employee[]) {
                    this.setEmployees.accept((Employee[]) response);
                }

                outQueue.offer("Received response from server...");
            }

            catch (IOException e) {
                break;
            }

            catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void writeLoop() {
        while (true) {
            try {
                String message = outQueue.take();
                out.writeObject(message);
                out.flush();
            } catch (IOException | InterruptedException e) {
                break;
            }
        }
    }
}