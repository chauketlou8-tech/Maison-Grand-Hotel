create table if not exists Notifications(
        notificationId serial primary key,
        type notificationtype not null,
        title text not null,
        message text not null,
        payment_id int references payments(id) on delete cascade,
        reservation_id int references reservations(id) on delete cascade,
        guest_id int references guests(id) on delete cascade,
        status notificationstatus default 'unread',
        created_at timestamp with time zone default now(),
        updated_at timestamp with time zone default now()
);