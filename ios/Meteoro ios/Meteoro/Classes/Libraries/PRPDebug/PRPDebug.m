/***
 * Excerpted from "iOS Recipes",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/cdirec for more book information.
 ***/
//
//  PRPDebug.m
//
//  Created by Matt Drance on 8/28/09.
//  Copyright 2009 Bookhouse. All rights reserved.
//

#import "PRPDebug.h"

void PRPDebug(NSString *className, NSString *selectorName, const char *fileName, int lineNumber, NSString *fmt, ...)
{
    //static vars
    static NSDateFormatter *debugFormatter = nil;
    if (debugFormatter == nil) {
        debugFormatter = [[NSDateFormatter alloc] init];
        [debugFormatter setDateFormat:@"yyyyMMdd.HH:mm:ss"];
    }
    
    static  NSString *appName = nil;
    if (appName == nil)
    {
        NSDictionary *info = [[NSBundle mainBundle] infoDictionary];
        appName = [info objectForKey:(NSString *)kCFBundleNameKey];
    }
    
    va_list args;
    va_start(args, fmt);
    
    NSString *msg = [[NSString alloc] initWithFormat:fmt arguments:args];
    NSString *filePath = [[NSString alloc] initWithUTF8String:fileName];    
    NSString *timestamp = [debugFormatter stringFromDate:[NSDate date]];
    
    NSString* logLine = [[NSString alloc] initWithFormat:@"%@ %@[%@:%d][%@:(%@)] %@",
                         timestamp,
                         appName,
                         [filePath lastPathComponent],
                         lineNumber,
                         className,
                         selectorName,
                         msg
                         ];
    
    [[NSNotificationCenter defaultCenter] postNotificationName:NEW_PRPDEBUG_LOG_LINE object:logLine];
    
    fprintf(stdout, "%s\n", [logLine UTF8String]);
    
    va_end(args);
    
    [msg release];
    [filePath release];
    [logLine release];
}