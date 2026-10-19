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
    ('EMP001', 'Lukhanyo Nqoko-Madikizela', 'lukhanyo.nqoko-madikizela@masonhotel.co.za', 'Lukhanyo!Nqoko88', 'ADMIN'),
    ('EMP002', 'Anele Sipho Maqoma',        'anele.maqoma@masonhotel.co.za',              'AneleMaqoma82',    'ADMIN'),
    ('EMP003', 'Asanda Zintle Jantjie',     'asanda.jantjie@masonhotel.co.za',            'AsandaJ#85',       'MANAGER'),
    ('EMP004', 'Amahle Nomsa Mbeki',        'amahle.mbeki@masonhotel.co.za',              'Amahle@Mbeki75',   'MANAGER'),
    ('EMP005', 'Naledi Mothibi',            'naledi.mothibi@masonhotel.co.za',            'NalediMothibi77',  'MANAGER'),
    ('EMP006', 'Liyabona Gxowa',            'liyabona.gxowa@masonhotel.co.za',            'LiyabonaGx79!',    'MANAGER'),

    # Front Desk / Guest Services (25)
    ('EMP100', 'Daniel Kowalski',           'daniel.kowalski@masonhotel.co.za',           'DanielKow85',      'MANAGER'),
    ('EMP101', 'Olwethu Bongani Madikizela','olwethu.madikizela@masonhotel.co.za',        'OlwethuM@83',      'RECEPTIONIST'),
    ('EMP102', 'Mateo Fernandez',           'mateo.fernandez@masonhotel.co.za',           'MateoFern90',      'RECEPTIONIST'),
    ('EMP103', 'Lwandile Qwabe-Pillay',     'lwandile.qwabe-pillay@masonhotel.co.za',     'Lwandile!Q97',     'RECEPTIONIST'),
    ('EMP104', 'Ntsako Mabunda',            'ntsako.mabunda@masonhotel.co.za',            'NtsakoMab87',      'RECEPTIONIST'),
    ('EMP105', 'Ayesha Khan',               'ayesha.khan@masonhotel.co.za',               'AyeshaKhan84',     'RECEPTIONIST'),
    ('EMP106', 'Rashied Adams',             'rashied.adams@masonhotel.co.za',             'Rashied@2000',     'RECEPTIONIST'),
    ('EMP107', 'Odirile Mogale',            'odirile.mogale@masonhotel.co.za',            'OdirileMog97',     'RECEPTIONIST'),
    ('EMP108', 'Mthokozisi Cele',           'mthokozisi.cele@masonhotel.co.za',           'MthokozisiC96',    'RECEPTIONIST'),
    ('EMP109', 'Zimasa Faku',               'zimasa.faku@masonhotel.co.za',               'ZimasaFaku88',     'RECEPTIONIST'),
    ('EMP110', 'Elena Petrova',             'elena.petrova@masonhotel.co.za',             'ElenaP@91',        'RECEPTIONIST'),
    ('EMP111', 'Mei Chen',                  'mei.chen@masonhotel.co.za',                  'MeiChen91!',       'RECEPTIONIST'),
    ('EMP112', 'Oliver Hughes',             'oliver.hughes@masonhotel.co.za',             'OliverHughes88',   'RECEPTIONIST'),
    ('EMP113', 'Aphiwe Sanele Nqakula',     'aphiwe.nqakula@masonhotel.co.za',            'AphiweNq98',       'RECEPTIONIST'),
    ('EMP114', 'Dikeledi Mahlangu',         'dikeledi.mahlangu@masonhotel.co.za',         'DikelediM88!',     'RECEPTIONIST'),
    ('EMP115', 'Sbonelo Zungu',             'sbonelo.zungu@masonhotel.co.za',             'SboneloZungu97',   'RECEPTIONIST'),
    ('EMP116', 'Shaun Williams',            'shaun.williams@masonhotel.co.za',            'ShaunW@92',        'RECEPTIONIST'),
    ('EMP117', 'Unathi Siphelele Mbeki',    'unathi.mbeki@masonhotel.co.za',              'UnathiMbeki95',    'RECEPTIONIST'),
    ('EMP118', 'Mulalo Netshidzivhe',       'mulalo.netshidzivhe@masonhotel.co.za',       'MulaloN98!',       'RECEPTIONIST'),
    ('EMP119', 'Sifiso Buthelezi',          'sifiso.buthelezi@masonhotel.co.za',          'SifisoButhelezi92','RECEPTIONIST'),
    ('EMP120', 'Xolani Baloyi',             'xolani.baloyi@masonhotel.co.za',             'XolaniB@97',       'RECEPTIONIST'),
    ('EMP121', 'Nobambele Ayanda Radebe',   'nobambele.radebe@masonhotel.co.za',          'NobambeleR96',     'RECEPTIONIST'),
    ('EMP122', 'Babalwa Mtya-Daniels',      'babalwa.mtya-daniels@masonhotel.co.za',      'Babalwa!Mtya97',   'RECEPTIONIST'),
    ('EMP123', 'Boitumelo Modise',          'boitumelo.modise@masonhotel.co.za',          'BoitumeloMod96',   'RECEPTIONIST'),
    ('EMP124', 'Mandla Sithole',            'mandla.sithole@masonhotel.co.za',            'MandlaSithole94',  'RECEPTIONIST'),

    # Housekeeping Supervisors/Manager (4)
    ('EMP200', 'Masingita Maluleke',        'masingita.maluleke@masonhotel.co.za',        'MasingitaM78!',    'HOUSEKEEPING'),
    ('EMP201', 'Hannah Schmidt',            'hannah.schmidt@masonhotel.co.za',            'HannahSchmidt86',  'HOUSEKEEPING'),
    ('EMP202', 'Chantelle Petersen-Mkhize', 'chantelle.petersen-mkhize@masonhotel.co.za', 'ChantellePM83',    'HOUSEKEEPING'),
    ('EMP203', 'Nolwazi Tshabalala',        'nolwazi.tshabalala@masonhotel.co.za',        'NolwaziTsh86!',    'HOUSEKEEPING'),

    # Maintenance Manager (1)
    ('EMP300', 'Bongiwe Ntsika',            'bongiwe.ntsika@masonhotel.co.za',            'BongiweNtsika78',  'MAINTENANCE'),

    # Restaurant Supervisor (1)
    ('EMP508', 'Amelia Smith',              'amelia.smith@masonhotel.co.za',              'AmeliaSmith92!',   'STAFF'),

    # Administrative (12)
    ('EMP600', 'Isabella Garcia',           'isabella.garcia@masonhotel.co.za',           'IsabellaG83',      'MANAGER'),
    ('EMP601', 'Tendani Nethengwe',         'tendani.nethengwe@masonhotel.co.za',         'TendaniN90!',      'STAFF'),
    ('EMP602', 'Lucas Martin',              'lucas.martin@masonhotel.co.za',              'LucasMartin82',    'STAFF'),
    ('EMP603', 'Nokuthula Mkhwanazi',       'nokuthula.mkhwanazi@masonhotel.co.za',       'NokuthulaM01!',    'STAFF'),
    ('EMP604', 'Nompumelelo Dube',          'nompumelelo.dube@masonhotel.co.za',          'NompumeleloD98',   'STAFF'),
    ('EMP605', 'Khensani Mabunda',          'khensani.mabunda@masonhotel.co.za',          'KhensaniMab85',    'STAFF'),
    ('EMP606', 'Thembeka Gxashe',           'thembeka.gxashe@masonhotel.co.za',           'ThembekaG99!',     'STAFF'),
    ('EMP607', 'Rendani Mudau',             'rendani.mudau@masonhotel.co.za',             'RendaniMud97',     'STAFF'),
    ('EMP608', 'Mapula Molepo',             'mapula.molepo@masonhotel.co.za',             'MapulaMolepo86',   'STAFF'),
    ('EMP609', 'Siyabonga Hadebe',          'siyabonga.hadebe@masonhotel.co.za',          'SiyabongaH93!',    'STAFF'),
    ('EMP610', 'Nokwanda Shabalala',        'nokwanda.shabalala@masonhotel.co.za',        'NokwandaSh95',     'STAFF'),
    ('EMP611', 'Zanele Mthembu',            'zanele.mthembu@masonhotel.co.za',            'ZaneleMthembu96',  'STAFF'),

    # Owner (1)
    ('EMP900', 'Tlou Elvis Chauke',         'owner@masonhotel.co.za',                     'TemaSecondary0909@','ADMIN'),
]

for employeeid, name, email, password, role in users:
    employee_id = employees.get(employeeid)
    if employee_id is None:
        print(f"Employee {employeeid} not found, skipping")
        continue

    password_hash = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt())

    cur.execute("""
        insert into users (employee_id, name, email, password_hash, role, account_status)
        values (%s, %s, %s, %s, %s, 'ACTIVE')
    """, (employee_id, name, email, password_hash.decode('utf-8'), role))

conn.commit()
cur.close()
conn.close()

print("Users seeded successfully!")