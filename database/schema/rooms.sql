create table if not exists Rooms(
        roomID serial primary key,
        roomNumber int unique not null,
        floorNumber int not null check (floorNumber >= 0),
        roomType text not null,
        price numeric(19, 4) not null check (price >= 0),
        capacity int not null check (capacity > 0),
        roomStatus text check ( roomStatus in ('available', 'occupied', 'maintenance', 'closed') ) default 'available',
        description text,
        created_at timestamp with time zone default now(),
        updated_at timestamp with time zone default now()
);