//
//  JSONASIHTTPRequest.m
//  ModelJsonAndASIHTTP
//
//  Created by Julio Andrés Carrettoni on 9/2/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "JSONASIHTTPRequest.h"
#import "NSString+SBJSON.h"
#import "NSDictionary+BSJSONAdditions.h"

@implementation JSONASIHTTPRequest

@synthesize theJSONResponse = _theJSONResponse;

#pragma mark - static constructors
+ (JSONASIHTTPRequest*) requestURL:(NSURL *)url withCallFinishSelector:(SEL)finishSelector orFailureSelector:(SEL)failSelector inObject:(id)obj
{
	JSONASIHTTPRequest* request = [self requestWithURL:url];
	[request setDelegate:obj];
	[request setDidFinishSelector:finishSelector];
	[request setDidFailSelector:failSelector];
	return request;
}

+ (ASIHTTPRequest*) requestUsingGetURL:(NSURL*)url andCallFinishSelector:(SEL)finishSelector orFailureSelector:(SEL)failSelector inObject:(id)obj
{
	JSONASIHTTPRequest* request = [self requestURL:url withCallFinishSelector:finishSelector orFailureSelector:failSelector inObject:obj];
	[request setRequestMethod:@"GET"];
	[request startAsynchronous];
	return request;
}

+ (ASIHTTPRequest*) requestUsingGetURL:(NSURL*)url withHeaders:(NSDictionary*)requestHeaders andCallFinishSelector:(SEL)finishSelector orFailureSelector:(SEL)failSelector inObject:(id)obj
{
	JSONASIHTTPRequest* request = [self requestURL:url withCallFinishSelector:finishSelector orFailureSelector:failSelector inObject:obj];
	[request setRequestMethod:@"GET"];
    NSArray* keys = [requestHeaders allKeys];
    for (NSString * key in keys) {
        [request addRequestHeader:key value:[requestHeaders objectForKey:key]];
    }
	[request startAsynchronous];
	return request;
}

+ (ASIHTTPRequest*) requestUsingPutURL:(NSURL*)url andCallFinishSelector:(SEL)finishSelector orFailureSelector:(SEL)failSelector inObject:(id)obj withRequestHeaders: (NSDictionary *) requestHeaders
{
	JSONASIHTTPRequest* request = [self requestURL:url withCallFinishSelector:finishSelector orFailureSelector:failSelector inObject:obj];
	[request setRequestMethod:@"PUT"];
    NSArray* keys = [requestHeaders allKeys];
    for (NSString * key in keys) {
        [request addRequestHeader:key value:[requestHeaders objectForKey:key]];
    }
    [request startAsynchronous];
	return request;
}

+ (ASIHTTPRequest*) requestUsingDeleteURL:(NSURL*)url andCallFinishSelector:(SEL)finishSelector orFailureSelector:(SEL)failSelector inObject:(id)obj {
	JSONASIHTTPRequest* request = [self requestURL:url withCallFinishSelector:finishSelector orFailureSelector:failSelector inObject:obj];
	[request setRequestMethod:@"DELETE"];
    [request startAsynchronous];
	return request;
}

+ (ASIHTTPRequest*) requestURL:(NSURL*)url withArrayParams:(NSArray*)_params andCallFinishSelector:(SEL)finishSelector orFailureSelector:(SEL)failSelector inObject:(id)obj
{
	JSONASIHTTPRequest* request = [self requestURL:url withCallFinishSelector:finishSelector orFailureSelector:failSelector inObject:obj];
	[request setRequestMethod:@"POST"];
	[request appendPostData:[[[NSDictionary dictionary] jsonStringForArray:_params withIndentLevel:0] dataUsingEncoding:NSUTF8StringEncoding]];
	[request startAsynchronous];
	return request;
}

+ (ASIHTTPRequest*) requestURL:(NSURL*)url withParams:(NSDictionary*)_params andCallFinishSelector:(SEL)finishSelector orFailureSelector:(SEL)failSelector inObject:(id)obj
{
	JSONASIHTTPRequest* request = [self requestURL:url withCallFinishSelector:finishSelector orFailureSelector:failSelector inObject:obj];
	[request setRequestMethod:@"POST"];
	[request appendPostData:[[_params jsonStringValue] dataUsingEncoding:NSUTF8StringEncoding]];
	[request startAsynchronous];
	return request;
}

#pragma mark - Overrided object methods
- (id)init {
    self = [super init];
    if (self) {
        self.theJSONResponse = nil;
    }
    return self;
}

- (void) dealloc
{
	self.theJSONResponse = nil;
	[super dealloc];
}

#pragma mark - Overrided Properties
- (void) setTheJSONResponse:(id<NSObject>)theJSONResponse
{
	[_theJSONResponse release];
	_theJSONResponse = [theJSONResponse retain];
}

- (id<NSObject>)theJSONResponse
{
	if (!_theJSONResponse)
	{
		NSString* jsonData = [[NSString alloc] initWithData:[self responseData] encoding:NSUTF8StringEncoding];
		self.theJSONResponse = [jsonData JSONValue];
        
		[jsonData release];
	}
	
	return _theJSONResponse;
}

@end
