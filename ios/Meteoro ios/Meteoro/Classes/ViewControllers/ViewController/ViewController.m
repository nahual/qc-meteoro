//
//  ViewController.m
//  Meteoro
//
//  Created by Julio Andrés Carrettoni on 30/04/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "ViewController.h"
#import "AddCityViewController.h"
#import "CityForecastView.h"
#import "City.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    theCarousel.decelerationRate = 0.4;
    thePageControl.hidesForSinglePage = YES;
    [thePageControl addTarget:self action:@selector(pageChanged) forControlEvents:UIControlEventValueChanged];
}

- (void) viewWillAppear:(BOOL) animated
{
    [super viewWillAppear:animated];
    [theCarousel reloadData];
}

- (void)viewDidUnload
{
    [fakeNavBar release];
    fakeNavBar = nil;
    [theCarousel release];
    theCarousel = nil;
    [thePageControl release];
    thePageControl = nil;
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}

- (void)dealloc {
    [fakeNavBar release];
    [theCarousel release];
    [thePageControl release];
    [super dealloc];
}

- (IBAction)onAddCityButtonTUI:(id)sender {
    AddCityViewController* controller = [AddCityViewController new];
    controller.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    [self presentModalViewController:controller animated:YES];
    [controller release];
}

- (void) pageChanged
{
    [theCarousel scrollToItemAtIndex:thePageControl.currentPage animated:YES];
}

#pragma mark iCarousel data & delegate
- (NSUInteger)numberOfItemsInCarousel:(iCarousel *)carousel
{
    thePageControl.numberOfPages = [Config getInstance].cities.count;
    return thePageControl.numberOfPages;
}

- (NSUInteger)numberOfVisibleItemsInCarousel:(iCarousel *)carousel
{
    return 3;
}

- (CGFloat)carouselItemWidth:(iCarousel *)carousel
{
    return 320;
}

- (void)carouselDidEndDecelerating:(iCarousel *)carousel
{
    thePageControl.currentPage = carousel.currentItemIndex;
}

- (UIView *)carousel:(iCarousel *)carousel viewForItemAtIndex:(NSUInteger)index reusingView:(UIView *)view
{
    CityForecastView* cityForecastView = (CityForecastView*)view;
    if (!cityForecastView)
        cityForecastView = [[[NSBundle mainBundle] loadNibNamed:@"CityForecastView" owner:nil options:nil] objectAtIndex:0];
    
    return cityForecastView;
}

@end
