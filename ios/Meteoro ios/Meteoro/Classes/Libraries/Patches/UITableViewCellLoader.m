#import "UITableViewCellLoader.h"
#import "NSObjectClassName.h"

@implementation UITableViewCell (loadCellNib)

+ (UITableViewCell*) loadCellNibForTableView:(UITableView*)tableView withIdentifier:(NSString*)cellNibName  {
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellNibName]; 
	
    if (cell == nil){
        NSArray *nibObjects = [[NSBundle mainBundle] loadNibNamed:cellNibName owner:nil options:nil];
        for (id currentObject in nibObjects){
            if([currentObject isKindOfClass:[UITableViewCell class]]){
                cell = (UITableViewCell*)currentObject;
                if (![cell.reuseIdentifier isEqualToString:cellNibName]){
					NSLog(@"Cell (%@) identifier does not match its nib name", cellNibName);
				}
                break;
            }
        }
    }
	
	if ( !cell ) {
		NSLog(@"Unable to load cell from nib: %@", cellNibName);
	}
	
	return cell;
}

+ (UITableViewCell*) loadCellNibForTableView:(UITableView*)tableView {
	return [[self class] loadCellNibForTableView:tableView withIdentifier:[self className]];
}
@end