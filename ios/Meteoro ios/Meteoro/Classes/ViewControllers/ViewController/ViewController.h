//
//  ViewController.h
//  Meteoro
//
//  Created by Julio Andrés Carrettoni on 30/04/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "iCarousel.h"

@interface ViewController : UIViewController <iCarouselDelegate, iCarouselDataSource>
{
    IBOutlet UINavigationBar *fakeNavBar;
    IBOutlet iCarousel *theCarousel;
    IBOutlet UIPageControl *thePageControl;
}

- (IBAction)onAddCityButtonTUI:(id)sender;

@end
