create table if not exists RoomHistory(
    id serial primary key,
    roomid int references rooms(roomid),
    status_date timestamp with time zone default now(),
    status text check ( status in ('available', 'occupied', 'maintenance', 'closed') )
)