#import <Foundation/Foundation.h>
#import "ElementState.h"

@interface Item : ElementState
{
	NSString *title;
	NSString *description;
	NSParsedURL *link;
	NSParsedURL *guid;
	NSString *author;
	NSMutableArray *categorySet;
}

@property (nonatomic,readwrite, retain) NSString *title;
@property (nonatomic,readwrite, retain) NSString *description;
@property (nonatomic,readwrite, retain) NSParsedURL *link;
@property (nonatomic,readwrite, retain) NSParsedURL *guid;
@property (nonatomic,readwrite, retain) NSString *author;
@property (nonatomic,readwrite, retain) NSMutableArray *categorySet;

- (void) setTitleWithReference: (NSString *) p_title;

- (void) setDescriptionWithReference: (NSString *) p_description;

- (void) setLinkWithReference: (NSParsedURL *) p_link;

- (void) setGuidWithReference: (NSParsedURL *) p_guid;

- (void) setAuthorWithReference: (NSString *) p_author;

- (void) setCategorySetWithReference: (NSMutableArray *) p_categorySet;

@end

