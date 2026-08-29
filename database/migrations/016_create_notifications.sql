create table if not exists Notifications(
    notificationId serial primary key,
    type text,
    title text,
    message text,
    reservationId int references reservations(id),
    guestId int references guests(id),
    status notificationstatus default 'unread',
    createdAt date default now()
)