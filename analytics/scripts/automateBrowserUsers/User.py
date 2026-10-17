import dotenv
import os
from playwright.sync_api import sync_playwright
import time


class User:
    """
    Drives a real Chromium browser against the Maison Grand Hotel site.

    Opens pages by short name ("home", "rooms", "booking", ...), optionally
    fills and submits the booking form, and records how long each call took.

    Attributes:
        base_url (str): Root of the site, read from BASE_URL in .env.
        pages (dict): Mapping of short name -> full URL.
        times (list): One entry per getPage call, in milliseconds.
        errors (list): One string per caught exception.
        playwright: The Playwright driver.
        browser: The launched Chromium browser.
        page: The current tab (recreated per getPage call).
        url: The URL being acted on in the current call.
    """

    def __init__(self, browser=None):
        """
        Start Playwright, launch Chromium (or reuse a passed-in browser),
        and set up the page URL map.

        Args:
            browser: An existing Playwright browser to reuse. If None, a
                new Chromium instance is launched.
        """
        dotenv.load_dotenv()

        self.base_url = os.getenv("BASE_URL")
        self.about_url = self.base_url + "/about"
        self.rooms_url = self.base_url + "/rooms"
        self.amenities_url = self.base_url + "/amenities"
        self.dining_url = self.base_url + "/dining"
        self.book_url = self.base_url + "/booking"

        self.pages = {
            "home": self.base_url,
            "about": self.about_url,
            "rooms": self.rooms_url,
            "amenities": self.amenities_url,
            "dining": self.dining_url,
            "booking": self.book_url,
        }

        self.times = []
        self.errors = []

        try:
            file = open("userCount", "r")
            id = int(file.readline().strip())

            self.id = id
            id += 1

            file = open("userCount", "w")
            file.write(str(id))
        except:
            f = open("userCount", "r")
            id = f.readline().strip()

            if not id:
                self.id = 1
                f = open("userCount", "w")
                f.write("1")
            else:
                self.id = int(id)
                f = open("userCount", "w")
                f.write(id+1)

        if browser is None:
            self.playwright = sync_playwright().start()
            self.browser = self.playwright.chromium.launch()
        else:
            self.playwright = None
            self.browser = browser

        self.page = None
        self.url = None

    def __str__(self):
        return f"id: {self.id}\ncurr_url: {self.url}\ntimes: {self.times}\nerrors: {self.errors}"

    def getPage(self, page="home", room="EX", firstName="TestUserFirst", lastName="TestUserLast", email="user@gmail.com", phone="+27 21 000 0000", idNumber="9001015800085", nationality="South African", street="123 Main St", city="Cape Town", province="Western Cape", country="South Africa", postal="7700", requests="Late check-in please", adults="2", children="0", checkIn="2026-11-01", checkOut="2026-11-06"):
        """
        Open a page by short name. If the name is "booking", run the full
        booking form flow with the given details.

        Args:
            page (str): Short name of the page ("home", "rooms", "booking", ...).
            room (str): Room type button to click on the booking page.
            firstName (str): Guest first name.
            lastName (str): Guest last name.
            email (str): Guest email.
            phone (str): Guest phone number.
            idNumber (str): Guest ID number.
            nationality (str): Guest nationality.
            street (str): Address street.
            city (str): Address city.
            province (str): Address province.
            country (str): Address country.
            postal (str): Address postal code.
            requests (str): Special requests text.
            adults (str): Number of adults as a string.
            children (str): Number of children as a string.
            checkIn (str): Check-in date as YYYY-MM-DD.
            checkOut (str): Check-out date as YYYY-MM-DD.

        Returns:
            None. Populates self.times on success, self.errors on failure.
        """
        self.page = self.browser.new_page()

        if page not in self.pages.keys():
            raise Exception(f"Page not found: {page}")

        if not self.page:
            raise Exception(f"Browser page missing")

        self.url = self.pages[page]

        if not self.url:
            raise Exception(f"Url missing")

        try:
            startTime = time.time()

            if page.lower() == "booking":
                self.getBookingPage(room, firstName, lastName, email, phone, idNumber, nationality, street, city, province, country, postal, requests, adults, children, checkIn, checkOut)
                self.times.append((time.time() - startTime) * 1000)

                url = self.page.url
                self.close()

                return url

            self.page.goto(self.url)
            self.page.wait_for_load_state("networkidle")

            self.times.append((time.time() - startTime) * 1000)
            return self.page.url
            #self.stop()

        except Exception as e:
            self.errors.append(str(e))

    def getBookingPage(self, room, firstName, lastName, email, phone, idNumber, nationality, street, city, province, country, postal, requests, adults, children, checkIn, checkOut):
        """
        Fill and submit the booking form with the given details.

        Assumes self.url already points at the booking page. Clicks the
        room button, fills every field, and submits.

        Args:
            room (str): Room type button to click (must match button text).
            firstName (str): Guest first name.
            lastName (str): Guest last name.
            email (str): Guest email.
            phone (str): Guest phone number.
            idNumber (str): Guest ID number.
            nationality (str): Guest nationality.
            street (str): Address street.
            city (str): Address city.
            province (str): Address province.
            country (str): Address country.
            postal (str): Address postal code.
            requests (str): Special requests text.
            adults (str): Number of adults as a string.
            children (str): Number of children as a string.
            checkIn (str): Check-in date as YYYY-MM-DD.
            checkOut (str): Check-out date as YYYY-MM-DD.
        """
        self.page.goto(self.url)
        self.page.wait_for_selector("h2:has-text('Choose Your Room')", timeout=30000)

        self.page.wait_for_selector(f'button:has-text("{room}"):not([disabled])',timeout=30000)
        self.page.click(f'button:has-text("{room}")')

        self.page.fill("#firstName", firstName)
        self.page.fill("#lastName", lastName)
        self.page.fill("#email", email)
        self.page.fill("#phone", phone)
        self.page.fill("#idNumber", idNumber)
        self.page.fill("#nationality", nationality)
        self.page.fill("#street", street)
        self.page.fill("#city", city)
        self.page.fill("#province", province)
        self.page.fill("#country", country)
        self.page.fill("#postalCode", postal)
        self.page.fill("#requests", requests)

        self.page.select_option("#adults", str(adults))
        self.page.select_option("#children", str(children))

        self.page.fill("#checkIn", checkIn)
        self.page.fill("#checkOut", checkOut)

        self.page.click("#submit")

    def close(self):
        self.page.close()
        if self.playwright is not None:
            self.browser.close()
            self.playwright.stop()