create table if not exists Reports (
    id serial primary key,
    report_date date not null unique,
    total_rooms int not null,
    occupied_rooms int not null,
    available_rooms int not null,
    maintenance_rooms int not null,
    arrivals int not null,
    departures int not null,
    in_house int not null,
    revenue numeric(12, 2) not null default 0,
    tasks_created int not null default 0,
    tasks_completed int not null default 0,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);