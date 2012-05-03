//
//  JSONASIHTTPRequest.h
//  ModelJsonAndASIHTTP
//
//  Created by Julio Andrés Carrettoni on 9/2/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ASIHTTPRequest.h"

@interface JSONASIHTTPRequest : ASIHTTPRequest {
    
}

@property (nonatomic, retain) id<NSObject> theJSONResponse;

#pragma mark - static constructors
+ (JSONASIHTTPRequest*) requestUsingGetURL:(NSURL*)url andCallFinishSelector:(SEL)finishSelector orFailureSelector:(SEL)failSelector inObject:(id)obj;
+ (JSONASIHTTPRequest*) requestURL:(NSURL*)url withArrayParams:(NSArray*)_params andCallFinishSelector:(SEL)finishSelector orFailureSelector:(SEL)failSelector inObject:(id)obj;
+ (JSONASIHTTPRequest*) requestUsingGetURL:(NSURL*)url withHeaders:(NSDictionary*)requestHeaders andCallFinishSelector:(SEL)finishSelector orFailureSelector:(SEL)failSelector inObject:(id)obj;
+ (JSONASIHTTPRequest*) requestURL:(NSURL*)url withParams:(NSDictionary*)_params andCallFinishSelector:(SEL)finishSelector orFailureSelector:(SEL)failSelector inObject:(id)obj;
+ (JSONASIHTTPRequest*) requestUsingPutURL:(NSURL*)url andCallFinishSelector:(SEL)finishSelector orFailureSelector:(SEL)failSelector inObject:(id)obj withRequestHeaders: (NSDictionary *) requestHeaders;
+ (ASIHTTPRequest*) requestUsingDeleteURL:(NSURL*)url andCallFinishSelector:(SEL)finishSelector orFailureSelector:(SEL)failSelector inObject:(id)obj;

@end
