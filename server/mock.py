from weather_provider import WeatherProvider
from datetime import datetime, timedelta

class MockWeatherProvider(WeatherProvider):
    def __init__(self, log_handlers):
        WeatherProvider.__init__(self, "MockWeahterProvider", log_handlers)
        self._cities = [
            {"code":"466863", "name": "Mar del Plata"},
            {"code":"466863", "name": "Buenos Aires"},
            {"code":"466863", "name": "La Plata"},
            {"code":"466863", "name": "Mendoza"},
            {"code":"466863", "name": "Cordoba"},
            {"code":"466863", "name": "Rosario"},
        ]          

    def get_forecast(self, city):
        today = datetime.now()
        return [
                {'date': today.strftime(self.date_format), 'temperature': 10, 'humidity': 60, 'chill': 10, 'status':'clear', 'wind': 'NE 100KM/h', 'min': '', 'max': ''},
                {'date': (today+timedelta(days=0)).strftime(self.date_format), 'temperature': '', 'humidity': '', 'chill': '', 'wind': '', 'status':'rainy', 'min': 20, 'max': 10},
                {'date': (today+timedelta(days=1)).strftime(self.date_format), 'temperature': '', 'humidity': '', 'chill': '', 'wind': '', 'status':'rainy', 'min': 20, 'max': 10},
                {'date': (today+timedelta(days=2)).strftime(self.date_format), 'temperature': '', 'humidity': '', 'chill': '', 'wind': '', 'status':'clear', 'min': 20, 'max': 10},
                {'date': (today+timedelta(days=3)).strftime(self.date_format), 'temperature': '', 'humidity': '', 'chill': '', 'wind': '', 'status':'clear', 'min': 20, 'max': 10}
               ]

