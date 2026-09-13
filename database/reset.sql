delete from notifications;
delete from payments;
delete from reservations;
delete from guests;
delete from address where id > 130;

alter sequence notifications_notificationid_seq restart with 1;
alter sequence payments_id_seq restart with 1;
alter sequence reservations_id_seq restart with 1;
alter sequence guests_id_seq restart with 1;
alter sequence address_id_seq restart with 131;

update rooms set roomstatus = 'available' where roomstatus <> 'available';

delete from roomhistory;
alter sequence roomhistory_id_seq restart with 1;

insert into roomhistory (roomid, status_date, status)
select roomid,date_trunc('day', now() at time zone 'UTC') at time zone 'UTC','available' from rooms;