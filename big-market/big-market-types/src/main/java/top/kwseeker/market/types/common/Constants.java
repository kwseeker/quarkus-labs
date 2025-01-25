package top.kwseeker.market.types.common;

public class Constants {

    public final static String SPLIT = ",";
    public final static String COLON = ":";
    public final static String SPACE = " ";
    public final static String UNDERLINE = "_";

    /**
     * 定义出缓存key的前缀标识，
     */
    public static class RedisKey {
        public static String BIG_MARKET_KEY_PREFIX = "big_market:";

        public static String ACTIVITY_KEY = BIG_MARKET_KEY_PREFIX + "activity_key_";
        public static String ACTIVITY_SKU_KEY = BIG_MARKET_KEY_PREFIX + "activity_sku_key_";
        public static String ACTIVITY_COUNT_KEY = BIG_MARKET_KEY_PREFIX + "activity_count_key_";
        public static String STRATEGY_KEY = BIG_MARKET_KEY_PREFIX + "strategy_key_";
        public static String STRATEGY_AWARD_KEY = BIG_MARKET_KEY_PREFIX + "strategy_award_key_";
        public static String STRATEGY_AWARD_LIST_KEY = BIG_MARKET_KEY_PREFIX + "strategy_award_list_key_";
        public static String STRATEGY_RATE_TABLE_KEY = BIG_MARKET_KEY_PREFIX + "strategy_rate_table_key_";
        public static String STRATEGY_RATE_RANGE_KEY = BIG_MARKET_KEY_PREFIX + "strategy_rate_range_key_";
        public static String RULE_TREE_VO_KEY = BIG_MARKET_KEY_PREFIX + "rule_tree_vo_key_";
        public static String STRATEGY_AWARD_COUNT_KEY = BIG_MARKET_KEY_PREFIX + "strategy_award_count_key_";
        public static String STRATEGY_AWARD_COUNT_QUERY_KEY = BIG_MARKET_KEY_PREFIX + "strategy_award_count_query_key";
        public static String STRATEGY_RULE_WEIGHT_KEY = BIG_MARKET_KEY_PREFIX + "strategy_rule_weight_key_";
        public static String ACTIVITY_SKU_COUNT_QUERY_KEY = BIG_MARKET_KEY_PREFIX + "activity_sku_count_query_key";
        public static String ACTIVITY_SKU_STOCK_COUNT_KEY = BIG_MARKET_KEY_PREFIX + "activity_sku_stock_count_key_";
        public static String ACTIVITY_SKU_COUNT_CLEAR_KEY = BIG_MARKET_KEY_PREFIX + "activity_sku_count_clear_key_";
        public static String ACTIVITY_ACCOUNT_LOCK = BIG_MARKET_KEY_PREFIX + "activity_account_lock_";
        public static String ACTIVITY_ACCOUNT_UPDATE_LOCK = BIG_MARKET_KEY_PREFIX + "activity_account_update_lock_";
        public static String USER_CREDIT_ACCOUNT_LOCK = BIG_MARKET_KEY_PREFIX + "user_credit_account_lock_";
        public static String STRATEGY_ARMORY_ALGORITHM_KEY = BIG_MARKET_KEY_PREFIX + "strategy_armory_algorithm_key_";
    }
}
