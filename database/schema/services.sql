create table if not exists Services (
    id serial primary key,
    name text not null unique,
    description text,
    recurring boolean not null default false,
    created_at timestamp with time zone default now(),
    updated_at timestamp with time zone default now()
);