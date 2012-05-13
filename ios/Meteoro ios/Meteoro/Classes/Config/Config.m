//
//  Config.m
//

#import "Config.h"
#import "City.h"

@implementation Config
@synthesize cities;
@synthesize CONFIG_BASE_URL;

static Config* instance = nil;

-(id) init
{
    self = [super init];
    if (self) {
	    
        self.CONFIG_BASE_URL = CONFIG_BASE_URL_DEFAULT;
        
        dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setDateFormat:@"MMMM-dd-yyyy"];
        [dateFormatter setLocale:[NSLocale currentLocale]];
        
        dateFormatterTwitter = [[NSDateFormatter alloc] init];
        [dateFormatterTwitter setDateFormat:@"eee MMM dd HH:mm:ss Z yyyy"];
        [dateFormatterTwitter setLocale:[NSLocale currentLocale]];
        
        numberSet = [[NSCharacterSet characterSetWithCharactersInString:@"0123456789"] retain];
        
        barTintColor = [[UIColor alloc] initWithRed:47.0/255 green:77.0/255 blue:153.0/255 alpha:1.0];
        
        NSUserDefaults *currentDefaults = [NSUserDefaults standardUserDefaults];
        NSData *dataRepresentingSavedArray = [currentDefaults objectForKey:@"cities"];
        if (dataRepresentingSavedArray != nil)
        {
            NSArray *oldSavedArray = [NSKeyedUnarchiver unarchiveObjectWithData:dataRepresentingSavedArray];
            if (oldSavedArray != nil)
                cities = [[NSArray alloc] initWithArray:oldSavedArray];
            else
                cities = [[NSArray alloc] init];
        } else {
            cities = [[NSArray alloc] init];
        }
    }

    return self;
}

- (void) saveCities
{
    [[NSUserDefaults standardUserDefaults] setObject:[NSKeyedArchiver archivedDataWithRootObject:self.cities] forKey:@"cities"];
}

- (void) addCity:(City*) city
{
    if (![cities containsObject:city])
    {
        NSMutableArray* mutArray = [NSMutableArray arrayWithArray:self.cities];
        [mutArray addObject:city];
        cities = [[NSArray alloc] initWithArray:mutArray];
    }
    [self saveCities];
}

+ (Config*)getInstance
{
	@synchronized(self)
	{
		if (!instance)
		{
			instance = [[Config alloc] init];
		}
		return instance;
	}
	return nil;
}

- (NSURL*) URLWithString:(NSString*)_string {
	return [NSURL URLWithString:[NSString stringWithFormat:@"%@%@", self.CONFIG_BASE_URL, _string]];
}

- (UIColor*) navigationBarTintColor {
    return barTintColor;
}

#pragma mark - User ids file name

- (NSString*) getIdsFileCompletePath {
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [[[paths objectAtIndex:0] stringByAppendingString:@"/"] retain];
    NSString* fileName = USERS_IDS_FILE_NAME;
    NSString *path = [NSString stringWithFormat:@"%@%@", documentsDirectory, fileName];
    [documentsDirectory release];
    return path;
}

#pragma mark -
#pragma mark Date Formater methods

- (NSString *)dateDifferenceStringFromString:(NSString *)dateString
                                  withFormat:(NSString *)dateFormat
{
    NSDateFormatter *_dateFormatter = [[NSDateFormatter alloc] init];
    [_dateFormatter setFormatterBehavior:NSDateFormatterBehavior10_4];
    [_dateFormatter setDateFormat:dateFormat];
    NSDate *date = [_dateFormatter dateFromString:dateString];
    [_dateFormatter release];
    NSDate *now = [NSDate date];
    double time = [date timeIntervalSinceDate:now];
    time *= -1;
    if(time < 1) {
        return dateString;
    } else if (time < 60) {
        return NSLocalizedString(@"less than a minute ago", @"");
    } else if (time < 3600) {
        int diff = (int) round(time / 60);
        if (diff == 1) 
            return [NSString stringWithFormat:NSLocalizedString(@"1 minute ago", @""), diff];
        return [NSString stringWithFormat:NSLocalizedString(@"%d minutes ago", @""), diff];
    } else if (time < 86400) {
        int diff = (int) round(time / 60 / 60);
        if (diff == 1)
            return [NSString stringWithFormat:NSLocalizedString(@"1 hour ago", @""), diff];
        return [NSString stringWithFormat:NSLocalizedString(@"%d hours ago", @""), diff];
    } else if (time < 604800) {
        int diff = (int) round(time / 60 / 60 / 24);
        if (diff == 1) 
            return [NSString stringWithFormat:NSLocalizedString(@"yesterday", @""), diff];
        if (diff == 7) 
            return [NSString stringWithFormat:NSLocalizedString(@"last week", @""), diff];
        return[NSString stringWithFormat:NSLocalizedString(@"%d days ago", @""), diff];
    } else {
        int diff = (int) round(time / 60 / 60 / 24 / 7);
        if (diff == 1)
            return [NSString stringWithFormat:NSLocalizedString(@"last week", @""), diff];
        return [NSString stringWithFormat:NSLocalizedString(@"%d weeks ago", @""), diff];
    }   
}


