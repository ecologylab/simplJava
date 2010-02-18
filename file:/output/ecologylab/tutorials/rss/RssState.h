#import <Foundation/Foundation.h>
#import "ElementState.h"

@interface RssState : ElementState
{
	float version;
	Channel *channel;
}

@property (nonatomic,readwrite) float version;
@property (nonatomic,readwrite, retain) Channel *channel;

- (void) setVersionWithReference: (float *) p_version;

@end

