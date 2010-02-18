#import "RssState.h"

@implementation RssState

@synthesize version;
@synthesize channel;

+ (void) initialize {
	[RssState class];
}

- (void) setVersionWithReference: (float *) p_version {
	version = *p_version;
}

@end

