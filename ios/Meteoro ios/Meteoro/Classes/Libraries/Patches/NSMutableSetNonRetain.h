const void* MRetainNoOp(CFAllocatorRef allocator, const void *value);
void MReleaseNoOp(CFAllocatorRef allocator, const void *value);

@interface NSMutableSet (SetNonRetaining)
/**
 * Creates a mutable set where the containing objects won't receive retain/release messages.
 */
+ (NSMutableSet*) setNonRetaining;
@end
