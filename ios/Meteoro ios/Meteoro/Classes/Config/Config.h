//
//  Config.h
//

#import <Foundation/Foundation.h>
#import "Config.h"

#import <MessageUI/MessageUI.h>
#import <MessageUI/MFMailComposeViewController.h>

// Dispatch period in seconds
#define kGANDispatchPeriodSec 10
// **************************************************************************
// PLEASE REPLACE WITH YOUR ACCOUNT DETAILS.
// **************************************************************************
#define kAnalyticsAccountId @"UA-28567719-1"

#define YOUTUBE_DEVELOPER_KEY @"AI39si4VPijQ8a17zmfZMiUdT4UWgc8tZoKYDRIneZZWs7ASzMEYEMz3e613rA0McqzFHvq4JjB-9ffjtDPgUUnef4Qba0a6UQ"
#define GOOGLE_CLIENT_ID @"658360655620.apps.googleusercontent.com"
#define GOOGLE_CLIENT_SECRET @"88MrO5bIEPvPOAs06loPEHtb"

#define BING_APP_ID @"D1D70F67149587128F2FBACCEF97000C25223035"
// Pangea URLS Stable  
#define PANGEA_CATEGORY @"channels"
#define PANGEA_CONTENT_ARTICLES  @"content/articles"
#define PANGEA_SEARCH @"search?query="
#define PANGEA_CONTENT @"content?url="
#define PANGEA_USER @"users"
#define PANGEA_USER_LIST @"users/list_users?ids="
#define PANGEA_USER_ACCOUNT @"users/authService"
#define PANGEA_CONTENT_TAG @"content/tags?category="
#define PANGEA_CONTENT_SOURCES @"content/sources?where="
#define PANGEA_CONTENT_SEARCH_TAGS @"content/search_tags?contains="
#define PANGEA_DELETE_ACCOUNT @"users/delete_account/"
#define PANGEA_ACCOUNT_IMAGES @"channel_images/"

// Font sizes keys
#define FONT_SIZE_KEY @"FONT_SIZE_OFFSET"
#define WEB_SIZE_KEY @"WEB_SIZE_OFFSET"

// Files names
#define USERS_IDS_FILE_NAME @"UsersIds"

//SERVER REQUEST
#define USER_AGENT @""
#define CONFIG_BASE_URL_DEFAULT @"http://meteoro.herokuapp.com/"
//#define CONFIG_BASE_URL @""
#define CRASH_REPORTING_PATH @"analytics/recordCrash"
#define USER_ACCOUNT_KEY @"__User_Account__" 

#define SERVICE_SIGNUP  @"create_account"
#define SERVICE_LOGIN  @"login"

//Animations delay
#define ANIM_DELAY 0.55

#define ALLOW_ALL_ORIENTATIONS NO

#define USERS_ACCOUNT_KEY @"__Users_Account__" 

//Corner radious
#define CORNER_RADIUS 15

@class City;
@interface Config : NSObject <MFMailComposeViewControllerDelegate, MFMessageComposeViewControllerDelegate> {
	
	//From where the email/message view was shown
	UIViewController* viewControllerHoldingUIMessageView;

	NSDateFormatter *dateFormatter;
    NSDateFormatter *dateFormatterTwitter;
    
	UIColor* barTintColor;
	NSCharacterSet* numberSet;
	
	int networkActivityIndicatorRetainCount;
    
    NSString* CONFIG_BASE_URL; 
}

@property (nonatomic, retain) NSString* CONFIG_BASE_URL;
@property (nonatomic, readonly) NSArray* cities;

- (void) addCity:(City*) city;
- (void) saveCities;

+ (Config*)getInstance;

- (NSString*) getIdsFileCompletePath;

- (NSURL*) URLWithString:(NSString*)_string;
- (UIColor*) navigationBarTintColor;

#pragma mark - manage time and formatters
- (int) getTimeIntFromString: (NSString *) time;
- (NSString*) dateToString:(NSDate*) date;
- (NSString*) stringFromDateInt:(int) intDate;
- (NSString*)timeElapsedFromInt:(int)time;
- (NSString*)timeElapsed:(NSDate*)_date;
- (NSDate*) getUTCDate;
- (NSDate*) getTimeDateFromTwitterString: (NSString *) time;
- (NSDate*) getTimeDateFromString: (NSString *) time;
- (NSString *)dateDifferenceStringFromString:(NSString *)dateString withFormat:(NSString *)dateFormat;

#pragma mark - NSUserDefaults Management
+ (void) eraseNSUserDefaults;
+ (NSString*) getCurrentUserKey;

#pragma mark - detecting device information
+ (BOOL) hasHDScreen;

#pragma mark - convert addressbook phone numbers to only numbers string
- (NSString*) getNormalizedMobileNumberFromString:(NSString*) mobileNumber;

#pragma mark - networkActivityIndicator
- (void) retainNetworkActivityIndicator;
- (void) releaseNetworkActivityIndicator;

#pragma mark - show alert
- (void) showAlertWithTitle:(NSString*) title andMsg:(NSString*) msg;
- (NSArray *) getTimeZoneArray;
- (NSArray *) getIndustryArray;
- (NSArray *) getCampaignArray;
- (NSArray*) getGmtFromTimeZone: (NSString*) timeZone;

#pragma mark - String Managment
- (NSString*) replaceOcurrenceInString:(NSString*)_string withStringsInArray:(NSArray*)_array;

#pragma mark - Random Numbers
+ (int)getRandomNumberFrom:(int)from To:(int)to;

#pragma mark - Mail management
- (void) sendEmail:(NSString*) body withSubject:(NSString*)subject toRecipients:(NSArray*) toRecipients fromViewController:(UIViewController*) viewController andDelegate: (id <MFMailComposeViewControllerDelegate>) delegate;
- (void) sendEmail:(NSString*) body withSubject:(NSString*)subject toRecipients:(NSArray*) toRecipients fromViewController:(UIViewController*) viewController;
- (void) sendText:(NSString*)body toRecipients:(NSArray*) toRecipients fromViewController:(UIViewController*) viewController;

#pragma mark - Mail management
+ (void) showAlertWithTitle:(NSString*)title andMessage:(NSString*)msg;
@end
