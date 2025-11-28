//package com.zyh.easyapplyresume.demo.agent;
//
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * ReAct模式的代理抽象类
// * 实现了思考-行动的循环模式
// * @author shiningCloud2025
// */
//@Data
//public abstract class ReActAgent extends BaseAgent{
//    /**
//     * 处理当前状态并决定下一步行动
//     * @return 是否需要执行行动,true标识需要执行，false表示不需要执行
//     */
//    public abstract boolean think();
//
//    /**
//     * 执行决定的行动
//     * @return
//     */
//    public abstract String act();
//
//    @Override
//    public String step(){
//        try{
//            boolean shouldAct = think();
//            if (!shouldAct){
//                return "思考完成-无需行动";
//            }
//            return act();
//        }catch (Exception e){
//            e.printStackTrace();
//            return "步骤执行失败: "+e.getMessage();
//        }
//    }
//}
