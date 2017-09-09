/**
 * @id jtype.bind_type_references
 * @description 
 * @group jtype.core
 */
MATCH (t:TYPE) MATCH (tref:TYPE_REFERENCE) WHERE t.fqn = tref.fqn CREATE (tref)-[:BOUND_TO]->(t)