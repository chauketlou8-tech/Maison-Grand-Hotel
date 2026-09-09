create table if not exists Guests(
    id serial primary key,
    guestID text unique not null,
    firstName text not null,
    lastName text not null,
    email text not null,
    phoneNumber text,
    address_id int references address(id) on delete set null,
    idNumber text,
    dateOfBirth date,
    nationality text,
    registrationDate date default now(),
    created_at timestamp with time zone default now(),
    updated_at timestamp with time zone default now()
);