create table if not exists Bookings(
    booking_id text primary key,
    reservation_id uuid references reservations(reservationid) on delete set null,
    guest_id int references guests(id) on delete cascade,
    room_id int references rooms(roomid) on delete set null,
    booking_date date not null,
    check_in_date date not null,
    check_out_date date not null,
    number_of_guests int not null check (number_of_guests > 0),
    booking_status bookingstatus default 'CHECKED_IN',
    total_cost numeric(10, 2) not null check (total_cost >= 0),
    selected_packages text[] default '{}',
    requests text,
    created_at timestamp with time zone default now(),
    updated_at timestamp with time zone default now()
);