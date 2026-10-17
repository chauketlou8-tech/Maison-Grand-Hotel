from User import User
from openpyxl import Workbook
from openpyxl.styles import Font, PatternFill, Alignment, Border, Side
import threading


def main():
    users = [User() for _ in range(1000)]
    pages = ["home", "about", "rooms", "amenities", "dining", "booking"]

    glob_errors = []
    success_users = []
    failed_users = []

    lock = threading.Lock()

    def run_user(user):
        user.get(pages[user.id % len(pages)])

        with lock:
            if len(user.errors) != 0:
                glob_errors.append(user.errors[0])
                failed_users.append(user)
            else:
                success_users.append(user)

        print(f"user {user.id} done!!")

    threads = []

    for user in users:
        t = threading.Thread(target=run_user, args=(user,))
        threads.append(t)
        t.start()

    for t in threads:
        t.join()

    print("Errors: ", len(glob_errors), glob_errors)
    print("Successes: ", len(success_users), success_users)
    print("Failed users: ", len(failed_users), failed_users)

    headers = ["ID", "TIMES", "ERRORS"]
    wb = Workbook()
    ws = wb.active

    for col_num, header in enumerate(headers, start=1):
        cell = ws.cell(row=1, column=col_num)
        cell.value = header
        cell.font = Font(bold=True, color="FFFFFF")
        cell.fill = PatternFill("solid", fgColor="4472C4")
        cell.alignment = Alignment(horizontal="center")

    records = success_users + failed_users
    data = [[] for _ in range(len(records))]

    for i, rec in enumerate(records, start=1):
        data[i-1].append(rec.id)
        data[i-1].append(rec.times[0] if len(rec.times) != 0 else "")
        data[i-1].append(rec.errors[0] if len(rec.errors) != 0 else "")

    for row in data:
        ws.append(row)

    thin = Side(style="thin")
    for row in ws.iter_rows():
        for cell in row:
            cell.border = Border(left=thin, right=thin, top=thin, bottom=thin)

    for column in ws.columns:
        max_length = 0
        column_letter = column[0].column_letter
        for cell in column:
            if cell.value:
                max_length = max(max_length, len(str(cell.value)))
        ws.column_dimensions[column_letter].width = max_length + 2

    wb.save("report1000Par.xlsx")
    print("saved!")

    file = open("userCount", "w")
    file.write("1")


if __name__ == "__main__":
    main()