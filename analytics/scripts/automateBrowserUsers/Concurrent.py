from dotenv import load_dotenv
from playwright.sync_api import sync_playwright
from User import User
from openpyxl import Workbook
from openpyxl.styles import Font, PatternFill, Alignment, Border, Side
from multiprocessing import Process, Manager

def create_user(rooms, errors, success_users, failed_users):
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        user = User(browser=browser)

        try:
            user_urls = []
            user_times = []

            booking_url = user.getPage(
                "booking",
                room=rooms[user.id % len(rooms)],
                firstName=f"TestUserFirst{user.id}",
                lastName=f"TestUserLast{user.id}",
                email=f"user{user.id}@gmail.com",
                phone=f"+27 21 000 {user.id:04d}",
                idNumber=f"900101580{user.id:04d}",
                nationality="South African",
                street=f"{user.id} Main St",
                city="Cape Town",
                province="Western Cape",
                country="South Africa",
                postal=f"{7700 + user.id}",
                requests=f"Late check-in please {user.id}",
                adults="2",
                children="0",
                checkIn="2026-11-01",
                checkOut="2026-11-06",
            )

            if booking_url:
                user_urls.append(booking_url)
                if len(user.times) != 0:
                    user_times.append(user.times[-1])

            if len(user.errors) == 0:
                find_url = user.getPage(
                    "findBooking",
                    email=f"user{user.id}@gmail.com",
                )
                if find_url:
                    user_urls.append(find_url)
                    if len(user.times) != 0:
                        user_times.append(user.times[-1])

            record = {
                "id": user.id,
                "urls": user_urls,
                "times": user_times,
                "errors": user.errors,
            }

            if len(user.errors) != 0:
                errors.append(user.errors[0])
                failed_users.append(record)
            else:
                success_users.append(record)

            print(f"user {user.id} done!!")

        finally:
            browser.close()

def main():
    with Manager() as manager:
        glob_errors = manager.list()
        success_users = manager.list()
        failed_users = manager.list()

        rooms = ["KI", "EX", "QU", "DE", "SU", "ST", "DO", "PE", "SI", "FA"]

        processes = []
        for _ in range(2):
            p = Process(target=create_user, args=(rooms, glob_errors, success_users, failed_users, ))
            processes.append(p)
            p.start()

        for p in processes:
            p.join()

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

        records = list(success_users) + list(failed_users)

        for rec in records:
            ws.append([
                rec["id"],
                ", ".join(str(u) for u in rec["urls"]),
                ", ".join(str(t) for t in rec["times"]),
                ", ".join(str(e) for e in rec["errors"]),
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

        wb.save("reportWithgetBookingCon.xlsx")
        print("saved!")

        file = open("userCount", "w")
        file.write("1")

if __name__ == "__main__":
    main()