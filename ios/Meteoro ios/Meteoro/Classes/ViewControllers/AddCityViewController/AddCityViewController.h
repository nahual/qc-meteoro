//
//  AddCityViewController.h
//  Meteoro
//
//  Created by Julio Andrés Carrettoni on 30/04/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
@class JSONASIHTTPRequest;
@class City;
@interface AddCityViewController : UIViewController <UITableViewDataSource, UITabBarDelegate, UISearchBarDelegate>
{
    IBOutlet UINavigationBar *fakeNavBar;
    IBOutlet UISearchBar *theSearchBar;
    IBOutlet UITableView *theTableView;
    
    JSONASIHTTPRequest* searchRequest;
    NSTimer *delayTimer;
    
    IBOutlet UIView *settingsViewPanel;
    
    NSArray* searchResultArray;
        
    IBOutlet UIButton *addCityButton;
    IBOutlet UIButton *blackDIMButton;
    
    IBOutlet UILabel *selectedCity;

    IBOutlet UILabel *temperaturaLabel;
    IBOutlet UILabel *humedadLabel;
    IBOutlet UILabel *presionLabel;
    IBOutlet UILabel *sensacionTermicaLabel;
    IBOutlet UILabel *rayosUVLabel;
    
    IBOutlet UISwitch *temperaturaSwitch;
    IBOutlet UISwitch *humedadSwitch;
    IBOutlet UISwitch *presionSwitch;
    IBOutlet UISwitch *sensacionTermicaSwitch;
    IBOutlet UISwitch *rayosSwitch;
}

@property (nonatomic, retain) City* city;

- (IBAction)onDoneButtonTUI:(id)sender;
- (IBAction)onAddCityButtonTUI:(id)sender;
- (IBAction)onBackgroundTUI:(id)sender;

@end
