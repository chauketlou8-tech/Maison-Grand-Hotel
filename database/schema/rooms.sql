create table if not exists Rooms(roomID serial primary key,
                                 roomNumber int,
                                 floorNumber int,
                                 roomType text,
                                 price numeric(19, 4),
                                 capacity int,
                                 roomStatus text check ( roomStatus in ('available', 'occupied', 'maintenance', 'closed') ) default 'available',
                                 description text
                                )