
#import "Model.h"
#import "Config.h"
#import <objc/runtime.h>
#import <objc/message.h>
#import "ModelCollection.h"
#import "NSObjectClassName.h"
#import "JSONASIHTTPRequest.h"
#import "NSMutableSetNonRetain.h"

@implementation Model

#pragma mark - encode/decode methods
- (void)encodeWithCoder:(NSCoder *)encoder {
	[encoder encodeObject:[self toDictionary]];
}

- (id)initWithCoder:(NSCoder *)decoder {
	[self setDictionary:[decoder decodeObject]];
	return self;
}
#pragma mark dealloc
- (void) dealloc {
	
    for (JSONASIHTTPRequest* r in requests) {
        r.delegate = nil;
        [r cancel];
    }
    
	[requests release];
    
	NSArray* keys = [[self serialization] allKeys];
	for (int i = 0; i < [keys count]; i++)
	{
		NSString* key = [keys objectAtIndex:i];
		const char* selectorName = (const char *)[[NSString stringWithFormat:@"set%@%@:", [[key substringToIndex:1] capitalizedString], [key substringFromIndex:1]] UTF8String];
		SEL selector = sel_getUid(selectorName);
		if ([self respondsToSelector:selector])
		{
			[self performSelector:selector withObject:nil];
		}
	}
	[super dealloc];
}

#pragma mark - tell observes about changes in the object state

- (void) tellObserversModelHasChanged
{
    NSSet* _observers = [observers copy];
	[_observers makeObjectsPerformSelector:@selector(modelHasChanged:) withObject:self];
	[_observers release];
}

- (void) tellObserversModelHasFailedToUpdateWithError:(NSError*) error
{
	NSSet* _observers = [observers copy];
	for (id<ModelObserver> observer in _observers) {
		if ([observer respondsToSelector:@selector(modelHasFailedToUpdate:withError:)]) {
			[observer performSelector:@selector(modelHasFailedToUpdate:withError:) withObject:self withObject:error];
		}
	}
	[_observers release];
}

#pragma mark - Set/Get object values from dictionaries
- (Model*) initWithDictionary:(NSDictionary*)dictionary {
	//[self init];
	[self setDictionary:dictionary];
	return [self init];
}

- (Model*) setDictionary:(NSDictionary*)dictionary {
	NSArray* keys = [dictionary allKeys];
	for (int i = 0; i < [dictionary count]; i++)
	{
		NSString* key = [keys objectAtIndex:i];
		id value = [dictionary valueForKey:key];
		const char* selectorName = (const char *)[[NSString stringWithFormat:@"set%@%@:", [[key substringToIndex:1] capitalizedString], [key substringFromIndex:1]] UTF8String];
		SEL selector = sel_getUid(selectorName);
		if ([self respondsToSelector:selector])
		{
			NSMethodSignature* signature = [self methodSignatureForSelector:selector];
			NSInvocation* invocation = [NSInvocation invocationWithMethodSignature:signature];
			[invocation setTarget:self];
			[invocation setSelector:selector];

			if (strcmp([signature getArgumentTypeAtIndex:2],@encode(int)) == 0)
			{
				int str = 0;
				if ([value respondsToSelector:@selector(intValue)]) str = [value intValue];
				[invocation setArgument:&str atIndex:2];
			} else if (strcmp([signature getArgumentTypeAtIndex:2],@encode(double)) == 0) {
				double str = [value doubleValue];
				[invocation setArgument:&str atIndex:2];
			} else if (strcmp([signature getArgumentTypeAtIndex:2],@encode(float)) == 0) {
				float str = [value doubleValue];
				[invocation setArgument:&str atIndex:2];
			} else if (strcmp([signature getArgumentTypeAtIndex:2],@encode(unsigned long long)) == 0) {
				unsigned long long str = [value longLongValue];
				[invocation setArgument:&str atIndex:2];
			} else if (strcmp([signature getArgumentTypeAtIndex:2],@encode(long long)) == 0) {
				long long str = [value longLongValue];
				[invocation setArgument:&str atIndex:2];
			} else if (strcmp([signature getArgumentTypeAtIndex:2], @encode(BOOL)) == 0) {
				BOOL str = [value boolValue];
				[invocation setArgument:&str atIndex:2];
			} else {
				NSString* str = value;
				[invocation setArgument:&str atIndex:2];
			}
			[invocation invoke];
		}
		else
		{
 			NSLog(@"%@ does not respond to %s", [self className], selectorName);
		}
	}
    [self tellObserversModelHasChanged];
	return self;
}

