//
//  ForecastCellView.h
//  Meteoro
//
//  Created by Julio Andrés Carrettoni on 13/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
@class Forecast;
@interface ForecastCellView : UITableViewCell
{
    IBOutlet UILabel *dayNameLabel;
    IBOutlet UIImageView *weatherImage;
    IBOutlet UILabel *maxminTempLabel;
}

@property (nonatomic, retain) Forecast* forecast;

@end
