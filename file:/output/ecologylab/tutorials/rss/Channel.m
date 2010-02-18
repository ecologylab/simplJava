#import "Channel.h"

@implementation Channel

@synthesize title;
@synthesize description;
@synthesize link;
@synthesize items;

+ (void) initialize {
	[Channel class];
}

- (void) setTitleWithReference: (NSString *) p_title {
	title = *p_title;
}
- (void) setDescriptionWithReference: (NSString *) p_description {
	description = *p_description;
}
- (void) setLinkWithReference: (NSParsedURL *) p_link {
	link = *p_link;
}

@end

