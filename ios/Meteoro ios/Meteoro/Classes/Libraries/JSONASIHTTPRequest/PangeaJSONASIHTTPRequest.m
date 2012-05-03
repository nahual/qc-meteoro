//
//  PangeaJSONASIHTTPRequest.m
//  Chicle
//
//  Created by FDV Solutions on 28/12/11.
//  Copyright (c) 2011 FDV Solutions. All rights reserved.
//

#import "PangeaJSONASIHTTPRequest.h"

@interface JSONASIHTTPRequest()

- (void) reportFailure;
- (void) reportFinished;

@end

@implementation PangeaJSONASIHTTPRequest

// If the request was made to Pangea, this mehods extracts "data" field of the response.
- (void)reportFinished
{
    if ([self.requestMethod isEqualToString:@"GET"]) {
        if([self.theJSONResponse isKindOfClass:[NSDictionary class]] && [(NSDictionary *)self.theJSONResponse objectForKey:@"data"]) {
            self.theJSONResponse = [(NSDictionary *)self.theJSONResponse objectForKey:@"data"];
            [super reportFinished];
        }
        else
        {
            [super reportFailure];
        }
    } else {
        [super reportFinished];
    }
}

@end
