from dotenv import load_dotenv
from playwright.sync_api import sync_playwright
from User import User
from openpyxl import Workbook
from openpyxl.styles import Font, PatternFill, Alignment, Border, Side

def main():
    browser = p.chromium.launch()
    rooms = ["KI", "EX", "QU", "DE", "SU", "ST", "DO", "PE", "SI", "FA"]

    glob_errors = []
    success_users = []
    failed_users = []

    headers = ["ID", "URL", "TIMES", "ERRORS"]

    wb = Workbook()
    ws = wb.active

if __name__ == "__main__":
    main()