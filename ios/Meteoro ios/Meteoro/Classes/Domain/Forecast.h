//
//  Forecast.h
//  Meteoro
//
//  Created by Julio Andrés Carrettoni on 13/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "Model.h"

@interface Forecast : Model
{
    NSString* status;
    NSInteger temperature;
    NSInteger uv;
    NSInteger humidity;
    NSInteger chill;
    NSDate*   date;
}

@property (nonatomic, retain) NSString* status;
@property (nonatomic) NSInteger temperature;
@property (nonatomic) NSInteger uv;
@property (nonatomic) NSInteger humidity;
@property (nonatomic) NSInteger chill;
@property (nonatomic, retain) NSDate* date;

@end
