package com.mcg.controller;


import Utils.SpendTime;
import com.alibaba.fastjson.JSONObject;
import com.mcg.common.sysenum.LogTypeEnum;
import com.mcg.entity.common.McgResult;
import com.mcg.entity.flow.connector.ConnectorData;
import com.mcg.entity.message.Message;
import com.mcg.entity.message.NotifyBody;
import com.mcg.plugin.ehcache.ConnectorCache;
import com.mcg.plugin.websocket.MessagePlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpSession;
import java.io.BufferedWriter;
import java.util.*;

@Controller
@RequestMapping("/connect")
public class ConnectController {

    private static Map<ConnectorData,Integer> map ;
    private static Map<String,Integer> indexMap ;
    private static int index ;
    private static Logger logger = LoggerFactory.getLogger(ConnectController.class);

//    public static void main(String args[]){
//        ConnectController instance = new ConnectController();
//        instance.add(new ConnectorData("1","2"));
//        instance.add(new ConnectorData("2","4"));
//        instance.add(new ConnectorData("2","3"));
//        instance.add(new ConnectorData("5","6"));
//        instance.add(new ConnectorData("5","7"));
//        instance.add(new ConnectorData("8","7"));
//        instance.add(new ConnectorData("8","6"));
//        SpendTime time = new SpendTime();
//        time.clockUp();
//        System.out.println(instance.isIllegal());
//        time.clockOver();
//        time.clockUp();
//        System.out.println(instance.TP());
//        time.clockOver();
//    }

    static {
        index = 0;
        indexMap = new HashMap<>();
        map = new TreeMap<>();
    }

    public static void cachePersistent(BufferedWriter bw) throws  Exception{
        int keySet[] = new int[map.size()];
        int i = 0;
        for(Map.Entry temp : map.entrySet()){
            keySet[i++] =(int) temp.getValue();
        }
        ToolController.cachePersistent(keySet,bw);
    }

    private boolean add(ConnectorData connectorData){
        map.put(connectorData,index);
        return ConnectorCache.put(index++,connectorData);
    }

    @RequestMapping(value="/addArray", method = RequestMethod.POST)
    @ResponseBody
    public McgResult addArray(String[] sourceId ,String []targetId){
        McgResult result = new McgResult();
        int len;
        len = Math.min(targetId.length,sourceId.length);
        for(int i = 0 ; i< len ; i++){
            logger.debug("source: "+sourceId[i] +" target: "+targetId[i]);
        }
        result.setStatusCode(1);
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    public McgResult add(String sourceId, String targetId , HttpSession session){
//        System.out.println(sourceId + " " +targetId);
        McgResult result = new McgResult();
        Message message = MessagePlugin.getMessage();
        NotifyBody body = new NotifyBody();
        ConnectorData data = new ConnectorData(sourceId,targetId);
        if(add(data)){
            body.setType(LogTypeEnum.SUCCESS.getValue());
            result.setStatusCode(1);
        } else{
            body.setType(LogTypeEnum.ERROR.getValue());
            result.setStatusCode(0);
        }
        message.setBody(body);
        result.addAttribute(session.getId(),message);
        return result;
    }

    @RequestMapping(value = "/delAll", method = RequestMethod.GET)
    @ResponseBody
    public McgResult delAll(){
        McgResult result = new McgResult();
        result.setStatusCode(1);
        for(Map.Entry<ConnectorData, Integer> t : map.entrySet()){
            if(!ConnectorCache.removr(t.getValue())){
                logger.debug("error occur "+ t.getKey().toString());
                result.setStatusCode(0);
                // add remove failed deal method here
                break;
            }
        }
        return result;
    }

    private boolean del (ConnectorData connectorData){
        int i = map.get(connectorData);
        map.remove(connectorData);
        if( !ConnectorCache.removr(i)){
            return false;
        }else{
            return true;
        }
    }

    @RequestMapping(value="/legal", method = RequestMethod.GET)
    @ResponseBody
    public McgResult isLegal(){
        McgResult result = new McgResult();
        if(isIllegal()){
            result.setStatusCode(0);
        }else{
            result.setStatusCode(1);
        }
        return result;
    }

    private boolean TP(){
        int s;
        int t;
        int nodeIndex = 0;
        int in [] = new int [map.size()*2];
        ArrayList<Integer> list [] = new ArrayList[map.size()*2];
        for(int i = 0 ; i < list.length ; i++){
            list[i] = new ArrayList<>();
        }
        ArrayList<Integer> queue = new ArrayList<>();
        indexMap = new HashMap<>();
        for (ConnectorData temp : map.keySet()) {
            if (indexMap.containsKey(temp.getSourceId())) {
                s = indexMap.get(temp.getSourceId());
            } else {
                indexMap.put(temp.getSourceId(), nodeIndex);
                s = nodeIndex++;
            }
            if (indexMap.containsKey(temp.getTargetId())) {
                t = indexMap.get(temp.getTargetId());
            } else {
                indexMap.put(temp.getTargetId(), nodeIndex);
                t = nodeIndex++;
            }
            list[s].add(t);
            in[t]++;
        }
        in = Arrays.copyOf(in,nodeIndex);
        for(int i = 0 ; i < nodeIndex ; i++){
            if(in[i] == 0)
                queue.add(i);
        }
        if(queue.isEmpty())
            return true;
        int count = queue.size();
        do{
            ArrayList<Integer> l = list[queue.get(queue.size()-1)];
            queue.remove(queue.size()-1);
            for(int index: l){
                if(--in[index] == 0){
                    queue.add(index);
                    count++;
                }
            }
        }while(!queue.isEmpty());
        if(count == nodeIndex)
            return false;
        else
            return true;
    }

    private boolean isIllegal(){
//        if (map.size() > 0) {
//            LinkedList<Stack<Integer>> stacks = new LinkedList<>();
//            int nodeIndex = 0;
//            int s;
//            int t;
//            for (ConnectorData temp : map.keySet()) {
//                if (indexMap.containsKey(temp.getSourceId())) {
//                    s = indexMap.get(temp.getSourceId());
//                } else {
//                    indexMap.put(temp.getSourceId(), ++nodeIndex);
//                    s = nodeIndex;
//                }
//                if (indexMap.containsKey(temp.getTargetId())) {
//                    t = indexMap.get(temp.getTargetId());
//                } else {
//                    indexMap.put(temp.getTargetId(), ++nodeIndex);
//                    t = nodeIndex;
//                }
//                if (s < t) {
//                    if (addWhere(stacks, s, t)) {
//                        return true;
//                    }
//                } else {
//                    if (addWhere(stacks, t, s)) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
        return TP();
    }

    private void addStack(LinkedList list ,Integer o1,Integer o2){
        Stack<Integer> temp = new Stack<>();
        temp.push(o1);
        temp.push(o2);
        list.add(temp);
    }

    private boolean addWhere( LinkedList<Stack<Integer>> stacks, Integer s, Integer t){
        boolean existStack = false;
        if (stacks.size() > 0) {
            for (Stack<Integer> var : stacks) {
                if (var.search(s) != -1) {
                    if (var.search(t) == -1) {
                        var.push(t);
                        existStack = true;
                        break;
                    } else {
                        return true;
                    }
                }
            }
        }
        if (!existStack) {
            addStack(stacks, s, t);
        }
        return false;
    }
}

