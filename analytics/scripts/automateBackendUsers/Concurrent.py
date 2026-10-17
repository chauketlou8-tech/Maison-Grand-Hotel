from User import User
from openpyxl import Workbook
from openpyxl.styles import Font, PatternFill, Alignment, Border, Side
import threading


def main():
    routes = ["book", "findBooking", "rooms", "setting"]

    glob_errors = []
    success_users = []
    failed_users = []

    lock = threading.Lock()

    def run_user():
        user = User()
        user.hitRoutes(routes[user.id % len(routes)])

        with lock:
            if len(user.errors) != 0:
                glob_errors.append(user.errors[0])
                failed_users.append(user)
            else:
                success_users.append(user)

        print(f"user {user.id} done!!")

    threads = []

    for _ in range(1000):
        t = threading.Thread(target=run_user)
        threads.append(t)
        t.start()

    for t in threads:
        t.join()

    print("Errors: ", len(glob_errors), glob_errors)
    print("Successes: ", len(success_users), success_users)
    print("Failed users: ", len(failed_users), failed_users)

    headers = ["ID", "URLS", "TIMES", "ERRORS"]
    wb = Workbook()
    ws = wb.active

    for col_num, header in enumerate(headers, start=1):
        cell = ws.cell(row=1, column=col_num)
        cell.value = header
        cell.font = Font(bold=True, color="FFFFFF")
        cell.fill = PatternFill("solid", fgColor="4472C4")
        cell.alignment = Alignment(horizontal="center")

    records = success_users + failed_users

    for rec in records:
        ws.append([
            rec.id,
            rec.url if rec.url else "",
            rec.times[0] if len(rec.times) != 0 else "",
            str(rec.errors[0]) if len(rec.errors) != 0 else "",
        ])

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

    wb.save("report1000RoutesPar.xlsx")
    print("saved!")

    file = open("userCount", "w")
    file.write("1")


if __name__ == "__main__":
    main()