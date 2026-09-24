insert into Services (name, description, recurring) values
    ('Cleaning', 'Standard room cleaning', true),
    ('Inspection', 'Inspect room for damages and missing items', false),
    ('Spa', 'Spa and wellness treatments', true),
    ('Dining', 'Restaurant and in-room dining', true),
    ('Transport', 'Airport transfers and city transport', false),
    ('Activities', 'Tours and experiences', false),
    ('Fitness', 'Gym and personal training', true),
    ('Maintenance', 'Repairs and upkeep', false)
on conflict (name) do nothing;