- (int) getTimeIntFromString: (NSString *) time {
	return [[dateFormatter dateFromString:time] timeIntervalSince1970];
}

- (NSDate*) getTimeDateFromString: (NSString *) time {
	return [dateFormatter dateFromString:time];
}

- (NSDate*) getTimeDateFromTwitterString: (NSString *) time {
	return [dateFormatterTwitter dateFromString:time];
}


- (NSString*) dateToString:(NSDate*) date
{
	return [dateFormatter stringFromDate:date];
}

- (NSString*) stringFromDateInt:(int) intDate
{
	NSDate* dateBefore = [NSDate dateWithTimeIntervalSince1970:intDate];
	return [self dateToString:dateBefore];
}

- (NSString*)timeElapsedForTime:(int)distanceTime {
	NSString *stringTime;
	
	if (distanceTime < 0) 
		distanceTime = 0;
	
	if(distanceTime < 60){
		stringTime = [NSString stringWithFormat:@"%ds ago",distanceTime];
	}else if(distanceTime < 3600)
	{
		stringTime = [NSString stringWithFormat:@"%dm ago",distanceTime/60];
	}else if(distanceTime < 3600*24) {
		stringTime = [NSString stringWithFormat:@"%dh ago",distanceTime/3600];
	}else if(distanceTime < 3600*24*7) {
		stringTime = [NSString stringWithFormat:@"%dd ago",distanceTime/3600/24];
	}else{
		stringTime = [NSString stringWithFormat:@"%dw ago",distanceTime/3600/24/7];		
	}
	
	return stringTime;
}

- (NSString*)timeElapsedFromInt:(int)time {
	
	int distanceTime = [[NSDate date] timeIntervalSince1970] - time;
	
	return [self timeElapsedForTime:distanceTime];
}

- (NSString*)timeElapsed:(NSDate*)_date {
	
	NSInteger distanceTime = [[NSDate date] timeIntervalSinceDate:_date];//beforeTime - afterTime;
	return [self timeElapsedForTime:distanceTime];
}

- (NSDate*) getUTCDate{
	NSTimeInterval offset = [[NSTimeZone defaultTimeZone] secondsFromGMT];
	NSDate* utc = [NSDate dateWithTimeInterval:-offset sinceDate:[NSDate date]];
	return utc;
}

#pragma mark - NSUserDefaults Management
+ (void) eraseNSUserDefaults
{
	NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
	
	[defaults setValue:nil forKey:[self getCurrentUserKey]];
	
	[defaults synchronize];
}

+ (NSString*) getCurrentUserKey
{
	return @"_CURRENT_USER_KEY_";
}


- (void) dealloc {
    
    self.CONFIG_BASE_URL = nil;
    
	instance = nil;
	[dateFormatter release];
	dateFormatter = nil;
	
    [dateFormatterTwitter release];
    dateFormatterTwitter = nil;
    
	[numberSet release];
	numberSet = nil;
	
	[barTintColor release];
	barTintColor = nil;
    
	[super dealloc];
}

#pragma mark -
#pragma mark detecting device information
+ (BOOL) hasHDScreen
{
	if ([[UIScreen mainScreen] respondsToSelector:@selector(scale)])
		if([UIScreen mainScreen].scale == 2)
			return YES;
	return NO;
}

#pragma mark -
#pragma mark convert addressbook phone numbers to only numbers string
- (NSString*) getNormalizedMobileNumberFromString:(NSString*) mobileNumber
{
	int length = mobileNumber.length;
	char c;
	int i;
	NSString* buffer = @"";
    
	for (i = 0; i < length; i++) {
		c = [mobileNumber characterAtIndex:i];		
		if ([numberSet characterIsMember:c])
			buffer = [buffer stringByAppendingFormat:@"%c", c];
	}
	
	//If first number is not the number 1, add the number 1
	NSString* finalNumber = @"";
	if (buffer.length > 0) {
		c = [buffer characterAtIndex:0];
		NSString* firstNumber = [NSString stringWithFormat:@"%c", c];
		if (![firstNumber isEqualToString:@"1"]) 
			finalNumber = @"1";	
		finalNumber = [finalNumber stringByAppendingFormat:@"%@", buffer];
	}
	
	return finalNumber;
}

