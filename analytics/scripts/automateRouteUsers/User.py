import os
import time
import dotenv
import requests as r


class User:
    """
    Drives HTTP requests against the Maison Grand Hotel site.

    Opens pages by short name ("home", "rooms", "booking", ...) and records
    how long each call took.

    Attributes:
        base_url (str): Root of the site, read from BASE_URL in .env.
        pages (dict): Mapping of short name -> full URL.
        times (list): One entry per get call, in milliseconds.
        errors (list): One string per caught exception.
        id (int): This user's id, incremented from the userCount file.
    """

    def __init__(self):
        """
        Load environment variables, build the page URL map, and assign
        this user an id by reading and incrementing the userCount file.
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
                f.write(str(id+1))

    def __str__(self):
        return f"id: {self.id}\ntimes: {self.times}\nerrors: {self.errors}"

    def get(self, page="home"):
        """
        Fire an HTTP GET at the page's URL and record the timing.

        Args:
            page (str): Short name of the page ("home", "rooms", "booking", ...).

        Returns:
            The requests.Response on success, None on failure. Populates
            self.times on every call and self.errors on non-200 or exception.
        """
        if page not in self.pages.keys():
            raise Exception(f"Page not found: {page}")

        url = self.pages[page]

        try:
            start = time.time()
            response = r.get(url)
            elapsed = (time.time() - start) * 1000

            self.times.append(elapsed)

            if response.status_code != 200:
                self.errors.append(f"{response.status_code} on {url}")

            return
        except Exception as e:
            self.errors.append(str(e))
            return