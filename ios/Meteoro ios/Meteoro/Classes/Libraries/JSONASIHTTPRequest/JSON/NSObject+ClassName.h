@interface NSObject (NSObjectClassNameReflection)
#if (TARGET_OS_IPHONE)
- (NSString *)className;
+ (NSString *)className;
#endif
@end