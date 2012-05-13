//
//  CityForecastView.m
//  Meteoro
//
//  Created by Julio Andrés Carrettoni on 13/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "CityForecastView.h"

@implementation CityForecastView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

- (void)dealloc {
    [cityNameLabel release];
    [tempActualLabel release];
    [sensacionTermicaActualLabel release];
    [humedadActualLabel release];
    [super dealloc];
}
@end