- (NSDictionary*) serialization {
	NSMutableDictionary* dictionary = [NSMutableDictionary dictionary];
	Class c = [self class];
	while (c != [Model class])
	{
		uint n = 0;
		Ivar* vars = class_copyIvarList(c, &n);
		for (int i = 0; i < n; i++)
		{
			const char* selectorName = ivar_getName(vars[i]);
			
			SEL selector = sel_getUid(selectorName);
			NSMethodSignature* signature = [self methodSignatureForSelector:selector];
			if ( !signature ) continue;
			NSString* sel = [NSString stringWithFormat:@"%s", selectorName];
			[dictionary setValue:sel forKey:sel];
		}
		c = class_getSuperclass(c);
		free(vars);
	}
	return (NSDictionary*)dictionary;
}

- (NSDictionary*) toDictionary {
	NSDictionary* dictionary = [self serialization];
	NSMutableDictionary* result = [NSMutableDictionary dictionary];
	for (int i = 0; i < [dictionary count]; i++)
	{
		NSString* key = [[dictionary allKeys] objectAtIndex:i];
		NSString* selectorName = [dictionary valueForKey:key];
		SEL selector = sel_getUid([selectorName UTF8String]);
		NSMethodSignature* signature = [self methodSignatureForSelector:selector];
		if ( !signature ) continue;
		NSInvocation* invocation = [NSInvocation invocationWithMethodSignature:signature];
		[invocation setTarget:self];
		[invocation setSelector:selector];
		[invocation invoke];
		
		id value = nil;
		if (strcmp([signature methodReturnType], @encode(double)) == 0)
		{
			double d;
			[invocation getReturnValue:&d];
			value = [NSNumber numberWithDouble:d];
		} else if (strcmp([signature methodReturnType], @encode(float)) == 0)
		{
			float f;
			[invocation getReturnValue:&f];
			value = [NSNumber numberWithFloat:f];
		} else if (strcmp([signature methodReturnType], @encode(int)) == 0)
		{
			int d;
			[invocation getReturnValue:&d];
			value = [NSNumber numberWithInt:d];
		} else if (strcmp([signature methodReturnType], @encode(long long unsigned)) == 0)
		{
			long long unsigned d;
			[invocation getReturnValue:&d];
			value = [NSNumber numberWithLongLong:d];
		} else if (strcmp([signature methodReturnType], @encode(long long)) == 0)
		{
			long long d;
			[invocation getReturnValue:&d];
			value = [NSNumber numberWithLongLong:d];
		} else if (strcmp([signature methodReturnType], @encode(BOOL)) == 0)
		{
			BOOL d;
			[invocation getReturnValue:&d];
			value = [NSNumber numberWithBool:d];
		} else if (strcmp([signature methodReturnType], "@") == 0)
		{
			id d;
			[invocation getReturnValue:&d];
			if (![d isKindOfClass:[NSString class]]) continue;
			value = d;
		} else 
		{
			NSLog(@"Unknown methodReturnType: '%s' for property '%@'", [signature methodReturnType], selectorName);
		}

		[result setValue:value forKey:key];
	}
	return (NSDictionary*)result;
}

