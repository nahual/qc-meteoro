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

from yaml import load
class MockWeatherProvider(WeatherProvider):
    def __init__(self, log_handlers):
        WeatherProvider.__init__(self, "MockWeahterProvider", log_handlers)
        with open('cities.yaml', 'r') as f:
            self._cities = load(f)
        self._cities_list = map(lambda c : strings.normalize(c), self._cities.values())          

    def get_cities(self, filtered):
        cities = self._cities_list
        if filtered:
            filtered = filtered.encode("UTF-8")
            matcher = re.compile(".*%s.*" % strings.normalize(filtered), re.IGNORECASE)
            cities = filter(lambda c : matcher.match(c), cities)
        cities = map(lambda c: {"code": c, "name": self._cities[c]}, cities)
        return cities

    def get_forecast(self, city):
        today = datetime.now()
        return [
                {'date': today.strftime("%Y-%m-%d"), 'temperature': 10, 'humidity': 60, 'chill': 10, 'status':'clear', 'min': 20, 'max': 10},
                {'date': (today+timedelta(days=1)).strftime("%Y-%m-%d"), 'temperature': '', 'humidity': '', 'chill': '', 'status':'rainy', 'min': 20, 'max': 10},
                {'date': (today+timedelta(days=2)).strftime("%Y-%m-%d"), 'temperature': '', 'humidity': '', 'chill': '', 'status':'clear', 'min': 20, 'max': 10},
                {'date': (today+timedelta(days=3)).strftime("%Y-%m-%d"), 'temperature': '', 'humidity': '', 'chill': '', 'status':'clear', 'min': 20, 'max': 10}
               ]

