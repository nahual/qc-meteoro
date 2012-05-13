from weather_provider import WeatherProvider

class YahooWeatherProvider(WeatherProvider):
    def __init__(self, log_handlers):
        WeatherProvider.__init__(self, "MockWeahterProvider", log_handlers)

    def get_cities(self, filtered):
        return [{"code":"466863", "name": "Mar del Plata"}]

    def get_forecast(self, city):
        today = datetime.now()
        return [
                {'date': today.strftime("%Y-%m-%d"), 'temperature': 10, 'humidity': 60, 'chill': 10, 'status':'clear', 'min': 20, 'max': 10},
                {'date': (today+timedelta(days=1)).strftime("%Y-%m-%d"), 'temperature': '', 'humidity': '', 'chill': '', 'status':'rainy', 'min': 20, 'max': 10},
                {'date': (today+timedelta(days=2)).strftime("%Y-%m-%d"), 'temperature': '', 'humidity': '', 'chill': '', 'status':'clear', 'min': 20, 'max': 10},
                {'date': (today+timedelta(days=3)).strftime("%Y-%m-%d"), 'temperature': '', 'humidity': '', 'chill': '', 'status':'clear', 'min': 20, 'max': 10}
               ]

