create table if not exists Reviews (
                                       id serial primary key,
                                       name text not null check (length(trim(name)) > 0),
                                       email text not null check (position('@' in email) > 1),
                                       comment text not null check (length(trim(comment)) > 0),
                                       rating int check (rating >= 1 and rating <= 5),
                                       create_at timestamp with time zone not null default now(),
                                       updated_at timestamp with time zone not null default now()
);