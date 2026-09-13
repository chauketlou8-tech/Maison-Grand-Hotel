create table if not exists Payments(
    id serial primary key,
    amount numeric(10, 2) not null check (amount >= 0),
    paymentDate date default now(),
    paymentStatus paymentstatus default 'created',
    transactionReference text unique,
    guest_id int references guests(id) on delete set null,
    yoco_checkout_id text,
    paymentMethod text,
    created_at timestamp with time zone default now(),
    updated_at timestamp with time zone default now()
);