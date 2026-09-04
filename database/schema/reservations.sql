create table if not exists Reservations (
    id serial primary key,
    reservationId uuid,
    guest_id int references guests(id),
    room_id int references rooms(roomid),
    requestedRoomType text,
    reservationDate timestamp with time zone default now(),
    checkInDate date,
    checkOutDate date,
    numberOfGuests int,
    reservationStatus reservationstatus default 'PENDING',
    selectedPackages text[],
    requests text
)