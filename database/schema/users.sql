create table if not exists Users(
        id serial primary key,
        employee_id int references Employees(id) on delete cascade,
        username text unique not null,
        password_hash text not null,
        role role not null,
        account_status employmentstatus default 'ACTIVE',
        created_at timestamp with time zone default now(),
        updated_at timestamp with time zone default now()
);