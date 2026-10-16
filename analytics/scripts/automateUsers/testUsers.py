from dotenv import load_dotenv
from playwright.sync_api import sync_playwright
from User import User
from openpyxl import Workbook
from openpyxl.styles import Font, PatternFill, Alignment, Border, Side

def main():
    with sync_playwright() as p:
        browser = p.chromium.launch()

        users = [User(browser=browser) for _ in range(1000)]
        rooms = ["KI", "EX", "QU", "DE", "SU", "ST", "DO", "PE", "SI", "FA"]

        glob_errors = []
        success_users = []
        failed_users = []

        headers = ["ID", "URL", "TIMES", "ERRORS"]

        wb = Workbook()
        ws = wb.active

        for user in users:
            user.getPage(
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
                adults="1",
                children="0",
                checkIn="2026-11-01",
                checkOut="2026-11-06",
            )

            if len(user.errors) != 0:
                glob_errors.append(user.errors[0])
                failed_users.append(user)
            else:
                success_users.append(user)

            print(f"user {user.id} done!!")


        print("Errors: ", len(glob_errors), glob_errors)
        print("Successes: ", len(success_users), success_users)
        print("Failed users: ", len(failed_users), failed_users)

        for col_num, header in enumerate(headers, start=1):
            cell = ws.cell(row=1, column=col_num)
            cell.value = header

            cell.font = Font(bold=True, color="FFFFFF")
            cell.fill = PatternFill("solid", fgColor="4472C4")
            cell.alignment = Alignment(horizontal="center")

        users = success_users + failed_users

        data = [[] for _ in range(len(users))]
        print(data)

        for i, user in enumerate(users, start=1):
            data[i-1].append(user.id)
            data[i-1].append(user.url)
            data[i-1].append(user.times[0] if len(user.times) != 0 else "")
            data[i-1].append(user.times[0] if len(user.errors) != 0 else "")

        for row in data:
            ws.append(row)

        thin = Side(style="thin")
        for row in ws.iter_rows():
            for cell in row:
                cell.border = Border(
                    left=thin,
                    right=thin,
                    top=thin,
                    bottom=thin
                )

        for column in ws.columns:
            max_length = 0
            column_letter = column[0].column_letter

            for cell in column:
                if cell.value:
                    max_length = max(max_length, len(str(cell.value)))

            ws.column_dimensions[column_letter].width = max_length + 2

        wb.save("report1000.xlsx")
        print("saved!")


        browser.close()

if __name__ == "__main__":
    main()