#pragma mark -
#pragma mark NetworkActivityIndicator
- (void) retainNetworkActivityIndicator
{
    //[UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
	networkActivityIndicatorRetainCount++;
	
	if (networkActivityIndicatorRetainCount == 1)
	{
		[UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
	}
}

- (void) releaseNetworkActivityIndicator
{
    //[UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
	networkActivityIndicatorRetainCount--;
	
	if (networkActivityIndicatorRetainCount < 1)
	{
		[UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
	}
}

#pragma mark - 
#pragma mark - Show alert

- (void) showAlertWithTitle:(NSString*) title andMsg:(NSString*) msg
{
    [[[[UIAlertView alloc] initWithTitle:title message:msg delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] autorelease] show];
}


- (NSArray *) getTimeZoneArray {
    NSArray *keys = [NSArray arrayWithObjects:  
                     @"(GMT-12:00) International Date Line West",
                     @"(GMT-11:00) Samoa",
                     @"(GMT-11:00) Coordinated Universal Time-11",
                     @"(GMT-10:00) Hawaii",
                     @"(GMT-09:00) Alaska",
                     @"(GMT-08:00) Pacific Time (US & Canada)",
                     @"(GMT-08:00) Baja California",
                     @"(GMT-07:00) Mountain Time (US & Canada)",
                     @"(GMT-07:00) Chihuahua, La Paz, Mazatlan - Old",
                     @"(GMT-07:00) Chihuahua, La Paz, Mazatlan - New",
                     @"(GMT-07:00) Arizona",
                     @"(GMT-06:00) Saskatchewan",
                     @"(GMT-06:00) Guadalajara, Mexico City, Monterrey - Old",
                     @"(GMT-06:00) Guadalajara, Mexico City, Monterrey - New",
                     @"(GMT-06:00) Central Time (US & Canada)",
                     @"(GMT-06:00) Central America",
                     @"(GMT-05:00) Indiana (East)",
                     @"(GMT-05:00) Eastern Time (US & Canada)",
                     @"(GMT-05:00) Bogota, Lima, Quito",
                     @"(GMT-04:30) Caracas",
                     @"(GMT-04:00) Santiago",
                     @"(GMT-04:00) Georgetown, La Paz, Manaus, San Juan",
                     @"(GMT-04:00) Cuiaba",
                     @"(GMT-04:00) Atlantic Time (Canada)",
                     @"(GMT) Coordinated Universal Time",
                     @"(GMT) Casablanca",
                     @"(GMT) Greenwich Mean Time : Dublin, Edinburgh, Lisbon, London",
                     @"(GMT) Monrovia, Reykjavik",
                     @"(GMT+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna",
                     @"(GMT+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague",
                     @"(GMT+01:00) Brussels, Copenhagen, Madrid, Paris",
                     @"(GMT+01:00) Sarajevo, Skopje, Warsaw, Zagreb",
                     @"(GMT+01:00) West Central Africa",
                     @"(GMT+01:00) Windhoek",
                     @"(GMT+02:00) Amman",
                     @"(GMT+02:00) Athens, Bucharest, Istanbul",
                     @"(GMT+02:00) Beirut",
                     @"(GMT+02:00) Cairo",
                     @"(GMT+02:00) Damascus",
                     @"(GMT+02:00) Harare, Pretoria",
                     @"(GMT+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius",
                     @"(GMT+02:00) Jerusalem",
                     @"(GMT+02:00) Minsk",
                     @"(GMT+03:00) Baghdad",
                     @"(GMT+03:00) Kuwait, Riyadh",
                     @"(GMT+03:00) Moscow, St. Petersburg, Volgograd",
                     @"(GMT+03:00) Nairobi",
                     @"(GMT+03:30) Tehran",
                     @"(GMT+04:00) Abu Dhabi, Muscat",
                     @"(GMT+04:00) Baku",
                     @"(GMT+04:00) Caucasus Standard Time",
                     @"(GMT+04:00) Port Louis",
                     @"(GMT+04:00) Tbilisi",
                     @"(GMT+04:00) Yerevan",
                     @"(GMT+04:30) Kabul",
                     @"(GMT+05:00) Ekaterinburg",
                     @"(GMT+05:00) Islamabad, Karachi",
                     @"(GMT+05:00) Tashkent",
                     @"(GMT+05:30) Chennai, Kolkata, Mumbai, New Delhi",
                     @"(GMT+05:30) Sri Jayawardenepura",
                     @"(GMT+05:45) Kathmandu",
                     @"(GMT+06:00) Astana",
                     @"(GMT+06:00) Dhaka",
                     @"(GMT+06:00) Novosibirsk",
                     @"(GMT+06:30) Yangon (Rangoon)",
                     @"(GMT+07:00) Bangkok, Hanoi, Jakarta",
                     @"(GMT+07:00) Krasnoyarsk",
                     @"(GMT+08:00) Beijing, Chongqing, Hong Kong, Urumqi",
                     @"(GMT+08:00) Irkutsk",
                     @"(GMT+08:00) Kuala Lumpur, Singapore",
                     @"(GMT+08:00) Perth",
                     @"(GMT+08:00) Taipei",
                     @"(GMT+08:00) Ulaanbaatar",
                     @"(GMT+09:00) Osaka, Sapporo, Tokyo",
                     nil];
    return keys;
}

- (NSArray *) getIndustryArray {
    NSArray *keys = [NSArray arrayWithObjects:  
                     @"Other",
                     @"Graphic Design",
                     @"Music",
                     @"Photography",
                     @"Commercial Real Estate",
                     @"Management Consulting",
                     @"Marketing & Advertising",
                     @"Public Relations",
                     @"Staffing & Recruiting",
                     @"Accounting",
                     @"Banking",
                     @"Financial Services",
                     @"Insurance",
                     @"Investment Management",
                     @"Real Estate",
                     @"Venture Capital",
                     @"Acupunture",
                     @"Alternative Medicine",
                     @"Chiropractic",
                     @"Dentistry",
                     @"Diet & Nutrition",
                     @"Health, Wellness",
                     @"Fitness",
                     @"Medical Practice",
                     @"Internet",
                     @"Bars",
                     @"Law Practice",
                     @"Legal Services",
                     @"Broadcast Media",
                     @"Design",
                     @"Entertainment",
                     @"Motion Pictures & Film",
                     @"Hotel & Hospitality",
                     @"Leisure & Travel",
                     @"Recreational Facilities & Services",
                     @"Apparel & Fashion",
                     @"Automotive",
                     @"Beauty, Spa, Salon",
                     @"Consumer Electronics",
                     @"Cosmetics",
                     @"Luxury Goods & Jewelry",
                     @"Sporting Goods",
                     @"Toys",
                     @"Restaurants",
                     nil];
    return keys;
}

- (NSArray *) getCampaignArray {
    NSArray *keys = [NSArray arrayWithObjects:  
                     @"Other",
                     @"Graphic Design",
                     @"Music",
                     @"Photography",
                     @"Commercial Real Estate",
                     @"Management Consulting",
                     @"Marketing & Advertising",
                     @"Public Relations",
                     @"Staffing & Recruiting",
                     @"Accounting",
                     @"Banking",
                     @"Financial Services",
                     @"Insurance",
                     @"Investment Management",
                     @"Real Estate",
                     @"Venture Capital",
                     @"Acupunture",
                     @"Alternative Medicine",
                     @"Chiropractic",
                     @"Dentistry",
                     @"Diet & Nutrition",
                     @"Health, Wellness",
                     @"Fitness",
                     @"Medical Practice",
                     @"Internet",
                     @"Bars",
                     @"Law Practice",
                     @"Legal Services",
                     @"Broadcast Media",
                     @"Design",
                     @"Entertainment",
                     @"Motion Pictures & Film",
                     @"Hotel & Hospitality",
                     @"Leisure & Travel",
                     @"Recreational Facilities & Services",
                     @"Apparel & Fashion",
                     @"Automotive",
                     @"Beauty, Spa, Salon",
                     @"Consumer Electronics",
                     @"Cosmetics",
                     @"Luxury Goods & Jewelry",
                     @"Sporting Goods",
                     @"Toys",
                     @"Restaurants",
                     nil];
    return keys;
}

- (NSArray*) getGmtFromTimeZone: (NSString*) timeZone {
    NSString * sign = [timeZone substringWithRange:NSMakeRange (0, 2)];
    NSString* hours = [timeZone substringWithRange:NSMakeRange (2, 2)];
    NSString* minutes = [timeZone substringWithRange:NSMakeRange (4, 2)];
    NSArray *aux = [NSArray arrayWithObjects: sign, hours, minutes, nil];
    return aux;
}

#pragma mark - Strings Managment

- (NSString*) replaceOcurrenceInString:(NSString*)_string withStringsInArray:(NSArray*)_array{
    NSString* aux = _string;
    for(NSInteger i = 0; i <= [_array count]-2; i+=2) {
        aux = [aux stringByReplacingOccurrencesOfString:[_array objectAtIndex:i] withString:[_array objectAtIndex:i+1]];
    }
    return aux;
}

#pragma mark - Random Numbers

+ (int)getRandomNumberFrom:(int)from To:(int)to {
	return (int)from + random() % (to-from+1);
}

#pragma mark - Mail management

- (void) sendEmail:(NSString*) body withSubject:(NSString*)subject toRecipients:(NSArray*) toRecipients fromViewController:(UIViewController*) viewController andDelegate: (id <MFMailComposeViewControllerDelegate>) delegate
{
    Class controllerClass = NSClassFromString(@"MFMailComposeViewController");
    if (controllerClass)
    {
        if ([MFMailComposeViewController canSendMail])
        {
            MFMailComposeViewController *picker = [[MFMailComposeViewController alloc] init];
            picker.mailComposeDelegate = delegate;
            [picker setSubject:subject];
            
            [picker setToRecipients:toRecipients];
            [picker setMessageBody:body isHTML:YES];
            
			viewControllerHoldingUIMessageView = viewController;
            [viewController presentModalViewController:picker animated:YES];
            [picker release];
        }
        else
        {
            UIAlertView* alert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Error", @"") message:NSLocalizedString(@"Email sending is not supported on this device.", @"") delegate:nil cancelButtonTitle: NSLocalizedString(@"OK", @"") otherButtonTitles:nil];
            [alert show];
            [alert release];
        }
    }
    else
	{
		UIAlertView* alert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Error", @"") message:NSLocalizedString(@"Email sending is not supported on this firmaware version, please upgrade.", @"") delegate:nil cancelButtonTitle: NSLocalizedString(@"OK", @"") otherButtonTitles:nil];
		[alert show];
		[alert release];
	}
}
- (void) sendEmail:(NSString*) body withSubject:(NSString*)subject toRecipients:(NSArray*) toRecipients fromViewController:(UIViewController*) viewController {
    [self sendEmail:body withSubject:subject toRecipients:toRecipients fromViewController:viewController andDelegate: self];
}
- (void)mailComposeController:(MFMailComposeViewController*)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError*)error
{
	[viewControllerHoldingUIMessageView becomeFirstResponder];
	[controller dismissModalViewControllerAnimated:YES];
	
	viewControllerHoldingUIMessageView = nil;
}

- (void) sendText:(NSString*)body toRecipients:(NSArray*) toRecipients fromViewController:(UIViewController*) viewController
{
    Class controllerClass = NSClassFromString(@"MFMessageComposeViewController");
    if (controllerClass)
    {
        if ([controllerClass canSendText])
        {
            MFMessageComposeViewController *smsController = [[controllerClass alloc] init];
            smsController.messageComposeDelegate = self;
            smsController.recipients = toRecipients;
            
			smsController.body = body;
            
			viewControllerHoldingUIMessageView = viewController;
            [viewController presentModalViewController:smsController animated:YES];
            [smsController release];
        }
        else
        {
            UIAlertView* alert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Error", @"") message:NSLocalizedString(@"Text Message are not supported on this device.", @"") delegate:nil cancelButtonTitle: NSLocalizedString(@"OK", @"") otherButtonTitles:nil];
            [alert show];
            [alert release];
        }
    }
    else
    {
        UIAlertView* alert = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Error", @"") message:NSLocalizedString(@"Text Message is not supported on this firmaware version, please upgrade.", @"") delegate:nil cancelButtonTitle: NSLocalizedString(@"OK", @"") otherButtonTitles:nil];
        [alert show];
        [alert release];
    }
}

- (void)messageComposeViewController:(MFMessageComposeViewController *)controller didFinishWithResult:(MessageComposeResult)result{
	[viewControllerHoldingUIMessageView becomeFirstResponder];
	[controller dismissModalViewControllerAnimated:YES];    
}

+ (void) showAlertWithTitle:(NSString*)title andMessage:(NSString*)msg
{
    UIAlertView* alert = [[UIAlertView alloc] initWithTitle:title message:msg delegate:nil cancelButtonTitle:NSLocalizedString(@"OK", @"") otherButtonTitles:nil];
    [alert show];
    [alert release];
}

@end
