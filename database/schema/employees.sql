create table if not exists Employees(
    id serial primary key,
    employeeID text,
    firstName text,
    lastName text,
    email text,
    phoneNumber text,
    address_id int references address(id),
    dateOfBirth date,
    gender gender,
    jobPosition text,
    department text,
    salary numeric(10, 2),
    hireDate date default now(),
    employmentStatus employmentstatus
)