from utils import llog

class WeatherProvider:
    def __init__(self, name, log_handlers):
        self._log = llog.create_logger("")
        for handler in log_handlers:
            self._log.addHandler(handler)

