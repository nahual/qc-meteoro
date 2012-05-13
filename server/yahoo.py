from weather_provider import WeatherProvider
import urllib2
import json
from datetime import datetime, timedelta

def safe_get(data, keys):
    try:
        for key in keys:
            data = data[key]
        return data
    except KeyError:
        return ''

class YahooWeatherProvider(WeatherProvider):
    def __init__(self, log_handlers):
        WeatherProvider.__init__(self, "MockWeahterProvider", log_handlers)
        self.url = 'http://weather.yahooapis.com/forecastjson?w=%s&d=3&u=c'
        self._cities = [{"code":"466863", "name": "Mar del Plata"}]

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
        rv = []
        rv.append({
            'date': today.strftime(self.date_format),
            'temperature': safe_get(response, ['condition', 'temperature']),
            'chill': safe_get(response, ['condition','temperature']),
            'humidity': safe_get(response, ['atmosphere','humidity']),
            'status': safe_get(response, ['condition','text']),
            'wind': "%s %s %s" % (safe_get(response,['wind','direction']), safe_get(response, ['wind','speed']), safe_get(response,['units','speed'])),
            'min': '', 'max': ''
        })
        forecast = safe_get(response, ['forecast'])
        if forecast:
            for i, day in enumerate(forecast):
                rv.append({
                    'date': (today+timedelta(days=i)).strftime(self.date_format),
                    'min': safe_get(day, ['low_temperature']),
                    'max': safe_get(day, ['high_temperature']),
                    'status': safe_get(day, ['condition']),
                    'temperature': '', 'chill': '', 'humidity': '', 'wind': ''
                })
        return rv

