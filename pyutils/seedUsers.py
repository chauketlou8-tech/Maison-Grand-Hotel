import bcrypt
import psycopg2

conn = psycopg2.connect(
    host="localhost",
    port=3001,
    database="masongrandhotel",
    user="postgres",
    password="TemaSecondary0909@"
)

cur = conn.cursor()

cur.execute("delete from users")

cur.execute("select id, employeeid from employees")
employees = {row[1]: row[0] for row in cur.fetchall()}

users = [
    # Management (6)
    ('EMP001', 'amahle.mbeki', 'Amahle1980', 'ADMIN'),
    ('EMP002', 'lukhanyo.nqoko', 'Lukhanyo2020', 'ADMIN'),
    ('EMP003', 'asanda.jantjie', 'Asanda2026', 'MANAGER'),
    ('EMP004', 'bongiwe.ntsika', 'Bongiwe2026', 'MANAGER'),
    ('EMP005', 'anele.maqoma', 'AneleHotel', 'MANAGER'),
    ('EMP006', 'liyabona.gxowa', 'Liyabona2026', 'MANAGER'),

    # Front Desk / Guest Services (25)
    ('EMP100', 'olwethu.madikizela', 'Olwethu123', 'MANAGER'),
    ('EMP101', 'nolwazi.tshabalala', 'Nolwazi2026', 'RECEPTIONIST'),
    ('EMP102', 'yandisa.mkonto', 'Yandisa2026', 'RECEPTIONIST'),
    ('EMP103', 'zimasa.faku', 'Zimasa2026', 'RECEPTIONIST'),
    ('EMP104', 'siyabulela.ntlabati', 'Siyabulela2026', 'RECEPTIONIST'),
    ('EMP105', 'nobambele.radebe', 'Nobambele2026', 'RECEPTIONIST'),
    ('EMP106', 'lwandile.qwabe', 'Lwandile2026', 'RECEPTIONIST'),
    ('EMP107', 'noluthando.maqoma', 'Noluthando2026', 'RECEPTIONIST'),
    ('EMP108', 'aphiwe.nqakula', 'Aphiwe2026', 'RECEPTIONIST'),
    ('EMP109', 'babalwa.mtya', 'Babalwa2026', 'RECEPTIONIST'),
    ('EMP110', 'ntsika.mavuso', 'Ntsika2026', 'RECEPTIONIST'),
    ('EMP111', 'noxolo.mabaso', 'Noxolo2026', 'RECEPTIONIST'),
    ('EMP112', 'khwezi.jantjies', 'Khwezi2026', 'RECEPTIONIST'),
    ('EMP113', 'zintle.mthethwa', 'Zintle2026', 'RECEPTIONIST'),
    ('EMP114', 'lunga.gqoba', 'Lunga2026', 'RECEPTIONIST'),
    ('EMP115', 'yamkela.mgwali', 'Yamkela2026', 'RECEPTIONIST'),
    ('EMP116', 'unathi.mbeki', 'Unathi2026', 'RECEPTIONIST'),
    ('EMP117', 'siyanda.ntshinga', 'Siyanda2026', 'RECEPTIONIST'),
    ('EMP118', 'nosipho.jantjie', 'Nosipho2026', 'RECEPTIONIST'),
    ('EMP119', 'lulama.qunta', 'Lulama2026', 'RECEPTIONIST'),
    ('EMP120', 'thembeka.gxashe', 'Thembeka2026', 'RECEPTIONIST'),
    ('EMP121', 'ayabonga.mabandla', 'Ayabonga2026', 'RECEPTIONIST'),
    ('EMP122', 'aaliyah.davids', 'Aaliyah2026', 'RECEPTIONIST'),
    ('EMP123', 'rashied.adams', 'Rashied2026', 'RECEPTIONIST'),
    ('EMP124', 'kayla.abrahams', 'Kayla2026', 'RECEPTIONIST'),

    # Housekeeping Supervisors/Manager (4)
    ('EMP200', 'jared.jacobs', 'Jared2026', 'HOUSEKEEPING'),
    ('EMP201', 'chantelle.petersen', 'Chantelle2026', 'HOUSEKEEPING'),
    ('EMP202', 'ricardo.fortuin', 'Ricardo2026', 'HOUSEKEEPING'),
    ('EMP203', 'nadine.cupido', 'Nadine2026', 'HOUSEKEEPING'),

    # Maintenance Manager (1)
    ('EMP300', 'naledi.mothibi', 'Naledi2026', 'MAINTENANCE'),

    # Restaurant Supervisor (1)
    ('EMP508', 'priya.govender', 'Priya2026', 'STAFF'),

    # Administrative (12)
    ('EMP600', 'daniel.kowalski', 'Daniel2026', 'MANAGER'),
    ('EMP601', 'amelia.smith', 'Amelia2026', 'STAFF'),
    ('EMP602', 'oliver.hughes', 'Oliver2026', 'STAFF'),
    ('EMP603', 'camille.dubois', 'Camille2026', 'STAFF'),
    ('EMP604', 'lucas.martin', 'Lucas2026', 'STAFF'),
    ('EMP605', 'hannah.schmidt', 'Hannah2026', 'STAFF'),
    ('EMP606', 'lukas.weber', 'Lukas2026', 'STAFF'),
    ('EMP607', 'isabella.garcia', 'Isabella2026', 'STAFF'),
    ('EMP608', 'mateo.fernandez', 'Mateo2026', 'STAFF'),
    ('EMP609', 'yuki.tanaka', 'Yuki2026', 'STAFF'),
    ('EMP610', 'ren.sato', 'Ren2026', 'STAFF'),
    ('EMP611', 'mei.chen', 'Mei2026', 'STAFF'),

    # Owner (1)
    ('EMP900', 'tlou.chauke', 'Mason2026', 'ADMIN')
]

for employeeid, username, password, role in users:
    employee_id = employees.get(employeeid)
    if employee_id is None:
        print(f"Employee {employeeid} not found, skipping")
        continue

    password_hash = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt())

    cur.execute("""
        insert into users (employee_id, username, password_hash, role, account_status)
        values (%s, %s, %s, %s, 'ACTIVE')
    """, (employee_id, username, password_hash.decode('utf-8'), role))

conn.commit()
cur.close()
conn.close()

print("Users seeded successfully!")