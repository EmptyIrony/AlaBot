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
    }

    public void onMsg(){
        modules.forEach(Module::onMessage);
    }

}
