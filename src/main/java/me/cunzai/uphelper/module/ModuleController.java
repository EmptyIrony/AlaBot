package me.cunzai.uphelper.module;

import me.cunzai.uphelper.module.impl.ChooseModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuleController {
    private List<Module> modules;

    public ModuleController(){
        modules = new ArrayList<>();

        modules.addAll(Arrays.asList(
                new ChooseModule()
        ));

        modules.forEach(Module::onEnable);
    }

    public void handlerMsg(long group,long user,String msg,int id){
        this.modules
                .forEach(module -> {
                    if (module.filter(group,user,msg)){
                        module.onMessage(group,user,msg,id);
                    }
                });
    }

}
