import os
import time
import random

from dotenv import load_dotenv
from locust import User as LocustUser, task, between

from User import User

load_dotenv()


class HotelUser(LocustUser):
    wait_time = between(1, 5)

    def on_start(self):
        self.user = User()

    def _fire(self, name, request_type, start_time, exception=None):
        """Helper to report a custom request to Locust."""
        self.environment.events.request.fire(
            request_type=request_type,
            name=name,
            response_time=(time.time() - start_time) * 1000,
            response_length=0,
            exception=exception,
        )

    @task(4)
    def book(self):
        start = time.time()
        self.user.hitRoutes("book")
        exception = None
        if self.user.errors:
            exception = self.user.errors[-1]
            self.user.errors.clear() # clear so we don't double-count
        self._fire("book", "POST", start, exception)

    @task(4)
    def findBooking(self):
        start = time.time()
        self.user.hitRoutes("findBooking")
        exception = None
        if self.user.errors:
            exception = self.user.errors[-1]
            self.user.errors.clear()
        self._fire("findBooking", "POST", start, exception)

    @task(3)
    def rooms(self):
        start = time.time()
        self.user.hitRoutes("rooms")
        exception = None
        if self.user.errors:
            exception = self.user.errors[-1]
            self.user.errors.clear()
        self._fire("rooms", "GET", start, exception)

    @task(2)
    def setting(self):
        start = time.time()
        self.user.hitRoutes("setting")
        exception = None
        if self.user.errors:
            exception = self.user.errors[-1]
            self.user.errors.clear()
        self._fire("setting", "GET", start, exception)