alter table rooms
add constraint room_status_check
check (roomStatus in ('available','occupied','maintenance','closed'));

alter table rooms
drop constraint room_status_check;

alter table rooms
add constraint room_status_check
check (roomStatus in ('available','occupied','maintenance','closed', 'reserved'));


