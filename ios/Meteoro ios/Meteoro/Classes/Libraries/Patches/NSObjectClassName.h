@interface NSObject (NSObjectClassNameReflection)
#if (TARGET_OS_IPHONE)
/**
 * Returns the string representation of the class of the instance
 */
- (NSString *)className;
/**
 * Returns the string representation of the class
 */
+ (NSString *)className;
#endif
@end