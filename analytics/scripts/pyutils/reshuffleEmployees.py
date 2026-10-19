import psycopg2

conn = psycopg2.connect(
    host="localhost",
    port=3001,
    database="masongrandhotel",
    user="postgres",
    password="TemaSecondary0909@"
)

cur = conn.cursor()

# Seniority bands -> which employeeid prefixes belong to each band.
# People are shuffled ONLY within their band so a 22-year-old doesn't become GM.
BANDS = {
    "executive":     ["EMP001", "EMP002", "EMP003", "EMP004", "EMP005", "EMP006", "EMP200", "EMP300", "EMP500"],
    "supervisory":   ["EMP100", "EMP101", "EMP201", "EMP202", "EMP203", "EMP501", "EMP502", "EMP503", "EMP508", "EMP600", "EMP601", "EMP605", "EMP607"],
    "standard":      ["EMP102", "EMP103", "EMP104", "EMP105", "EMP106", "EMP107", "EMP108",
                      "EMP109", "EMP110", "EMP111", "EMP112", "EMP113", "EMP114", "EMP115",
                      "EMP116", "EMP117", "EMP118", "EMP119", "EMP120", "EMP121", "EMP122",
                      "EMP123", "EMP124","EMP204", "EMP205", "EMP206", "EMP207", "EMP208",
                      "EMP209", "EMP210","EMP211", "EMP212", "EMP213", "EMP214", "EMP215",
                      "EMP216", "EMP217","EMP218", "EMP219", "EMP220", "EMP221", "EMP222",
                      "EMP223", "EMP224","EMP225", "EMP226", "EMP227", "EMP228", "EMP229",
                      "EMP230", "EMP231", "EMP232", "EMP233", "EMP234", "EMP235", "EMP236",
                      "EMP237", "EMP238","EMP239","EMP301", "EMP302", "EMP303", "EMP304",
                      "EMP305", "EMP306", "EMP307","EMP308", "EMP309", "EMP310", "EMP311",
                      "EMP504", "EMP505", "EMP506", "EMP507", "EMP509", "EMP510", "EMP511",
                      "EMP512", "EMP513", "EMP514", "EMP515", "EMP516", "EMP517", "EMP518",
                      "EMP519", "EMP520", "EMP521", "EMP522", "EMP523", "EMP524","EMP602",
                      "EMP603", "EMP604", "EMP606", "EMP608", "EMP609", "EMP610", "EMP611"],
    "entry":         ["EMP700", "EMP701", "EMP702", "EMP703", "EMP704","EMP800", "EMP801", "EMP802", "EMP803"],
}

# Owner is never touched
OWNER = "EMP900"


def band_of(employeeid):
    for band, ids in BANDS.items():
        if employeeid in ids:
            return band
    return None


# Pull every employee
cur.execute("""
    select employeeid, firstname, lastname, email, phonenumber, address_id,
           dateofbirth, gender, jobposition, department, salary, hiredate,
           employmentstatus
    from employees
    order by employeeid
""")
rows = cur.fetchall()

columns = ["employeeid", "firstname", "lastname", "email", "phonenumber", "address_id",
           "dateofbirth", "gender", "jobposition", "department", "salary", "hiredate",
           "employmentstatus"]

employees = [dict(zip(columns, r)) for r in rows]

# Person-owned columns: travel with the person
PERSON_COLS = ["firstname", "lastname", "email", "phonenumber", "address_id",
               "dateofbirth", "gender"]

# Hotel-owned columns: tied to the slot
SLOT_COLS = ["employeeid", "jobposition", "department", "salary", "hiredate",
             "employmentstatus"]

# Group by band, keeping the owner separate
by_band = {}
for emp in employees:
    b = band_of(emp["employeeid"])
    if b is None:
        if emp["employeeid"] != OWNER:
            print(f"WARNING: {emp['employeeid']} not in any band, skipping shuffle for this row")
        continue
    by_band.setdefault(b, []).append(emp)

owner_row = next((e for e in employees if e["employeeid"] == OWNER), None)

# For each band, extract slots and people, shuffle people, and reapply
import random
random.seed()  # non-deterministic

for band, emps in by_band.items():
    slots = [{col: e[col] for col in SLOT_COLS} for e in emps]
    people = [{col: e[col] for col in PERSON_COLS} for e in emps]

    # Shuffle people, assign to slots. Keep slots in order so employeeid prefix
    # still matches the department.
    random.shuffle(people)

    # Write back into DB
    for slot, person in zip(slots, people):
        cur.execute("""update employees set firstname = %s, lastname = %s, email = %s, phonenumber = %s, address_id = %s, dateofbirth = %s,gender = %s where employeeid = %s""", (
            person["firstname"],
            person["lastname"],
            person["email"],
            person["phonenumber"],
            person["address_id"],
            person["dateofbirth"],
            person["gender"],
            slot["employeeid"]
        ))

conn.commit()
cur.close()
conn.close()

print("Shuffle complete.")