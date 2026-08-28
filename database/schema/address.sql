create table if not exists Address (
    id serial primary key,
    street text,
    city text,
    province text,
    country text,
    postalCode text
)