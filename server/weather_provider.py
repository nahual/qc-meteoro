import logging
from utils import llog
import re
from datetime import datetime, timedelta
from utils import strings

class WeatherProvider:
    def __init__(self, name, log_handlers):
        self._log = llog.create_logger("")
        for handler in log_handlers:
            self._log.addHandler(handler)

from yaml import load, dump
class MockWeatherProvider(WeatherProvider):
    def __init__(self, log_handlers):
        WeatherProvider.__init__(self, "MockWeahterProvider", log_handlers)
        with open('cities.yaml', 'r') as f:
            self._cities = load(f)
            print self._cities
        self._cities_list = map(lambda c : strings.normalize(c), self._cities.values())          

    def get_cities(self, filtered):
        cities = self._cities_list
        if filtered:
            filtered = filtered.encode("UTF-8")
            matcher = re.compile(".*%s.*" % strings.normalize(filtered), re.IGNORECASE)
            cities = map(lambda c: {"code": c, "name": self._cities[c]}, filter(lambda c : matcher.match(c), self._cities_list))
        return cities

    def get_forecast(self, city):
        today = datetime.now()
        return [
                {'date': today.strftime("%Y-%m-%d"), 'temperature': 10, 'humidity': 60, 'uv': 10, 'chill': 20, 'status':'clear'},
                {'date': (today+timedelta(days=1)).strftime("%Y-%m-%d"), 'temperature': 20, 'humidity': 50, 'uv': 20, 'chill': 30, 'status':'rainy'},
                {'date': (today+timedelta(days=2)).strftime("%Y-%m-%d"), 'temperature': 20, 'humidity': 50, 'uv': 20, 'chill': 30, 'status':'rainy'},
                {'date': (today+timedelta(days=3)).strftime("%Y-%m-%d"), 'temperature': 20, 'humidity': 50, 'uv': 20, 'chill': 30, 'status':'rainy'}
               ]

