#import <Foundation/Foundation.h>
#import "ElementState.h"

@interface Channel : ElementState
{
	NSString *title;
	NSString *description;
	NSParsedURL *link;
	NSMutableArray *items;
}

@property (nonatomic,readwrite, retain) NSString *title;
@property (nonatomic,readwrite, retain) NSString *description;
@property (nonatomic,readwrite, retain) NSParsedURL *link;
@property (nonatomic,readwrite, retain) NSMutableArray *items;

- (void) setTitleWithReference: (NSString *) p_title;

- (void) setDescriptionWithReference: (NSString *) p_description;

- (void) setLinkWithReference: (NSParsedURL *) p_link;

@end

