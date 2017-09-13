/**
 * @id jtype.tag_parent_packages
 * @description 
 * @group jtype.core
 */
MATCH (n:DIRECTORY)-[:CONTAINS*]->(t:PACKAGE) set n :PACKAGE
