@protocol ModelResultSelect

- (NSDictionary*) row;
- (NSArray*) result;

@end

@protocol ModelResultInsert

- (int)insertId;

@end

@protocol ModelResultUpdate


@end

@protocol ModelResultDelete


@end

@protocol ModelDelegate <NSObject>

- (id <ModelResultSelect>) getFrom:(NSString*)from where:(NSDictionary*)_where orderBy:(NSString*)_orderBy limit:(int)_limit offset:(int) _offset;
- (id <ModelResultInsert>) insertInto:(NSString*)_tableName values:(NSDictionary*)_values;
- (id <ModelResultUpdate>) update:(NSString*)_tableName set:(NSDictionary*)_values where:(NSDictionary*)_where;
- (id <ModelResultDelete>) delete:(NSString*)_tableName where:(NSDictionary*)_where;

@end

@protocol ModelClause

+ (id<ModelClause>)clauseWithDictionary:(NSDictionary*)dictionary;
+ (id<ModelClause>)clauseWithClauses:(NSArray*)dictionary;
+ (id<ModelClause>)clauseWithClauses:(NSArray*)_clauses withAny:(BOOL)_any;
- (NSString*)toString;

@end
