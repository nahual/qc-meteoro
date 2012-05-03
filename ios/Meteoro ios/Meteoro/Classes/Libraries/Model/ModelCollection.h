//
//  ModelCollection.h
//  circle_of_moms
//
//  Created by Sebastian Waisbrot on 9/17/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ModelCollectionObserver.h"

@class JSONASIHTTPRequest;
@interface ModelCollection : NSObject {
	NSArray* models;
	NSMutableSet* observers;
	Class modelSubclass;
	NSMutableSet* requests;
}

/**
 * The collection of models
 */
@property (nonatomic, retain) NSArray* models;
/**
 * The subclass of model to use when receiving new information.
 */
@property (nonatomic, assign) Class modelSubclass;

#pragma mark - manage observers
/**
 * Adds a new observer to notify when the collection changes.
 * This observer is NOT retained by this class. If it gets dealloc'ed it should remove itself from this list.
 * When Adding a observer, all other observers get notified of a change in the collection
 */
- (void) addObserver:(id<ModelCollectionObserver>)_observer;

/**
 * Adds a new observer to notify when the collection changes.
 * This observer is NOT retained by this class. If it gets dealloc'ed it should remove itself from this list.
 * forceReload in YES will notify all observers of a change in the collection (same as 'addObserver:')
 */
- (void) addObserver:(id<ModelCollectionObserver>)_observer forceReload:(BOOL) force;

/**
 * Removes an observer from the list.
 */
- (void) removeObserver:(id<ModelCollectionObserver>)_observer;

/**
 * Removes all observers.
 */
- (void) removeAllObservers;

#pragma mark - Requests management
/**
 * Stores a request reference. Useful to cancel when dealloc'ing.
 * @protected
 */
- (void) addRequest:(JSONASIHTTPRequest*)_request;

/**
 * Remove a request reference.
 * @protected
 */
- (void) removeRequest:(JSONASIHTTPRequest*)_request;

#pragma mark - Request Collection from URL
/**
 * Request a JSON collection from a URL without parameters.
 * @protected
 */
- (void) requestURL:(NSString*)urlStr;

/**
 * Request a JSON collection from a URL with given JSON headers.
 * @protected
 */
- (void) requestURL:(NSString*)urlStr withRequestHeaders:(NSDictionary*)requestHeaders;

/**
 * Request a JSON collection from a URL with given JSON parameters.
 * @protected
 */
- (void) requestURL:(NSString*)urlStr withParams:(NSDictionary*)_params;

/**
 * Request a JSON collection from a URL with given JSON parameters and headers.
 * @protected
 */
- (void) requestURL:(NSString*)urlStr withParams:(NSDictionary*)_params andHeaders:(NSDictionary*)_headers;

#pragma mark - Requests responses
/**
 * Utility method to set the information from a JSON request. It should return a JSON array of JSON objects matching the properties the class have.
 * @protected
 */
- (void)setCollectionFromRequest:(JSONASIHTTPRequest*)request;

#pragma mark - Set Models from data
/**
 * Utility method to set the information from a array.
 * @protected
 */
- (void) setModelsFromArray:(NSArray*)response;

/**
 * Utility method to set the information from a Diccionary. The default method just create ONE object
 * and add it into the collection, if you are specting a Object containing an array from wich to extrac
 * that collection, you need to override this method to get that array and the call 'setModelsFromArray:'
 * @protected
 */
- (void) setModelsFromDictionary:(NSDictionary*)response;

#pragma mark - tell observers
/**
 * Utility method to notify observes about changes on the collection
 * @protected
 */
- (void) tellObserversRequestHasFailed;
- (void) tellObserversModelCollectionHasChanged;

@end