package ecologylab.translators.hibernate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ecologylab.generic.Debug;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.XMLTools;

/**
 * default implementation of the DbNameGenerator, with cache.
 * 
 * @author quyin
 *
 */
@SuppressWarnings("rawtypes")
public class DefaultCachedDbNameGenerator extends Debug implements DbNameGenerator
{
	
	/* note that sql reserved are not case sensitive */
	/* according to: http://www.postgresql.org/docs/9.0/static/sql-keywords-appendix.html */
	/* for postgresql / sql:2008 only */
	private static final Set<String> SQL_RESERVED = new HashSet<String>(Arrays.asList(new String[] {
		"abs",
		"all",
		"allocate",
		"alter",
		"analyse",
		"analyze",
		"and",
		"any",
		"are",
		"array",
		"array_agg",
		"as",
		"asc",
		"asensitive",
		"asymmetric",
		"at",
		"atomic",
		"authorization",
		"avg",
		"begin",
		"between",
		"bigint",
		"binary",
		"blob",
		"boolean",
		"both",
		"by",
		"call",
		"called",
		"cardinality",
		"cascaded",
		"case",
		"cast",
		"ceil",
		"ceiling",
		"char",
		"character",
		"character_length",
		"char_length",
		"check",
		"clob",
		"close",
		"coalesce",
		"collate",
		"collect",
		"column",
		"commit",
		"concurrently",
		"condition",
		"connect",
		"constraint",
		"convert",
		"corr",
		"corresponding",
		"count",
		"covar_pop",
		"covar_samp",
		"create",
		"cross",
		"cube",
		"cume_dist",
		"current",
		"current_catalog",
		"current_date",
		"current_default_transform_group",
		"current_path",
		"current_role",
		"current_schema",
		"current_time",
		"current_timestamp",
		"current_transform_group_for_type",
		"current_user",
		"cursor",
		"cycle",
		"datalink",
		"date",
		"day",
		"deallocate",
		"dec",
		"decimal",
		"declare",
		"default",
		"deferrable",
		"delete",
		"dense_rank",
		"deref",
		"desc",
		"describe",
		"deterministic",
		"disconnect",
		"distinct",
		"dlnewcopy",
		"dlpreviouscopy",
		"dlurlcomplete",
		"dlurlcompleteonly",
		"dlurlcompletewrite",
		"dlurlpath",
		"dlurlpathonly",
		"dlurlpathwrite",
		"dlurlscheme",
		"dlurlserver",
		"dlvalue",
		"do",
		"double",
		"drop",
		"dynamic",
		"each",
		"element",
		"else",
		"end",
		"end-exec",
		"escape",
		"every",
		"except",
		"exec",
		"execute",
		"exists",
		"exp",
		"external",
		"extract",
		"false",
		"fetch",
		"filter",
		"first_value",
		"float",
		"floor",
		"for",
		"foreign",
		"free",
		"from",
		"full",
		"function",
		"fusion",
		"get",
		"global",
		"grant",
		"group",
		"grouping",
		"having",
		"hold",
		"hour",
		"identity",
		"import",
		"in",
		"indicator",
		"initially",
		"inner",
		"inout",
		"insensitive",
		"insert",
		"int",
		"integer",
		"intersect",
		"intersection",
		"interval",
		"into",
		"is",
		"join",
		"lag",
		"language",
		"large",
		"last_value",
		"lateral",
		"lead",
		"leading",
		"left",
		"like",
		"like_regex",
		"limit",
		"ln",
		"local",
		"localtime",
		"localtimestamp",
		"lower",
		"match",
		"max",
		"max_cardinality",
		"member",
		"merge",
		"method",
		"min",
		"minute",
		"mod",
		"modifies",
		"module",
		"month",
		"multiset",
		"national",
		"natural",
		"nchar",
		"nclob",
		"new",
		"no",
		"none",
		"normalize",
		"not",
		"nth_value",
		"ntile",
		"null",
		"nullif",
		"numeric",
		"occurrences_regex",
		"octet_length",
		"of",
		"offset",
		"old",
		"on",
		"only",
		"open",
		"or",
		"order",
		"out",
		"outer",
		"over",
		"overlaps",
		"overlay",
		"parameter",
		"partition",
		"percentile_cont",
		"percentile_disc",
		"percent_rank",
		"placing",
		"position",
		"position_regex",
		"power",
		"precision",
		"prepare",
		"primary",
		"procedure",
		"range",
		"rank",
		"reads",
		"real",
		"recursive",
		"ref",
		"references",
		"referencing",
		"regr_avgx",
		"regr_avgy",
		"regr_count",
		"regr_intercept",
		"regr_r2",
		"regr_slope",
		"regr_sxx",
		"regr_sxy",
		"regr_syy",
		"release",
		"result",
		"return",
		"returning",
		"returns",
		"revoke",
		"right",
		"rollback",
		"rollup",
		"row",
		"rows",
		"row_number",
		"savepoint",
		"scope",
		"scroll",
		"search",
		"second",
		"select",
		"sensitive",
		"session_user",
		"set",
		"similar",
		"smallint",
		"some",
		"specific",
		"specifictype",
		"sql",
		"sqlexception",
		"sqlstate",
		"sqlwarning",
		"sqrt",
		"start",
		"static",
		"stddev_pop",
		"stddev_samp",
		"submultiset",
		"substring",
		"substring_regex",
		"sum",
		"symmetric",
		"system",
		"system_user",
		"table",
		"tablesample",
		"then",
		"time",
		"timestamp",
		"timezone_hour",
		"timezone_minute",
		"to",
		"trailing",
		"translate",
		"translate_regex",
		"translation",
		"treat",
		"trigger",
		"trim",
		"trim_array",
		"true",
		"truncate",
		"uescape",
		"union",
		"unique",
		"unknown",
		"unnest",
		"update",
		"upper",
		"user",
		"using",
		"value",
		"values",
		"varbinary",
		"varchar",
		"variadic",
		"varying",
		"var_pop",
		"var_samp",
		"when",
		"whenever",
		"where",
		"width_bucket",
		"window",
		"with",
		"within",
		"without",
		"xml",
		"xmlagg",
		"xmlattributes",
		"xmlbinary",
		"xmlcast",
		"xmlcomment",
		"xmlconcat",
		"xmldocument",
		"xmlelement",
		"xmlexists",
		"xmlforest",
		"xmliterate",
		"xmlnamespaces",
		"xmlparse",
		"xmlpi",
		"xmlquery",
		"xmlserialize",
		"xmltable",
		"xmltext",
		"xmlvalidate",
		"year",
	}));

