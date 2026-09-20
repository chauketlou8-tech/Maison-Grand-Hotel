delete from bookings;
delete from notifications;
delete from payments;
delete from reservations;
delete from guests;
delete from address where id > 130;
delete from servicetasks;
delete from serviceitems;

alter sequence notifications_notificationid_seq restart with 1;
alter sequence payments_id_seq restart with 1;
alter sequence reservations_id_seq restart with 1;
alter sequence guests_id_seq restart with 1;
alter sequence address_id_seq restart with 131;
alter sequence servicetasks_id_seq restart with 1;
alter sequence serviceitems_id_seq restart with 1;

update rooms set roomstatus = 'available' where roomstatus <> 'available';

delete from roomhistory;
alter sequence roomhistory_id_seq restart with 1;

insert into roomhistory (roomid, status_date, status)
select roomid, date_trunc('day', now() at time zone 'Africa/Johannesburg') at time zone 'UTC', 'available'
from rooms;