//
//  ModelCollection.m
//  circle_of_moms
//
//  Created by Sebastian Waisbrot on 9/17/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "Config.h"
#import "ModelCollection.h"
#import "NSObjectClassName.h"
#import "JSONASIHTTPRequest.h"
#import "NSMutableSetNonRetain.h"

@implementation ModelCollection

@synthesize models, modelSubclass;

#pragma mark - manage observers

- (id)init {
    self = [super init];
    if (self) {
        observers = [[NSMutableSet setNonRetaining] retain];
    }
    return self;
}

- (void) addObserver:(id<ModelCollectionObserver>)_observer forceReload:(BOOL) force
{
	[observers addObject:_observer];
	if (force)
	{
		[self tellObserversModelCollectionHasChanged];
	}
}

- (void) addObserver:(id<ModelCollectionObserver>)_observer {
	[self addObserver:_observer forceReload:YES];
}

- (void) removeObserver:(id<ModelCollectionObserver>)_observer {
	[observers removeObject:_observer];
}

- (void) removeAllObservers {
	[observers release];
	observers = nil;
}

#pragma mark - Requests management
- (void) addRequest:(JSONASIHTTPRequest*)_request {
	if (!requests) requests = [[NSMutableSet setNonRetaining] retain];;
	[requests addObject:_request];
}

- (void) removeRequest:(JSONASIHTTPRequest*)_request {
	[requests removeObject:_request];
}

#pragma mark - Request Collection from URL
- (void) requestURL:(NSString*)urlStr{
	[self requestURL:urlStr withParams:[NSDictionary dictionary]];
}

- (void) requestURL:(NSString*)urlStr withRequestHeaders:(NSDictionary*)requestHeaders{
	[self requestURL:urlStr withParams:[NSDictionary dictionary] andHeaders:requestHeaders];
}

- (void) requestURL:(NSString*)urlStr withParams:(NSDictionary*)_params {
	NSURL* url = [[Config getInstance] URLWithString:urlStr];
	[self addRequest:[JSONASIHTTPRequest requestUsingGetURL:url andCallFinishSelector:@selector(setCollectionFromRequest:) orFailureSelector:@selector(requestFailed:) inObject:self]];
}

- (void) requestURL:(NSString*)urlStr withParams:(NSDictionary*)_params andHeaders:(NSDictionary*)requestHeaders{
	NSURL* url = [[Config getInstance] URLWithString:urlStr];
	[self addRequest:[JSONASIHTTPRequest requestUsingGetURL:url withHeaders:requestHeaders andCallFinishSelector:@selector(setCollectionFromRequest:) orFailureSelector:@selector(requestFailed:) inObject:self]];  
}

#pragma mark - Requests responses
- (void)setCollectionFromRequest:(JSONASIHTTPRequest*)request {
	[self removeRequest:request];
	id response = request.theJSONResponse;
	if ([response isKindOfClass:[NSDictionary class]])
	{
		[self setModelsFromDictionary:response];
	}
	else if ([response isKindOfClass:[NSArray class]])
	{
		[self setModelsFromArray:response];
	}
	else {
		self.models = nil;
	}
}
- (void) requestFailed:(JSONASIHTTPRequest*)request
{
	[self retain];
	[self removeRequest:request];
    [self tellObserversRequestHasFailed];
    
	[self release];
}

#pragma mark - Set Models from data
- (void) setModelsFromDictionary:(NSDictionary*)response {
	NSObject* obj = [[modelSubclass alloc] initWithDictionary:response];
	self.models = [NSArray arrayWithObject:obj];
	[obj release];
}

- (void) setModelsFromArray:(NSArray*)response {
	NSMutableArray* array = [[NSMutableArray alloc] initWithCapacity:[response count]];
    
	for (int i = 0; i < [response count]; i++) {
		NSObject* obj = [[modelSubclass alloc] initWithDictionary:[response objectAtIndex:i]];
		[array addObject:obj];
		[obj release];
	}
    
	NSArray* tmpArray = [[NSArray alloc] initWithArray:array];
	self.models = tmpArray;
	[tmpArray release];
	[array release];
}

#pragma mark - tell observers
- (void) tellObserversRequestHasFailed
{
	if (observers && observers.count > 0)
	{
		NSSet* copySet = [[NSSet alloc] initWithSet:observers];
		[copySet makeObjectsPerformSelector:@selector(requestHasFailed:) withObject:self];
		[copySet release];
	}
}

- (void) tellObserversModelCollectionHasChanged
{
	if (observers && observers.count > 0)
	{
		NSSet* copySet = [[NSSet alloc] initWithSet:observers];
		[copySet makeObjectsPerformSelector:@selector(modelCollectionHasChanged:) withObject:self];
		[copySet release];
	}
}

#pragma mark - Overrided methods
- (void) setModels:(NSArray*)_models {
	[models release];
	models = [_models retain];
	[self tellObserversModelCollectionHasChanged];
}

- (void) dealloc {
    
    for (JSONASIHTTPRequest* request in requests)
    {
        request.delegate = nil;
        [request cancel];
    }
    [requests release];
    requests = nil;
    
    
    [observers removeAllObjects];
    [observers release];
    observers = nil;
    
    [models release];
    models = nil;
    
	[super dealloc];
}

- (NSString*) description {
	NSString* desc = [NSString stringWithFormat:@"Collection:%@\nWith objects:\n%@", [self className], [models description]];
	return desc;
}

@end