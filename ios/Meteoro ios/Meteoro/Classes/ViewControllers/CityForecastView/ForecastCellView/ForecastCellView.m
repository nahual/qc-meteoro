//
//  ForecastCellView.m
//  Meteoro
//
//  Created by Julio Andrés Carrettoni on 13/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "ForecastCellView.h"
#import "Forecast.h"

@implementation ForecastCellView
@synthesize forecast = _forecast;

- (void) setForecast:(Forecast *)forecast
{
    Forecast* old = [_forecast retain];
    _forecast = [forecast retain];
    [old release];
    
    if (_forecast)
    {
#warning TODO: setForecast informationHere
    }
}

- (Forecast*) forecast
{
    return _forecast;
}

- (void)dealloc {
    [dayNameLabel release];
    [maxminTempLabel release];
    [weatherImage release];
    self.forecast = nil;
    [super dealloc];
}
@end
