create table if not exists Payments(
    id serial primary key,
    amount numeric (10,2),
    paymentDate date default now(),
    paymentStatus paymentstatus,
    transactionReference text
)