create table if not exists ServiceTasks (
    id serial primary key,
    service_id int not null references Services(id) on delete cascade,
    room_id int references Rooms(roomid) on delete set null,
    assigned_to int references employees(id) on delete set null,
    status text not null default 'pending',
    scheduled_for timestamp with time zone,
    completed_at timestamp with time zone,
    notes text,
    created_at timestamp with time zone default now(),
    updated_at timestamp with time zone default now()
);