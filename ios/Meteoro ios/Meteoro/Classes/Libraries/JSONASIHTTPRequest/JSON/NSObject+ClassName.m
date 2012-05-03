#import <objc/runtime.h>
#import <objc/message.h>

@implementation NSObject (NSObjectClassNameReflection)
#if (TARGET_OS_IPHONE)
- (NSString *)className
{
	return [NSString stringWithUTF8String:class_getName([self class])];
}
+ (NSString *)className
{
	return [NSString stringWithUTF8String:class_getName(self)];
}
#endif
@end