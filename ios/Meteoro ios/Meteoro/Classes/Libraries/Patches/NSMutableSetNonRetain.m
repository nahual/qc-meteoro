const void* MRetainNoOp(CFAllocatorRef allocator, const void *value) { return value; }
void MReleaseNoOp(CFAllocatorRef allocator, const void *value) { }

@implementation NSMutableSet (SetNonRetaining)
+ (NSMutableSet*) setNonRetaining {
    CFSetCallBacks callbacks = kCFTypeSetCallBacks;
    callbacks.retain = MRetainNoOp;
    callbacks.release = MReleaseNoOp;
	NSMutableSet* set = (NSMutableSet*)CFSetCreateMutable(nil, 0, &callbacks);
    return [set autorelease];
}
@end