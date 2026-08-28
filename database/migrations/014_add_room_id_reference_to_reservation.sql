alter table reservations add column room_id int references rooms(roomid);