#pragma mark - Request response
- (void) setFromRequest:(JSONASIHTTPRequest*) request {
	[self removeRequest:request];
	
	id response = request.theJSONResponse;
	NSInteger statusCode = request.responseStatusCode;
	if (response || (statusCode < 400 && statusCode >= 200))
	{
		if ([response isKindOfClass:[NSDictionary class]])
		{
			[self setDictionary:response];
		}
		else if ([response isKindOfClass:[NSArray class]])
		{
			[self setDictionary:[response lastObject]];
		}
		else if (response != nil && ![response isKindOfClass:[NSNull class]]) 
		{
			[NSException raise:@"Invalid response" format:@"Didn't get a valid JSON response"];
		}
	} 
	else if (request.responseStatusCode == 204)
	{
		[self setDictionary:[NSDictionary dictionary]];
	}
	else
	{
		[self tellObserversModelHasFailedToUpdateWithError:request.error];
	}
}

- (void)requestFailed:(JSONASIHTTPRequest *) request
{
	[self removeRequest:request];
	[self tellObserversModelHasFailedToUpdateWithError:request.error];
}

#pragma mark - fetch object in JSON format from URL
+ (Model*)fetchFromURL:(NSString*)urlStr {
	Model* model = [[self alloc] init];
	[model requestURL:urlStr withParams:[NSDictionary dictionary]];
	return [model autorelease];
}

+ (Model*)fetchFromURL:(NSString*)urlStr withParams:(NSDictionary*)_params {
	Model* model = [[self alloc] init];
	[model requestURL:urlStr withParams:_params];
	return [model autorelease];
}

- (void) requestURL:(NSString*)urlStr withParams:(NSDictionary*)_params {
	NSURL* url = [[Config getInstance] URLWithString:urlStr];
	[self addRequest:[JSONASIHTTPRequest requestUsingGetURL:url andCallFinishSelector:@selector(setFromRequest:) orFailureSelector:@selector(requestFailed:) inObject:self]];
}

#pragma mark - Fetch collection
+ (Class) collectionClass {
	return [ModelCollection class];
}

+ (ModelCollection*)fetchCollectionFromURL:(NSString*)urlStr {
	ModelCollection* collection = [[[[self collectionClass] alloc] init] autorelease];
	collection.modelSubclass = [self class];
	[collection requestURL:urlStr];
	return collection;
}


+ (ModelCollection*)fetchCollectionFromURL:(NSString*)urlStr withRequestHeaders:(NSDictionary*)requestHeaders{
	ModelCollection* collection = [[[[self collectionClass] alloc] init] autorelease];
	collection.modelSubclass = [self class];
	[collection requestURL:urlStr withRequestHeaders:requestHeaders]; 
	return collection;
}

+ (ModelCollection*)fetchCollectionFromURL:(NSString*)urlStr andArrayParams:(NSArray*)_params {
	
	ModelCollection* collection = [[[[self collectionClass] alloc] init] autorelease];
	collection.modelSubclass = [self class];
	[collection requestURL:urlStr];
	return collection;
}

+ (ModelCollection*)fetchCollectionFromURL:(NSString*)urlStr andParams:(NSDictionary*)_params {
	ModelCollection* collection = [[[self collectionClass] alloc] init];
	collection.modelSubclass = [self class];
	[collection requestURL:urlStr withParams:_params];
	return [collection autorelease];
}

#pragma mark - Observer management
- (void) addObserver:(id<ModelObserver>)_observer {
	if (!observers) observers = [[NSMutableSet setNonRetaining] retain];
	[observers addObject:_observer];
}

- (void) removeObserver:(id<ModelObserver>)_observer {
	[observers removeObject:_observer];
}

- (void) removeAllObservers {
	[observers release];
	observers = nil;
}

#pragma mark - Request Management
- (void) addRequest:(JSONASIHTTPRequest*)_request {
	if (!requests) 
    {
        requests = nil;
        requests = [[NSMutableSet setNonRetaining] retain];
    }
    
	[requests addObject:_request];
}

- (void) removeRequest:(JSONASIHTTPRequest*)_request {
	[requests removeObject:_request];
}

- (BOOL) hasRequestsPending
{
	return ([requests count] > 0);
}

- (NSString*) description {
	return [[self toDictionary] description];
}

#pragma mark - DB related methods
+ (id <ModelDelegate>) getDelegate {
	NSLog(@"%@ should implement the getDelegate method", [self className]);
	return nil;
}

