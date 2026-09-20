create table if not exists Users(
        id serial primary key,
        employee_id int references Employees(id) on delete cascade,
        name text not null,
        email text not null unique ,
        password_hash text not null,
        role role not null,
        account_status employmentstatus default 'ACTIVE',
        created_at timestamp with time zone default now(),
        updated_at timestamp with time zone default now()
);