
@interface UITableViewCell (loadCellNib)
/**
 * Reuses the cell if exists, otherwise creates a new instance inflating a xib with the same name as the class.
 */
+ (UITableViewCell*) loadCellNibForTableView:(UITableView*)tableView;
/**
 * Reuses the cell if exists, otherwise creates a new instance inflating a xib with the name passed in the parameter.
 */
+ (UITableViewCell*) loadCellNibForTableView:(UITableView*)tableView withIdentifier:(NSString*)cellNibName;
@end
