create table if not exists Reservations (
        id serial primary key,
        reservationId uuid default gen_random_uuid() unique not null,
        guest_id int references guests(id) on delete cascade,
        room_id int references rooms(roomid) on delete set null,
        requestedRoomType text not null,
        reservationDate timestamp with time zone default now(),
        checkInDate date not null,
        checkOutDate date not null,
        numberOfGuests int not null check (numberOfGuests > 0),
        reservationStatus reservationstatus default 'PENDING',
        selectedPackages text[] default '{}',
        requests text,
        total numeric(10, 2),
        created_at timestamp with time zone default now(),
        updated_at timestamp with time zone default now()
);