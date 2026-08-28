create table if not exists Guests(
                                     id serial primary key,
                                     guestID text,
                                     firstName text,
                                     lastName text,
                                     email text,
                                     phoneNumber text,
                                     address_id int references address(id),
                                     idNumber text,
                                     dateOfBirth date,
                                     nationality text,
                                     registrationDate date default now()
)