create table if not exists serviceitems (
    id serial primary key,
    service_id int not null references services(id) on delete cascade,
    name text not null,
    category text not null,
    description text,
    price numeric(10, 2) not null default 0,
    duration text,
    active boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);