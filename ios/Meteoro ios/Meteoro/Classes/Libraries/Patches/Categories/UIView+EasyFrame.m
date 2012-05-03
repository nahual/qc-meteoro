//
//  UIView+EasyFrame.m
//  Chicle
//
//  Created by Ramiro Diaz Ortiz on 14/03/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "UIView+EasyFrame.h"

@implementation UIView (EasyFrame)

- (CGFloat) x
{
    return self.frame.origin.x;
}

- (CGFloat) y
{
    return self.frame.origin.y;
}

- (CGFloat) width
{
    return self.frame.size.width;
}

- (CGFloat) height
{
    return self.frame.size.height;
}

- (void) setX:(CGFloat)x
{
    self.frame = CGRectMake(x, self.frame.origin.y, self.frame.size.width, self.frame.size.height);
}

- (void) setY:(CGFloat)y
{
    self.frame = CGRectMake(self.frame.origin.x, y, self.frame.size.width, self.frame.size.height);
}

- (void) setHeight:(CGFloat)height
{
    self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y, self.frame.size.width, height);
}

- (void) setWidth:(CGFloat)width
{
    self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y, width, self.frame.size.height);
}

@end
