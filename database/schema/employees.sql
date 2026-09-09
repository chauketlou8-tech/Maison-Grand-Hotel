create table if not exists Employees(
        id serial primary key,
        employeeID text unique not null,
        firstName text not null,
        lastName text not null,
        email text unique not null,
        phoneNumber text not null,
        address_id int references address(id) on delete set null,
        dateOfBirth date not null,
        gender gender not null,
        jobPosition text not null,
        department text not null,
        salary numeric(10, 2) default 0 check (salary >= 0),
        hireDate date default now(),
        employmentStatus employmentstatus default 'ACTIVE',
        created_at timestamp with time zone default now(),
        updated_at timestamp with time zone default now()
);