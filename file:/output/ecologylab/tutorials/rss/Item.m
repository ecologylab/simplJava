#import "Item.h"

@implementation Item

@synthesize title;
@synthesize description;
@synthesize link;
@synthesize guid;
@synthesize author;
@synthesize categorySet;

+ (void) initialize {
	[Item class];
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
- (void) setGuidWithReference: (NSParsedURL *) p_guid {
	guid = *p_guid;
}
- (void) setAuthorWithReference: (NSString *) p_author {
	author = *p_author;
}
- (void) setCategorySetWithReference: (NSMutableArray *) p_categorySet {
	categorySet = *p_categorySet;
}

@end

