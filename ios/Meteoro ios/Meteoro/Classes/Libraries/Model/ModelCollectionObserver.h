//
//  ModelObserver.h
//  circle_of_moms
//
//  Created by Sebastian Waisbrot on 9/17/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@class ModelCollection;

@protocol ModelCollectionObserver <NSObject>

- (void) modelCollectionHasChanged:(ModelCollection*)modelCollection;
- (void) requestHasFailed:(ModelCollection*)modelCollection;

@end
