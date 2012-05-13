//
//  City.m
//  Meteoro
//
//  Created by Julio Andrés Carrettoni on 30/04/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "City.h"
#import "JSONASIHTTPRequest.h"
#import "Forecast.h"
#import "Config.h"

@implementation City

@synthesize code;
@synthesize name;
@synthesize temperatura;
@synthesize humedad;
@synthesize presion;
@synthesize sensacionTermica;
@synthesize rayos;
@synthesize forecasts;

- (void) refreshForecast
{
    [self cleanUpRequest];
    NSURL* searchURL = [self getURLSearchQuery];
    forecastResquest = [[JSONASIHTTPRequest requestUsingGetURL:searchURL andCallFinishSelector:@selector(forecastRequestFinished:) orFailureSelector:@selector(forecastRequestFailed:) inObject:self] retain];
}

- (NSURL*) getURLSearchQuery
{
    return [[Config getInstance] URLWithString:[NSString stringWithFormat:@"get_forecast?city=%@", [self.code stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]]];
}


#pragma mark - request landing methods
- (void) forecastRequestFinished:(JSONASIHTTPRequest*) request
{

    NSArray* array = [(NSArray*)request.theJSONResponse retain];
    NSMutableArray* mutArray = [NSMutableArray array];
    for (NSDictionary* dic in array)
    {
        Forecast* aForecast = [[Forecast alloc] initWithDictionary:dic];
        [mutArray addObject:aForecast];
        [aForecast release];
    }
    self.forecasts = [[NSArray arrayWithArray:mutArray] retain];
    [[Config getInstance] saveCities];
    [self cleanUpRequest];
}

- (void) forecastRequestFailed:(JSONASIHTTPRequest*) request
{
    [Config showAlertWithTitle:NSLocalizedString(@"Error", @"") andMessage:NSLocalizedString(@"An error has ocurred", @"")];
    [forecastResquest release];
    forecastResquest = nil;
    [self cleanUpRequest];
}

- (void) cleanUpRequest
{
    [forecastResquest cancel];
    forecastResquest.delegate = nil;
    [forecastResquest release];
    forecastResquest = nil;
}

- (void) dealloc
{
    [self cleanUpRequest];
    [super dealloc];
}

@end