	private static final String	TABLE_MAGIC_PREFIX	= "simpl_table__";

	private static final String	COLUMN_MAGIC_PREFIX	= "simpl_column__";

	public static final String	ASSOCIATION_TABLE_SEP	= "__";

	private Map<Object, String>	cachedNames						= new HashMap<Object, String>();

	protected String createTableName(String classSimpleName)
	{
		String name = XMLTools.getXmlTagName(classSimpleName, null).toLowerCase();
		if (SQL_RESERVED.contains(name))
			name = TABLE_MAGIC_PREFIX + name;
		return name;
	}

	protected String createColumnName(FieldDescriptor fd)
	{
		return createColumnName(fd.getName());
	}

	protected String createColumnName(String fieldName)
	{
		String name = XMLTools.getXmlTagName(fieldName, null);
		if (SQL_RESERVED.contains(name))
			name = COLUMN_MAGIC_PREFIX + name;
		return name;
	}

	@Override
	public String getTableName(ClassDescriptor cd)
	{
		if (cachedNames.containsKey(cd))
			return cachedNames.get(cd);
		String name = createTableName(cd.getDescribedClassSimpleName());
		cachedNames.put(cd, name);
		return name;
	}

	@Override
	public String getColumnName(FieldDescriptor fd)
	{
		if (cachedNames.containsKey(fd))
			return cachedNames.get(fd);
		String name = createColumnName(fd);
		cachedNames.put(fd, name);
		return name;
	}

	@Override
	public String getColumnName(String fieldName)
	{
		return createColumnName(fieldName);
	}

	@Override
	public String getAssociationTableName(ClassDescriptor cd, FieldDescriptor fd)
	{
		return getTableName(cd) + ASSOCIATION_TABLE_SEP + getColumnName(fd);
	}

	@Override
	public String getAssociationTableColumnName(ClassDescriptor cd)
	{
		return getTableName(cd) + "_id";
	}
	
	@Override
	public String getAssociationTableIndexName(ClassDescriptor cd, FieldDescriptor fd)
	{
		return getAssociationTableName(cd, fd) + "__index";
	}
	
	@Override
	public void clearCache()
	{
		cachedNames.clear();
	}

}
