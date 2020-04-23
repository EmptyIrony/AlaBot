package me.cunzai.uphelper.module.impl;

import me.cunzai.uphelper.module.Module;
import me.cunzai.uphelper.util.cooldown.Cooldown;
import me.cunzai.uphelper.util.cooldown.TimeUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChooseModule implements Module {
    private Random random;
    private Map<Long, Cooldown> cooldownCache;

    @Override
    public void onEnable() {
        this.random = new Random();
        this.cooldownCache = new HashMap<>();
    }

    @Override
    public void onMessage(long fromGroup, long fromUser, String msg, int msgId) {
        cooldownCache.putIfAbsent(fromUser,new Cooldown(0, TimeUnit.MilliSecond));
        if (!cooldownCache.get(fromUser).isComplete()){
            return;
        }
        msg = msg.substring(2);
        String[] args = msg.split(" ");
        if (args.length == 0){
            return;
        }

        int i = random.nextInt(args.length);
        String chose = args[i];

        this.cq.sendGroupMsg(fromGroup)
                .append(cqCode.at(fromUser))
                .append("\r\n")
                .append("建议你选择: ")
                .append(chose)
                .send();
    }

    @Override
    public boolean filter(long fromGroup, long fromUser, String msg) {
        return msg.startsWith("选 ");
    }
}
