from weather_provider import WeatherProvider
import urllib2
import json
from datetime import datetime, timedelta

class YahooWeatherProvider(WeatherProvider):
    def __init__(self, log_handlers):
        WeatherProvider.__init__(self, "MockWeahterProvider", log_handlers)
        self.url = 'http://weather.yahooapis.com/forecastjson?w=%s&d=3&u=c'

    def get_cities(self, filtered):
        return [{"code":"466863", "name": "Mar del Plata"}]

    def get_forecast(self, city):
        try:
            data = urllib2.urlopen(self.url % city)
            response = json.loads(data.read())
            if 'Message' in response:
                raise Exception(response['Message'])
            return self._build_meteoro_result(response)
        except Exception as e:
            self._log.error("Error while getting weather from Yahoo", exc_info = True)
            return {'error':'Problem with Yahoo!: %s' % e}

    def _build_meteoro_result(self, response):
        today = datetime.now()
        return [
                {'date': today.strftime("%Y-%m-%d"), 'temperature': 10, 'humidity': 60, 'chill': 10, 'status':'clear', 'min': 20, 'max': 10},
                {'date': (today+timedelta(days=1)).strftime("%Y-%m-%d"), 'temperature': '', 'humidity': '', 'chill': '', 'status':'rainy', 'min': 20, 'max': 10},
                {'date': (today+timedelta(days=2)).strftime("%Y-%m-%d"), 'temperature': '', 'humidity': '', 'chill': '', 'status':'clear', 'min': 20, 'max': 10},
                {'date': (today+timedelta(days=3)).strftime("%Y-%m-%d"), 'temperature': '', 'humidity': '', 'chill': '', 'status':'clear', 'min': 20, 'max': 10}
               ]