+ (NSArray*) fetchList {
	return [self find:nil orderBy:nil limit:0];
}

+ (NSArray*) find:(NSDictionary*)_filters {
	return [self find:_filters orderBy:nil limit:0];
}

+ (NSArray*) find:(NSDictionary*)_filters orderBy:(NSString*)orderBy {
	return [self find:_filters orderBy:orderBy limit:0];
}

+ (NSArray*) find:(NSDictionary*)_filters orderBy:(NSString*)orderBy limit:(int)_limit 
{
	return [self find:_filters orderBy:orderBy limit:_limit offset:0];
}

+ (NSArray*) find:(NSDictionary*)_filters orderBy:(NSString*)orderBy limit:(int)_limit offset:(int)_offset {
	NSMutableArray* result = [NSMutableArray arrayWithCapacity:0];
	id<ModelDelegate> delegate = [self getDelegate];
	id<ModelResultSelect> query = [delegate getFrom:[self className] where:_filters orderBy:orderBy limit:_limit offset:_offset];
	NSArray* _result =[query result];
	for (int i = 0; i < [_result count]; i++)
	{
		[result addObject:[[[self alloc] initWithDictionary:[_result objectAtIndex:i]] autorelease]];
	}
	return (NSArray*)result;
}

- (NSString*) getIdName {
	return [NSString stringWithFormat:@"%@ID", [[self className] lowercaseString]];
}

+ (Model*) findOne:(NSDictionary*)_filters {
	NSArray* found = [self find:_filters orderBy:nil limit:1];
	if ([found count] == 0) return nil;
	else return [found objectAtIndex:0];
}

+ (Model*) findOneById:(int)_id {
	NSMutableDictionary* dictionary = [NSMutableDictionary dictionaryWithCapacity:0];
	[dictionary setValue:[NSNumber numberWithInt:_id] forKey:@"id"];
	return [self findOne:dictionary];
}

//- (void) setId:(int)_id {
//	const char* selectorName = [[NSString stringWithFormat:@"set%@_id:", [[self className] capitalizedString]] UTF8String];
//	SEL selector = sel_getUid(selectorName);
//	if ([self respondsToSelector:selector])
//    {
//		objc_msgSend(self, selector, _id);
//    }
//	else
//    {
//		NSLog(@"Unable to find selector to set id. '%s' was the attempt.", selectorName);
//    }
//}

- (int) getId {
	const char* selectorName = [[self getIdName] UTF8String];
	SEL selector = sel_getUid(selectorName);
	if ([self respondsToSelector:selector])
	{
		NSInvocation* invocation = [NSInvocation invocationWithMethodSignature:[self methodSignatureForSelector:selector]];
		[invocation setTarget:self];
		[invocation setSelector:selector];
		[invocation invoke];
		int _id;
		[invocation getReturnValue:&_id];
		return _id;
	}
	return 0;
}

- (void) save {
	[self delete];
	[self insert];
}

- (void) insert {
	NSDictionary* dictionary = [self toDictionary];
	id <ModelResultInsert> insert = [[[self class] getDelegate] insertInto:[self className] values:(NSDictionary*)dictionary];
	//[self setId:[insert insertId]];
}

- (void) update {
	NSDictionary* dictionary = [self toDictionary];
	NSDictionary* clauses = [NSDictionary dictionaryWithObject:[NSNumber numberWithInt:[self getId]] forKey:@"id"];
	[[[self class] getDelegate] update:[self className] set:dictionary where:clauses];
}


- (void) delete {
	NSMutableDictionary* dictionary = [NSMutableDictionary dictionaryWithCapacity:0];
	[dictionary setValue:[NSNumber numberWithInt:[self getId]] forKey:@"id"];
	[[[self class] getDelegate] delete:[self className] where:dictionary];
}

+ (void) deleteAll{
	[[[self class] getDelegate] delete:[self className] where:nil];
}

+ (void) deleteWhere: (NSDictionary*)dictionary{
	[[[self class] getDelegate] delete:[self className] where:dictionary];
}

@end
