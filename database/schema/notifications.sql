create table if not exists Notifications(
    notificationId serial primary key,
    type text,
    title text,
    message text,
    payment_id int references payments(id),
    reservation_id int references reservations(id),
    guest_id int references guests(id),
    status notificationstatus default 'unread',
    createdAt date default now()
)