package com.art.manager.util;

import com.art.manager.enums.GoodsEnum;
import com.art.manager.enums.OrderNoPrefixEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

/**
 * 订单生成
 */
public class IdUtils {

    //拍品商品
    private static final String AUCTION_NO_KEY = "_AUCTION_GOODS_NO";
    //普通商品
    private static final String COMMON_NO_KEY = "_COMMON_GOODS_NO";

    //提现
    private static final String TRANSFERS_NO_KEY = "_TRANSFERS_NO_KEY";

    /**
     * 根据前缀生产id
     * @param orderNoPrefixEnum
     * @return
     */
    public static String generate(GoodsEnum goodsEnum, OrderNoPrefixEnum orderNoPrefixEnum){
        RedisTemplate redisTemplate = (RedisTemplate) SpringUtils.getBean("redisTemplate");
        String prefix = orderNoPrefixEnum.getCode();
        String key = getKey(goodsEnum, prefix);
        Long value = redisTemplate.opsForValue().increment(key);
        String format = FormatUtil.FORMAT_YMDHMS.get().format(new Date());
        String no = prefix + format +String.format("%05d", value);
        return no;
    }

    public static String getKey(GoodsEnum goodsEnum, String prefix){
        if(goodsEnum == null || StringUtils.isBlank(goodsEnum.getCode())){
            throw new RuntimeException("商品类型不存在");
        }
        switch(goodsEnum.getCode()){
            case "auction":
                return prefix + AUCTION_NO_KEY;
            case "common":
                return prefix + COMMON_NO_KEY;
            case "transfers":
                return prefix +TRANSFERS_NO_KEY;

            default:
                throw new RuntimeException("商品类型不存在");
        }
    }

}
