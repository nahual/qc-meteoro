from utils import llog
import re
from utils import strings

class WeatherProvider:
    def __init__(self, name, log_handlers):
        self._log = llog.create_logger("")
        for handler in log_handlers:
            self._log.addHandler(handler)
        self.date_format = '%Y-%m-%d'

    def get_cities(self, filtered):
        cities = self._cities
        if filtered:
            filtered = filtered.encode("UTF-8")
            matcher = re.compile(".*%s.*" % strings.normalize(filtered), re.IGNORECASE)
            cities = filter(lambda c : matcher.match(strings.normalize(c['name'].encode('UTF-8'))), cities)
        return cities

