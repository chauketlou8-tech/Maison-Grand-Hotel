package sockets;

import models.*;
import records.Payment;
import records.RoomHistory;
import records.Address;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    Consumer<Room[]> setRooms;
    Consumer<Notification[]> setNotifications;
    Consumer<Reservation[]> setReservations;
    Consumer<Payment[]> setPayments;
    Consumer<RoomHistory[]> setRoomHistory;
    Consumer<Guest[]> setGuests;
    Consumer<Address[]> setAddresses;
    Consumer<Booking[]> setBookings;

    public Client(
            Consumer<Room[]> setRooms,
            Consumer<Notification[]> setNotifications,
            Consumer<Reservation[]> setReservations,
            Consumer<Payment[]> setPayments,
            Consumer<RoomHistory[]> setRoomHistory,
            Consumer<Guest[]> setGuests,
            Consumer<Address[]> setAddresses,
            Consumer<Booking[]> setBookings
    ) throws IOException {

        this.setRooms = setRooms;
        this.setNotifications = setNotifications;
        this.setReservations = setReservations;
        this.setPayments = setPayments;
        this.setRoomHistory = setRoomHistory;
        this.setGuests = setGuests;
        this.setAddresses = setAddresses;
        this.setBookings = setBookings;

        Socket client = new Socket("localhost", 3000);

        this.out = new ObjectOutputStream(client.getOutputStream());
        this.in = new ObjectInputStream(client.getInputStream());

        this.out.writeObject("Connecting to the server...");
        this.out.flush();
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

                this.out.flush();

                out.writeObject("Received response from server...");
            }

            catch (IOException e) {
                break;
            }

            catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

}