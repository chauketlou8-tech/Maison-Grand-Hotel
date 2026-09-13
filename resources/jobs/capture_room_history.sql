insert into roomhistory (roomid, status_date, status)
select roomid, date_trunc('day', now()), roomstatus
from rooms
on conflict do nothing;