create table if not exists Reservations (
                                            id serial primary key,
                                            guest_id int references guests(id),
                                            requestedRoomType text,
                                            reservationDate date,
                                            checkInDate date,
                                            checkOutDate date,
                                            numberOfGuests int,
                                            reservationStatus reservationstatus default 'PENDING'
)