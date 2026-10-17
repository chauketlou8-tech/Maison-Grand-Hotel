import os
import time
from dotenv import load_dotenv
import requests
from filelock import FileLock


class User:
    """
    Drives HTTP requests against the backend API directly.

    Opens routes by short name ("book", "findBooking", "rooms", ...) and
    records how long each call took.

    Attributes:
        base_route (str): Root of the API, read from BACKEND_URL in .env.
        routes (dict): Mapping of short name -> full URL.
        times (list): One entry per hitRoutes call, in milliseconds.
        errors (list): One string per caught exception or non-200 response.
        id (int): This user's id, incremented from the userCount file.
        url (str): The URL being acted on in the current call.
    """

    def __init__(self):
        """
        Load environment variables, build the route URL map, and assign
        this user an id by reading and incrementing the userCount file.
        """
        load_dotenv()

        self.base_route = os.getenv("BACKEND_URL") + "/api/v1/"
        self.booking_route = self.base_route + "book"
        self.findBooking_route = self.base_route + "book/find"
        self.rooms_route = self.base_route + "rooms"
        self.setting_route = self.base_route + "settings"

        self.routes = {
            "book": self.booking_route,
            "findBooking": self.findBooking_route,
            "rooms": self.rooms_route,
            "setting": self.setting_route,
        }

        self.times = []
        self.errors = []

        with FileLock("userCount.lock"):
            try:
                with open("userCount", "r") as f:
                    raw = f.read().strip()
            except FileNotFoundError:
                raw = ""

            if not raw:
                self.id = 1
            else:
                self.id = int(raw)

            with open("userCount", "w") as f:
                f.write(str(self.id + 1))

        self.url = None

    def __str__(self):
        return f"id: {self.id}\nurl: {self.url}\ntimes: {self.times}\nerrors: {self.errors}"

    def hitRoutes(self, route="rooms"):
        """
        Fire the correct HTTP request at the route's URL and record the timing.

        Args:
            route (str): Short name of the route ("book", "findBooking",
                "rooms", "setting", "payment").

        Returns:
            None. Populates self.times on every call and self.errors on
            non-200 or exception.
        """
        if route not in self.routes.keys():
            raise AttributeError(f"Invalid route: {route}")

        self.url = self.routes[route]
        payload = None

        if route == "book":
            room_names = [
                "Single Room",
                "Standard Room",
                "Double Room",
                "Queen Room",
                "King Room",
                "Deluxe Room",
                "Executive Room",
                "Family Room",
                "Suite",
                "Penthouse",
            ]
            # Max guests each room can hold (SI, ST, DO, QU, KI, DE, EX, FA, SU, PE)
            room_capacities = [1, 2, 2, 2, 2, 2, 2, 4, 4, 6]
            idx = self.id % len(room_names)
            payload = {
                "firstName": f"TestUserFirst{self.id}",
                "lastName": f"TestUserLast{self.id}",
                "email": f"user{self.id}@gmail.com",
                "phone": f"+27 21 000 {self.id:04d}",
                "idNumber": f"900101580{self.id:04d}",
                "nationality": "South African",
                "dateOfBirth": "1990-01-01",
                "address": {
                    "street": f"{self.id} Main St",
                    "city": "Cape Town",
                    "province": "Western Cape",
                    "country": "South Africa",
                    "postalCode": f"{7700 + self.id}",
                },
                "requests": f"Late check-in please {self.id}",
                "checkIn": "2026-11-01",
                "checkOut": "2026-11-06",
                "guests": room_capacities[idx],
                "selectedRoom": room_names[idx],
                "selectedPackages": [],
                "total": 1000,
            }
        elif route == "findBooking":
            payload = {"email": f"user{self.id}@gmail.com"}

        try:
            start = time.time()

            if payload is None:
                response = requests.get(self.url)
            else:
                response = requests.post(self.url, json=payload)

            elapsed = (time.time() - start) * 1000

            self.times.append(elapsed)

            if response.status_code != 200:
                self.errors.append(response)

            return
        except Exception as e:
            self.errors.append(str(e))
            return