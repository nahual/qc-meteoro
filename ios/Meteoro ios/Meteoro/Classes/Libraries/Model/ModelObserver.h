//
//  ModelObserver.h
//  circle_of_moms
//
//  Created by Sebastian Waisbrot on 9/17/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@class Model;

@protocol ModelObserver <NSObject>

- (void)modelHasChanged:(Model*)model;

@optional
- (void)modelHasFailedToUpdate:(Model*)model withError:(NSError*)error;

@end
