//
//  CityForecastView.h
//  Meteoro
//
//  Created by Julio Andrés Carrettoni on 13/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CityForecastView : UIView <UITableViewDataSource, UITableViewDelegate>
{
    IBOutlet UILabel *cityNameLabel;
    
    IBOutlet UILabel *tempActualLabel;
    IBOutlet UILabel *sensacionTermicaActualLabel;
    IBOutlet UILabel *humedadActualLabel;
    
    IBOutlet UITableView *forecastTable;
    
}

@end
