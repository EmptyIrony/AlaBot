package me.cunzai.uphelper.module;

import me.cunzai.uphelper.UpHelper;
import org.meowy.cqp.jcq.entity.CoolQ;
import org.meowy.cqp.jcq.message.CQCode;

public interface Module {
    CoolQ cq = UpHelper.getInstance().getCoolQ();
    CQCode cqCode = UpHelper.getInstance().getCQCode();

    void onEnable();
    void onMessage(long fromGroup,long fromUser,String msg,int msgId);
    boolean filter(long fromGroup,long fromUser,String msg);


}
