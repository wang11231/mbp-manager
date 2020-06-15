package com.art.manager.service.impl.auction;

import com.art.manager.enums.AddPriceTypeEnum;
import com.art.manager.exception.AuctionGoodsException;
import com.art.manager.mapper.auction.AuctionGoodsMapper;
import com.art.manager.mapper.auction.AuctionProcessMapper;
import com.art.manager.pojo.Page;
import com.art.manager.pojo.auction.AuctionProcess;
import com.art.manager.request.AuctionGoodsReq;
import com.art.manager.request.AuctionProcessReq;
import com.art.manager.service.auction.AuctionProcessService;
import com.art.manager.service.base.BaseService;
import com.art.manager.vo.AuctionGoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class AuctionProcessServiceImpl extends BaseService implements AuctionProcessService {

    @Autowired
    private AuctionProcessMapper auctionProcessMapper;
    @Autowired
    private AuctionGoodsMapper auctionGoodsMapper;

    @Override
    public boolean insert(AuctionProcess auctionProcess) {
        if(auctionProcess == null || auctionProcess.getGoodsId() == null || StringUtils.isBlank(auctionProcess.getGoodsName())){
            throw new AuctionGoodsException("商品名为空！");
        }
        if(StringUtils.isBlank(auctionProcess.getAddPriceType())){
            throw new AuctionGoodsException("加价形式为空！");
        }
        /**
         * todo 信息不完整
         */
        //修改订单下之前流程状态为已出局 1
        auctionProcess.setProcessStatus("1");
        auctionProcessMapper.updateStatusByGoodsId(auctionProcess);
        //插入新的流程 状态为领先 2
        auctionProcess.setProcessStatus("2");
        int count = auctionProcessMapper.insert(auctionProcess);
        return count > 0 ? true : false;
    }

    @Override
    public Map<String, Object> getList(AuctionProcessReq auctionProcessReq) {
        if(auctionProcessReq == null || auctionProcessReq.getGoodsId() == null){
            throw new AuctionGoodsException("商品id为空！");
        }
        Page page = new Page();
        setPrePageInfo(page, auctionProcessReq);
        List<AuctionProcess> list = auctionProcessMapper.getList(auctionProcessReq);
        Map<String, Object> result = setAfterPageInfo(list, page);
        AuctionGoodsReq auctionGoodsreq = new AuctionGoodsReq();
        auctionGoodsreq.setId(auctionProcessReq.getGoodsId());
        AuctionGoodsVo auctionGoodsVo = auctionGoodsMapper.selectOneById(auctionGoodsreq);
        result.put("goodsStatus",auctionGoodsVo==null?null:auctionGoodsVo.getGoodsStatus());
        return result;
    }

    @Override
    public AuctionProcess getAuctionProcess(Long goodsId){
        if(goodsId == null){
            throw new AuctionGoodsException("商品id为空！");
        }
        List<AuctionProcess> processes = auctionProcessMapper.selectByIds(Arrays.asList(goodsId));
        if(processes != null && processes.size() > 0){
            return processes.get(0);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> selectByUsername(AuctionProcessReq auctionProcessReq) {
        if(auctionProcessReq == null || StringUtils.isBlank(auctionProcessReq.getUsername())){
            throw new AuctionGoodsException("用户名为空！");
        }
        Map<Long, Map<String, BigDecimal>> map = new HashMap<>();
        List<AuctionProcess> list = auctionProcessMapper.selectByUsername(auctionProcessReq.getUsername());
        if(list != null && list.size() > 0){
            for(AuctionProcess process:list){
                Map<String, BigDecimal> innerMap = map.get(process.getGoodsId());
                if(innerMap == null){
                    innerMap= new HashMap<>();
                }
                innerMap.put(process.getAddPriceType(), process.getCurrentPrice());
                map.put(process.getGoodsId(), innerMap);
            }
        }
        List<Map<String, Object>> result = new ArrayList<>();
        //补全 代理出价、立即出价、一口价，为null的补位0
        if(map != null && map.size() > 0){
            BigDecimal zero = new BigDecimal(0);
            Set<Long> ids = map.keySet();
            //查询拍品订单的最高价
            List<AuctionProcess> listProcesses = auctionProcessMapper.selectByIds(new ArrayList(ids));
            Iterator<Long> iterator = ids.iterator();
            while(iterator.hasNext()){
                Long key = iterator.next();
                AuctionProcess process = getProcess(listProcesses, key);
                if(process == null){
                    continue;
                }
                Map<String, BigDecimal> currentMap = map.get(key);
                Map<String, BigDecimal> itemsMap = new HashMap<>();
                if(currentMap.get(AddPriceTypeEnum.PROXY.getCode()) == null){
                    itemsMap.put(AddPriceTypeEnum.PROXY.getCode(), zero);
                }else{
                    itemsMap.put(AddPriceTypeEnum.PROXY.getCode(), currentMap.get(AddPriceTypeEnum.PROXY.getCode()));
                }
                if(currentMap.get(AddPriceTypeEnum.IMMEDIATELY.getCode()) == null){
                    itemsMap.put(AddPriceTypeEnum.IMMEDIATELY.getCode(), zero);
                }else{
                    itemsMap.put(AddPriceTypeEnum.IMMEDIATELY.getCode(), currentMap.get(AddPriceTypeEnum.IMMEDIATELY.getCode()));
                }
                if(currentMap.get(AddPriceTypeEnum.ONE.getCode()) == null){
                    itemsMap.put(AddPriceTypeEnum.ONE.getCode(), zero);
                }else{
                    itemsMap.put(AddPriceTypeEnum.ONE.getCode(), currentMap.get(AddPriceTypeEnum.ONE.getCode()));
                }
                Map<String,Object> resultMap = new HashMap<>();
                resultMap.put("id", key);
                resultMap.put("goodsName", process.getGoodsName());
                resultMap.put("currenPrice", process.getCurrentPrice());
                resultMap.put("items", itemsMap);
                result.add(resultMap);
            }
        }
        return result;
    }


    private AuctionProcess getProcess(List<AuctionProcess> listProcesses, Long id){
        if(id == null || listProcesses == null || listProcesses.size() == 0){
            return null;
        }
        for(AuctionProcess process:listProcesses){
            if(process.getGoodsId().equals(id)){
                return process;
            }
        }
        return null;
    }
}
